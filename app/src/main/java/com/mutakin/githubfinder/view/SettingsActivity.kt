package com.mutakin.githubfinder.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.mutakin.githubfinder.R
import com.mutakin.githubfinder.alarm.AlarmReceiver

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        private lateinit var reminderKey: String
        private lateinit var setReminder: CheckBoxPreference
        private lateinit var languageKey: String
        private lateinit var changeLanguage: Preference
        private lateinit var alarmReceiver: AlarmReceiver


        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            init()
            setSummaries()
        }


        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        private fun init() {
            reminderKey = resources.getString(R.string.set_reminder)
            setReminder = findPreference(reminderKey)!!
            languageKey = resources.getString(R.string.language)
            changeLanguage = findPreference(languageKey)!!
            changeLanguage.setOnPreferenceClickListener {
                Intent(Settings.ACTION_LOCALE_SETTINGS).also {
                    startActivity(it)
                }
                return@setOnPreferenceClickListener true
            }
        }

        private fun setSummaries() {
            val sharedPref = preferenceManager.sharedPreferences
            setReminder.isChecked = sharedPref.getBoolean(reminderKey, false)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
            when (key) {
                reminderKey -> {
                    setReminder.isChecked = sharedPreferences.getBoolean(reminderKey, false)
                    alarmReceiver = AlarmReceiver()
                    val context = context as Context
                    if (setReminder.isChecked) {
                        val repeatMessage = "Lets Find Popular User On Github"
                        alarmReceiver.setRepeatingAlarm(
                            context,
                            AlarmReceiver.TYPE_REPEATING,
                            repeatMessage
                        )
                    } else {
                        alarmReceiver.cancelAlarm(context)
                    }
                }

            }
        }


    }
}