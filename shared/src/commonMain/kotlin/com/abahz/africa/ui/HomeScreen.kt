package com.abahz.africa.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.abahz.africa.model.Customer
import com.abahz.africa.model.OrderItem
import com.abahz.africa.model.Orders
import com.abahz.africa.model.ProductType
import com.abahz.africa.model.Products
import com.abahz.africa.viewmodel.CustomerViewModel
import com.abahz.africa.viewmodel.OrderViewModel
import com.abahz.africa.viewmodel.ProductViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
    onMenuClick: () -> Unit,
    isMobile: Boolean,
    cartItemCount: Int
) {
    val categories = listOf("Tous", ProductType.PIZZA, ProductType.TACOS, ProductType.DESSERT, ProductType.BOIS)
    
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isMobile) {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
                    }
                } else {
                    Icon(
                        Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Mama Africa",
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isMobile) 18.sp else 24.sp,
                    color = Color(0xFFD32F2F)
                )
            }
        },
        actions = {
            if (!isMobile) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 32.dp)) {
                    categories.forEach { category ->
                        CategoryItem(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }
            }
            
            IconButton(onClick = onCartClick) {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(containerColor = Color(0xFFD32F2F)) {
                                Text(cartItemCount.toString(), color = Color.White)
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.Black)
                }
            }
            IconButton(onClick = onProfileClick) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(if (isMobile) 8.dp else 24.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun CategoryItem(category: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = category,
            color = if (isSelected) Color(0xFFD32F2F) else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(16.dp)
                    .height(2.dp)
                    .background(Color(0xFFD32F2F))
            )
        } else {
            Box(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun HomeScreen(
    productViewModel: ProductViewModel = koinViewModel(),
    customerViewModel: CustomerViewModel = koinViewModel(),
    orderViewModel: OrderViewModel = koinViewModel()
) {
    val products by productViewModel.products.collectAsState()
    val loading by productViewModel.loading.collectAsState()
    val orderLoading by orderViewModel.loading.collectAsState()
    val orderError by orderViewModel.error.collectAsState()
    
    var selectedCategory by remember { mutableStateOf("Tous") }
    var isCartOpen by remember { mutableStateOf(false) }
    var isMenuOpen by remember { mutableStateOf(false) }

    // Cart State
    val cartItems = remember { mutableStateMapOf<Products, Int>() }

    // Customer Logic
    var showCustomerDialog by remember { mutableStateOf(false) }
    var currentCustomer by remember { mutableStateOf<Customer?>(null) }
    var pendingProduct by remember { mutableStateOf<Products?>(null) }
    
    // Checkout logic
    var showCheckoutDetailsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isMobile = maxWidth < 600.dp
        val columns = when {
            maxWidth < 600.dp -> 1
            maxWidth < 900.dp -> 2
            maxWidth < 1200.dp -> 3
            else -> 4
        }

        Scaffold(
            topBar = {
                HomeTopBar(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { 
                        selectedCategory = it
                        isMenuOpen = false
                    },
                    onCartClick = { isCartOpen = !isCartOpen },
                    onProfileClick = { showCustomerDialog = true },
                    onMenuClick = { isMenuOpen = true },
                    isMobile = isMobile,
                    cartItemCount = cartItems.values.sum()
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .weight(if (isCartOpen && !isMobile) 0.7f else 1f)
                            .fillMaxHeight()
                            .background(Color(0xFFF8F9FA))
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Spacer(modifier = Modifier.height(if (isMobile) 16.dp else 32.dp))

                            if (loading) {
                                Box(modifier = Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = Color(0xFFD32F2F))
                                }
                            } else {
                                val filteredProducts = if (selectedCategory == "Tous") products else products.filter { it.type == selectedCategory }
                                
                                Box(modifier = Modifier.padding(horizontal = if (isMobile) 16.dp else 48.dp).fillMaxWidth()) {
                                    Column(verticalArrangement = Arrangement.spacedBy(if (isMobile) 16.dp else 32.dp)) {
                                        filteredProducts.chunked(columns).forEach { rowProducts ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(if (isMobile) 16.dp else 24.dp)
                                            ) {
                                                rowProducts.forEach { product ->
                                                    Box(modifier = Modifier.weight(1f)) {
                                                        ProductCard(
                                                            product = product,
                                                            isMobile = isMobile,
                                                            onAddClick = {
                                                                if (currentCustomer == null) {
                                                                    pendingProduct = product
                                                                    showCustomerDialog = true
                                                                } else {
                                                                    cartItems[product] = (cartItems[product] ?: 0) + 1
                                                                    isCartOpen = true
                                                                }
                                                            }
                                                        )
                                                    }
                                                }
                                                repeat(columns - rowProducts.size) {
                                                    Box(modifier = Modifier.weight(1f))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(if (isMobile) 40.dp else 80.dp))
                        }
                        
                        HomeFooter(isMobile = isMobile)
                    }

                    if (isCartOpen && !isMobile) {
                        CartSidebar(
                            cartItems = cartItems,
                            isLoading = orderLoading,
                            onUpdateQty = { product, delta ->
                                val currentQty = cartItems[product] ?: 0
                                val newQty = currentQty + delta
                                if (newQty <= 0) cartItems.remove(product) else cartItems[product] = newQty
                            },
                            onRemoveItem = { cartItems.remove(it) },
                            onCheckout = {
                                showCheckoutDetailsDialog = true
                            },
                            onClose = { isCartOpen = false }, 
                            modifier = Modifier.width(400.dp).fillMaxHeight()
                        )
                    }
                }
                
                // Left Drawer for Categories (Mobile)
                AnimatedVisibility(
                    visible = isMobile && isMenuOpen,
                    enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                    exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                ) {
                    CategoryDrawer(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { 
                            selectedCategory = it
                            isMenuOpen = false
                        },
                        onClose = { isMenuOpen = false }
                    )
                }

                // Full screen cart overlay (Mobile)
                if (isCartOpen && isMobile) {
                    CartSidebar(
                        cartItems = cartItems,
                        isLoading = orderLoading,
                        onUpdateQty = { product, delta ->
                            val currentQty = cartItems[product] ?: 0
                            val newQty = currentQty + delta
                            if (newQty <= 0) cartItems.remove(product) else cartItems[product] = newQty
                        },
                        onRemoveItem = { cartItems.remove(it) },
                        onCheckout = {
                            showCheckoutDetailsDialog = true
                        },
                        onClose = { isCartOpen = false }, 
                        modifier = Modifier.fillMaxSize().background(Color.White)
                    )
                }
            }
        }
    }

    if (showCustomerDialog) {
        CustomerRegistrationDialog(
            initialCustomer = currentCustomer,
            customerViewModel = customerViewModel,
            onDismiss = { showCustomerDialog = false },
            onLoginOrRegister = { phone, password ->
                customerViewModel.signInOrRegister(phone, password) { customer ->
                    if (customer != null) {
                        currentCustomer = customer
                        showCustomerDialog = false
                        pendingProduct?.let {
                            cartItems[it] = (cartItems[it] ?: 0) + 1
                            isCartOpen = true
                        }
                        pendingProduct = null
                    }
                }
            }
        )
    }
    
    if (showCheckoutDetailsDialog) {
        CheckoutDetailsDialog(
            initialName = currentCustomer?.name ?: "",
            initialAddress = currentCustomer?.address ?: "",
            onDismiss = { showCheckoutDetailsDialog = false },
            onConfirm = { name, address ->
                val updatedCustomer = currentCustomer?.copy(name = name, address = address) ?: Customer(name = name, address = address)
                customerViewModel.updateCustomer(updatedCustomer)
                currentCustomer = updatedCustomer
                
                val total = cartItems.entries.sumOf { it.key.price * it.value }
                val order = Orders(
                    customer = name,
                    total = total,
                    created = 0L // Timestamp logic removed
                )
                val items = cartItems.map { (product, qty) ->
                    OrderItem(
                        pid = product.id ?: "",
                        qty = qty.toString(),
                        created = 0L
                    )
                }
                orderViewModel.placeOrder(order, items)
                
                cartItems.clear()
                showCheckoutDetailsDialog = false
                isCartOpen = false
            }
        )
    }
}

@Composable
fun CheckoutDetailsDialog(
    initialName: String,
    initialAddress: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var address by remember { mutableStateOf(initialAddress) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Détails de Livraison", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Veuillez confirmer vos coordonnées pour le suivi de votre commande.", fontSize = 14.sp, color = Color.Gray)
                OutlinedTextField(
                    value = name, 
                    onValueChange = { name = it }, 
                    label = { Text("Votre Nom") }, 
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Adresse de Livraison") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && address.isNotBlank()) {
                        onConfirm(name, address)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirmer la Commande")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = Color.Gray)
            }
        }
    )
}

