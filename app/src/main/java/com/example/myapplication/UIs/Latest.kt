package com.example.myapplication.UIs

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.ViewModels.LatestProductsViewModel
import com.example.myapplication.models.LatestProduct

@Composable
fun LatestProductCard(product: LatestProduct, navController: NavController){
    Log.d("LatestProductCard", "Loading product: ${product.name}, Image URL: ${product.imageUrl}")
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .width(200.dp)
            .height(280.dp)
            .clickable { 
                // Navigate to the detailed view with product details as URL parameters
                val productName = product.name.replace(" ", "-")
                val encodedUrl = java.net.URLEncoder.encode(product.imageUrl, "UTF-8")
                val price = product.price.toString()
                navController.navigate("latest-product-detail/$productName/$encodedUrl/$price") 
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ){
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(130.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            Text(
                text = product.name,
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)
            )
            Text(
                text = "LKR ${product.price}",
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            Button(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                onClick = {
                    // Navigate to the detailed view with product details as URL parameters
                    val productName = product.name.replace(" ", "-")
                    val encodedUrl = java.net.URLEncoder.encode(product.imageUrl, "UTF-8")
                    val price = product.price.toString()
                    navController.navigate("latest-product-detail/$productName/$encodedUrl/$price")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "View Details",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun LatestProductList(viewModel: LatestProductsViewModel, navController: NavController){
    val latestProducts by viewModel.latestProducts.collectAsState()

    LazyRow(modifier = Modifier){
        items(latestProducts){
                product-> LatestProductCard(product= product, navController = navController)
        }
    }
}