package mohammad.toriq.util

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import mohammad.toriq.configs.Constants
import okhttp3.*
import java.io.InputStream
import java.util.concurrent.TimeUnit


@GlideModule
open class MyAppGlideModule : AppGlideModule(){
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = OkHttpClient.Builder()
            .connectTimeout(Constants.IMG_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(Constants.IMG_READ_TIMEOUT, TimeUnit.MILLISECONDS)
//        .writeTimeout(Constants.IMG_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()
//        val factory = OkHttpUrlLoader.Factory(client)
        val factory = OkHttpUrlLoader.Factory(client)
//        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)

    }
}
