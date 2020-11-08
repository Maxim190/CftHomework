package com.example.cfthomework.ui.contentprovider

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cfthomework.App
import com.example.cfthomework.R
import kotlinx.android.synthetic.main.fragment_contentprovider.*


class PhoneBookFragment: Fragment() {

    private lateinit var storage: PhoneBookStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_contentprovider, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!checkPermissions()) {
            askPermissions()
        }
        else {
            val recyclerView = fragment_contProvider_recyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)

            fragment_contProvider_progressBar.visibility = View.VISIBLE

            storage = PhoneBookStorage(requireContext())
            storage.getProfiles().observe(viewLifecycleOwner) {
                recyclerView.adapter = PhoneBookAdapter(it)
                fragment_contProvider_progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
    }

    private fun askPermissions() {
        //requestCode ??
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 123)
    }
}