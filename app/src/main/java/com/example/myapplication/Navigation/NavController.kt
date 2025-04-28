package com.example.myapplication.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.DataSource
import com.example.myapplication.UIs.Cart
import com.example.myapplication.UIs.Checkout
import com.example.myapplication.UIs.Home
import com.example.myapplication.UIs.LatestProductDetailView
import com.example.myapplication.UIs.Login
import com.example.myapplication.UIs.MasterView
import com.example.myapplication.UIs.ProductsScreen
import com.example.myapplication.UIs.ProfileScreen
import com.example.myapplication.UIs.SignUp
import com.example.myapplication.UIs.getStarted
import com.example.myapplication.ViewModels.AuthViewModel
import com.example.myapplication.ViewModels.CartViewModel


@Composable
fun Navigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, cartViewModel: CartViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "getstarted", builder = {
        composable("getstarted"){
            getStarted(navController);
        }
        composable("signup"){
            SignUp(navController, authViewModel);
        }
        composable("login"){
            Login(modifier, navController, authViewModel);
        }
        composable("home"){
            Home(navController, authViewModel);
        }
        composable("cart"){
            Cart(navController, cartViewModel)
        }
        composable("checkout"){
            Checkout(navController, cartViewModel)
        }
        composable("profile"){
            ProfileScreen(navController, authViewModel)
        }
        composable("products"){
            ProductsScreen(navController)
        }
        composable("masterview/{imageResourceId}") { backStackEntry ->
            val imageResourceId = backStackEntry.arguments?.getString("imageResourceId")?.toInt()
            val product = DataSource().loadTrending().find { it.imagesResourceId == imageResourceId }
                ?: DataSource().loadNike().find { it.imagesResourceId == imageResourceId }

            product?.let {
                MasterView(product = it, navController = navController, cartViewModel = cartViewModel)
            }
        }
        
        // Route for latest products from API/JSON
        composable(
            route = "latest-product-detail/{productName}/{imageUrl}/{price}"
        ) { backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            val price = backStackEntry.arguments?.getString("price") ?: "0.0"
            
            LatestProductDetailView(
                navController = navController,
                productName = productName,
                imageUrl = imageUrl,
                price = price,
                cartViewModel = cartViewModel
            )
        }
    })
}