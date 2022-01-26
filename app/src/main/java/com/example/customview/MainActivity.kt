package com.example.customview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.customview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    companion object {
        val TAG = MainActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {
            setOnClickListener(undo, redo, clear, addText, addCanvas)
            swDrawMode.setOnCheckedChangeListener { view, isChecked ->
                view.text = if (isChecked) "On" else "Off"
                doodle.setDrawMode(isChecked)
                setViewState(binding.addText, false)
                setViewState(binding.addCanvas, false)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.undo -> {
                binding.doodle.undo()
            }
            R.id.redo -> {
                binding.doodle.redo()
            }
            R.id.clear -> {
                binding.doodle.clearDraw()
            }
            R.id.add_text -> {
                if (binding.doodle.getDrawMode()) {
                    setViewState(binding.addText)
                }
            }
            R.id.add_canvas -> {
                if (binding.doodle.getDrawMode()) {
                    setViewState(binding.addCanvas)
                }
            }
        }
    }

    private fun setOnClickListener(vararg views: View) {
        views.forEach {
            it.setOnClickListener(this)
        }
    }

    private fun setViewState(view: View, boolean: Boolean? = null) {
        if (boolean == null) {
            view.isSelected = !view.isSelected
            return
        }
        view.isSelected = boolean
    }
}