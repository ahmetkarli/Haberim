package com.ahmetkarli.haberim.ui.profile

import android.app.Dialog
import android.app.Service
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetkarli.haberim.R
import com.ahmetkarli.haberim.databinding.FragmentProfileBinding
import com.ahmetkarli.haberim.databinding.FragmentTopHeadlinesBinding
import com.ahmetkarli.haberim.databinding.LayoutBottomSheetBinding
import com.ahmetkarli.haberim.helper.CommonUtils
import com.ahmetkarli.haberim.helper.ConnectionLiveData
import com.ahmetkarli.haberim.ui.profile.adapter.SavedNewsAdapter
import com.ahmetkarli.haberim.ui.topheadlines.adapter.CategoryAdapter
import com.ahmetkarli.haberim.ui.topheadlines.adapter.NewsAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var newsAdapter: SavedNewsAdapter
    private lateinit var cld: ConnectionLiveData

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
                return info!!.state== NetworkInfo.State.CONNECTED
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
        viewModel.getSavedNews()

        newsAdapter = SavedNewsAdapter(requireContext()){
            showLoading()
            if(hasConnection()){
                viewModel.deleteArticle(it)
            }else{
                showBottomSheet()
            }

        }
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner) { news ->
            newsAdapter.newsList = news
            hideLoading()
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}