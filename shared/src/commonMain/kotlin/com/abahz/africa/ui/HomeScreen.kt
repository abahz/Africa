package com.abahz.africa.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.abahz.africa.model.Customer
import com.abahz.africa.model.ProductType
import com.abahz.africa.model.Products
import com.abahz.africa.viewmodel.CustomerViewModel
import com.abahz.africa.viewmodel.ProductViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onCartClick: () -> Unit, onProfileClick: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 24.dp)
            ) {
                Icon(
                    Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Mama Africa",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFFD32F2F)
                )
                
                Spacer(modifier = Modifier.width(48.dp))
                
                // Nav Links
                Text(
                    "Menu",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp).clickable { }
                )
                // Red underline for active link
                Box(modifier = Modifier.width(40.dp).height(2.dp).background(Color(0xFFD32F2F)).align(Alignment.Bottom))
                
                Text(
                    "Reservations",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp).clickable { }
                )
            }
        },
        actions = {
            IconButton(onClick = onCartClick) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.Black)
            }
            IconButton(onClick = onProfileClick) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(24.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun HomeScreen(
    productViewModel: ProductViewModel = koinViewModel(),
    customerViewModel: CustomerViewModel = koinViewModel()
) {
    val products by productViewModel.products.collectAsState()
    val loading by productViewModel.loading.collectAsState()
    var selectedCategory by remember { mutableStateOf(ProductType.PIZZA) }
    var isCartOpen by remember { mutableStateOf(false) }

    // Customer Logic
    var showCustomerDialog by remember { mutableStateOf(false) }
    var currentCustomer by remember { mutableStateOf<Customer?>(null) }
    var pendingProduct by remember { mutableStateOf<Products?>(null) }

    val categories = listOf(ProductType.PIZZA, ProductType.TACOS, ProductType.DESSERT, ProductType.BOIS)

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                onCartClick = { isCartOpen = !isCartOpen },
                onProfileClick = { showCustomerDialog = true }
            )
        }
    ) { padding ->
        Row(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .weight(if (isCartOpen) 0.7f else 1f)
                    .fillMaxHeight()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 80.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Découvrez notre Carte",
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Explore a curated selection of culinary delights designed for the modern epicurean,\nreimagined with the warmth and spirit of Mama Africa.",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Category Pills
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        categories.forEach { category ->
                            val isSelected = selectedCategory == category
                            Surface(
                                modifier = Modifier
                                    .clickable { selectedCategory = category }
                                    .clip(RoundedCornerShape(24.dp)),
                                color = if (isSelected) Color(0xFFD32F2F) else Color.White,
                                border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE0E0E0)) else null,
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Text(
                                    text = category,
                                    color = if (isSelected) Color.White else Color.Black,
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                // Products Grid
                if (loading) {
                    Box(modifier = Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFD32F2F))
                    }
                } else {
                    val filteredProducts = products.filter { it.type == selectedCategory }
                    
                    Box(modifier = Modifier.padding(horizontal = 48.dp).fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                            filteredProducts.chunked(4).forEach { rowProducts ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                                ) {
                                    rowProducts.forEach { product ->
                                        Box(modifier = Modifier.weight(1f)) {
                                            ProductCard(
                                                product = product,
                                                onAddClick = {
                                                    if (currentCustomer == null) {
                                                        pendingProduct = product
                                                        showCustomerDialog = true
                                                    } else {
                                                        isCartOpen = true
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    repeat(4 - rowProducts.size) {
                                        Box(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
                HomeFooter()
            }

            if (isCartOpen) {
                CartSidebar(onClose = { isCartOpen = false }, modifier = Modifier.weight(0.3f).fillMaxHeight())
            }
        }
    }

    if (showCustomerDialog) {
        CustomerRegistrationDialog(
            initialCustomer = currentCustomer,
            onDismiss = { showCustomerDialog = false },
            onLoginOrRegister = { phone, password ->
                customerViewModel.signInOrRegister(phone, password) { customer ->
                    if (customer != null) {
                        currentCustomer = customer
                        showCustomerDialog = false
                        if (pendingProduct != null) {
                            isCartOpen = true
                            pendingProduct = null
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun CustomerRegistrationDialog(
    initialCustomer: Customer? = null,
    onDismiss: () -> Unit,
    onLoginOrRegister: (String, String) -> Unit
) {
    var phone by remember { mutableStateOf(initialCustomer?.phone ?: "") }
    var password by remember { mutableStateOf(initialCustomer?.password ?: "") }

    val isAlreadyLoggedIn = initialCustomer != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isAlreadyLoggedIn) "Mon Compte" else "Connexion / Inscription", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (isAlreadyLoggedIn) {
                    Text("Vous êtes connecté avec le numéro : ${initialCustomer?.phone}", fontSize = 14.sp)
                } else {
                    Text("Entrez votre numéro et mot de passe pour commander.", fontSize = 14.sp, color = Color.Gray)
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Téléphone") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mot de passe") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            }
        },
        confirmButton = {
            if (!isAlreadyLoggedIn) {
                Button(
                    onClick = {
                        if (phone.isNotBlank() && password.isNotBlank()) {
                            onLoginOrRegister(phone, password)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Valider")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isAlreadyLoggedIn) "Fermer" else "Annuler", color = Color.Gray)
            }
        }
    )
}

@Composable
fun CartSidebar(onClose: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxHeight().width(400.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(24.dp).fillMaxHeight()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Votre Panier", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cart Items List Placeholder
            Box(modifier = Modifier.weight(1f)) {
                Text("Le panier est vide.", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Order Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Order Summary", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("0 Fc", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("PROCEED TO CHECKOUT", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Products, onAddClick: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                if (product.image.isNotEmpty()) {
                    AsyncImage(
                        model = product.image,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp).align(Alignment.Center),
                        tint = Color.LightGray
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = product.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.desc ?: "Classic delight with premium ingredients.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 3,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${product.price} Fc",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Button(
                        onClick = onAddClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF343A40))
            .padding(vertical = 60.dp, horizontal = 48.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Mama Africa",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Text(
                "© 2024 Mama Africa. Professional Epicurean Logistics.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Text("Privacy Policy", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp, modifier = Modifier.clickable { })
                Text("Terms of Service", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp, modifier = Modifier.clickable { })
                Text("Contact Support", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp, modifier = Modifier.clickable { })
            }
        }
    }
}
