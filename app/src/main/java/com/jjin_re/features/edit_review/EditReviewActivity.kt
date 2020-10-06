package com.jjin_re.features.edit_review

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
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
import com.jjin_re.R
import com.jjin_re.adapter.AddReviewPhotoRVAdapter
import com.jjin_re.databinding.ActivityEditReviewBinding
import com.jjin_re.model.ReviewModel
import com.jjin_re.utils.*
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLDecoder
import java.util.*
import kotlin.collections.ArrayList

class EditReviewActivity: BaseActivity() {
    val binding by binding<ActivityEditReviewBinding>(R.layout.activity_edit_review)
    val viewModel by GetViewModel(EditReviewViewModel::class.java)
    lateinit var requestManager: RequestManager
    lateinit var addReviewPhotoRVAdapter: AddReviewPhotoRVAdapter
    private lateinit var mRevealAnimation: RevealAnimation

    lateinit var reviewModel : ReviewModel

    private var isDeletePhotoItem = false
    private var isReloadImagePhoto = false
    private var postUrlList = ArrayList<String>()
    private var postPhotoList = ArrayList<Photo>()
    lateinit var name : RequestBody

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_modify, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.menu_modify -> {
                if (viewModel.editReviewProductName.value!!.isNotEmpty() && viewModel.editReviewContents.value!!.isNotEmpty() && viewModel.postUriList.value!!.size > 2) {
                    progressON()
                    viewModel.sendFile()
                } else {
                    Utils.showSnackBar("내용을 채워주세요.", binding.root, true)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@EditReviewActivity)
        setSupportActionBar(binding.tbEditReview)
        Utils.setIconTintDark(this@EditReviewActivity, true)
        binding.lifecycleOwner = this@EditReviewActivity
        binding.viewModel = viewModel
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding.tbEditReview.setNavigationOnClickListener {
            mRevealAnimation.unRevealActivity()
        }

        reviewModel = intent.getSerializableExtra("EXTRA_REVIEW") as ReviewModel
        viewModel.reviewModel.postValue(reviewModel)
        val x = intent.getIntExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, 0)
        val y = intent.getIntExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, 0)

        mRevealAnimation = RevealAnimation(binding.root, x, y, this@EditReviewActivity)

        requestManager = Glide.with(this@EditReviewActivity)
        initEditReviewActivity()


        viewModel.postPhotoList.observe(this@EditReviewActivity, {
            if (it.size > 0) {
                binding.tvEditReviewPhotoCount.text = "${it.size}/10"
                if (!isReloadImagePhoto) it.add(0, Photo(null, isClickable = false, isDelActive = false))
                addReviewPhotoRVAdapter.setPhotoData(it)
                if (viewModel.postUriList.value!!.size > 0) {
                    val imageList = ArrayList<MultipartBody.Part>()
                    val nameList = ArrayList<RequestBody>()
                    for (uri in viewModel.postUriList.value!!) {
                        if (uri.scheme != "https") {
                            val path = FileUtil.getPath(uri, this@EditReviewActivity)
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
                    }
                    viewModel.multipartList.postValue(imageList)
                    viewModel.requestBody.postValue(nameList)

                }
            }
        })

        viewModel.responseCode.observe(this@EditReviewActivity, {
            if (!it.isNullOrEmpty() && it == "200") {
                Utils.showToast(viewModel.responseMessage.value!!, this@EditReviewActivity)
                progressOFF()
                mRevealAnimation.unRevealActivity()
            } else if (!it.isNullOrEmpty()) {
                Utils.showSnackBar(viewModel.responseMessage.value!!, binding.root, true)
            }
        })

        viewModel.editReviewPhotoList.observe(this@EditReviewActivity, {
            if (!it.isNullOrEmpty()) viewModel.editReview()
        })
    }

    @SuppressLint("SetTextI18n")
    fun initEditReviewActivity() {
        addReviewPhotoRVAdapter = AddReviewPhotoRVAdapter(requestManager)
        binding.rvEditReviewPhoto.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = SpeedyLinearLayoutManager(
                this@EditReviewActivity,
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

        viewModel.editReviewContents.postValue(reviewModel.contents)
        viewModel.editReviewProductName.postValue(reviewModel.productName)
        viewModel.editReviewProductRating.postValue("${reviewModel.rating}")

        binding.ratingEditReview.rating = reviewModel.rating
        binding.spinnerEditReviewCategory.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            arrCategory
        )
        binding.spinnerEditReviewCategory.setSelection(reviewModel.category.toInt() - 1)
        binding.spinnerEditReviewCategory.background.setColorFilter(
            ContextCompat.getColor(
                this@EditReviewActivity,
                R.color.colorBlack
            ), PorterDuff.Mode.SRC_ATOP
        )
        binding.spinnerEditReviewCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.editReviewCategory.postValue("${position + 1}")
                when(position) {
                    8 -> {
                        binding.etEditReviewCategory.visibility = View.VISIBLE
                        viewModel.editReviewEtcCategory.postValue(reviewModel.categoryEtc)
                    }
                    else -> {
                        binding.etEditReviewCategory.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        binding.ratingEditReview.setOnRatingChangeListener { _, rating, _ ->
            viewModel.editReviewProductRating.postValue("$rating")
        }

        if (reviewModel.imgUrl.isNotEmpty()) {
            try {
                val urlArr = reviewModel.imgUrl.split("[@]")
                postUrlList.addAll(urlArr)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            for (url in postUrlList) {
                viewModel.postUriList.value!!.add(Uri.parse(url))
                requestManager.asBitmap().override(300, 300).load(url)
                    .into(object : SimpleTarget<Bitmap?>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                            postPhotoList.add(Photo(resource, isClickable = false, isDelActive = true))
                            viewModel.postPhotoList.postValue(postPhotoList)
                        }
                    })
            }
        }

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
    }

    @SuppressLint("SetTextI18n")
    fun onCallBottomPicker() {
        val intRange: IntRange = if (!isReloadImagePhoto) 2..10
        else 1..(10 - viewModel.postUriList.value!!.size)

        TedImagePicker.with(this@EditReviewActivity)
            .title("구매내역필수첨부")
            .mediaType(MediaType.IMAGE)
            .backButton(R.drawable.ic_arr_back_black)
            .startMultiImage { uriList ->
                if (uriList.size in intRange) {
                    viewModel.postUriList.value!!.addAll(uriList)
                    for (item in viewModel.postUriList.value!!) {
                        if (item.scheme != "https") {
                            val path = FileUtil.getPath(item, this)
                            val options = BitmapFactory.Options()
                            options.inSampleSize = 2
                            val src = BitmapFactory.decodeFile(path, options)
                            val orientation = Utils.getOrientationOfImage(path)
                            val changeOrientationSrc = Utils.getRotatedBitmap(src, orientation)
                            postPhotoList.add(
                                Photo(
                                    changeOrientationSrc!!,
                                    isClickable = false,
                                    isDelActive = true
                                )
                            )
                        }
                        viewModel.postPhotoList.postValue(postPhotoList)
                    }
                } else {
                    onCallBottomPicker()
                    Utils.showToast("사진은 2장이상 10장이하로 첨부 가능합니다.", this@EditReviewActivity)
                }
            }
    }

    override fun onBackPressed() {
        mRevealAnimation.unRevealActivity()
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@EditReviewActivity)
    }
}