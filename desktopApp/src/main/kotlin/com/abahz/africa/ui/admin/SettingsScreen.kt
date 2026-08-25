package com.abahz.africa.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abahz.africa.viewmodel.ShopViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    shopViewModel: ShopViewModel = koinViewModel()
) {
    val currentShop by shopViewModel.currentShop.collectAsState()
    
    var shopName by remember { mutableStateOf(currentShop?.name ?: "") }
    var address by remember { mutableStateOf(currentShop?.address ?: "") }
    var phone by remember { mutableStateOf(currentShop?.phone ?: "") }
    
    LaunchedEffect(currentShop) {
        currentShop?.let {
            shopName = it.name
            address = it.address
            phone = it.phone
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Text(
                text = "Paramètres de l'établissement",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1A1A)
            )
            Text(
                text = "Gérez les informations, les horaires et les services de votre restaurant.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            // Left Column: General Info
            Column(modifier = Modifier.weight(1f)) {
                SettingsSectionTitle("Informations Générales", Icons.Default.Restaurant)
                
                SettingsCard {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SettingsTextField(label = "Nom du restaurant", value = shopName, onValueChange = { shopName = it })
                        SettingsTextField(label = "Adresse physique", value = address, onValueChange = { address = it })
                        SettingsTextField(label = "Numéro de contact", value = phone, onValueChange = { phone = it })
                        
                        Button(
                            onClick = { /* Update shop logic */ },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Enregistrer les modifications", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                SettingsSectionTitle("Livraison & Tarifs", Icons.Default.LocalShipping)
                SettingsCard {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                SettingsTextField(label = "Frais de livraison (Fc)", value = "5000", onValueChange = {})
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                SettingsTextField(label = "Commande minimum (Fc)", value = "15000", onValueChange = {})
                            }
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = true, onCheckedChange = {}, colors = CheckboxDefaults.colors(checkedColor = Color(0xFFD32F2F)))
                            Text("Accepter les paiements à la livraison", fontSize = 14.sp)
                        }
                    }
                }
            }

            // Right Column: Business Hours & Security
            Column(modifier = Modifier.weight(1f)) {
                SettingsSectionTitle("Horaires d'ouverture", Icons.Default.AccessTime)
                SettingsCard {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        DayRow("Lundi - Vendredi", "09:00 - 22:00", true)
                        DayRow("Samedi", "10:00 - 23:00", true)
                        DayRow("Dimanche", "Fermé", false)
                        
                        TextButton(onClick = {}, modifier = Modifier.align(Alignment.End)) {
                            Text("Modifier les horaires", color = Color(0xFFD32F2F))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                SettingsSectionTitle("Sécurité du compte", Icons.Default.Security)
                SettingsCard {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Changer le mot de passe administrateur", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Ancien mot de passe") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Nouveau mot de passe") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Mettre à jour le mot de passe")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
        Icon(icon, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
    }
}

@Composable
fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.padding(24.dp)) {
            content()
        }
    }
}

@Composable
fun SettingsTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFD32F2F),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )
    }
}

@Composable
fun DayRow(day: String, hours: String, isOpen: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = day, fontSize = 14.sp, color = if (isOpen) Color.Black else Color.Gray)
        Text(
            text = hours,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (hours == "Fermé") Color(0xFFD32F2F) else Color.DarkGray
        )
    }
}
