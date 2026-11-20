package uk.ac.tees.mad.s3533898.core.navigation

/**
 * Type-safe navigation routes.
 *
 * The secure-access flow (splash, register, login) and the main scaffold (home,
 * add, reminders, profile) were built in Sprint 1. From Sprint 2 the Add route
 * is the real add-medicine form; Reminders and Profile remain placeholders until
 * their sprints (reminders, GDPR profile).
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
