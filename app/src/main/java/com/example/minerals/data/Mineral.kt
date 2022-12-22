package com.example.minerals.data

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "minerals")
data class Mineral (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var image: String = String(),
    var name: String = String(),
    var type: String = String(),
    var note: String = String()
) : Parcelable