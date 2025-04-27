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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.myapplication.Navigation.Navbar
import com.example.myapplication.Network.ConnectivityStatusBanner
import com.example.myapplication.R

@Composable
fun Cart(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp) // Reserve space for the navbar
        ) {
            // Logo
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo), // Replace with your logo resource
                    contentDescription = "Logo",
                    modifier = Modifier.size(170.dp), // Adjust size as needed
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


            // Cart Items
            CartItem(
                imageResId = R.drawable.shoe26,
                name = "Nike Dunk Retro",
                category = "Women's Shoes",
                color = "Black and White",
                size = "UK 9",
                quantity = "1",
                price = "LKR 28,000"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CartItem(
                imageResId = R.drawable.shoe32,
                name = "Adidas Ultraboost",
                category = "Men's Shoes",
                color = "White",
                size = "UK 10",
                quantity = "2",
                price = "LKR 32,100"
            )
            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f), thickness = 1.dp)

            // Summary
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Shipping",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
                Text(
                    text = "LKR 1,500",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sub Total",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                )
                Text(
                    text = "LKR 85,500",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Clear action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Clear",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { navController.navigate("checkout") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Buy",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Position at the bottom
                .fillMaxWidth()
        ) {
            Navbar(navController = navController)
        }
    }
}

@Composable
fun CartItem(
    imageResId: Int,
    name: String,
    category: String,
    color: String,
    size: String,
    quantity: String,
    price: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Product Image
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Product Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        fontSize = 16.sp,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Color: $color",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Size: $size",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Qty: $quantity",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // Price & Remove Button
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Remove item action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .width(100.dp) // Adjust width to match the look in the image
                        .height(30.dp)
                ) {
                    Text(
                        text = "Remove",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = price,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Right,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
