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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.abahz.africa.model.Products
import com.abahz.africa.viewmodel.ProductViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MenuManagementScreen(
    productViewModel: ProductViewModel = koinViewModel()
) {
    val products by productViewModel.products.collectAsState()
    val loading by productViewModel.loading.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tous") }

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
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
                    text = "Gestion des Menus",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "Créez, modifiez et gérez les plats disponibles à la carte.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Rechercher un plat...") },
                    modifier = Modifier.width(300.dp),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Categories
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            val categories = listOf("Tous", "Pizza", "Tacos", "Dessert", "Boissons")
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD32F2F),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Menu Table
        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Table Header
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFFF9F9F9)).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("PLAT", modifier = Modifier.weight(3f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("TYPE", modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("PRIX", modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("DISPONIBILITE", modifier = Modifier.weight(1.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("ACTIONS", modifier = Modifier.weight(1.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                }

                if (loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFD32F2F))
                    }
                } else {
                    val filteredProducts = products.filter {
                        (selectedCategory == "Tous" || it.type == selectedCategory) &&
                                (it.name.contains(searchQuery, ignoreCase = true) || it.desc?.contains(searchQuery, ignoreCase = true) == true)
                    }

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(filteredProducts) { product ->
                            MenuRow(
                                product = product,
                                onDelete = {
                                    productViewModel.deleteProduct(product)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuRow(product: Products, onDelete: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(3f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray)
                ) {
                    if (product.image.isNotEmpty()) {
                        AsyncImage(
                            model = product.image,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.align(Alignment.Center))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = product.desc ?: "", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                }
            }
            Text(text = product.type, modifier = Modifier.weight(1f), fontSize = 14.sp)
            Text(text = "${product.price} Fc", modifier = Modifier.weight(1f), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            
            Box(
                modifier = Modifier.weight(1.5f)
            ) {
                val isAvailable = product.qty > 0
                val bg = if (isAvailable) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                val txt = if (isAvailable) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(50)).background(bg).padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(text = if (isAvailable) "Disponible" else "Indisponible", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = txt)
                }
            }

            Row(modifier = Modifier.weight(1.5f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { }, contentPadding = PaddingValues(0.dp)) {
                    Text("Modifier", fontSize = 12.sp)
                }
                IconButton(onClick = onDelete, modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color(0xFFFFEBEE))) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
                }
            }
        }
        HorizontalDivider(color = Color(0xFFF5F5F5))
    }
}
