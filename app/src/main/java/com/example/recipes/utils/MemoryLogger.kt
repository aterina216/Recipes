package com.example.recipes.utils

import android.util.Log

object MemoryLogger {
    fun logMemory(tag: String) {
        val runtime = Runtime.getRuntime()
        val usedMB = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        val maxMB = runtime.maxMemory() / (1024 * 1024)

        Log.d("MEMORY", "$tag: ${usedMB}MB / ${maxMB}MB")
    }
}