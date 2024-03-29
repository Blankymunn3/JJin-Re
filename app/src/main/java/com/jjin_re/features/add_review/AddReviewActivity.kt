package com.jjin_re.features.add_review

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.jjin_re.R
import com.jjin_re.adapter.AddReviewPhotoRVAdapter
import com.jjin_re.databinding.ActivityAddReviewBinding
import com.jjin_re.model.ReviewModel
import com.jjin_re.utils.*
import com.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_X
import com.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_Y
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.Serializable
import java.net.URLDecoder
import java.util.*
import kotlin.collections.ArrayList


open class AddReviewActivity: BaseActivity() {
    val binding by binding<ActivityAddReviewBinding>(R.layout.activity_add_review)
    val viewModel by GetViewModel(AddReviewViewModel::class.java)

    private lateinit var mRevealAnimation: RevealAnimation
    private var isViewVisible = false
    private var isCancelImageSelect = false

    private lateinit var addReviewPhotoRVAdapter: AddReviewPhotoRVAdapter
    private lateinit var requestManager: RequestManager

    private var postPhotoList = ArrayList<Photo>()
    lateinit var name : RequestBody

    private var isDeletePhotoItem = false
    private var isReloadImagePhoto = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_upload, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.menu_upload -> {
                if (viewModel.addReviewProductName.value!!.isNotEmpty() && viewModel.addReviewContents.value!!.isNotEmpty() && viewModel.postUriList.value!!.size >= 2) {
                    progressON()
                    viewModel.sendFile()
                } else {
                    Utils.showSnackBar("내용을 채워주세요.", binding.layoutAddReviewBody, true)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@AddReviewActivity)

        initActivityView()

        addReviewPhotoRVAdapter.setItemClick(object :
            AddReviewPhotoRVAdapter.OnItemClick {
            override fun onDeleteClick(position: Int) {
                isDeletePhotoItem = true
                viewModel.postUriList.value!!.removeAt(position - 1)
                postPhotoList.removeAt(position)
                viewModel.postPhotoList.postValue(postPhotoList)
                if (viewModel.postUriList.value!!.size == 0) onCallBottomPicker()
            }

            override fun onAddClick(position: Int) {
                isReloadImagePhoto = true
                onCallBottomPicker()
            }

        })

        binding.ratingAddReview.setOnRatingChangeListener { _, rating, _ ->
            viewModel.addReviewProductRating.postValue("$rating")
        }

        viewModel.postPhotoList.observe(this@AddReviewActivity, {
            if (it.size > 0) {
                binding.tvAddReviewPhotoCount.text = "${it.size}/10"
                if (!isDeletePhotoItem)
                    it.add(0, Photo(null, isClickable = false, isDelActive = false))
                addReviewPhotoRVAdapter.setPhotoData(it)
                if (viewModel.postUriList.value!!.size > 0) {
                    val imageList = ArrayList<MultipartBody.Part>()
                    val nameList = ArrayList<RequestBody>()
                    for (uri in viewModel.postUriList.value!!) {
                        val path = FileUtil.getPath(uri, this@AddReviewActivity)
                        val file = File(path!!)
                        try {
                            val requestFile = RequestBody.create(
                                "multipart/form-data".toMediaTypeOrNull(),
                                file
                            )
                            val body =
                                MultipartBody.Part.createFormData("img", file.name, requestFile)
                            name = RequestBody.create(MultipartBody.FORM, file.name)
                            imageList.add(body)
                            nameList.add(name)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    viewModel.multipartList.postValue(imageList)
                    viewModel.requestBody.postValue(nameList)

                }
            }
        })

        viewModel.responseCode.observe(this@AddReviewActivity, {
            if (!it.isNullOrEmpty() && it == "200") {
                Utils.showToast(viewModel.responseMessage.value!!, this@AddReviewActivity)
                progressOFF()
                mRevealAnimation.unRevealActivity()
            } else if (!it.isNullOrEmpty()) {
                progressOFF()
                Utils.showSnackBar(viewModel.responseMessage.value!!, binding.root, true)
            }
        })

        viewModel.addReviewPhotoList.observe(this@AddReviewActivity, {
            if (!it.isNullOrEmpty()) viewModel.writeReview()
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initActivityView() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        Utils.setIconTintDark(this@AddReviewActivity, true)
        binding.lifecycleOwner = this@AddReviewActivity
        binding.viewModel = viewModel

        setSupportActionBar(binding.tbAddReview)
        binding.tbAddReview.setNavigationOnClickListener {
            mRevealAnimation.unRevealActivity()
        }

        val x = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
        val y = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)


        mRevealAnimation = RevealAnimation(binding.layoutAddReviewMain, x, y, this@AddReviewActivity)


        requestManager = Glide.with(this@AddReviewActivity)
        addReviewPhotoRVAdapter = AddReviewPhotoRVAdapter(requestManager)
        binding.rvAddReviewPhoto.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = SpeedyLinearLayoutManager(
                this@AddReviewActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = addReviewPhotoRVAdapter
        }
        val arrCategory = ArrayList<String>()
        for ((i, str) in resources.getStringArray(R.array.str_review_category).withIndex()) {
            if(i != 0) {
                arrCategory.add(str)
            }
        }
        binding.spinnerAddReviewCategory.adapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrCategory)
        binding.spinnerAddReviewCategory.setSelection(0)
        binding.spinnerAddReviewCategory.background.setColorFilter(
            ContextCompat.getColor(
                this@AddReviewActivity,
                R.color.colorBlack
            ), PorterDuff.Mode.SRC_ATOP
        )
        binding.spinnerAddReviewCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.addReviewCategory.postValue("${position + 1}")
                when(position) {
                    8 -> {
                        binding.etAddReviewCategory.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.etAddReviewCategory.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun checkPermission() {
        val listener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Log.d("Permission :: ", "GRANTED")
                onCallBottomPicker()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Log.d("Permission :: ", "DENIED")
                Utils.showToast("권한을 허용해야 리뷰작성이 가능합니다.", this@AddReviewActivity)
                finish()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(listener)
            .setRationaleMessage("리뷰 작성을 이용하기 위해서는 권한이 필요합니다.")
            .setDeniedMessage("권한을 허용하지 않으시면 앱 서비스를 정상적으로 이용할 수 없습니다.")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).check()
    }

    @SuppressLint("SetTextI18n")
    fun onCallBottomPicker() {
        val intRange: IntRange = if (!isReloadImagePhoto) 2..10
        else 1..(10 - viewModel.postUriList.value!!.size)
        if (!isCancelImageSelect) {
            isCancelImageSelect = true
            TedImagePicker.with(this@AddReviewActivity)
                .title("구매내역필수첨부")
                .mediaType(MediaType.IMAGE)
                .backButton(R.drawable.ic_arr_back_black)
                .startMultiImage { uriList ->
                    if (uriList.size in intRange) {
                        val postPhoto: ArrayList<Photo> = ArrayList()
                        viewModel.postUriList.value!!.addAll(uriList)
                        isViewVisible = true
                        for (item in viewModel.postUriList.value!!) {
                            val path = FileUtil.getPath(item, this)
                            val options = BitmapFactory.Options()
                            options.inSampleSize = 2
                            val src = BitmapFactory.decodeFile(path, options)
                            val orientation = Utils.getOrientationOfImage(path)
                            val changeOrientationSrc = Utils.getRotatedBitmap(src, orientation)
                            if (changeOrientationSrc == null)
                                postPhoto.add(Photo(src, isClickable = false, isDelActive = true))
                             else
                                postPhoto.add(Photo(changeOrientationSrc, isClickable = false, isDelActive = true))

                            postPhotoList = postPhoto
                            viewModel.postPhotoList.postValue(postPhoto)
                        }
                    } else {
                        onCallBottomPicker()
                        Utils.showToast("사진은 2장이상 10장이하로 첨부 가능합니다.", this@AddReviewActivity)
                    }

                }
        } else {
            finish()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && !isViewVisible) {
            if (!TedPermission.isGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
                checkPermission()
            } else {
                onCallBottomPicker()
            }
        }
    }

    override fun onBackPressed() {
        mRevealAnimation.unRevealActivity()
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@AddReviewActivity)
    }

}