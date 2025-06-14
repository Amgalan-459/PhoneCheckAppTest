package com.example.phonecheckapp1.ui.phone_num

import androidx.lifecycle.ViewModel
import com.example.phonecheckapp1.data.remote.api.phoneNum.PhoneApi
import com.example.phonecheckapp1.data.remote.dto.PhoneInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import java.io.IOException

class PhoneNumViewModel(
    private val phoneApi: PhoneApi
) : ViewModel() {
    private val _phoneInfo = MutableStateFlow<PhoneInfo?>(null)
    val phoneInfo: StateFlow<PhoneInfo?> = _phoneInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    suspend fun fetchDataPhoneInf(phoneNum: String): String {
        _isLoading.value = true
        try {
            val respone = phoneApi.getPhoneInfo(phoneNum)

            _phoneInfo.value = respone
        }
        catch (e: HttpException) {
            return "Http Error ${e.code()}"
        }
        catch (e: IOException) {
            return "IO Error ${e.message}"
        }
        catch (e: Exception) {
            return "Error ${e.message}"
        }
        finally {
            _isLoading.value = false
        }
        return ""
    }
}