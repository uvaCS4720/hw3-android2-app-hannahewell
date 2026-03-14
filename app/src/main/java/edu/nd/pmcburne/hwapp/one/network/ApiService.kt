package edu.nd.pmcburne.hwapp.one.network

import androidx.activity.viewModels
import edu.nd.pmcburne.hwapp.one.MainViewModel
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import kotlin.getValue


private var BASE_URL = "https://ncaa-api.henrygd.me/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiService{
    @GET("scoreboard/basketball-women/d1/{url}")
    suspend fun getData(
        @Path("url") url: String): String
}

object API{
    val retrofitService : ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }
}