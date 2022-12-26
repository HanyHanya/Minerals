package com.example.minerals.data

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

enum class Quality (val value: String) {
    Gem("Gem"),
    Not("Not a precious"),
    Semi("Semiprecious");
    override fun toString(): String {
        return value
    }
    companion object Converter {
        fun convertToQuality(string: String): Quality? {
            when(string) {
                "Gem" -> return Quality.Gem
                "Not a precious" -> return Quality.Not
                "Semiprecious" -> return Quality.Semi
                else -> {
                    return null
                }
            }
        }
    }
}

@Parcelize
@Entity(tableName = "minerals")
data class Mineral (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var image: String = String(),
    var name: String = String(),
    var type: String = String(),
    var subtype: String = String(),
    var location: String = String(),
    var quality: Quality? = null,
    var weight: String = String()
) : Parcelable

//функционал: редактировать, удалять, добавлять, сортировака