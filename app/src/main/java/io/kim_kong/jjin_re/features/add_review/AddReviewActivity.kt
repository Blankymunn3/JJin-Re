package io.kim_kong.jjin_re.features.add_review

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.adapter.AddReviewPhotoRVAdapter
import io.kim_kong.jjin_re.databinding.ActivityAddReviewBinding
import io.kim_kong.jjin_re.utils.*
import io.kim_kong.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_X
import io.kim_kong.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_Y
import java.util.*


open class AddReviewActivity: BaseActivity() {
    val binding by binding<ActivityAddReviewBinding>(R.layout.activity_add_review)
    val viewModel by GetViewModel(AddReviewViewModel::class.java)

    private lateinit var mRevealAnimation: RevealAnimation
    private var isViewVisible = false

    private lateinit var addReviewPhotoRVAdapter: AddReviewPhotoRVAdapter
    private lateinit var requestManager: RequestManager

    private var postPhotoList = ArrayList<Photo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@AddReviewActivity)
        Utils.setIconTintDark(this@AddReviewActivity, true)
        binding.viewModel = viewModel
        setSupportActionBar(binding.tbAddReview)
        binding.tbAddReview.setNavigationOnClickListener {
            finish()
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

        addReviewPhotoRVAdapter.setDeleteItemClick(object :
            AddReviewPhotoRVAdapter.DeleteItemClick {
            override fun onClick(position: Int) {
                viewModel.postUriList.value!!.removeAt(position)
                postPhotoList.removeAt(position)
                viewModel.postPhotoList.postValue(postPhotoList)
            }

        })

        binding.spinnerAddReviewCategory.adapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.str_review_category))
        binding.spinnerAddReviewCategory.setSelection(0)
        binding.spinnerAddReviewCategory.background.setColorFilter(ContextCompat.getColor(this@AddReviewActivity, R.color.colorBlack), PorterDuff.Mode.SRC_ATOP)
        binding.spinnerAddReviewCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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

        viewModel.postPhotoList.observe(this@AddReviewActivity, {
            binding.tvAddReviewPhotoCount.text = "${it.size}/10"
            addReviewPhotoRVAdapter.setData(it)
        })
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
            .setRationaleMessage("덕스토리 작성을 이용하기 위해서는 권한이 필요합니다.")
            .setDeniedMessage("권한을 허용하지 않으시면 앱 서비스를 정상적으로 이용할 수 없습니다.")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).check()
    }

    @SuppressLint("SetTextI18n")
    fun onCallBottomPicker() {
        TedImagePicker.with(this@AddReviewActivity)
            .title("구매내역필수첨부")
            .mediaType(MediaType.IMAGE)
            .backButton(R.drawable.ic_arr_back_black)
            .startMultiImage { uriList ->
                if (uriList.size <= 10) {
                    viewModel.postUriList.value!!.addAll(uriList)
                    isViewVisible = true
                    for (item in viewModel.postUriList.value!!) {
                        val path = FileUtil.getPath(item, this)
                        val options = BitmapFactory.Options()
                        options.inSampleSize = 2
                        val src = BitmapFactory.decodeFile(path, options)
                        val orientation = Utils.getOrientationOfImage(path)
                        val changeOrientationSrc = Utils.getRotatedBitmap(src, orientation)
                        postPhotoList.add(Photo(changeOrientationSrc!!, isClickable = false, isDelActive = true))
                        viewModel.postPhotoList.postValue(postPhotoList)
                    }
                } else {
                    onCallBottomPicker()
                    Utils.showToast("사진은 10장이하로 첨부 가능합니다.", this@AddReviewActivity)
                }
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