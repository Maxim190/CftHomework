package com.example.mycustomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity_main_editText.width
        activity_main_editText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.toString()?.let {
                    val value = if (!it.isEmpty()) {
                        it.toInt()
                    }
                    else {
                        0
                    }
                    activity_main_speedometerView.setCurrentSpeed(value)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}