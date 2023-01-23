package com.vylo.main.component.bottomsheet.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.util.GoogleAnalytics
import com.vylo.common.util.enums.ResponseType
import com.vylo.main.R
import com.vylo.main.component.bottomsheet.domain.entity.SheetModel
import com.vylo.main.component.sharedviewmodel.ActivityFragmentSharedViewModel
import com.vylo.main.databinding.ItemSheetBinding

class BottomSheetAdapter(
    adapterCallBack: AdapterCallBack,
    private val context: Context,
    private val viewModel: ActivityFragmentSharedViewModel
) : RecyclerView.Adapter<BottomSheetAdapter.SheetHolder>() {

    interface AdapterCallBack {
        fun setAdapterType(responseType: Int)
    }

    private val callBack = adapterCallBack
    private var data = mutableListOf<SheetModel>()

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetHolder {
        val binding = ItemSheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SheetHolder(binding)
    }

    override fun onBindViewHolder(holder: SheetHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class SheetHolder(private val binding: ItemSheetBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sheetModel: SheetModel) {

            binding.actionImage.setBackgroundResource(sheetModel.imageId)
            binding.actionName.text = sheetModel.name

            itemView.setOnClickListener {
                when(sheetModel.name) {
                    context.resources.getString(R.string.label_record_video_sheet) -> {
                        callBack.setAdapterType(ResponseType.VIDEO.type)
                        viewModel.setUploadType(GoogleAnalytics.CREATE_VIDEO)
                    }
                    context.resources.getString(R.string.label_record_audi_sheet) -> {
                        callBack.setAdapterType(ResponseType.AUDIO.type)
                        viewModel.setUploadType(GoogleAnalytics.CREATE_AUDIO)
                    }
                    context.resources.getString(R.string.label_upload_video_sheet) -> {
                        callBack.setAdapterType(ResponseType.UPLOAD.type)
                        viewModel.setUploadType(GoogleAnalytics.CREATE_UPLOAD)
                    }
                }

            }
        }
    }

    fun setData(data: List<SheetModel>) {
        this.data.addAll(data)
    }

    fun clearData() {
        data.clear()
    }
}