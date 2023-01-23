package com.vylo.main.component.bottomsheet.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vylo.common.util.TITLE_OF_SHEET
import com.vylo.main.R
import com.vylo.main.component.bottomsheet.domain.entity.SheetModel
import com.vylo.main.component.bottomsheet.presentation.adapter.BottomSheetAdapter
import com.vylo.main.component.sharedviewmodel.ActivityFragmentSharedViewModel
import com.vylo.main.databinding.ViewCreateResponseBinding

class BottomSheetDialog(
    private val viewModel: ActivityFragmentSharedViewModel
) : BottomSheetDialogFragment(),
    BottomSheetAdapter.AdapterCallBack {

    interface BottomSheetCallBack {
        fun openResponseScreen(responseType: Int)
    }

    private lateinit var viewBinder: ViewCreateResponseBinding
    private lateinit var adapter: BottomSheetAdapter
    private lateinit var bottomSheetCallBack: BottomSheetCallBack

    private val data = mutableListOf<SheetModel>()
    private var newsID: String? = null
    private var newsTitle: String? = null
    private var categoryId: String? = null
    private var categoryName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder =
            ViewCreateResponseBinding.inflate(LayoutInflater.from(context), container, false)
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    private fun beginning() {
        val title = arguments?.getString(TITLE_OF_SHEET)
        data.add(
            SheetModel(
                R.drawable.ic_icon_create_video,
                getString(R.string.label_record_video_sheet)
            )
        )
        data.add(
            SheetModel(
                R.drawable.ic_icon_create_audio,
                getString(R.string.label_record_audi_sheet)
            )
        )
        data.add(
            SheetModel(
                R.drawable.ic_icon_upload,
                getString(R.string.label_upload_video_sheet)
            )
        )
        adapter = BottomSheetAdapter(
            this,
            requireContext(),
            viewModel
        )
        viewBinder.labelCreate.text = title

        adapter.setData(data)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        viewBinder.actionList.layoutManager = linearLayoutManager
        viewBinder.actionList.adapter = adapter
        data.clear()
    }

    fun setNewsID(id: String?) {
        newsID = id
    }

    fun getNewsID() = newsID

    fun setNewsTitle(title: String?) {
        newsTitle = title
    }

    fun getNewsTitle() = newsTitle

    fun setCategoryId(id: String?) {
        categoryId = id
    }

    fun getCategoryId() = categoryId

    fun setCategoryName(name: String?) {
        categoryName = name
    }

    fun getCategoryName() = categoryName

    override fun setAdapterType(responseType: Int) {
        bottomSheetCallBack.openResponseScreen(responseType)
    }

    fun setBottomSheetListener(bottomSheetCallBack: BottomSheetCallBack) {
        this.bottomSheetCallBack = bottomSheetCallBack
    }
}