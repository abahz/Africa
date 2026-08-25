package com.abahz.africa.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.abahz.africa.model.Customer
import com.abahz.africa.viewmodel.CustomerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserManagementScreen(
    customerViewModel: CustomerViewModel = koinViewModel()
) {
    val customers by customerViewModel.customers.collectAsState()
    val loading by customerViewModel.loading.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        customerViewModel.loadCustomers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "User Management",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "View and manage customer accounts.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Refresh Button
                IconButton(onClick = { customerViewModel.loadCustomers() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color(0xFFD32F2F))
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search users...", fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.width(300.dp).height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Table Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TableHeader()
                HorizontalDivider(color = Color(0xFFEEEEEE))

                if (loading) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFD32F2F))
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(min = 400.dp)) {
                        items(customers.filter { it.name?.contains(searchQuery, ignoreCase = true) == true || it.phone.contains(searchQuery, ignoreCase = true) }) { customer ->
                            UserRow(
                                customer = customer,
                                onDelete = {
                                    customer.id?.let { customerViewModel.deleteCustomer(it) }
                                }
                            )
                            HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }

                // Footer / Pagination
                TableFooter(totalResults = customers.size)
            }
        }
    }
}

@Composable
fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F9F9))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderText("NAME", Modifier.weight(1.5f))
        HeaderText("PHONE", Modifier.weight(2f))
        HeaderText("ADDRESS", Modifier.weight(2f))
        HeaderText("ACTIONS", Modifier.weight(1f))
    }
}

@Composable
fun HeaderText(text: String, modifier: Modifier) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = modifier
    )
}

@Composable
fun UserRow(customer: Customer, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Name with Avatar
        Row(modifier = Modifier.weight(1.5f), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE9ECEF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = customer.name?.takeIf { it.isNotEmpty() }?.take(2)?.uppercase() ?: "??",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = customer.name ?: "Inconnu", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }

        // Phone
        Text(text = customer.phone, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.weight(2f))

        // Address
        Text(text = customer.address ?: "Non spécifiée", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.weight(2f))

        // Action Button
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            IconButton(onClick = onDelete, modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color(0xFFFFEBEE))) {
                Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun TableFooter(totalResults: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Showing 1 to $totalResults of $totalResults results",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = Color.Gray)
            }
            
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFD32F2F)),
                contentAlignment = Alignment.Center
            ) {
                Text("1", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.width(4.dp))
            PageButton("2")
            Spacer(modifier = Modifier.width(4.dp))
            PageButton("3")

            IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}

@Composable
fun PageButton(page: String) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Text(page, fontSize = 14.sp, color = Color.Gray)
    }
}
