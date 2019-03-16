package com.workfort.weatherkit.util.helper

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()
private val handler: Handler = Handler(Looper.getMainLooper())

fun onIoThread(t: () -> Unit) {
    IO_EXECUTOR.execute(t)
}


fun onUiThread(t: () -> Unit) {
    handler.post(t)
}

fun onUiThread(delay: Long, t: () -> Unit) {
    handler.postDelayed(t, delay)
}