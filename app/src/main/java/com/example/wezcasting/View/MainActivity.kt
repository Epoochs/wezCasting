package com.example.wezcasting.View

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.wezcasting.R
import com.example.wezcasting.View.Alert.AlertFragment
import com.example.wezcasting.View.Favourite.FavouriteFragment
import com.example.wezcasting.View.Home.HomeFragment
import com.example.wezcasting.View.Settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(){

    var homeFragment: HomeFragment? = null
    var alertFragment: AlertFragment? = null
    var favourateFragment: FavouriteFragment? = null
    var settingsFragment: SettingsFragment? = null

    var fragmentTransaction: FragmentTransaction? = null
    var fragmentManager: FragmentManager? = null
    var bottomNavigationView: BottomNavigationView? = null
    var fragmentContainerView: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        init()

        /* BottomBar Navigation Handler through Fragments */
        bottomNavigationView?.setOnItemSelectedListener { item: MenuItem ->
            val itemID = item.itemId
            if (R.id.navigation_home == itemID) {
                (if (homeFragment == null) HomeFragment().also { homeFragment = it } else homeFragment)?.let {
                    replaceFragment(it)
                }
            } else {
                if (R.id.navigation_search == itemID) {
                    (if (favourateFragment == null) FavouriteFragment().also { favourateFragment = it } else favourateFragment)?.let {
                        replaceFragment(
                            it
                        )
                    }
                } else {
                    if (R.id.navigation_favorite == itemID) {
                        (if (alertFragment == null) AlertFragment().also { alertFragment = it } else alertFragment)?.let {
                            replaceFragment(
                                it
                            )
                        }
                    } else {
                        if (R.id.navigation_favorite == itemID) {
                            (if (settingsFragment == null) SettingsFragment().also { settingsFragment = it } else settingsFragment)?.let {
                                replaceFragment(
                                    it
                                )
                            }
                        }
                    }
                }
            }
            true
        }

        (if (homeFragment == null) HomeFragment().also {
            homeFragment = it
        } else homeFragment)?.let { replaceFragment(it) }
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager = getSupportFragmentManager()
        fragmentTransaction = fragmentManager!!.beginTransaction()

        val ifExist: Fragment? = fragmentManager!!.findFragmentByTag(fragment.javaClass.simpleName)

        if (ifExist == null) {
            fragmentTransaction!!.replace(
                R.id.fragmentContainerView,
                fragment,
                fragment.javaClass.simpleName
            )
        } else {
            fragmentTransaction!!.replace(R.id.fragmentContainerView, ifExist)
        }
        fragmentTransaction!!.commit()
    }

    private fun init() {
        fragmentContainerView = findViewById(R.id.fragmentContainerView)
        bottomNavigationView = findViewById(R.id.bottomNavBar)
    }
}

