package com.abahz.africa.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abahz.africa.model.Shop
import com.abahz.africa.viewmodel.ShopViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShopRegistrationScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: ShopViewModel = koinViewModel()
) {
    var phone by remember { mutableStateOf("") }
    var shopName by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var rccm by remember { mutableStateOf("") }
    var idNat by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var passwordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left Side - Image and Branding
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF121212)) 
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(60.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Mama Africa",
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Professional Epicurean Logistics.\nMaster your kitchen's supply chain.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 20.sp,
                    lineHeight = 28.sp
                )
            }
        }

        // Right Side - Registration Form
        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight()
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(48.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Create an Account",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "Join Mama Africa to manage your culinary operations.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 12.dp, bottom = 32.dp)
                    )

                    if (error != null) {
                        Surface(
                            color = Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                        ) {
                            Text(
                                text = error!!,
                                color = Color(0xFFD32F2F),
                                modifier = Modifier.padding(16.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    RegistrationField(
                        label = "Phone Number",
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "+1 (555) 000-0000",
                        icon = Icons.Default.Phone
                    )

                    RegistrationField(
                        label = "Shop Name",
                        value = shopName,
                        onValueChange = { shopName = it },
                        placeholder = "Mama Africa Kitchen",
                        icon = Icons.Default.Home
                    )

                    RegistrationField(
                        label = "Owner Full Name",
                        value = ownerName,
                        onValueChange = { ownerName = it },
                        placeholder = "Chef Aïcha Diallo",
                        icon = Icons.Default.Person
                    )

                    RegistrationField(
                        label = "Password (Admin)",
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "••••••••",
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordToggle = { passwordVisible = !passwordVisible }
                    )

                    RegistrationField(
                        label = "Confirm Password",
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "••••••••",
                        icon = Icons.Default.Lock,
                        isPassword = true
                    )

                    Text(
                        text = "Optional Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )

                    RegistrationField(
                        label = "RCCM",
                        value = rccm,
                        onValueChange = { rccm = it },
                        placeholder = "RCCM-12345",
                        icon = Icons.Default.Description
                    )

                    RegistrationField(
                        label = "ID Nat",
                        value = idNat,
                        onValueChange = { idNat = it },
                        placeholder = "ID-NAT-67890",
                        icon = Icons.Default.Badge
                    )

                    RegistrationField(
                        label = "Address",
                        value = address,
                        onValueChange = { address = it },
                        placeholder = "123 Culinary St, Kinshasa",
                        icon = Icons.Default.LocationOn
                    )

                    Row(
                        modifier = Modifier.padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreeToTerms,
                            onCheckedChange = { agreeToTerms = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFD32F2F))
                        )
                        val annotatedString = buildAnnotatedString {
                            append("I agree to the ")
                            withStyle(style = SpanStyle(color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)) {
                                append("Terms of Service")
                            }
                            append(" and ")
                            withStyle(style = SpanStyle(color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)) {
                                append("Privacy Policy")
                            }
                            append(".")
                        }
                        Text(
                            text = annotatedString,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Button(
                        onClick = {
                            if (password == confirmPassword && agreeToTerms && phone.isNotBlank() && shopName.isNotBlank() && ownerName.isNotBlank() && password.isNotBlank()) {
                                viewModel.addShop(
                                    Shop(
                                        phone = phone,
                                        name = shopName,
                                        owner = ownerName,
                                        admin = password, // admin is used as password
                                        rccm = rccm,
                                        idNat = idNat,
                                        address = address,
                                        created = System.currentTimeMillis(),
                                        updated = System.currentTimeMillis()
                                    )
                                )
                            }
                        },
                        enabled = !loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        if (loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Create Account", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Already have an account? ", fontSize = 14.sp, color = Color.Gray)
                        Text(
                            text = "Log in here",
                            fontSize = 14.sp,
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToLogin() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RegistrationField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 16.sp, color = Color.LightGray) },
            leadingIcon = { Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp)) },
            trailingIcon = if (isPassword && onPasswordToggle != null) {
                {
                    IconButton(onClick = onPasswordToggle) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFFD32F2F),
                unfocusedContainerColor = Color(0xFFF9F9F9),
                focusedContainerColor = Color(0xFFF9F9F9)
            ),
            singleLine = true
        )
    }
}
