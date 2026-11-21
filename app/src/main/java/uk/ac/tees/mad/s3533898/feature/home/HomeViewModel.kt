package uk.ac.tees.mad.s3533898.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import uk.ac.tees.mad.s3533898.data.repository.AuthRepository
import uk.ac.tees.mad.s3533898.data.repository.MedicineRepository
import uk.ac.tees.mad.s3533898.domain.model.Medicine
import javax.inject.Inject

data class HomeUiState(
    val displayName: String = "",
    val medicines: List<Medicine> = emptyList(),
    val isLoading: Boolean = true
)

/**
 * Home dashboard state (story B2): the user's greeting plus the live list of
 * medicines observed from the offline Room database. The interaction banner and
 * detail navigation are added in later sprints.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    medicineRepository: MedicineRepository,
    authRepository: AuthRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        combine(
            medicineRepository.observeMedicines(),
            authRepository.userPreferences
        ) { medicines, prefs ->
            HomeUiState(
                displayName = prefs.displayName,
                medicines = medicines,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )
}
