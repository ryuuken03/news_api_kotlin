package mohammad.toriq.configs

import androidx.multidex.BuildConfig

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
class Constants {
    companion object {
        val IS_LOG: Boolean = getIsLog()
        val BASE_URL = "https://newsapi.org/v2/"

        val CONNECT_TIMEOUT : Long = 60
        val READ_TIMEOUT: Long = 60
        val WRITE_TIMEOUT: Long = 60
        val IMG_CONNECT_TIMEOUT : Long = 10000
        val IMG_READ_TIMEOUT: Long = 10000
        val IMG_WRITE_TIMEOUT: Long = 10000
        val DATE_OUT_FORMAT_DEF0 = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val DATE_OUT_FORMAT_DEF1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val DATE_OUT_FORMAT_DEF2 = "yyyy-MM-dd"
        val DATE_OUT_FORMAT_DEF3 = "MMM dd, yyyy"

        fun getIsLog(): Boolean {
            return true
//            return  if (!BuildConfig.DEBUG) {
//                false
//            } else {
//                true
//            }
        }

    }
}