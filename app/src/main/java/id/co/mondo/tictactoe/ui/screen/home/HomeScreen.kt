package id.co.mondo.tictactoe.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dagger.hilt.android.EntryPointAccessors
import id.co.mondo.tictactoe.di.AnalyticsEntryPoint
import id.co.mondo.tictactoe.ui.navigation.Screen
import id.co.mondo.tictactoe.ui.theme.ComponentStyles
import id.co.mondo.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val analyticsHelper = remember(context) {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            AnalyticsEntryPoint::class.java
        ).analyticsHelper()
    }

    HomeMenuContent(
        onOnlineClick = {
            analyticsHelper.logMenuClick("online")
            Toast.makeText(context, "Mode online dalam pengembangan", Toast.LENGTH_SHORT).show()
        },
        onOfflineClick = {
            analyticsHelper.logMenuClick("offline")
            navController.navigate(Screen.OfflineSetup.route)
        },
        onHistoryClick = {
            analyticsHelper.logMenuClick("history")
            navController.navigate(Screen.History.route)
        }
    )
}

@Composable
fun HomeMenuContent(
    onOnlineClick: () -> Unit,
    onOfflineClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Main Container with max width constraint for tablets/landscape
                Column(
                    modifier = Modifier.widthIn(max = 480.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Header / Logo Badge
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "TicTacToe",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Main bersama teman offline atau online",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = ComponentStyles.cardShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = ComponentStyles.cardElevation),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Pilih Mode Permainan",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            HomeMenuItemCard(
                                title = "Mode Offline",
                                subtitle = "Main 2 pemain di 1 HP",
                                icon = Icons.Filled.Groups,
                                onClick = onOfflineClick,
                                isPrimary = true
                            )

                            HomeMenuItemCard(
                                title = "Mode Online",
                                subtitle = "Segera hadir",
                                icon = Icons.Filled.Wifi,
                                onClick = onOnlineClick,
                                isPrimary = false
                            )

                            HomeMenuItemCard(
                                title = "Riwayat Game",
                                subtitle = "Lihat riwayat pertandingan",
                                icon = Icons.Filled.History,
                                onClick = onHistoryClick,
                                isPrimary = false
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeMenuItemCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isPrimary: Boolean,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isPrimary) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (isPrimary) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    OutlinedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 68.dp),
        shape = ComponentStyles.buttonShape,
        colors = CardDefaults.outlinedCardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isPrimary) MaterialTheme.colorScheme.primary else contentColor
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Preview(name = "Phone Portrait", device = Devices.PHONE, showBackground = true)
@Preview(name = "Tablet Landscape", device = Devices.TABLET, showBackground = true)
@Composable
private fun HomeMenuContentPreview() {
    TicTacToeTheme {
        HomeMenuContent(
            onOnlineClick = {},
            onOfflineClick = {},
            onHistoryClick = {}
        )
    }
}
