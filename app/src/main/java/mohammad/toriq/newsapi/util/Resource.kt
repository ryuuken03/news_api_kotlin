package mohammad.toriq.views.util

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
sealed class Resource<out T> {
    data class Loading<out T>(val state: Boolean) : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(val errorMessage: String) : Resource<T>()
}