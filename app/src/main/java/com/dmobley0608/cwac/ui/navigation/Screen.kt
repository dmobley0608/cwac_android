package com.dmobley0608.cwac.ui.navigation

import androidx.annotation.DrawableRes
import com.dmobley0608.cwac.R

sealed class Screen(val route:String, val title:String, @DrawableRes val icon:Int) {
    object LoginScreen:Screen("login-screen", "Login", R.drawable.baseline_more_horiz_24 )
    object SignupScreen:Screen("signup-screen", "Register", R.drawable.baseline_more_horiz_24)
    object Home:Screen("home", "Home", R.drawable.ic_home)
    object WorkOrderList:Screen("work-orders", "Orders", R.drawable.ic_work_orders)
    object WorkDetails:Screen("work-order-details", "Order Details", R.drawable.ic_work_orders)
    object ShowDetails:Screen("show-details", "Show Details", R.drawable.ic_work_orders)
    object Inventory:Screen("inventory", "Supplies",R.drawable.ic_inventory)
}

val bottomNav = listOf(
    Screen.Home,
    Screen.WorkOrderList,
    Screen.Inventory
)