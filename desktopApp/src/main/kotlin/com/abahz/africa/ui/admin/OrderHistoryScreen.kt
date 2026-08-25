package com.abahz.africa.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.abahz.africa.viewmodel.OrderViewModel
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderHistoryScreen(
    orderViewModel: OrderViewModel = koinViewModel()
) {
    val orders by orderViewModel.orders.collectAsState()
    val loading by orderViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        orderViewModel.loadOrders()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Historique des commandes",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "Consultez et gérez vos précédentes expériences culinaires.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = { orderViewModel.loadOrders() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5), contentColor = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Actualiser")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFD32F2F))
            }
        } else if (orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aucune commande trouvée", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders.sortedByDescending { it.created }) { order ->
                    val dateStr = try {
                        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.FRENCH)
                        sdf.format(Date(order.created))
                    } catch (e: Exception) {
                        "Date inconnue"
                    }

                    OrderCard(
                        id = "#ORD-${order.id}",
                        date = dateStr,
                        type = if (order.fid > 0) "Livraison" else "À emporter",
                        status = "Reçu", // Real status logic could be added to Model later
                        amount = "${order.total} Fc",
                        statusBg = Color(0xFFE8F5E9),
                        statusTxt = Color(0xFF2E7D32),
                        typeIcon = if (order.fid > 0) Icons.Default.DirectionsBike else Icons.Default.ShoppingBag
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    id: String,
    date: String,
    type: String,
    status: String,
    amount: String,
    statusBg: Color,
    statusTxt: Color,
    typeIcon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("N° DE COMMANDE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(id, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1A1A))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("DATE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(date, fontSize = 16.sp, color = Color.DarkGray)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("TYPE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(typeIcon, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(type, fontSize = 16.sp, color = Color.DarkGray)
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("STATUT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(50)).background(statusBg).padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(status, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = statusTxt)
                }
            }

            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text("MONTANT TOTAL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(amount, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFD32F2F))
                
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.Gray)
                    }
                }
            }
        }
    }
}