@Composable
fun CategoryDrawer(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onClose: () -> Unit
) {
    val categories = listOf("Tous", ProductType.PIZZA, ProductType.TACOS, ProductType.DESSERT, ProductType.BOIS)

    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)).clickable { onClose() }) {
        Surface(
            modifier = Modifier.fillMaxHeight().width(280.dp).align(Alignment.CenterStart).clickable(enabled = false) {},
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Catégories", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onCategorySelected(category) },
                        color = if (isSelected) Color(0xFFD32F2F).copy(alpha = 0.1f) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(16.dp),
                            color = if (isSelected) Color(0xFFD32F2F) else Color.Black,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerRegistrationDialog(
    initialCustomer: Customer? = null,
    customerViewModel: CustomerViewModel,
    onDismiss: () -> Unit,
    onLoginOrRegister: (String, String) -> Unit
) {
    var phone by remember { mutableStateOf(initialCustomer?.phone ?: "") }
    var password by remember { mutableStateOf(initialCustomer?.password ?: "") }
    val loading by customerViewModel.loading.collectAsState()
    val error by customerViewModel.error.collectAsState()

    val isAlreadyLoggedIn = initialCustomer != null

    AlertDialog(
        onDismissRequest = if (!loading) onDismiss else ({}),
        title = { Text(if (isAlreadyLoggedIn) "Mon Compte" else "Connexion / Inscription", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (isAlreadyLoggedIn) {
                    Text("Vous êtes connecté avec le numéro : ${initialCustomer.phone}", fontSize = 14.sp)
                } else {
                    Text("Entrez votre numéro et mot de passe pour commander.", fontSize = 14.sp, color = Color.Gray)
                    
                    if (error != null) {
                        Text(text = error!!, color = Color.Red, fontSize = 12.sp)
                    }

                    OutlinedTextField(
                        value = phone, 
                        onValueChange = { phone = it }, 
                        label = { Text("Téléphone") }, 
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !loading,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mot de passe") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        enabled = !loading,
                        shape = RoundedCornerShape(12.dp)
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    enabled = !loading,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Text("Valider")
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !loading) {
                Text(if (isAlreadyLoggedIn) "Fermer" else "Annuler", color = Color.Gray)
            }
        }
    )
}

@Composable
fun CartSidebar(
    cartItems: Map<Products, Int>,
    isLoading: Boolean = false,
    onUpdateQty: (Products, Int) -> Unit,
    onRemoveItem: (Products) -> Unit,
    onCheckout: () -> Unit,
    onClose: () -> Unit, 
    modifier: Modifier = Modifier
) {
    val total = cartItems.entries.sumOf { it.key.price * it.value }

    Surface(
        modifier = modifier,
        color = Color.White,
        shadowElevation = 16.dp
    ) {
        Column(modifier = Modifier.padding(20.dp).fillMaxHeight()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Votre Panier", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = onClose, enabled = !isLoading) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Le panier est vide.", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(cartItems.entries.toList()) { (product, qty) ->
                        CartItemRow(product, qty, onUpdateQty, onRemoveItem, isEnabled = !isLoading)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (cartItems.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Récapitulatif", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("$total Fc", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onCheckout,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("PASSER LA COMMANDE", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    product: Products, 
    qty: Int, 
    onUpdateQty: (Products, Int) -> Unit,
    onRemoveItem: (Products) -> Unit,
    isEnabled: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF5F5F5))) {
            if (product.image.isNotEmpty()) {
                AsyncImage(model = product.image, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            } else {
                Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.align(Alignment.Center), tint = Color.LightGray)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("${product.price} Fc", color = Color(0xFFD32F2F), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onUpdateQty(product, -1) }, modifier = Modifier.size(24.dp), enabled = isEnabled) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = Color.Gray)
                }
                Text(qty.toString(), modifier = Modifier.padding(horizontal = 12.dp), fontWeight = FontWeight.Bold)
                IconButton(onClick = { onUpdateQty(product, 1) }, modifier = Modifier.size(24.dp), enabled = isEnabled) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFD32F2F))
                }
            }
        }
        IconButton(onClick = { onRemoveItem(product) }, enabled = isEnabled) {
            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.LightGray)
        }
    }
}

