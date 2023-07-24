package com.example.vegasfirebase

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import javax.inject.Inject

interface JSONPlaceHolderApi {
    @POST("send")
    fun postData(@Body body: FcmPostData,
                 @Header("Authorization") authorization:String,
                 ): retrofit2.Call<FcmResponsData>
}

class FcmData {
    @SerializedName("some_important_id")
    var some_important_id: String = "55"
}

class FcmNotification(title: String, body: String) {
    @SerializedName("body")
    var body: String = title

    @SerializedName("title")
    var title: String = body
}

class FcmPostData (to: String, title: String, body: String){

    @SerializedName("to")
    var to: String = to

    @SerializedName("notification")
    var notification = FcmNotification(title, body)

    @SerializedName("data")
    var data = FcmData()
}

data class FcmResultsData(
    var message_id: String? = null,
)

data class FcmResponsData(
    var multicast_id: String? = null,
    var success: String? = null,
    var failure: String? = null,
    var canonical_ids: String? = null,
    var results: MutableList<FcmResultsData>? = null,
)

// https://console.firebase.google.com/project/vegasfirebase/settings/cloudmessaging/android:com.example.vegasfirebase
const val server_key = "key=AAAAt9K8BH4:APA91bF_PmP3UWHKuKVBzBvtm19pLl-_iNcXWTa5zMDTP5BFLyg_vXKgvZ5op_2VCljf9FFLDaMYrKPvUeTWnCis9GgzuuPHGEBO6urWNeaYXZPVGOLhm-nWQNsXdKgWLVj0JUoWoQsd"

@HiltViewModel
class VegasViewModel @Inject constructor(

) : ViewModel() {

    var askToExitFromApp: Boolean = true

    var currentTheme = mutableStateOf(0)
    val keepScreenOn = mutableStateOf(false)

    var token =  mutableStateOf("")

    var title = "Firebase notification"
    var body = "Hello World"

    var notification_title =  mutableStateOf("")
    var notification_body =  mutableStateOf("")

    fun fmcRequest() {

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(
            {

                val fmcRetrofit: Retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://fcm.googleapis.com/fcm/")
                    .build()

                var fmcService = fmcRetrofit.create(JSONPlaceHolderApi::class.java)
                val fmcCall = fmcService.postData(FcmPostData(it, title, body), server_key)

                fmcCall.enqueue(
                    object : retrofit2.Callback<FcmResponsData> {
                        override fun onResponse(
                            call: retrofit2.Call<FcmResponsData>,
                            response: retrofit2.Response<FcmResponsData>
                        ) {
                            response.body()?.let { resp -> Log.d("fcm", "fcm respons: ${resp}") }
                        }

                        override fun onFailure(call: retrofit2.Call<FcmResponsData>, t: Throwable) {
                            Log.d("fcm", "onFailure: ${t}")
                        }
                    }
                )
        })
    }
}