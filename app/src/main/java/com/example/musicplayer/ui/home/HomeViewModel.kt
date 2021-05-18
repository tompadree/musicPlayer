package com.example.musicplayer.ui.home

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.musicplayer.data.models.SessionObject
import com.example.musicplayer.data.models.Result
import com.example.musicplayer.data.source.HomeRepository
import com.example.musicplayer.utils.SingleLiveEvent
import com.example.musicplayer.utils.network.InternetConnectionManager
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository,
                    private val internetConnectionManager: InternetConnectionManager
) : ViewModel() {

    companion object {
        var PAGE_LIMIT = 10
        var CURRENT_INDEX = 0
    }

    val _currentPage = ObservableField<Int>(0)
    val _currentSearch = ObservableField<String>("")
    val isSearchEmpty = ObservableField<Boolean>(true)
    val isInternetAvailable = ObservableField(true)

    val isDataLoadingError = MutableLiveData<Boolean>(false)

    protected val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _sessions: LiveData<List<SessionObject>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            viewModelScope.launch {
                handleResponseWithError(repository.getSessions(true, _currentSearch.get()!!,
                        _currentPage.get()!!, PAGE_LIMIT))
                _dataLoading.value = false
            }
        } else
            _dataLoading.value = false

        repository.observeSessions(_currentSearch.get()!!).map {
            if (it is Result.Success) {
                isDataLoadingError.value = false
                it.data

            } else if(it is Result.Error){
                _error.postValue(it.exception)
                isDataLoadingError.value = true
                emptyList()
            }
            else
                emptyList()
        }
    }

    val items: LiveData<List<SessionObject>> = _sessions

    fun refresh(refresh: Boolean) {
        loadSessions(refresh)
    }

    fun onSearchTextChanged(query: String){
        _dataLoading.postValue(true)
        _currentPage.set(0)
        CURRENT_INDEX = 0
        _currentSearch.set(query)
        _forceUpdate.postValue(true)
        isSearchEmpty.set(query.isEmpty())
    }

    private fun loadSessions(isLoad: Boolean) {
        if(!internetConnectionManager.hasInternetConnection()) {
            isInternetAvailable.set(false)
        }
        else {
            if(isInternetAvailable.get() == false) isInternetAvailable.set(true)
            _dataLoading.postValue(isLoad)
            _forceUpdate.postValue(isLoad)

        }
    }

    private fun handleResponseWithError(response: Result<List<SessionObject>>): LiveData<List<SessionObject>> {
        return when (response) {
            is Result.Success -> {
                isDataLoadingError.value = false
                MutableLiveData(response.data) as LiveData<List<SessionObject>>
            }
            is Result.Error -> {
                isDataLoadingError.value = true
                _error.postValue(response.exception)
                MutableLiveData( response.exception) as LiveData<List<SessionObject>>
            }
            is Result.Loading -> MutableLiveData( null)
        }
    }

    fun listScrolled(lastVisibleItemPosition: Int, totalItemCount: Int) {

               if( CURRENT_INDEX <= totalItemCount
                && lastVisibleItemPosition > totalItemCount - 3
                && !_dataLoading.value!!
        ) {
            viewModelScope.launch {
                CURRENT_INDEX = totalItemCount + PAGE_LIMIT
                _currentPage.set(CURRENT_INDEX)
                loadSessions(true)
            }
        }
    }
}