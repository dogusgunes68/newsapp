package com.example.newsapp.model

import androidx.room.TypeConverter
import org.json.JSONObject

class SourceTypeConverter {
    @TypeConverter
    fun fromSource(source: Source): String {
        return JSONObject().apply {
            if (source.id != null){
                put("id", source.id)
            }else{
                put("id", "null")
            }

            put("name", source.name)
        }.toString()
    }

    @TypeConverter
    fun toSource(source: String): Source {
        val json = JSONObject(source)
        return Source(json.get("id") as String, json.getString("name"))
    }
}