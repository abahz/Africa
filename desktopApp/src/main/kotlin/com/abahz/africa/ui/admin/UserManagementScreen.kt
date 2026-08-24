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
import com.abahz.africa.viewmodel.ShopViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserManagementScreen(
    customerViewModel: CustomerViewModel = koinViewModel(),
    shopViewModel: ShopViewModel = koinViewModel()
) {
    val currentShop by shopViewModel.currentShop.collectAsState()
    val customers by customerViewModel.customers.collectAsState()
    val loading by customerViewModel.loading.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(currentShop) {
        currentShop?.id?.let { customerViewModel.loadCustomers(it) }
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
                    text = "View and manage customer accounts, roles, and access statuses.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
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

                Spacer(modifier = Modifier.width(16.dp))

                // Filter Button
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Filter")
                }
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
                        items(customers.filter { it.name.contains(searchQuery, ignoreCase = true) || it.email.contains(searchQuery, ignoreCase = true) }) { customer ->
                            UserRow(
                                customer = customer,
                                onActionClick = {
                                    val newStatus = if (customer.status == "Active") "Blocked" else "Active"
                                    customerViewModel.updateCustomer(customer.copy(status = newStatus))
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
        HeaderText("EMAIL", Modifier.weight(2f))
        HeaderText("ROLE", Modifier.weight(1f))
        HeaderText("STATUS", Modifier.weight(1f))
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
fun UserRow(customer: Customer, onActionClick: () -> Unit) {
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
                    text = customer.name.take(2).uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = customer.name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }

        // Email
        Text(text = customer.email, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.weight(2f))

        // Role
        Text(text = customer.role, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.weight(1f))

        // Status Tag
        Box(modifier = Modifier.weight(1f)) {
            val bgColor = if (customer.status == "Active") Color(0xFFE3F2FD) else Color(0xFFF8D7DA)
            val textColor = if (customer.status == "Active") Color(0xFF0D47A1) else Color(0xFF721C24)

            Surface(
                color = bgColor,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = customer.status,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Action Button
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            OutlinedButton(
                onClick = onActionClick,
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Text(
                    text = if (customer.status == "Active") "Block" else "Unblock",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
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
