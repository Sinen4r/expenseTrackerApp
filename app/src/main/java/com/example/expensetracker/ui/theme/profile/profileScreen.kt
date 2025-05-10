package com.example.expensetracker.ui.theme.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensetracker.navigation.Screen

@Composable
fun profileScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // User Profile Info
        UserProfileSection()

        Spacer(modifier = Modifier.height(24.dp))

        // User Info List
        ProfileInfoList()

        Spacer(modifier = Modifier.height(48.dp))

        // Logout Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    // Navigate to login page
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red.copy(alpha = 0.8f)
                )
            ) {
                Text(
                    text = "Sign OUT",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun UserProfileSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username
        Text(
            text = "mohamed_ali",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = "Joined: March 2023",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun ProfileInfoList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileInfoItem("Name", "Mohamed Ali")
        Divider()

        ProfileInfoItem("Email", "mohamed@example.com")
        Divider()

        ProfileInfoItem("Account Created", "March 15, 2023")
        Divider()

        ProfileInfoItem("Total Saved", "$2,750")
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}