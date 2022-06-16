package com.ahmetkarli.haberim.ui.explore

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.Service
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetkarli.haberim.R
import com.ahmetkarli.haberim.databinding.FragmentExploreBinding
import com.ahmetkarli.haberim.databinding.LayoutBottomSheetBinding
import com.ahmetkarli.haberim.helper.CommonUtils
import com.ahmetkarli.haberim.helper.ConnectionLiveData
import com.ahmetkarli.haberim.ui.explore.adapter.ExploreAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ExploreFragment : Fragment() {


    private val viewModel: ExploreViewModel by viewModels()
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!


    private lateinit var newsAdapter: ExploreAdapter
    private lateinit var cld: ConnectionLiveData

    private var binding2: LayoutBottomSheetBinding? = null
    private var loadingDialog: Dialog? = null
    var connectivity: ConnectivityManager? = null
    var info: NetworkInfo? = null

    var cal = Calendar.getInstance()

    var query: String = ""
    var fromTime: String = ""
    var toTime: String = ""
    var sortBy: String = ""

    val sortByList = arrayListOf("publishedAt", "relevancy", "popularity")
    var pageArray = ArrayList<Int>()

    var page = 1

    override fun onResume() {
        super.onResume()

        super.onStart()

        val search = binding.searchText.text.toString().trim()
        if (search.isEmpty() || search.length < 3) {

        } else {
            showLoading()
            if (hasConnection()) {
                setupRv(1)
            } else {
                showBottomSheet("Lütfen internet bağlantınızı kontrol ediniz !")
            }

        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (!it) {
                hideLoading()
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgClear.setOnClickListener {
            binding.dateFrom.setText("")
            binding.dateTo.setText("")
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_sortName,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSortBy.adapter = adapter
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        val dateSetListenerTo = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInViewTo()
            }
        }
        binding.dateFrom.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })

        binding.dateTo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    requireContext(),
                    dateSetListenerTo,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })

        viewModel.responseNews.observe(viewLifecycleOwner) { result ->
            binding.rvNews.visibility=View.VISIBLE
            binding.txtSearchNumber.visibility = View.VISIBLE
            binding.txtPageNumber.visibility = View.VISIBLE
            binding.txtPageNumber2.visibility = View.VISIBLE
            binding.btnGo.visibility = View.VISIBLE
            binding.txtPageNumber2.text = "/" + (result.totalResults?.div(20)?.plus(1).toString())
            binding.txtSearchNumber.text = "Arama Sonucu (" + result.totalResults.toString() + "),"
            binding.spinnerPageNumber.visibility = View.VISIBLE
            result.totalResults?.let {
                pageArray.clear()
                for (i in 0..result.totalResults / 20) {
                    pageArray.add(i, i + 1)
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, pageArray)
                binding.spinnerPageNumber.adapter = adapter
                binding.spinnerPageNumber.setSelection(page-1)
            }
        }

        binding.btnGo.setOnClickListener {

            page = pageArray[binding.spinnerPageNumber.selectedItemPosition]

            val search = binding.searchText.text.toString().trim()
            if (search.isEmpty() || search.length < 3) {
                showBottomSheet("Aranacak kelime boş veya üç karakterden az olamaz !")
            } else {
                showLoading()
                if (hasConnection()) {
                    setupRv(page)
                } else {
                    hideLoading()
                    showBottomSheet("Lütfen internet bağlantınızı kontrol ediniz !")
                }
            }
        }


        binding.btnSearch.setOnClickListener {

            val search = binding.searchText.text.toString().trim()
            if (search.isEmpty() || search.length < 3) {
                showBottomSheet("Aranacak kelime boş veya üç karakterden az olamaz !")
            } else {
                showLoading()
                if (hasConnection()) {
                    setupRv(1)
                } else {
                    hideLoading()
                    showBottomSheet("Lütfen internet bağlantınızı kontrol ediniz !")
                }

            }

        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (!it) {
                hideLoading()
            }
        }

    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.dateFrom.setText(sdf.format(cal.time))
    }

    private fun updateDateInViewTo() {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.dateTo.setText(sdf.format(cal.time))
    }

    private fun setupRv(page: Int) {

        query = binding.searchText.text.toString()
        fromTime = binding.dateFrom.text.toString()
        toTime = binding.dateTo.text.toString()
        sortBy = sortByList[binding.spinnerSortBy.selectedItemPosition]

        println(query + fromTime + toTime + sortBy+page.toString())

        viewModel.getAllNewsBySearch(query, fromTime, toTime, sortBy, page)

        newsAdapter = ExploreAdapter(requireContext())
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.responseNews.observe(viewLifecycleOwner) { news ->
            newsAdapter.newsList = news.articles!!
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


    private fun showBottomSheet(message: String) {

        val bottomSheetDialog = BottomSheetDialog(
            requireContext(), R.style.BottomSheetDialogTheme
        )
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
            R.layout.layout_bottom_sheet,
            binding2?.bottomSheet
        )

        bottomSheetView.findViewById<TextView>(R.id.txtWarning).text = message

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
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}