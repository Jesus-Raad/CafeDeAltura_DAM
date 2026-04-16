package com.example.cafedealtura_dam.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.example.cafedealtura_dam.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    if (!navController.popBackStack(R.id.homeFragment, false)) {
                        navController.navigate(R.id.homeFragment)
                    }
                    true
                }

                R.id.productsFragment -> {
                    if (!navController.popBackStack(R.id.productsFragment, false)) {
                        navController.navigate(R.id.productsFragment)
                    }
                    true
                }

                R.id.cartFragment -> {
                    if (!navController.popBackStack(R.id.cartFragment, false)) {
                        navController.navigate(R.id.cartFragment)
                    }
                    true
                }

                R.id.profileFragment -> {
                    if (!navController.popBackStack(R.id.profileFragment, false)) {
                        navController.navigate(R.id.profileFragment)
                    }
                    true
                }

                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment,
                R.id.registerFragment -> {
                    bottomNav.visibility = View.GONE
                }

                else -> {
                    bottomNav.visibility = View.VISIBLE
                }
            }

            when (destination.id) {
                R.id.homeFragment -> {
                    bottomNav.menu.findItem(R.id.homeFragment)?.isChecked = true
                }

                R.id.productsFragment,
                R.id.filteredProductsFragment,
                R.id.productDetailFragment -> {
                    bottomNav.menu.findItem(R.id.productsFragment)?.isChecked = true
                }

                R.id.cartFragment,
                R.id.paymentFragment,
                R.id.paymentSuccessFragment -> {
                    bottomNav.menu.findItem(R.id.cartFragment)?.isChecked = true
                }

                R.id.profileFragment,
                R.id.ordersFragment,
                R.id.orderDetailFragment,
                R.id.direccionesFragment,
                R.id.paymentMethodsFragment,
                R.id.settingsFragment,
                R.id.changePasswordFragment,
                R.id.favoritosFragment -> {
                    bottomNav.menu.findItem(R.id.profileFragment)?.isChecked = true
                }
            }
        }
    }
}