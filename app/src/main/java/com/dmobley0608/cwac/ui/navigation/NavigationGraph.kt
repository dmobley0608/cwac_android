package com.dmobley0608.cwac.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dmobley0608.cwac.data.view_model.AuthViewModel
import com.dmobley0608.cwac.data.view_model.MainViewModel
import com.dmobley0608.cwac.ui.views.home.HomeScreen
import com.dmobley0608.cwac.ui.views.inventory.InventoryScreen
import com.dmobley0608.cwac.ui.views.show.showDetailScreen.ShowDetailScreen
import com.dmobley0608.cwac.ui.views.sign_in.SignInScreen
import com.dmobley0608.cwac.ui.views.sign_up.SignUpScreen
import com.dmobley0608.cwac.ui.views.workOrder.detailScreen.WorkOrderDetailScreen
import com.dmobley0608.cwac.ui.views.workOrder.workOrderList.WorkOrderListScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    navController: NavHostController
) {

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.SignupScreen.route) {
            mainViewModel.setCurrentScreen(Screen.SignupScreen)
            SignUpScreen(authViewModel=authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) }
            )
        }
        composable(Screen.LoginScreen.route) {
            mainViewModel.setCurrentScreen(Screen.LoginScreen)
            if(authViewModel.userIsAuthenticated.value){
                navController.navigate(Screen.Home.route)
            }
            SignInScreen(
                authViewModel=authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
                onLoginSuccess = {navController.navigate(Screen.WorkOrderList.route)}
            )
        }
        composable(Screen.Home.route) {
            authenticateRoute(navController, authViewModel)
            mainViewModel.setCurrentScreen(Screen.Home)
            if(mainViewModel.currentUser.value == null){
                mainViewModel.loadCurrentUser()
            }
            HomeScreen(
                navToWorkOrderDetail = {navController.navigate("${Screen.WorkDetails.route}/${it}")},
                navToShowDetail = {navController.navigate("${Screen.ShowDetails.route}/${it}")}
            )


        }
        composable(Screen.WorkOrderList.route){
            authenticateRoute(navController, authViewModel)
            mainViewModel.setCurrentScreen(Screen.WorkOrderList)
            WorkOrderListScreen {
                navController.navigate("${Screen.WorkDetails.route}/${it}")
            }
        }
        composable("${Screen.WorkDetails.route}/{workOrderId}"){
            authenticateRoute(navController, authViewModel)
            val workOrderId:String = it
                .arguments?.getString("workOrderId") ?: ""
            mainViewModel.setCurrentScreen(Screen.WorkDetails)
            WorkOrderDetailScreen(
                onMarkComplete = {navController.navigateUp()},
                onSendOrderBack = {navController.navigateUp()},
                workOrderId = workOrderId)
        }
        composable("${Screen.ShowDetails.route}/{showId}"){
            authenticateRoute(navController, authViewModel)
            val showId:String = it
                .arguments?.getString("showId") ?: ""
            mainViewModel.setCurrentScreen(Screen.ShowDetails)
            ShowDetailScreen(showKey = showId )
        }
        composable(Screen.Inventory.route){
           authenticateRoute(navController, authViewModel)
            mainViewModel.setCurrentScreen(Screen.Inventory)
           InventoryScreen()
        }
    }
}

fun authenticateRoute(navController: NavHostController, viewModel: AuthViewModel){
    if(!viewModel.userIsAuthenticated.value){
        navController.navigate(Screen.LoginScreen.route)
    }
}