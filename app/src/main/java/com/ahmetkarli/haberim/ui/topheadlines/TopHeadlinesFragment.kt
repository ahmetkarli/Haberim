package com.ahmetkarli.haberim.ui.topheadlines
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetkarli.haberim.databinding.FragmentTopHeadlinesBinding
import com.ahmetkarli.haberim.ui.topheadlines.adapter.CategoryAdapter
import com.ahmetkarli.haberim.ui.topheadlines.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopHeadlinesFragment : Fragment() {


    private val viewModel: TopHeadlinesViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var newsAdapter: NewsAdapter

    private var _binding: FragmentTopHeadlinesBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRv()
    }

    private fun setupRv() {
        categoryAdapter= CategoryAdapter()
        binding.rvCategory.apply {
            adapter=categoryAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
        }

        newsAdapter= NewsAdapter()
        binding.rvNews.apply {
            adapter=newsAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            //setHasFixedSize(true)
        }

        viewModel.responseNews.observe(viewLifecycleOwner,{ news ->
            newsAdapter.newsList = news.articles!!
        })


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopHeadlinesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}