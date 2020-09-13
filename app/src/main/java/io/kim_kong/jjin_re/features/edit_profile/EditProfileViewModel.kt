package io.kim_kong.jjin_re.features.edit_profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.kim_kong.jjin_re.model.UserModel

class EditProfileViewModel: ViewModel() {
    val userModel : MutableLiveData<UserModel> = MutableLiveData()
}