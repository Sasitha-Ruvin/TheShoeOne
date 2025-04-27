package com.example.myapplication.UIs

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Navigation.Navbar
import com.example.myapplication.Network.ConnectivityStatusBanner
import com.example.myapplication.R
import com.example.myapplication.ViewModels.AuthState
import com.example.myapplication.ViewModels.AuthViewModel

@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    // --- auth redirect logic (unchanged) ---
    val authState by authViewModel.authState.observeAsState()
    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo("profile") { inclusive = true }
            }
        }
    }

    // Context for SharedPreferences
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

    // User profile state variables
    var isEditing by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf(sharedPrefs.getString("username", "MickyPerera") ?: "MickyPerera") }
    var password by remember { mutableStateOf(sharedPrefs.getString("password", "********") ?: "********") }
    var email by remember { mutableStateOf(sharedPrefs.getString("email", "micky@gmail.com") ?: "micky@gmail.com") }
    var contact by remember { mutableStateOf(sharedPrefs.getString("contact", "0123-345-678") ?: "0123-345-678") }
    var displayName by remember { mutableStateOf(sharedPrefs.getString("displayName", "Micky Perera") ?: "Micky Perera") }

    // --- launchers for camera & gallery ---
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        // bitmap != null → camera opened successfully
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // uri != null → user picked an image
    }

    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Row(
                    modifier = Modifier.fillMaxWidth(),
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

                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(16.dp))

                // Profile card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Profile image + camera icon overlay
                        Box {
                            // placeholder profile pic
                            Image(
                                painter = painterResource(R.drawable.profile),
                                contentDescription = "Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            )
                            IconButton(
                                onClick = { showDialog = true },
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Change Photo",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        
                        // Display name - editable when in edit mode
                        if (isEditing) {
                            OutlinedTextField(
                                value = displayName,
                                onValueChange = { displayName = it },
                                label = { Text("Name", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    cursorColor = MaterialTheme.colorScheme.primary,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        
                        // Profile fields - show different UI based on edit mode
                        if (isEditing) {
                            // Editable profile fields
                            EditableProfileField(
                                value = username,
                                onValueChange = { username = it },
                                label = "Username",
                                icon = Icons.Default.Person
                            )
                            
                            EditableProfileField(
                                value = password,
                                onValueChange = { password = it },
                                label = "Password",
                                icon = Icons.Default.Lock,
                                isPassword = true
                            )
                            
                            EditableProfileField(
                                value = email,
                                onValueChange = { email = it },
                                label = "E-mail",
                                icon = Icons.Default.Email
                            )
                            
                            EditableProfileField(
                                value = contact,
                                onValueChange = { contact = it },
                                label = "Contact",
                                icon = Icons.Default.Phone
                            )
                        } else {
                            // Static profile fields
                            ProfileDetailRow("Username", username)
                            ProfileDetailRow("Password", password)
                            ProfileDetailRow("E-mail", email)
                            ProfileDetailRow("Contact", contact)
                        }
                        
                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { 
                                    if (isEditing) {
                                        // Save profile data to SharedPreferences
                                        val editor = sharedPrefs.edit()
                                        editor.putString("username", username)
                                        editor.putString("password", password)
                                        editor.putString("email", email)
                                        editor.putString("contact", contact)
                                        editor.putString("displayName", displayName)
                                        editor.apply()
                                        
                                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    isEditing = !isEditing 
                                },
                                modifier = Modifier.weight(1f).height(40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(if (isEditing) "Save" else "Edit", color = MaterialTheme.colorScheme.onPrimary)
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = { /* Delete */ },
                                modifier = Modifier.weight(1f).height(40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Delete", color = MaterialTheme.colorScheme.onError)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { authViewModel.signOut() },
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Logout", color = MaterialTheme.colorScheme.onError)
                        }
                    }
                }
            }
        }

        // navigation bar
        Box(modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth()) {
            Navbar(navController)
        }

        // choice dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Change Profile Photo") },
                text = { Text("Select an option") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            cameraLauncher.launch(null)
                            showDialog = false
                        }
                    ) { Text("Take Photo") }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            galleryLauncher.launch("image/*")
                            showDialog = false
                        }
                    ) { Text("Choose from Gallery") }
                }
            )
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EditableProfileField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = { 
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}