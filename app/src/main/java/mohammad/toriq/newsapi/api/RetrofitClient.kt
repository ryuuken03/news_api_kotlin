package mohammad.toriq.newsapi.api

import android.content.Context
import com.google.gson.GsonBuilder
import mohammad.toriq.configs.Constants
import mohammad.toriq.util.Log
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.LoggingEventListener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.net.Proxy
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        private var retrofit: Retrofit? = null
        private val httpClient = OkHttpClient.Builder()

        private val CACHE_SIZE_BYTES: Long = 1024 * 1024 * 5
        var retry = 0
        var request_count = 0
        fun getClient(context: Context?): Retrofit? {
            if (Log.LOG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(logging)
            }
            // set Accept-Language header
            httpClient.addInterceptor(Interceptor { chain ->
                try {
                    val request =
                        chain.request().newBuilder().addHeader("Accept-Language", "id-ID")
                            .build()

                    return@Interceptor chain.proceed(request)

                } catch (sto: SocketTimeoutException) {
                    if (retry == 3) {
                        retry = 0
                        throw SocketTimeoutException()
                    } else {
                        retry++
                        val request =
                            chain.request().newBuilder().addHeader("Accept-Language", "id-ID")
                                .build()
                        return@Interceptor chain.proceed(request)
                    }
                }
            })

            var clientRetrofit = retrofit

            if (clientRetrofit == null) {
                if (context != null) {
//                    var printListener = PrintingEventListener()
//                    httpClient.eventListener(printListener)

                    if (Log.LOG) {
                        val loggingListener = LoggingEventListener.Factory()
                        httpClient.eventListenerFactory(loggingListener)
                    }

                    httpClient.cache(Cache(context.cacheDir, CACHE_SIZE_BYTES))
                    httpClient.connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS);
                }
                val gson = GsonBuilder().setLenient().create()
                clientRetrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

                retrofit = clientRetrofit
            }
            return clientRetrofit
        }
    }
}
