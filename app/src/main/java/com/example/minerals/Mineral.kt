package com.example.minerals

import android.net.Uri
import java.io.Serializable

class Type : Serializable{
    val type : String = String()

}

class Mineral : Serializable{
    var image: Uri? = null
    var name: String = String()
    var type: Type? = null
}