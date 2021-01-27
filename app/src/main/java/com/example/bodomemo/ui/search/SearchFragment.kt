package com.example.bodomemo.ui.search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bodomemo.R
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private lateinit var dashboardViewModel: SearchViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)

    //* Set spinner layout
        val spinner = root.sp_filter_select
        val spinnerItem = resources.getStringArray(R.array.sp_game_filter)
        activity?.let { setSpinnerItem(it, spinner, spinnerItem) }


        return root
    }


    private fun setSpinnerItem(activity: Activity, spinner: Spinner, item: Array<String>): Spinner {
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        return spinner
    }
}