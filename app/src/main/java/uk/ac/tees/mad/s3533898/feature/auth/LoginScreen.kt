package uk.ac.tees.mad.s3533898.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.ac.tees.mad.s3533898.core.security.BiometricAuthenticator
import uk.ac.tees.mad.s3533898.core.util.findFragmentActivity

@Composable
fun LoginScreen(
    onAuthenticated: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val prefs by viewModel.userPreferences.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var pin by remember { mutableStateOf("") }

    LaunchedEffect(state.success) { if (state.success) onAuthenticated() }

    // Auto-offer biometric unlock if the user enabled it.
    LaunchedEffect(prefs?.biometricEnabled) {
        val activity = context.findFragmentActivity()
        if (prefs?.biometricEnabled == true && activity != null &&
            BiometricAuthenticator.isAvailable(activity)
        ) {
            BiometricAuthenticator.authenticate(
                activity = activity,
                onSuccess = { viewModel.onBiometricSuccess() },
                onError = { /* fall back to PIN entry */ }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome back", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        prefs?.displayName?.takeIf { it.isNotBlank() }?.let {
            Text(it, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 4.dp))
        }

        OutlinedTextField(
            value = pin,
            onValueChange = { if (it.length <= 8 && it.all(Char::isDigit)) pin = it },
            label = { Text("Enter PIN") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        )

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 12.dp))
        }

        Button(
            onClick = { viewModel.login(pin) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        ) {
            if (state.isLoading) CircularProgressIndicator(modifier = Modifier.padding(4.dp))
            else Text("Sign in")
        }

        if (prefs?.biometricEnabled == true) {
            OutlinedButton(
                onClick = {
                    val activity = context.findFragmentActivity() ?: return@OutlinedButton
                    BiometricAuthenticator.authenticate(
                        activity = activity,
                        onSuccess = { viewModel.onBiometricSuccess() },
                        onError = {}
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            ) { Text("Use biometric") }
        }
    }
}
