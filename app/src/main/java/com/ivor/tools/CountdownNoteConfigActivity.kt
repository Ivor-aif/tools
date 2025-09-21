package com.ivor.tools

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ivor.tools.ui.theme.ToolsTheme
import java.text.SimpleDateFormat
import java.util.*

class CountdownNoteConfigActivity : ComponentActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 获取widget ID
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID
        
        // 如果没有有效的widget ID，关闭Activity
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
        
        setContent {
            ToolsTheme {
                ConfigScreen()
            }
        }
    }

    @Composable
    fun ConfigScreen() {
        var noteText by remember { mutableStateOf("") }
        var selectedDateTime by remember { mutableStateOf<Long?>(null) }
        
        val context = LocalContext.current
        
        // 加载已保存的数据
        LaunchedEffect(Unit) {
            val prefs = getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
            noteText = prefs.getString("note_text_$appWidgetId", "") ?: ""
            val savedTime = prefs.getLong("target_time_$appWidgetId", 0L)
            if (savedTime > 0) {
                selectedDateTime = savedTime
            }
        }
        
        // 实时保存文本内容
        LaunchedEffect(noteText) {
            if (noteText.isNotEmpty()) {
                val prefs = getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
                prefs.edit()
                    .putString("note_text_$appWidgetId", noteText)
                    .apply()
            }
        }
        
        // 实时保存目标时间
        LaunchedEffect(selectedDateTime) {
            selectedDateTime?.let { dateTime ->
                val prefs = getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
                prefs.edit()
                    .putLong("target_time_$appWidgetId", dateTime)
                    .apply()
            }
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "倒计时记事本设置",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = noteText,
                            onValueChange = { noteText = it },
                            label = { Text("输入记事内容") },
                            placeholder = { Text("在这里输入你的记事内容...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            maxLines = 5
                        )
                        
                        Button(
                            onClick = {
                                showDateTimePicker(context) { dateTime ->
                                    selectedDateTime = dateTime
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (selectedDateTime != null) {
                                    "目标时间: ${formatDateTime(selectedDateTime!!)}"
                                } else {
                                    "选择目标时间"
                                }
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { finish() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("取消")
                            }
                            
                            Button(
                                onClick = {
                                    saveConfiguration(noteText, selectedDateTime ?: 0L)
                                    updateWidget()
                                    finish()
                                },
                                enabled = noteText.isNotEmpty(),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("保存")
                            }
                        }
                    }
                }
                
                // 显示当前配置预览
                if (noteText.isNotEmpty() || selectedDateTime != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "预览",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            if (noteText.isNotEmpty()) {
                                Text(
                                    text = noteText,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            
                            if (selectedDateTime != null) {
                                val currentTime = System.currentTimeMillis()
                                val timeDiff = selectedDateTime!! - currentTime
                                
                                Text(
                                    text = if (timeDiff > 0) {
                                        "剩余时间: ${formatTimeRemaining(timeDiff)}"
                                    } else {
                                        "时间已到！"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (timeDiff > 0) 
                                        MaterialTheme.colorScheme.primary 
                                    else 
                                        MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    private fun showDateTimePicker(context: Context, onDateTimeSelected: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute, 0)
                        onDateTimeSelected(calendar.timeInMillis)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    
    private fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    private fun formatTimeRemaining(millisUntilFinished: Long): String {
        val days = millisUntilFinished / (1000 * 60 * 60 * 24)
        val hours = (millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (millisUntilFinished % (1000 * 60)) / 1000
        
        return when {
            days > 0 -> "${days}天 ${hours}小时 ${minutes}分钟"
            hours > 0 -> "${hours}小时 ${minutes}分钟 ${seconds}秒"
            minutes > 0 -> "${minutes}分钟 ${seconds}秒"
            else -> "${seconds}秒"
        }
    }
    
    private fun saveConfiguration(noteText: String, targetTime: Long) {
        val prefs = getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("note_text_$appWidgetId", noteText)
            .putLong("target_time_$appWidgetId", targetTime)
            .apply()
    }
    
    private fun updateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        
        // 直接调用小工具的更新方法
        CountdownNoteWidget().updateAppWidget(this, appWidgetManager, appWidgetId)
        
        // 设置结果
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
    }
}