package com.example.homework6.extensions

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Context.showErrorMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showErrorMessage(message: String) {
    requireContext().showErrorMessage(message)
}

fun Context.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showMessage(message: String) {
    requireContext().showMessage(message)
}
