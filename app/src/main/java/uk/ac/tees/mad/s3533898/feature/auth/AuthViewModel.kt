package uk.ac.tees.mad.s3533898.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.s3533898.data.repository.AuthRepository
import uk.ac.tees.mad.s3533898.domain.model.UserPreferences
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    /** Used by the splash screen to decide the first destination. */
    val userPreferences: StateFlow<UserPreferences?> =
        authRepository.userPreferences.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun register(
        displayName: String,
        pin: String,
        confirmPin: String,
        biometricEnabled: Boolean
    ) {
        val validation = validateRegistration(displayName, pin, confirmPin)
        if (validation != null) {
            _uiState.value = AuthUiState(error = validation)
            return
        }
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            runCatching { authRepository.register(displayName, pin, biometricEnabled) }
                .onSuccess { _uiState.value = AuthUiState(success = true) }
                .onFailure { _uiState.value = AuthUiState(error = "Could not create account. Try again.") }
        }
    }

    fun login(pin: String) {
        if (pin.length < 4) {
            _uiState.value = AuthUiState(error = "Enter your PIN")
            return
        }
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val ok = authRepository.verifyPin(pin)
            _uiState.value =
                if (ok) AuthUiState(success = true)
                else AuthUiState(error = "Incorrect PIN")
        }
    }

    /** Called when biometric unlock succeeds. */
    fun onBiometricSuccess() {
        _uiState.value = AuthUiState(success = true)
    }

    fun consumeError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun validateRegistration(name: String, pin: String, confirm: String): String? = when {
        name.isBlank() -> "Please enter a display name"
        pin.length !in 4..8 || !pin.all { it.isDigit() } -> "PIN must be 4–8 digits"
        pin != confirm -> "PINs do not match"
        else -> null
    }
}
