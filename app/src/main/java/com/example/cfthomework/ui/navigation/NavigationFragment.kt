package com.example.cfthomework.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.cfthomework.R
import kotlinx.android.synthetic.main.fragment_navigation.*

class NavigationFragment : Fragment() {
    private var localHistory: String? = null

    companion object {
        var currentColorIndex = 0
        val colors = listOf(
            R.color.lush_lava,
            R.color.aqua_menthe,
            R.color.japan
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            val args = NavigationFragmentArgs.fromBundle(it)
            localHistory = args.history
            fragment_nav_history_tv.text = localHistory
            fragment_nav_label_tv.text =  args.myTag
            fragment_nav_label_tv.setBackgroundResource(getNextColor())
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (fragment_nav_label_tv.text == "A") {
                        val navDirection =
                            NavigationFragmentDirections.actionNavigationFragmentToMainFragment()
                        Navigation.findNavController(requireView()).navigate(navDirection)
//                        activity?.finish()
//                        System.exit(0)
                    }
                    else {
                        Navigation.findNavController(requireView()).popBackStack()
                    }
                }
            })

        fragment_nav_buttonA.setOnClickListener { openFragment(tag = "A") }
        fragment_nav_buttonB.setOnClickListener { openFragment(tag = "B") }
        fragment_nav_buttonC.setOnClickListener { openFragment(tag = "C") }
    }
    private fun getNextColor(): Int {
        currentColorIndex = (currentColorIndex + 1) % colors.size
        return colors[currentColorIndex]
    }

    private fun openFragment(tag: String) {
        val newHistory = "$localHistory -> $tag"

        val action =
            NavigationFragmentDirections
                .actionNavigationFragmentSelf(tag, newHistory)
        Navigation
            .findNavController(requireView())
            .navigate(action)
    }
}