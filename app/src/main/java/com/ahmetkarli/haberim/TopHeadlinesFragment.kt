package com.ahmetkarli.haberim

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TopHeadlinesFragment : Fragment() {

    companion object {
        fun newInstance() = TopHeadlinesFragment()
    }

    private lateinit var viewModel: TopHeadlinesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_headlines, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TopHeadlinesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}