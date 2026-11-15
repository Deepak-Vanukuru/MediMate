package uk.ac.tees.mad.s3533898.core.navigation

/**
 * Type-safe navigation routes.
 *
 * Sprint 1 scope: the secure-access flow (splash, register, login) plus the
 * main navigation scaffold (home, add, reminders, profile). The Add, Reminders
 * and Profile destinations are placeholders this sprint; they are filled in in
 * later sprints (manage medicines, reminders, GDPR profile).
 */
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Register : Screen("register")
    data object Login : Screen("login")

    data object Home : Screen("home")
    data object Add : Screen("add")
    data object Reminders : Screen("reminders")
    data object Profile : Screen("profile")
}
