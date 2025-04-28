package com.example.myapplication.UIs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.Navigation.Navbar
import com.example.myapplication.Network.ConnectivityStatusBanner
import com.example.myapplication.R
import com.example.myapplication.ViewModels.CartViewModel
import com.example.myapplication.models.CartItem
import java.text.NumberFormat
import java.util.Locale

@Composable
fun Cart(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    // Get the cart items
    val items = remember { cartViewModel.cartItems }

    // Recalculate subtotal/shipping/total whenever items change
    val subtotal = remember(items) { cartViewModel.getSubtotal() }
    val shipping = remember(items) { cartViewModel.getShippingCost() }
    val total = remember(items) { cartViewModel.getTotal() }

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "LK")).apply {
        maximumFractionDigits = 0
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp) // space for navbar
        ) {
            // Logo
            Row(
                modifier = Modifier.fillMaxWidth(),
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

            // Header
            Text(
                text = "Your cart",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            if (items.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Your cart is empty",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add some items to your cart",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("home") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(text = "Continue Shopping")
                        }
                    }
                }
            } else {
                // List of items
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    items(items) { item ->
                        CartItemRow(
                            cartItem = item,
                            onRemove = { cartViewModel.removeFromCart(item.id) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Summary & actions
                Divider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SummaryRow("Shipping", shipping, currencyFormat)
                    Spacer(modifier = Modifier.height(8.dp))
                    SummaryRow("Sub Total", subtotal, currencyFormat, bold = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    SummaryRow("Total", total, currencyFormat, big = true)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { cartViewModel.clearCart() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Clear",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { navController.navigate("checkout") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Buy",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Bottom navbar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Navbar(navController = navController)
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    amount: Double,
    currencyFormat: NumberFormat,
    bold: Boolean = false,
    big: Boolean = false
) {
    val style = when {
        big -> MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        bold -> MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        else -> MaterialTheme.typography.bodyMedium
    }
    val fontSize = when {
        big -> 20.sp
        bold -> 18.sp
        else -> 16.sp
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = style,
            fontSize = fontSize,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = currencyFormat.format(amount),
            style = style,
            fontSize = fontSize,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                if (cartItem.isFromApi && cartItem.imageUrl != null) {
                    AsyncImage(
                        model = cartItem.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (cartItem.imageResId != null) {
                    Image(
                        painter = painterResource(id = cartItem.imageResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = cartItem.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = cartItem.category,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Color: ${cartItem.color}", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary)
                    Text("Size: ${cartItem.size}", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary)
                    Text("Qty: ${cartItem.quantity}", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            // Price & remove
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onRemove,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .width(100.dp)
                        .height(30.dp)
                ) {
                    Text(
                        text = "Remove",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                val priceFormat = NumberFormat.getCurrencyInstance(Locale("en", "LK"))
                    .apply { maximumFractionDigits = 0 }

                Text(
                    text = priceFormat.format(cartItem.price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Right,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
