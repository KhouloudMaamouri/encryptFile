package com.khouloud.encryptfiles.features.encryptFile.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.khouloud.encryptfiles.MainActivity
import com.khouloud.encryptfiles.core.Constants
import com.khouloud.encryptfiles.core.Constants.STORAGE_PERMISSION_CODE
import com.khouloud.encryptfiles.databinding.FragmentEncryptFileBinding
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileCacheEntity
import com.khouloud.encryptfiles.features.encryptFile.domain.model.FileDecryptionResult
import com.khouloud.encryptfiles.features.encryptFile.domain.model.FileEncryptionResult
import com.khouloud.encryptfiles.features.encryptFile.domain.model.GetAvailableFilesResult


class EncryptFileFragment() : Fragment(), MainActivity.MainListeners {

    private lateinit var viewModel: EncryptFileViewModel
    private lateinit var binding: FragmentEncryptFileBinding
    private val TAG = EncryptFileFragment::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(EncryptFileViewModel::class.java)
        binding = FragmentEncryptFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFileInformation()
        subscribeObserverGetFileResult()
        interceptUserClick()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setListener(this)
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).setListener(null)
    }

    private fun showLoader(isVisible: Boolean) {
        when (isVisible) {
            true -> binding.loader.visibility = View.VISIBLE
            false -> binding.loader.visibility = View.GONE
        }
    }

    private fun showScanFilesBtn(isVisible: Boolean) {
        when (isVisible) {
            true -> binding.scanFiles.visibility = View.VISIBLE
            false -> binding.scanFiles.visibility = View.GONE
        }
    }

    private fun showDecryptBtn(isVisible: Boolean) {
        when (isVisible) {
            true -> binding.decryptFile.visibility = View.VISIBLE
            false -> binding.decryptFile.visibility = View.GONE
        }
    }

    private fun interceptUserClick() {
        binding.scanFiles.setOnClickListener {
            showScanFilesBtn(isVisible = false)
            // Check and request necessary permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestStoragePermission()
            } else {
                viewModel.getFile()
                subscribeObserverFileResult()
            }
        }

        binding.decryptFile.setOnClickListener {
            viewModel.decryptFile(requireContext(),viewModel.mFileCacheEntity)
            subscribeObserverDecryptedFileResult()
        }
    }

    private fun subscribeObserverDecryptedFileResult() {
        Log.d(TAG, "subscribeObserverDecryptedFileResult: Adding observer")
        viewModel.decryptedFileResult.removeObservers(viewLifecycleOwner)
        viewModel.decryptedFileResult.observe(viewLifecycleOwner) { result ->
            Log.d(TAG, "subscribeObserverDecryptedFileResult: Observer triggered")
            when (result) {
                is FileDecryptionResult.Success -> {
                    Log.e(
                        TAG,
                        "subscribeObserverDecryptedFileResult: Success $result"
                    )

                    Toast.makeText(requireActivity(),"File has been decrypted in package name of app",Toast.LENGTH_SHORT).show()
                }
                is FileDecryptionResult.Failure -> {
                    Log.e(
                        TAG,
                        "subscribeObserverDecryptedFileResult: Failure ${result.errorMessage}"
                    )
                }
            }
        }
    }


    private fun subscribeObserverFileResult() {
        viewModel.fileResult.removeObservers(viewLifecycleOwner)
        viewModel.fileResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is GetAvailableFilesResult.Loading -> {
                    Log.i(TAG, "subscribeObserverFileResult: Loading")
                    showLoader(isVisible = true)
                }
                is GetAvailableFilesResult.Success -> {
                    Log.e(TAG, "subscribeObserverFileResult: Success", )
                    viewModel.encryptFile(file = result.file,requireActivity())
                    subscribeObserverEncryptedFileResult()
                }
                is GetAvailableFilesResult.Failure -> {
                    Log.e(TAG, "subscribeObserverFileResult: Failure ${result.errorMessage}")
                    Toast.makeText(requireContext(),result.errorMessage,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onReqPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onReqPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Log.e(TAG, "onRequestPermissionsResult: Permission granted")
                viewModel.getFile()
                subscribeObserverFileResult()
            } else {
                // Permission denied
                Log.e(TAG, "Storage permission denied")
            }
        }
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_CODE
            )
        } else {
            // Permission already granted
            viewModel.getFile()
            subscribeObserverFileResult()
        }
    }

    private fun subscribeObserverEncryptedFileResult() {
        viewModel.encryptedFileResult.removeObservers(viewLifecycleOwner)
        viewModel.encryptedFileResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is FileEncryptionResult.Success -> {
                    Log.e(
                        TAG,
                        "subscribeObserverEncryptedFileResult: Success ${result.encryptedFile.name}"
                    )
                    viewModel.insertFileInformation(FileCacheEntity(
                        id = System.currentTimeMillis().toString(),
                        name = result.encryptedFile.nameWithoutExtension,
                        extension = result.encryptedFile.extension
                    ))
                    subscribeObserverInsertFileResult()
                }
                is FileEncryptionResult.Failure -> {
                    Log.e(
                        TAG,
                        "subscribeObserverEncryptedFileResult: Failure ${result.errorMessage}"
                    )
                }
            }
        }
    }

    private fun subscribeObserverInsertFileResult() {
        viewModel.insertFileResult.removeObservers(viewLifecycleOwner)
        viewModel.insertFileResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                Constants.SUCCESS -> {
                    Log.e(
                        TAG,
                        "subscribeObserverInsertFileResult: Success"
                    )
                    viewModel.getFileInformation()
                    subscribeObserverGetFileResult()
                }
                Constants.ERROR -> {
                    Log.e(
                        TAG,
                        "subscribeObserverEncryptedFileResult: Failure"
                    )
                    Toast.makeText(
                        requireActivity(),
                        "Une erreur est survenue. réessayez l'action",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun subscribeObserverGetFileResult() {
        viewModel.getFileResult.removeObservers(viewLifecycleOwner)
        viewModel.getFileResult.observe(viewLifecycleOwner) { result ->
            when (result.isNotEmpty()) {
                true -> {
                    viewModel.mFileCacheEntity = result.first()
                    Log.e(
                        TAG,
                        "subscribeObserverGetFileResult: Success"
                    )
                    val builder = StringBuilder()
                    builder.append("= File has been encrypted in package name of app = \n")
                        .append("File Name : ${result.first().name} \n")
                        .append("File Type :${result.first().extension}")

                    binding.fileInfo.text = builder.toString()
                    showLoader(false)
                    showScanFilesBtn(false)
                    showDecryptBtn(true)
                }
                false -> {
                    Log.e(
                        TAG,
                        "subscribeObserverGetFileResult: Failure"
                    )
                    showLoader(false)
                    showScanFilesBtn(true)
                    showDecryptBtn(false)
                }
            }
        }
    }



}
