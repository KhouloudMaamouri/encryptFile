package com.khouloud.encryptfiles.features.encryptFile.domain.manager

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.khouloud.encryptfiles.BuildConfig
import com.khouloud.encryptfiles.core.Constants.EXTENSION_ENCRYPTED_FILE
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileCacheEntity
import java.io.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class EncryptionManager @Inject constructor(){

    private val TAG = EncryptionManager::class.simpleName
    private val TRANSFORMATION = "AES/CBC/PKCS5PADDING"

    @RequiresApi(Build.VERSION_CODES.M)
    fun encrypt(context: Context, mFile: File): File {
        val contentFile = mFile.readBytes()
        val destination = "${context.getExternalFilesDir(null)}"

        val key = generateKey(BuildConfig.SECRET_KEY)
        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(Cipher.ENCRYPT_MODE, key)
        val dataCrypto = cipher.doFinal(contentFile)

        val fileCrypto = File(destination, "${mFile.nameWithoutExtension}$EXTENSION_ENCRYPTED_FILE")

        if(!fileCrypto.exists()) {
            fileCrypto.createNewFile()
        }

        FileOutputStream(fileCrypto).use { fileOutput ->
            fileOutput.write(cipher.iv)
            fileOutput.write(dataCrypto)
        }
        return  mFile
    }

    private fun generateKey(password: String): SecretKeySpec {
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray()
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }

    fun decrypt(context: Context, mFile: FileCacheEntity): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val key = generateKey(BuildConfig.SECRET_KEY)
        val destination = "${context.getExternalFilesDir(null)}"

        val getFileCrypted = File(destination, "${mFile.name}$EXTENSION_ENCRYPTED_FILE")
        val iVFromFile = readIVFromFile(file = getFileCrypted)

        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iVFromFile))

        val dataDecrypted = cipher.doFinal(readBytesAfter16(file = getFileCrypted))

        val file = File(destination, "${mFile.name}.${mFile.extension}")

        if(!file.exists()) {
            file.createNewFile()
        }

        FileOutputStream(file).use { fileOutput ->
            fileOutput.write(dataDecrypted)
        }

        return dataDecrypted
    }

    private fun readBytesAfter16(file: File): ByteArray? {
        val fileLength = file.length()

        if (fileLength >= 17) {
            val inputStream = BufferedInputStream(file.inputStream())

            // Skip the first 16 bytes
            inputStream.skip(16)

            // Read the remaining bytes into a byte array
            val remainingBytes = ByteArray((fileLength - 16).toInt())
            inputStream.read(remainingBytes)

            inputStream.close()

            return remainingBytes
        } else {
            Log.e(TAG, "readBytesAfter16: The file is not long enough to start from the 17th byte.", )
            return null
        }
    }
    
    
    private fun readIVFromFile(file: File): ByteArray {
        val inputStream = FileInputStream(file)
        try {
            val ivLength = 16 //   length based on algorithm AES
            val iv = ByteArray(ivLength)
            inputStream.read(iv)
            return iv
        } catch (e: IOException) {
            throw RuntimeException("Error reading IV from file: ${e.message}", e)
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                throw RuntimeException("Error closing file input stream: ${e.message}", e)
            }
        }
    }

}