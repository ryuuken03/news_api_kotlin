package mohammad.toriq.newsapi.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mohammad.toriq.newsapi.R
import mohammad.toriq.newsapi.models.ResponseSources
import mohammad.toriq.newsapi.models.Source
import mohammad.toriq.newsapi.util.Repository
import mohammad.toriq.newsapi.util.Util
import mohammad.toriq.views.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***
 * Created By Mohammad Toriq on 19/10/2023
 */
class SourceViewModel(
    context : Context
): ViewModel() {
    private val context = context
    private val repository =  Repository(context)
    private val apikey  = context.getString(R.string.apikey)
    val _sources = MutableLiveData<Resource<ArrayList<Source>>>()
    val sources : LiveData<Resource<ArrayList<Source>>>
        get() = _sources

    fun getSources(category:String) = viewModelScope.launch {
        _sources.postValue(Resource.Loading(true))
        try {
            repository.getSources(apikey,category)?.enqueue(
                object : Callback<ResponseSources> {
                    override fun onResponse(
                        call: Call<ResponseSources>,
                        response: Response<ResponseSources>
                    ) {
                        _sources.postValue(Resource.Loading(false))
                        if (response.isSuccessful) {
                            var tmp = response.body()!!
                            _sources.postValue(Resource.Success(tmp.sources))
                        }else{
                            _sources.postValue(Resource.Failure(Util.parseError(response).toString()))
                        }
                    }
                    @SuppressLint("ResourceType")
                    override fun onFailure(call: Call<ResponseSources>, t: Throwable) {
                        _sources.postValue(Resource.Loading(false))
                        var errorMessage = Util.parseThrowable(context,t)
                        _sources.postValue(Resource.Failure(errorMessage))
                    }
                }
            )
        } catch (e: Exception) {
            _sources.postValue(Resource.Failure(e.message.toString()))
        } finally {
            _sources.postValue(Resource.Loading(false))
        }
    }
}