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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.util.Locale

@SuppressLint("MissingPermission")
@Composable
fun Checkout(navController: NavController) {
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                        Text("LKR 85,500", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Shipping", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                        Text("LKR 1,500", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                    }
                    
                    Divider(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total", 
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            "LKR 87,000", 
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
            
            // Place Order Button
            Button(
                onClick = {
                    if (name.isBlank()) {
                        Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                    } else {
                        orderPlaced = true
                        Toast.makeText(
                            context,
                            "Thank you for your order, $name! Your shoes will be delivered soon.",
                            Toast.LENGTH_LONG
                        ).show()
                        
                        // Navigate back to home after order
                        navController.navigate("home")
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