package com.example.recipes.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object DownLoad {

    // Функция для скачивания изображения
    @SuppressLint("CoroutineCreationDuringComposition")
    suspend fun downloadImage(context: Context, imageUrl: String, filename: String) {
        Log.d("DOWNLOAD", "downloadImage вызвана: $imageUrl")

        withContext(Dispatchers.IO) {
            try {
                Log.d("DOWNLOAD", "Начинаем загрузку изображения")

                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()

                Log.d("DOWNLOAD", "HTTP код: ${connection.responseCode}")

                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                Log.d("DOWNLOAD", "Изображение загружено, размер: ${bitmap.width}x${bitmap.height}")

                // ПРОСТОЙ СПОСОБ СОХРАНЕНИЯ
                val success = saveImageSimple(context, bitmap, filename)

                Log.d("DOWNLOAD", "Сохранение завершено: $success")

                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(context, "Изображение сохранено!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("DOWNLOAD", "Ошибка скачивания", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // ПРОСТАЯ ФУНКЦИЯ СОХРАНЕНИЯ
    private fun saveImageSimple(context: Context, bitmap: Bitmap, filename: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Для Android 10+
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "$filename.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { stream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                    }
                } != null
            } else {
                // Для старых версий
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val imageFile = File(imagesDir, "$filename.jpg")

                FileOutputStream(imageFile).use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                }

                // Обновляем галерею
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(imageFile.absolutePath),
                    arrayOf("image/jpeg"),
                    null
                )
                true
            }
        } catch (e: Exception) {
            Log.e("SAVE", "Ошибка сохранения", e)
            false
        }
    }
}