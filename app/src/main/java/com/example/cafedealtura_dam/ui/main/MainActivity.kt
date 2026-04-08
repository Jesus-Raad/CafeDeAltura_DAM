package com.example.cafedealtura_dam.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.model.Users
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // PRUEBA
        UserSession.setUser(
            Users(
                id_user = 3,
                name = "Jesus",
                surname = "Raad",
                password = "123456",
                rol = "admin",
                email = "jesus2026@gmail.com",
                phone = "+34634482312"
            )
        )
        // ///////

        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        setupBottomNavigation(bottomNav, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    bottomNav.visibility = View.GONE
                }
                else -> {
                    bottomNav.visibility = View.VISIBLE
                }
            }

            when (destination.id) {
                R.id.homeFragment -> bottomNav.menu.findItem(R.id.homeFragment).isChecked = true
                R.id.productsFragment,
                R.id.productDetailFragment,
                R.id.filteredProductsFragment -> {
                    bottomNav.menu.findItem(R.id.productsFragment).isChecked = true
                }
                R.id.cartFragment,
                R.id.paymentFragment,
                R.id.paymentSuccessFragment -> {
                    bottomNav.menu.findItem(R.id.cartFragment).isChecked = true
                }
                R.id.profileFragment,
                R.id.ordersFragment,
                R.id.direccionesFragment,
                R.id.paymentMethodsFragment,
                R.id.settingsFragment,
                R.id.changePasswordFragment -> {
                    bottomNav.menu.findItem(R.id.profileFragment).isChecked = true
                }
            }
        }
    }

    private fun setupBottomNavigation(
        bottomNav: BottomNavigationView,
        navController: NavController
    ) {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navigateToTopLevel(navController, R.id.homeFragment)
                    true
                }

                R.id.productsFragment -> {
                    navigateToTopLevel(navController, R.id.productsFragment)
                    true
                }

                R.id.cartFragment -> {
                    navigateToTopLevel(navController, R.id.cartFragment)
                    true
                }

                R.id.profileFragment -> {
                    navigateToTopLevel(navController, R.id.profileFragment)
                    true
                }

                else -> false
            }
        }

        bottomNav.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> navigateToTopLevel(navController, R.id.homeFragment)
                R.id.productsFragment -> navigateToTopLevel(navController, R.id.productsFragment)
                R.id.cartFragment -> navigateToTopLevel(navController, R.id.cartFragment)
                R.id.profileFragment -> navigateToTopLevel(navController, R.id.profileFragment)
            }
        }
    }

    private fun navigateToTopLevel(navController: NavController, destinationId: Int) {
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(false)
            .setPopUpTo(navController.graph.startDestinationId, false)
            .build()

        if (navController.currentDestination?.id != destinationId) {
            navController.navigate(destinationId, null, options)
        } else {
            navController.popBackStack(destinationId, false)
        }
    }
}