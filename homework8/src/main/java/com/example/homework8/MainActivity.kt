package com.example.homework8

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val TAG = "tag"
    private val textChangeSubject = PublishSubject.create<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sourceText = resources.getString(R.string.source_text)
        val result = findViewById<TextView>(R.id.result)

        findViewById<SearchView>(R.id.edit_text).setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                textChangeSubject.onNext(newText)
                return true
            }
        })

        textChangeSubject
            .filter { it.isNotEmpty() }
            .debounce(700, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .map { s -> Regex(s).findAll(sourceText).count() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                result.text = it.toString()
            }

//        textChangeSubject
//                .filter { it.isNotEmpty() }
//                .debounce(700, TimeUnit.MILLISECONDS)
//                .distinctUntilChanged()
//                .switchMap { s ->
//                    Observable.fromIterable(Regex(s).findAll(sourceText).toList())
//                }
//                .zipWith(Observable.range(1, Integer.MAX_VALUE), {_, counter ->
//                    counter
//                })
//                .doOnNext { Thread.sleep(10L) }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    result.text = it.toString()
//                }

    }
}
