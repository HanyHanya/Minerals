package com.example.minerals

import android.net.Uri
import java.io.Serializable


class Mineral (
    var image: Uri? = null,
    var id: String = String(),
    var name: String = String(),
    var type: String = String(),
    var note: String = String()
)