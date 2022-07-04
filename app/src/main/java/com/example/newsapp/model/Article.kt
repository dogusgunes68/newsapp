package com.example.newsapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Article(@ColumnInfo(name = "source")
                   @SerializedName("source")
                   val source : Source?,
                   @ColumnInfo()
                   val author:String?,
                   @ColumnInfo()
                   val title:String?,
                   @ColumnInfo()
                   val description:String?,
                   @ColumnInfo()
                   val url:String?,
                   @ColumnInfo()
                   val urlToImage:String?,
                   @ColumnInfo()
                   val publishedAt:String?,
                   @ColumnInfo()
                   val content:String?
                   ) {

    @PrimaryKey(autoGenerate = true)
    var uuid : Long = 0;


}