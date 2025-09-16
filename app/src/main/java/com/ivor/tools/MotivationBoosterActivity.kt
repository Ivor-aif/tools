package com.ivor.tools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
class MotivationBoosterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotivationBoosterScreen(
                onBack = { finish() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotivationBoosterScreen(
    onBack: () -> Unit = {}
) {
    var selectedType by remember { mutableStateOf("motivation") }
    var currentContent by remember { mutableStateOf(MotivationData.getRandomMotivation()) }
    var isAutoMode by remember { mutableStateOf(false) }
    var showPulseAnimation by remember { mutableStateOf(false) }
    
    // 脉冲动画
    val pulseScale by animateFloatAsState(
        targetValue = if (showPulseAnimation) 1.1f else 1.0f,
        animationSpec = tween(durationMillis = 600),
        label = "pulse"
    )
    
    // 自动模式协程
    LaunchedEffect(isAutoMode, selectedType) {
        if (isAutoMode) {
            while (isAutoMode) {
                delay(4000) // 每4秒切换一次
                currentContent = MotivationData.getRandomContentByType(selectedType)
                showPulseAnimation = true
                delay(600)
                showPulseAnimation = false
            }
        }
    }
    
    // 更新内容函数
    fun updateContent() {
        currentContent = MotivationData.getRandomContentByType(selectedType)
        showPulseAnimation = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF6B35), // 橙红色
                        Color(0xFFE63946), // 红色
                        Color(0xFF1D3557)  // 深蓝色
                    ),
                    radius = 1200f
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
                        "💪 强力鼓励工具",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
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
                            updateContent()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "刷新内容",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
            
            // 主要内容区域
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 类型选择标签
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "选择鼓励类型",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        // 类型选择按钮
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "motivation" to "💪",
                                "quote" to "🌟",
                                "action" to "🚀"
                            ).forEach { (typeKey, emoji) ->
                                FilterChip(
                                    onClick = { 
                                        selectedType = typeKey
                                        updateContent()
                                    },
                                    label = { 
                                        Text(
                                            "$emoji ${MotivationData.getTypeDisplayName(typeKey)}",
                                            fontSize = 12.sp
                                        ) 
                                    },
                                    selected = selectedType == typeKey,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFFFD60A),
                                        selectedLabelColor = Color.Black,
                                        containerColor = Color.White.copy(alpha = 0.2f),
                                        labelColor = Color.White
                                    )
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "tip" to "💡",
                                "energy" to "⚡"
                            ).forEach { (typeKey, emoji) ->
                                FilterChip(
                                    onClick = { 
                                        selectedType = typeKey
                                        updateContent()
                                    },
                                    label = { 
                                        Text(
                                            "$emoji ${MotivationData.getTypeDisplayName(typeKey)}",
                                            fontSize = 12.sp
                                        ) 
                                    },
                                    selected = selectedType == typeKey,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFFFD60A),
                                        selectedLabelColor = Color.Black,
                                        containerColor = Color.White.copy(alpha = 0.2f),
                                        labelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
                
                // 鼓励内容显示区域
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(pulseScale),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 类型图标
                        Text(
                            text = when (selectedType) {
                                "motivation" -> "💪"
                                "quote" -> "🌟"
                                "action" -> "🚀"
                                "tip" -> "💡"
                                "energy" -> "⚡"
                                else -> "💪"
                            },
                            fontSize = 48.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // 鼓励内容
                        Text(
                            text = currentContent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1D3557),
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )
                    }
                }
                
                // 控制按钮区域
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 自动模式按钮
                    Button(
                        onClick = { 
                            isAutoMode = !isAutoMode
                            if (isAutoMode) {
                                updateContent()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAutoMode) Color(0xFF28A745) else Color(0xFF6C757D)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAutoMode) "停止自动" else "自动鼓励",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // 手动刷新按钮
                    Button(
                        onClick = { updateContent() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B35)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "立即鼓励",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                // 使用提示
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "💡 使用提示",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "• 选择不同类型获得针对性鼓励\n• 开启自动模式持续获得动力\n• 感到拖延时点击立即鼓励\n• 让积极的话语点燃你的斗志",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MotivationBoosterScreenPreview() {
    MotivationBoosterScreen()
}