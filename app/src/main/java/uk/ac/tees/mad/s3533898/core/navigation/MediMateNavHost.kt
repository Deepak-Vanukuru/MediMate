package uk.ac.tees.mad.s3533898.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.s3533898.feature.auth.LoginScreen
import uk.ac.tees.mad.s3533898.feature.auth.RegisterScreen
import uk.ac.tees.mad.s3533898.feature.home.HomeScreen
import uk.ac.tees.mad.s3533898.feature.home.PlaceholderScreen
import uk.ac.tees.mad.s3533898.feature.medicine.AddMedicineScreen
import uk.ac.tees.mad.s3533898.feature.splash.SplashScreen

/**
 * App navigation graph.
 *
 * Sprint 1 wires the full access flow (splash then register or login then home)
 * and the scaffold from Home out to Add, Reminders and Profile. The three
 * placeholder destinations prove navigation and back behaviour work before the
 * real features arrive in later sprints.
 */
@Composable
fun MediMateNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(
                onDecide = { hasAccount ->
                    val target = if (hasAccount) Screen.Login.route else Screen.Register.route
                    navController.navigate(target) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistered = { navController.goToHome() },
                onGoToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(onAuthenticated = { navController.goToHome() })
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onAddClick = { navController.navigate(Screen.Add.route) },
                onRemindersClick = { navController.navigate(Screen.Reminders.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }

        composable(Screen.Add.route) {
            AddMedicineScreen(onDone = { navController.popBackStack() })
        }

        composable(Screen.Reminders.route) {
            PlaceholderScreen(
                title = "Reminders",
                message = "Dose reminders arrive in Sprint 4.",
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            PlaceholderScreen(
                title = "Profile",
                message = "Profile, security settings and GDPR data controls arrive in later sprints.",
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private fun NavHostController.goToHome() {
    navigate(Screen.Home.route) {
        popUpTo(0) { inclusive = true } // clear the auth back stack
        launchSingleTop = true
    }
}
