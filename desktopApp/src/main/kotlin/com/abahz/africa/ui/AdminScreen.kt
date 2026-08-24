package com.abahz.africa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
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
import coil3.compose.AsyncImage
import com.abahz.africa.ui.admin.*
import com.abahz.africa.model.ProductType
import com.abahz.africa.model.Products
import com.abahz.africa.viewmodel.ProductViewModel
import com.abahz.africa.viewmodel.ShopViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminScreen(
    shopViewModel: ShopViewModel = koinViewModel(),
    productViewModel: ProductViewModel = koinViewModel(),
    onLogout: () -> Unit = {}
) {
    val currentShop by shopViewModel.currentShop.collectAsState()
    val error by productViewModel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedItem by remember { mutableStateOf("Dashboard") }
    var showAddProductDialog by remember { mutableStateOf(false) }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Row(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF8F9FA))) {
            // Sidebar
            Sidebar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                onLogout = onLogout,
                shopName = currentShop?.name ?: "Mama Africa",
                onNewEntry = { showAddProductDialog = true }
            )

            // Main Content
            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedItem) {
                    "Dashboard" -> DashboardScreen()
                    "Menu Management" -> MenuManagementScreen()
                    "Orders" -> OrderHistoryScreen()
                    "User Management" -> UserManagementScreen()
                    "Reservations" -> PlaceholderScreen("Reservations")
                    else -> DashboardScreen()
                }
            }
        }
    }

    if (showAddProductDialog) {
        AddProductDialog(
            onDismiss = { showAddProductDialog = false },
            onAdd = { product, imageBytes ->
                productViewModel.addProduct(product, imageBytes, refreshByShop = currentShop?.id)
                showAddProductDialog = false
            },
            shopId = currentShop?.id ?: ""
        )
    }
}

@Composable
fun Sidebar(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    onLogout: () -> Unit,
    shopName: String,
    onNewEntry: () -> Unit
) {
    Surface(
        modifier = Modifier.width(280.dp).fillMaxHeight(),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxHeight()
        ) {
            // Logo and Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color(0xFFD32F2F))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = shopName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFD32F2F))
                    Text(text = "Admin Panel • Restaurant Logistics", fontSize = 11.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // New Entry Button (Dynamic label based on context)
            val buttonLabel = if (selectedItem == "Menu Management") "Nouveau Plat" else "New Entry"
            Button(
                onClick = onNewEntry,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(buttonLabel, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Navigation Items
            val navItems = listOf(
                "Dashboard" to Icons.Default.Dashboard,
                "Menu Management" to Icons.Default.RestaurantMenu,
                "User Management" to Icons.Default.People,
                "Orders" to Icons.AutoMirrored.Filled.ReceiptLong,
                "Reservations" to Icons.AutoMirrored.Filled.EventNote
            )

            navItems.forEach { (title, icon) ->
                NavItem(
                    title = title,
                    icon = icon,
                    isSelected = selectedItem == title,
                    onClick = { onItemSelected(title) }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Items
            NavItem(title = "Settings", icon = Icons.Default.Settings, isSelected = false, onClick = {})
            NavItem(title = "Logout", icon = Icons.AutoMirrored.Filled.Logout, isSelected = false, onClick = onLogout)
        }
    }
}

@Composable
fun NavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) Color.White else Color(0xFF444444)
    val backgroundColor = if (isSelected) Color(0xFFD32F2F) else Color.Transparent

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "$title Screen - Coming Soon", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onAdd: (Products, ByteArray?) -> Unit,
    shopId: String
) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(ProductType.BOIS) }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var isTypeExpanded by remember { mutableStateOf(false) }

    val productTypes = listOf(ProductType.PIZZA, ProductType.TACOS, ProductType.DESSERT, ProductType.BOIS)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter un nouveau menu") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Image Picker
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5))
                        .clickable {
                            val fileDialog = java.awt.FileDialog(null as java.awt.Frame?, "Choisir une image", java.awt.FileDialog.LOAD)
                            fileDialog.isVisible = true
                            if (fileDialog.file != null) {
                                val file = java.io.File(fileDialog.directory, fileDialog.file)
                                imageBytes = file.readBytes()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageBytes != null) {
                        AsyncImage(
                            model = imageBytes,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                            Text("Cliquer pour choisir une image", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nom") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())

                // Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = isTypeExpanded,
                    onExpandedChange = { isTypeExpanded = !isTypeExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isTypeExpanded,
                        onDismissRequest = { isTypeExpanded = false }
                    ) {
                        productTypes.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    type = selectionOption
                                    isTypeExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Prix") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = qty, onValueChange = { qty = it }, label = { Text("Quantité") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Catégorie") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val product = Products(
                        name = name,
                        desc = desc,
                        price = price.toLongOrNull() ?: 0,
                        qty = qty.toDoubleOrNull() ?: 0.0,
                        category = category,
                        type = type,
                        shopid = shopId,
                        created = System.currentTimeMillis()
                    )
                    onAdd(product, imageBytes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = Color.Gray)
            }
        }
    )
}
