package com.dmobley0608.cwac

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

import com.dmobley0608.cwac.data.view_model.AuthViewModel
import com.dmobley0608.cwac.ui.navigation.NavigationGraph

import kotlinx.coroutines.launch
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetValue
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ScaffoldState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.dmobley0608.cwac.data.model.Show
import com.dmobley0608.cwac.data.model.WorkOrder
import com.dmobley0608.cwac.data.view_model.MainViewModel
import com.dmobley0608.cwac.ui.navigation.Screen
import com.dmobley0608.cwac.ui.navigation.bottomNav
import com.dmobley0608.cwac.ui.theme.NavBarBackground
import com.dmobley0608.cwac.ui.theme.PrimaryBackground
import com.dmobley0608.cwac.ui.theme.SecondaryBackground
import com.dmobley0608.cwac.ui.views.LoadingScreen
import com.dmobley0608.cwac.ui.views.show.AddEditShowDialog
import com.dmobley0608.cwac.ui.views.workOrder.addEditDialog.AddEditWorkOrderDialog
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CWACApp(
    navController: NavController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val modalSheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val isSheetFullScreen by remember { mutableStateOf(false) }
    val modifier = if (isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    val showAddShowDialog = remember { mutableStateOf(false) }
    val showAddWorkOrderDialog = remember { mutableStateOf(false) }
    if (mainViewModel.isLoading) {
        LoadingScreen()
    } else {
        ModalBottomSheetLayout(

            sheetContent = {
                if (authViewModel.userIsAuthenticated.value) {
                    MoreBottomSheet(
                        viewModel = mainViewModel,
                        modifier = modifier,
                        modalSheetState = modalSheetState,
                        scope = scope,
                        showAddShowDialog = showAddShowDialog,
                        authViewModel = authViewModel,
                        showAddWorkOrderDialog = showAddWorkOrderDialog,
                        navController = navController
                    )
                }
            },
            sheetShape = RoundedCornerShape(5.dp),
            sheetState = modalSheetState
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {
                    if (authViewModel.userIsAuthenticated.value) {
                        BottomNavigation(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = NavBarBackground,
                        ) {
                            bottomNav.forEach { screen ->
                                BottomNavigationItem(
                                    selected = true,
                                    onClick = {
                                        mainViewModel.setCurrentScreen(screen)
                                        navController.navigate(screen.route)
                                    },

                                    selectedContentColor = Color.White,
                                    unselectedContentColor = Color.White,
                                    label = { Text(screen.title, color = Color.White, fontWeight=FontWeight(450)) },
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = screen.icon),
                                            contentDescription = "Home Nav",
                                            tint = Color.White
                                        )
                                    },
                                )
                            }


                            BottomNavigationItem(
                                selected = true,
                                onClick = {
                                    scope.launch {
                                        if (modalSheetState.isVisible) {
                                            modalSheetState.hide()
                                        } else {
                                            modalSheetState.show()

                                        }
                                    }
                                },
                                selectedContentColor = Color.Blue,
                                unselectedContentColor = Color.Black,
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                                        contentDescription = "More",
                                        tint = Color.White
                                    )
                                },

                                )
                        }
                        val show = Show()
                        if (showAddShowDialog.value) {
                            AddEditShowDialog(
                                showAddShowDialog = showAddShowDialog,
                                show = show,
                                handleOnSubmit = { navController.navigate(Screen.Home.route) }
                            )
                        }
                        if (showAddWorkOrderDialog.value) {
                            AddEditWorkOrderDialog(
                                showDialog = showAddWorkOrderDialog,
                                navToWorkOrderDetail = { /*TODO*/ },
                                workOrder = WorkOrder()
                            )
                        }
                    }
                }


            ) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .background(
                            brush = Brush.linearGradient(
                                start = Offset(1f, .5f),
                                colors = listOf(PrimaryBackground, SecondaryBackground)
                            )
                        )
                ) {
                    NavigationGraph(
                        authViewModel = authViewModel,
                        mainViewModel = mainViewModel,
                        navController = navController as NavHostController
                    )


                }


            }
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MoreBottomSheet(
    viewModel: MainViewModel,
    modifier: Modifier,
    modalSheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    showAddShowDialog: MutableState<Boolean>,
    authViewModel: AuthViewModel,
    showAddWorkOrderDialog: MutableState<Boolean>,
    navController: NavController
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(NavBarBackground)
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxSize(), verticalArrangement = Arrangement.Top
        ) {
            //Home Screen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (viewModel.currentUser.value?.role == "ADMIN") {
                    IconButton(
                        onClick = {
                            scope.launch {
                                modalSheetState.hide()
                                showAddShowDialog.value = !showAddShowDialog.value
                            }
                        },
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_show),
                                contentDescription = "add show",
                                Modifier.size(35.dp),
                                tint = Color.White
                            )
                            Text("Add Show", color = Color.White)
                        }

                    }
                    IconButton(
                        onClick = {
                            scope.launch {
                                showAddWorkOrderDialog.value = !showAddWorkOrderDialog.value
                                modalSheetState.hide()
                            }
                        },
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_work_orders),
                                contentDescription = "add order",
                                Modifier.size(35.dp),
                                tint = Color.White
                            )
                            Text("Add Order", color=Color.White)

                        }

                    }
                }
                if (authViewModel.userIsAuthenticated.value) {
                    IconButton(
                        onClick = {
                            authViewModel.signOut()
                            viewModel.resetUser()
                            scope.launch {
                                modalSheetState.hide()
                            }
                            navController.navigate(Screen.LoginScreen.route)
                        },
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_logout),
                                contentDescription = "add news",
                                Modifier.size(35.dp),
                                tint=Color.White
                            )
                            Text("Sign-out", color=Color.White)
                        }

                    }
                }
            }
        }
    }
}
