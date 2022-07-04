package com.example.newsapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class Source(@ColumnInfo()
                  val id:String?,
                  @ColumnInfo()
                  val name:String?) {



}