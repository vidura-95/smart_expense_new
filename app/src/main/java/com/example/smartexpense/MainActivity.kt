package com.example.smartexpense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartexpense.ui.theme.SmartExpenseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartExpenseTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {

                    // 1. Login Screen
                    composable("login") {
                        LoginScreen(
                            onNavigateToRegister = { navController.navigate("register") },
                            // UPDATED: Navigate to dashboard on success
                            onLoginSuccess = {
                                navController.navigate("dashboard") {
                                    // Pop login from stack so back button doesn't go to login
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    //2. NEW: Register Screen
                    composable("register") {
                        RegisterScreen(
                            onNavigateBack = { navController.popBackStack() }, // Go back to login
                            onRegisterSuccess = {
                                // After successful register, go to dashboard or back to login
                                // For now, let's go to dashboard
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }


                    // 3. NEW: Dashboard Screen
                    composable("dashboard") {
                        DashboardScreen(
                            onNavigateToFood = { navController.navigate("food") },
                            onNavigateToClothes = { navController.navigate("clothes") },
                            onNavigateToOther = { navController.navigate("other") }
                        )
                    }

                    // 4. Food Expense Screen
                    composable("food") {
                        FoodExpenseScreen(
                            onNavigateToClothes = {
                                navController.navigate("clothes") {
                                    popUpTo("dashboard") // Keep back stack clean
                                }
                            },
                            onNavigateToOther = {
                                navController.navigate("other") {
                                    popUpTo("dashboard")
                                }
                            }
                        )
                    }

                    // 5. Clothes Expense Screen
                    composable("clothes") {
                        ClothExpenseScreen(
                            onNavigateToFood = {
                                navController.navigate("food") {
                                    popUpTo("dashboard")
                                }
                            },
                            onNavigateToOther = {
                                navController.navigate("other") {
                                    popUpTo("dashboard")
                                }
                            }
                        )
                    }

                    // 6. Other Expense Screen
                    composable("other") {
                        OtherExpenseScreen(
                            onNavigateToFood = {
                                navController.navigate("food") {
                                    popUpTo("dashboard")
                                }
                            },
                            onNavigateToClothes = {
                                navController.navigate("clothes") {
                                    popUpTo("dashboard")
                                }
                            }
                        )
                    }
                    // 7. NEW: Add Expense Screen
                    composable("add_expense") {
                        AddExpenseScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
