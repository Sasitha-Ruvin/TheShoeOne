package com.example.myapplication.UIs

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.myapplication.Navigation.Navbar
import com.example.myapplication.Network.ConnectivityStatusBanner
import com.example.myapplication.R
import com.example.myapplication.ViewModels.CartViewModel

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.util.Locale
import java.text.NumberFormat

@SuppressLint("MissingPermission")
@Composable
fun Checkout(navController: NavController, cartViewModel: CartViewModel) {
    // Animation
    var offsetX by remember { mutableStateOf(1000.dp) }
    val animatedOffsetX by animateDpAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 500)
    )

    // States
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("Fetching your location...") }
    var orderPlaced by remember { mutableStateOf(false) }
    
    // Context
    val context = LocalContext.current
    
    // Cart details
    val cartItems = cartViewModel.cartItems
    val subtotal = remember { cartViewModel.getSubtotal() }
    val shipping = remember { cartViewModel.getShippingCost() }
    val total = remember { cartViewModel.getTotal() }
    
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "LK"))
    currencyFormat.maximumFractionDigits = 0
    
    // Location client
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    // Permission
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val permissionsGranted = permissions.values.all { it }
        if (permissionsGranted) {
            getLocation(context, fusedLocationClient) { fetchedAddress ->
                address = fetchedAddress
            }
        }
    }
    
    // Trigger animation
    LaunchedEffect(Unit) {
        delay(100)
        offsetX = 0.dp
        
        // Request permissions if not granted
        if (!hasLocationPermissions(context)) {
            permissionLauncher.launch(locationPermissions)
        } else {
            getLocation(context, fusedLocationClient) { fetchedAddress ->
                address = fetchedAddress
            }
        }
    }
    
    // Main UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = animatedOffsetX)
                .padding(bottom = 56.dp) // Reserve space for navbar
                .verticalScroll(rememberScrollState())
        ) {
            // Logo
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(170.dp),
                    contentScale = ContentScale.Fit
                )
            }
            
            ConnectivityStatusBanner()
            
            // Checkout Header
            Text(
                text = "Checkout",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            
            // Checkout Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Name field
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        placeholder = { Text("Enter your full name") },
                        leadingIcon = { 
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Name",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        singleLine = true
                    )
                    
                    // Address field
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Delivery Address") },
                        placeholder = { Text("Your delivery address") },
                        leadingIcon = { 
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 3
                    )
                    
                    // Payment method (for display only)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CreditCard,
                                contentDescription = "Payment",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Cash on Delivery",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            // Order Summary
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Order Summary",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    
                    if (cartItems.isEmpty()) {
                        Text(
                            text = "Your cart is empty",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        // Item count
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Items (${cartItems.size})",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = currencyFormat.format(subtotal),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        // Shipping
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Shipping",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = currencyFormat.format(shipping),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        
                        // Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = currencyFormat.format(total),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            // Place Order Button
            Button(
                onClick = {
                    if (name.isNotBlank() && address.isNotBlank() && cartItems.isNotEmpty()) {
                        // Process the order
                        orderPlaced = true
                        // Clear cart after successful order
                        cartViewModel.clearCart()
                        
                        // Show success toast
                        Toast.makeText(
                            context,
                            "Order placed successfully!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        // Show error toast
                        val message = when {
                            name.isBlank() -> "Please enter your name"
                            address.isBlank() -> "Please enter your address"
                            cartItems.isEmpty() -> "Your cart is empty"
                            else -> "Please fill in all fields"
                        }
                        
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Place Order",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Bottom Navigation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Navbar(navController = navController)
        }
    }
}

// Helper functions for location handling
private fun hasLocationPermissions(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

@SuppressLint("MissingPermission")
private fun getLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onAddressReceived: (String) -> Unit
) {
    if (hasLocationPermissions(context)) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Get address from location
                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val addresses: List<Address>? = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )
                        
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val addressLines = mutableListOf<String>()
                            
                            // Add address components if available
                            for (i in 0..address.maxAddressLineIndex) {
                                addressLines.add(address.getAddressLine(i))
                            }
                            
                            onAddressReceived(addressLines.joinToString(", "))
                        } else {
                            onAddressReceived("${location.latitude}, ${location.longitude}")
                        }
                    } catch (e: Exception) {
                        onAddressReceived("${location.latitude}, ${location.longitude}")
                    }
                } else {
                    onAddressReceived("Location not available")
                }
            }
            .addOnFailureListener {
                onAddressReceived("Failed to get location")
            }
    } else {
        onAddressReceived("Location permission not granted")
    }
}

// Function to navigate to home with delay
private suspend fun homeNavigationWithDelay(navController: NavController) {
    delay(2000) // 2 second delay
    navController.navigate("home") {
        popUpTo("home") { inclusive = true }
    }
} 