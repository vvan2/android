package org.sopt.animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.animation.ui.theme.AlamiTheme


class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlamiTheme {
                var selectedTab by remember { mutableStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        CustomBottomBar(selectedTab = selectedTab) {
                            selectedTab = it
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("선택된 탭: ${selectedTab}")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf("Home", "Grid", "Stats", "Profile")
    val icons = listOf(Icons.Default.Home, Icons.Default.Settings, Icons.Default.FavoriteBorder, Icons.Default.Person)

    Box(
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.9f)
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, label ->
                    val isEdge = index == 0 || index == items.size - 1
                    val isSelected = index == selectedTab

                    if (isEdge) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { onTabSelected(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = label,
                                tint = if (isSelected) Color.Black else Color.Gray
                            )
                        }
                    } else {
                        IconButton(onClick = { onTabSelected(index) }) {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = label,
                                tint = if (isSelected) Color.White else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomBarScreen() {
    AlamiTheme {
        var selectedTab by remember { mutableStateOf(0) }

        Scaffold(
            bottomBar = {
                CustomBottomBar(selectedTab = selectedTab) {
                    selectedTab = it
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("선택된 탭: ${selectedTab}")
            }
        }
    }
}
