package com.example.myapplication

import android.content.Context
import android.content.ContextWrapper
import androidx.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity

import com.example.myapplication.languages.ContextUtils
import java.util.*

open class BaseActivity: AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
// get chosen language from shared preference
        val localeToSwitchTo = Locale(PreferenceManager.getDefaultSharedPreferences(newBase).getString("mylang", "en"))
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}