package com.example.cfthomework.ui.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cfthomework.MyService
import com.example.cfthomework.R
import kotlinx.android.synthetic.main.fragment_service.*

class ServiceFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val intent = Intent(context, MyService::class.java)

        registerReceiver()

        fragment_service_startBtn.setOnClickListener {
            requireContext().startService(intent)
            fragment_service_statusLabel.text = "Service started"
            fragment_service_progressBar.visibility = View.VISIBLE
        }
        fragment_service_stopBtn.setOnClickListener {
            requireContext().stopService(intent)
            fragment_service_statusLabel.text = "Service stopped"
            fragment_service_progressBar.visibility = View.INVISIBLE
        }
    }

    private fun registerReceiver() {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                activity?.runOnUiThread {
                    fragment_service_statusLabel?.text =
                        intent?.extras?.getString(MyService.MSG_NAME)
                            ?: "failed to receive msg from service"
                    fragment_service_progressBar?.visibility = View.INVISIBLE
                }
            }
        }

        val filter = IntentFilter(MyService.BROADCAST_ACTION)
        requireContext().registerReceiver(receiver, filter)
    }

}