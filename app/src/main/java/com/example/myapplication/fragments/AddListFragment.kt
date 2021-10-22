package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.viewmodels.FirebaseViewModel
import com.google.android.material.textfield.TextInputEditText

class AddListFragment : DialogFragment() {
    private lateinit var saveBtn: Button
    private lateinit var vInputListName: TextInputEditText
    private lateinit var cancelBtn: Button
    private var accepted: Boolean = false
    private val firebaseViewModel: FirebaseViewModel by viewModels({requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false) as View
        saveBtn = view.findViewById(R.id.save_button)
        vInputListName = view.findViewById(R.id.textInput)
        cancelBtn = view.findViewById(R.id.cancel_button)
        saveBtn.setOnClickListener {
            if (accepted) {
                firebaseViewModel.addEmptyFavoriteList(vInputListName.text.toString())
                dismiss()
            }
        }
        cancelBtn.setOnClickListener{ dismiss()}
        vInputListName.doAfterTextChanged { validateListName(vInputListName.text.toString()) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun validateListName(listName: String){
        val exist = firebaseViewModel.favoriteListExists(listName)
        if (exist) {
            vInputListName.error = "Favorite list ${listName} already exists"
            accepted = false
        } else accepted = true
    }
}