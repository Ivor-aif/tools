package com.ivor.tools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
class SleepHelperActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SleepHelperScreen(
                onBack = { finish() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepHelperScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var isReading by remember { mutableStateOf(false) }
    var currentContent by remember { mutableStateOf(BoringContentData.getRandomContent("course", context)) }
    var selectedType by remember { mutableStateOf("course") }
    var scrollOffset by remember { mutableStateOf(0f) }
    
    // 自动滚动效果
    LaunchedEffect(isReading) {
        if (isReading) {
            while (isReading) {
                delay(100)
                scrollOffset += 1f
            }
        }
    }
    
    // 自动切换内容
    LaunchedEffect(isReading) {
        if (isReading) {
            while (isReading) {
                delay(30000) // 30秒切换一次内容
                currentContent = BoringContentData.getRandomContent(selectedType, context)
                scrollOffset = 0f
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E),
                        Color(0xFF0F3460)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 顶部导航栏
            TopAppBar(
                title = { 
                    Text(
                        "入眠助手",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val intent = Intent(context, AddContentActivity::class.java)
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "添加内容",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
            
            // 主要内容区域
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 内容类型指示器
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("课程", "小说", "手册", "报告").forEach { type ->
                            val typeKey = when (type) {
                                "课程" -> "course"
                                "小说" -> "novel"
                                "手册" -> "manual"
                                "报告" -> "report"
                                else -> "course"
                            }
                            val isSelected = selectedType == typeKey
                            
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (isSelected) Color(0xFF4A90E2).copy(alpha = 0.3f)
                                        else Color.Transparent
                                    )
                                    .clickable {
                                        selectedType = typeKey
                                        currentContent = BoringContentData.getRandomContent(typeKey, context)
                                        scrollOffset = 0f
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = type,
                                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    
                    // 内容显示区域
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2A2A3E).copy(alpha = 0.8f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            // 标题
                            Text(
                                text = currentContent.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            
                            // 内容
                            Text(
                                text = currentContent.content,
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 24.sp,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        translationY = -scrollOffset
                                    }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // 控制按钮区域
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 刷新按钮
                        FloatingActionButton(
                            onClick = {
                                currentContent = BoringContentData.getRandomContent(selectedType, context)
                                scrollOffset = 0f
                            },
                            modifier = Modifier.size(56.dp),
                            containerColor = Color(0xFF6C7B7F),
                            contentColor = Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "随机切换内容",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        // 播放/暂停按钮
                        FloatingActionButton(
                            onClick = { isReading = !isReading },
                            modifier = Modifier.size(72.dp),
                            containerColor = Color(0xFF4A90E2),
                            contentColor = Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = if (isReading) "暂停" else "开始",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 进度指示器
                    LinearProgressIndicator(
                        progress = { (scrollOffset / 1000f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color(0xFF4A90E2),
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SleepHelperPreview() {
    SleepHelperScreen()
}