package com.ivor.tools

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object UserContentManager {
    private const val PREFS_NAME = "user_content_prefs"
    private const val KEY_USER_CONTENTS = "user_contents"
    
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * 保存用户自定义内容
     */
    fun saveContent(context: Context, title: String, content: String, type: String) {
        val prefs = getPrefs(context)
        val gson = Gson()
        
        // 获取现有内容列表
        val existingContents = getAllUserContents(context).toMutableList()
        
        // 添加新内容
        val newContent = BoringContent(
            title = title,
            content = content,
            type = type
        )
        existingContents.add(newContent)
        
        // 保存到SharedPreferences
        val json = gson.toJson(existingContents)
        prefs.edit().putString(KEY_USER_CONTENTS, json).apply()
    }
    
    /**
     * 获取所有用户自定义内容
     */
    fun getAllUserContents(context: Context): List<BoringContent> {
        val prefs = getPrefs(context)
        val json = prefs.getString(KEY_USER_CONTENTS, null) ?: return emptyList()
        
        return try {
            val gson = Gson()
            val type = object : TypeToken<List<BoringContent>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * 根据类型获取用户自定义内容
     */
    fun getUserContentsByType(context: Context, type: String): List<BoringContent> {
        return getAllUserContents(context).filter { it.type == type }
    }
    
    /**
     * 删除指定内容
     */
    fun deleteContent(context: Context, title: String, type: String) {
        val prefs = getPrefs(context)
        val gson = Gson()
        
        val existingContents = getAllUserContents(context).toMutableList()
        existingContents.removeAll { it.title == title && it.type == type }
        
        val json = gson.toJson(existingContents)
        prefs.edit().putString(KEY_USER_CONTENTS, json).apply()
    }
    
    /**
     * 清空所有用户内容
     */
    fun clearAllUserContents(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().remove(KEY_USER_CONTENTS).apply()
    }
    
    /**
     * 获取用户内容数量统计
     */
    fun getUserContentCount(context: Context): Map<String, Int> {
        val contents = getAllUserContents(context)
        return mapOf(
            "course" to contents.count { it.type == "course" },
            "novel" to contents.count { it.type == "novel" },
            "manual" to contents.count { it.type == "manual" },
            "report" to contents.count { it.type == "report" }
        )
    }
}