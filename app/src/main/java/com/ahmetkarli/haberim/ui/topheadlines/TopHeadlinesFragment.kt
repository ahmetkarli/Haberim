package com.ahmetkarli.haberim.ui.topheadlines

import android.app.Dialog
import android.app.Service
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetkarli.haberim.R
import com.ahmetkarli.haberim.databinding.FragmentTopHeadlinesBinding
import com.ahmetkarli.haberim.databinding.LayoutBottomSheetBinding
import com.ahmetkarli.haberim.helper.CommonUtils
import com.ahmetkarli.haberim.helper.ConnectionLiveData
import com.ahmetkarli.haberim.ui.topheadlines.adapter.CategoryAdapter
import com.ahmetkarli.haberim.ui.topheadlines.adapter.NewsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopHeadlinesFragment : Fragment() {


    private val viewModel: TopHeadlinesViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var cld: ConnectionLiveData

    private var _binding: FragmentTopHeadlinesBinding? = null
    private val binding get() = _binding!!

    private var binding2: LayoutBottomSheetBinding? = null

    private var loadingDialog: Dialog? = null

    var connectivity : ConnectivityManager?=null
    var info : NetworkInfo?=null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()
        if(hasConnection()){
            setupRv()
        }else{
            showBottomSheet()
        }
        checkNetworkConnection()

        viewModel.isLoading.observe(viewLifecycleOwner){
            if(!it){ hideLoading() }
        }
    }
    private fun hasConnection():Boolean{
        connectivity=requireContext().getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(connectivity!=null){
            info=connectivity!!.activeNetworkInfo
            if(info!=null){
                return info!!.state==NetworkInfo.State.CONNECTED
            }

        }
        return false

    }

    private fun checkNetworkConnection() {
        cld = ConnectionLiveData(requireActivity().application)
        cld.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                setupRv()
            } else {
                showBottomSheet()
            }

        }

    }

    private fun showBottomSheet(){

        val bottomSheetDialog = BottomSheetDialog(
            requireContext(), R.style.BottomSheetDialogTheme
        )
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.layout_bottom_sheet,
            binding2?.bottomSheet
        )

        bottomSheetView.findViewById<View>(R.id.btnOkay).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun hideLoading() {
        loadingDialog?.let {
            if(it.isShowing){
                it.cancel()
            }
        }
    }

    private fun showLoading() {
        hideLoading()
        loadingDialog = CommonUtils.showLoadingDialog(requireContext())
    }
    private fun setupRv() {

        categoryAdapter = CategoryAdapter{
            showLoading()
            if(hasConnection()){
                viewModel.getAllNews(it)
            }else{
                showBottomSheet()
            }

        }

        binding.rvCategory.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        viewModel.getAllNews("")
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.responseNews.observe(viewLifecycleOwner) { news ->
            newsAdapter.newsList = news.articles!!
        }

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