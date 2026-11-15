package uk.ac.tees.mad.s3533898.core.util

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity

/** Unwraps a Compose [Context] to the hosting [FragmentActivity] (needed for BiometricPrompt). */
fun Context.findFragmentActivity(): FragmentActivity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is FragmentActivity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
