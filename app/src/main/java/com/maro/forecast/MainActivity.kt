package com.maro.forecast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maro.forecast.ui.loading.LoadingScreen
import com.maro.forecast.ui.locationsetting.LocationSettingScreen
import com.maro.forecast.ui.main.MainScreen
import com.maro.forecast.ui.theme.ForecastTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val rememberNavController = rememberNavController()
            ForecastTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = rememberNavController, startDestination = "loading" ) {
                        composable("loading") {
                            LoadingScreen(rememberNavController)
                        }
                        composable("main") {
                            MainScreen()
                        }
                        composable("LocationSetting") {
                            LocationSettingScreen(rememberNavController)
                        }
                    }
                }
            }
        }
    }
}