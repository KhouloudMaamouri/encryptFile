package com.khouloud.encryptfiles.features.encryptFile.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileCacheEntity
import com.khouloud.encryptfiles.features.encryptFile.domain.model.FileDecryptionResult
import com.khouloud.encryptfiles.features.encryptFile.domain.model.FileEncryptionResult
import com.khouloud.encryptfiles.features.encryptFile.domain.model.GetAvailableFilesResult
import com.khouloud.encryptfiles.features.encryptFile.domain.usesCases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EncryptFileViewModel @Inject constructor(
    private val applicationContext: Application,
    private val mGetAvailableFilesUseCase: GetAvailableFilesUseCase,
    private val mEncryptFileUseCase: EncryptFileUseCase,
    private val mDecryptFileUseCase: DecryptFileUseCase,
    private val mInsertFileUseCase: InsertFileUseCase,
    private val mGetFileUseCase: GetFileUseCase
) : ViewModel() {

    private val _fileResult: MutableLiveData<GetAvailableFilesResult> =
        MutableLiveData<GetAvailableFilesResult>()
    val fileResult: LiveData<GetAvailableFilesResult>
        get() = _fileResult

    private val _encryptedFileResult: MutableLiveData<FileEncryptionResult> =
        MutableLiveData<FileEncryptionResult>()
    val encryptedFileResult: LiveData<FileEncryptionResult>
        get() = _encryptedFileResult

    private val _decryptedFileResult: MutableLiveData<FileDecryptionResult> =
        MutableLiveData<FileDecryptionResult>()
    val decryptedFileResult: LiveData<FileDecryptionResult>
        get() = _decryptedFileResult

    private val _insertFileResult: MutableLiveData<Long> =
        MutableLiveData<Long>()
    val insertFileResult: LiveData<Long>
        get() = _insertFileResult

    private val _getFileResult: MutableLiveData<List<FileCacheEntity>> =
        MutableLiveData<List<FileCacheEntity>>()
    val getFileResult: LiveData<List<FileCacheEntity>>
        get() = _getFileResult

    lateinit var mFileCacheEntity:FileCacheEntity

    fun getFile() {
        viewModelScope.launch(Dispatchers.IO) {
            mGetAvailableFilesUseCase.invoke().onEach { resource ->
                _fileResult.postValue(resource)
            }.launchIn(viewModelScope)
        }
    }

    @SuppressLint("LogNotTimber")
    fun decryptFile(mContext: Context, mFileCacheEntity: FileCacheEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            mDecryptFileUseCase.decryptFile(
              mContext, mFileCacheEntity
            ).onEach { resource ->
                _decryptedFileResult.postValue(resource)
            }.launchIn(viewModelScope)
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("LogNotTimber")
    fun encryptFile(file: File, requireActivity: FragmentActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            mEncryptFileUseCase.encryptFile(
                context = requireActivity,
                inputFile = file
            ).onEach { resource ->
                _encryptedFileResult.postValue(resource)
            }.launchIn(viewModelScope)
        }
    }


    fun insertFileInformation(mFileCacheEntity: FileCacheEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            mInsertFileUseCase.invoke(mFileCacheEntity).onEach { resource ->
                _insertFileResult.postValue(resource)
            }.launchIn(viewModelScope)
        }
    }

    fun getFileInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            mGetFileUseCase.invoke().onEach { resource ->
                _getFileResult.postValue(resource)
            }.launchIn(viewModelScope)
        }
    }
}