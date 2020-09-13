package io.kim_kong.jjin_re.features.photo_view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.bumptech.glide.RequestManager
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.adapter.PhotoRVAdapter
import io.kim_kong.jjin_re.databinding.DialogPhotoViewLayoutBinding
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.SnapHelperOneByOne
import io.kim_kong.jjin_re.utils.SpeedyLinearLayoutManager

class PhotoViewDialog(private val requestManager: RequestManager, private val imgUri: List<String>): DialogFragment() {
    val viewModel by GetViewModel(PhotoViewViewModel::class.java)
    private lateinit var photoRVAdapter: PhotoRVAdapter
    private lateinit var linearSnapHelper: LinearSnapHelper
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = setUpDialog()
        viewModel.requestManager.postValue(requestManager)
        viewModel.imgUri.postValue(imgUri)
        return dialog
    }

    private fun setUpDialog(): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_photo_view_layout, null, false)
        val binding = DialogPhotoViewLayoutBinding.bind(view)
        binding.lifecycleOwner = this@PhotoViewDialog
        binding.viewModel = viewModel

        linearSnapHelper = SnapHelperOneByOne()
        photoRVAdapter = PhotoRVAdapter(requestManager)

        binding.rvPhoto.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = SpeedyLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            linearSnapHelper.attachToRecyclerView(binding.rvPhoto)
            adapter = photoRVAdapter
        }

        val dialog = AlertDialog.Builder(activity)
            .setView(view)
            .create()

        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                dialog.dismiss()
                return@setOnKeyListener true
            }
            false
        }

        viewModel.imgUri.observe(this@PhotoViewDialog, {
            photoRVAdapter.setData(it)
        })

        return dialog
    }
}