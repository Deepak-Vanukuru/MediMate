package uk.ac.tees.mad.s3533898

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.s3533898.core.navigation.MediMateNavHost
import uk.ac.tees.mad.s3533898.core.theme.MediMateTheme

/**
 * Single-activity host. Extends [FragmentActivity] so BiometricPrompt can attach.
 * [AndroidEntryPoint] lets Hilt inject ViewModels into the Compose tree.
 */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediMateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MediMateNavHost()
                }
            }
        }
    }
}