@Composable
fun ProductCard(product: Products, isMobile: Boolean, onAddClick: () -> Unit = {}) {
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
                    .height(if (isMobile) 160.dp else 220.dp)
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
                        modifier = Modifier.size(48.dp).align(Alignment.Center),
                        tint = Color.LightGray
                    )
                }
                
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                    color = Color(0xFFD32F2F),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "${product.price} Fc",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(if (isMobile) 12.dp else 20.dp)) {
                Text(
                    text = product.name,
                    fontSize = if (isMobile) 16.sp else 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.desc ?: "Délicieux plat authentique.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    lineHeight = 16.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AJOUTER", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun HomeFooter(isMobile: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF212529))
            .padding(vertical = if (isMobile) 40.dp else 60.dp, horizontal = if (isMobile) 24.dp else 60.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isMobile) Arrangement.Center else Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Mama Africa", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            
            if (!isMobile) {
                Text("© 2024 Mama Africa. Logistics & Gastronomy.", color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp)
                
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    FooterLink("Privacy Policy")
                    FooterLink("Terms of Service")
                    FooterLink("Contact")
                }
            }
        }
        
        if (isMobile) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("© 2024 Mama Africa.", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FooterLink("Privacy")
                Spacer(modifier = Modifier.width(16.dp))
                FooterLink("Terms")
                Spacer(modifier = Modifier.width(16.dp))
                FooterLink("Support")
            }
        }
    }
}

@Composable
fun FooterLink(text: String) {
    Text(
        text = text,
        color = Color.White.copy(alpha = 0.6f),
        fontSize = 13.sp,
        modifier = Modifier.clickable { }
    )
}
