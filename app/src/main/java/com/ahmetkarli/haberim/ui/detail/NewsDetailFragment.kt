package com.ahmetkarli.haberim.ui.detail


import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ahmetkarli.haberim.R
import com.ahmetkarli.haberim.databinding.FragmentNewsDetailBinding
import com.ahmetkarli.haberim.databinding.LayoutBottomSheetBinding
import com.ahmetkarli.haberim.helper.CommonUtils
import com.ahmetkarli.haberim.helper.ConnectionLiveData
import com.ahmetkarli.haberim.models.ArticleDbModel
import com.ahmetkarli.haberim.ui.topheadlines.TopHeadlinesFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding!!

    private val args: NewsDetailFragmentArgs by navArgs()

    private val viewModel: NewsDetailViewModel by viewModels()

    private var binding2: LayoutBottomSheetBinding? = null

    private var loadingDialog: Dialog? = null

    var connectivity: ConnectivityManager? = null
    var info: NetworkInfo? = null
    private lateinit var cld: ConnectionLiveData

    val isLoading = MutableLiveData<Boolean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val article = args.articleDb
        val url = article.url
        val title = article.title

        binding.toolbar.inflateMenu(R.menu.menu_toolbar)
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_fav-> {
                    viewModel.saveArticle(ArticleDbModel(publishedAt = article.publishedAt, title = article.title, url = article.url, urlToImage = article.urlToImage))
                    Snackbar.make(view,"Haber favorilere eklendi.",Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.action_share-> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "$title \n\n $url")
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                    true
                }
                else-> false
            }
        }

        showLoading()
        if (hasConnection()) {
            setupWebView()
        } else {
            showBottomSheet()
        }
        checkNetworkConnection()

        isLoading.observe(viewLifecycleOwner) {
            if (!it) {
                hideLoading()
            }
        }


    }

    private fun setupWebView() {
        val url = args.articleDb.url
        binding.webview.webViewClient = WebViewClient()
        binding.webview.loadUrl(url!!)
        url.let {
            binding.webview.webViewClient = object : WebViewClient() {
                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    isLoading.postValue(false)
                }
            }
        }
    }

    private fun hasConnection(): Boolean {
        connectivity =
            requireContext().getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            info = connectivity!!.activeNetworkInfo
            if (info != null) {
                return info!!.state == NetworkInfo.State.CONNECTED
            }

        }
        return false

    }

    private fun checkNetworkConnection() {
        cld = ConnectionLiveData(requireActivity().application)
        cld.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                setupWebView()

            } else {
                showBottomSheet()
            }

        }

    }

    private fun showBottomSheet() {

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
            if (it.isShowing) {
                it.cancel()
            }
        }
    }

    private fun showLoading() {
        hideLoading()
        loadingDialog = CommonUtils.showLoadingDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
    }



}