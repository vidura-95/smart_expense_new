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
                            onLoginSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 2. Register Screen
                    composable("register") {
                        RegisterScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onRegisterSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 3. Dashboard Screen
                    composable("dashboard") {
                        DashboardScreen(
                            onNavigateToFood = { navController.navigate("food") },
                            onNavigateToClothes = { navController.navigate("clothes") },
                            onNavigateToOther = { navController.navigate("other") },
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("dashboard") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 4. Food Expense Screen
                    composable("food") {
                        FoodExpenseScreen(
                            onNavigateToClothes = {
                                navController.navigate("clothes") {
                                    popUpTo("dashboard")
                                }
                            },
                            onNavigateToOther = {
                                navController.navigate("other") {
                                    popUpTo("dashboard")
                                }
                            },
                            onNavigateToExpenseInput = { navController.navigate("expense_input") },
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToExpenseUpdate = { expenseId ->
                                navController.navigate("update_expense/$expenseId")
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
                            },
                            onNavigateToExpenseInput = { navController.navigate("expense_input") },
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToExpenseUpdate = { expenseId ->
                                navController.navigate("update_expense/$expenseId")
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
                            },
                            onNavigateToExpenseInput = { navController.navigate("expense_input") },
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToExpenseUpdate = { expenseId ->
                                navController.navigate("update_expense/$expenseId")
                            }
                        )
                    }

                    // 7. Expense Input Form Screen
                    composable("expense_input") {
                        ExpenseInputForm(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    // 8. Expense Update Screen
                    composable("update_expense/{expenseId}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("expenseId")?.toIntOrNull() ?: 0
                        ExpenseUpdateForm(
                            expenseId = id,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
