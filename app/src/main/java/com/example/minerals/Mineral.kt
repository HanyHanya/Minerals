package com.example.minerals

import android.net.Uri
import java.io.Serializable


class Mineral : Serializable{
    var image: Uri? = null
    var name: String = String()
    var type: String = String()
    var note: String = String()
}