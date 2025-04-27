package com.example.myapplication.UIs

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.DataSource
import com.example.myapplication.LogoBar
import com.example.myapplication.Navigation.Navbar
import com.example.myapplication.Network.ConnectivityStatusBanner
import com.example.myapplication.models.Product
import kotlinx.coroutines.delay

@Composable
fun ProductsScreen(navController: NavController){
    var offsetX by remember { mutableStateOf(1000.dp) } // Start off-screen
    val animatedOffsetX by animateDpAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 500)
    )

    // Trigger the animation when the screen is displayed
    LaunchedEffect(Unit) {
        delay(100) // Optional delay to make the transition smoother
        offsetX = 0.dp // Move the screen into view
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = animatedOffsetX),
    )
    {
        Column (
            modifier = Modifier.fillMaxSize(),
        )
        {
            Spacer(modifier = Modifier.height(15.dp))

            Column (modifier = Modifier.weight(2f).verticalScroll(rememberScrollState()).fillMaxWidth().padding(bottom = 60.dp))
            {
                LogoBar()
                ConnectivityStatusBanner()
                Heading()
                Spacer(modifier = Modifier.height(8.dp))
                ReusableTextHeader(title = "Nike",onButtonClick = {})
                NikeList(productList = DataSource().loadNike(), navController = navController)
                Spacer(modifier = Modifier.height(8.dp))
                ReusableTextHeader(title = "Adidas", onButtonClick = {})
                NikeList(productList = DataSource().loadAdidas(), navController = navController)
            }
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
        ){
            Navbar(navController)
        }


    }



}

@Composable
fun Heading(){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically)
    {
        Text(
            text = "Products",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun Nikes(product: Product, modifier: Modifier = Modifier, navController: NavController) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .width(200.dp)
            .height(330.dp) // Adjust height to match the design
            .clip(RoundedCornerShape(16.dp)), // Rounded corners
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary) // Use MaterialTheme primary color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Product image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(product.imagesResourceId),
                    contentDescription = null, // Content description for accessibility
                    modifier = Modifier.size(300.dp), // Image size
                    contentScale = ContentScale.Fit
                )
            }

            // Product details
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(product.stringResourceId),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = stringResource(product.categoryRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = stringResource(product.priceRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // View button
            Button(
                onClick = {navController.navigate("masterview/${product.imagesResourceId}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = "View",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}



@Composable
fun NikeList(productList: List<Product>, navController: NavController){
    LazyRow()
    {
        items(productList){
                product -> Nikes(product = product, modifier = Modifier.padding(10.dp), navController = navController)
        }
    }
}