package com.example.cfthomework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.cfthomework.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragment_main_btn_navigation.setOnClickListener {
            openFragment(R.id.navigationFragment)
        }
        fragment_main_btn_service.setOnClickListener {
            openFragment(R.id.serviceFragment)
        }
        fragment_main_btn_contentProvider.setOnClickListener {
            openFragment(R.id.phoneBookFragment)
        }
    }

    private fun openFragment(id: Int) {
        Navigation.findNavController(requireView()).navigate(id)
    }
}