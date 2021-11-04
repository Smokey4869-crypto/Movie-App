package com.example.myapplication.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.myapplication.R
import com.example.myapplication.languages.LocaleHelper

class SettingTabFragment : BaseFragment() {
    private lateinit var spinner: Spinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting_tab, container, false) as View
        spinner = view.findViewById(R.id.language_spinner)
        initDropDownSpinner()
        return view
    }

    private fun initDropDownSpinner() {
        val languages : List<String> = listOf("English", "Vietnamese", "Japanese")
        val adapter: ArrayAdapter<String>? =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, languages) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedLang: String = p0?.getItemAtPosition(p2).toString()
                if (selectedLang == "English") {
//                    context = activity?.let { LocaleHelper.setLocale(it, "en") }
                } else if (selectedLang == "Vietnamese") {
//                    context = activity?.let { LocaleHelper.setLocale(it, "vi") }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

}

