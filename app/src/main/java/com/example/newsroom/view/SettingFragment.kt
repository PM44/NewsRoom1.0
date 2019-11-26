package com.example.newsroom.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.newsroom.R
//Setting fragment to show setting founctionallity
class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefrences, rootKey)
    }
}