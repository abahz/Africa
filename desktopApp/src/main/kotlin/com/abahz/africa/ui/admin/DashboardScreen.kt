package com.abahz.africa.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Text(
                text = "Tableau de Bord Administrateur",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1A1A)
            )
            Text(
                text = "Aperçu en temps réel des opérations du restaurant.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            StatCard(modifier = Modifier.weight(1f), label = "RÉSERVATIONS", value = "2", icon = Icons.Outlined.CalendarMonth, iconBg = Color(0xFFFFEBEE), iconTint = Color(0xFFD32F2F))
            StatCard(modifier = Modifier.weight(1f), label = "PLATS ACTIFS", value = "11", icon = Icons.Outlined.Restaurant, iconBg = Color(0xFFE3F2FD), iconTint = Color(0xFF1976D2))
            StatCard(modifier = Modifier.weight(1f), label = "COMPTES CLIENTS", value = "2", icon = Icons.Outlined.Person, iconBg = Color(0xFFFFF3E0), iconTint = Color(0xFFF57C00))
            StatCard(modifier = Modifier.weight(1f), label = "COMPTES ADMIN", value = "2", icon = Icons.Outlined.Group, iconBg = Color(0xFFF3E5F5), iconTint = Color(0xFF7B1FA2))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Activité Récente", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                    Text(
                        text = "VOIR TOUT",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F),
                        modifier = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Text("TYPE", modifier = Modifier.weight(1.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("DÉTAILS", modifier = Modifier.weight(2f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("DATE/HEURE", modifier = Modifier.weight(1.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("STATUT", modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                }
                
                HorizontalDivider(color = Color(0xFFF5F5F5))

                ActivityRow(Icons.Default.Description, "Commande #1042", "Table 4 - 3 Plats", "Aujourd'hui, 19:30", "EN COURS", Color(0xFFE3F2FD), Color(0xFF1976D2))
                ActivityRow(Icons.Default.Event, "Réservation", "Fatou Ndiaye - 2 Pers.", "Aujourd'hui, 20:00", "CONFIRMÉ", Color(0xFFFFEBEE), Color(0xFFD32F2F))
                ActivityRow(Icons.Default.Description, "Commande #1041", "À emporter - 1 Plat", "Aujourd'hui, 18:45", "TERMINÉ", Color(0xFFF5F5F5), Color.Gray)
                ActivityRow(Icons.Default.Event, "Réservation", "Marie Curie - 4 Pers.", "Demain, 12:30", "CONFIRMÉ", Color(0xFFFFEBEE), Color(0xFFD32F2F))
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(text = value, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1A1A))
            }
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun ActivityRow(
    icon: ImageVector,
    type: String,
    details: String,
    time: String,
    status: String,
    statusBg: Color,
    statusColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1.5f), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = type, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
            }
            Text(text = details, modifier = Modifier.weight(2f), fontSize = 14.sp, color = Color.DarkGray)
            Text(text = time, modifier = Modifier.weight(1.5f), fontSize = 14.sp, color = Color.DarkGray)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(statusBg)
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = statusColor)
            }
        }
        HorizontalDivider(color = Color(0xFFF5F5F5))
    }
}
