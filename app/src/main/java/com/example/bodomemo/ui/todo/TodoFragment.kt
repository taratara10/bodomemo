package com.example.bodomemo.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bodomemo.R

class TodoFragment : Fragment() {

    private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        todoViewModel =
                ViewModelProvider(this).get(TodoViewModel::class.java)
        val root = inflater.inflate(R.layout.todo_activity, container, false)
//        val textView: TextView = root.findViewById(R.id.todoActivity)
        todoViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })
        return root
    }
}