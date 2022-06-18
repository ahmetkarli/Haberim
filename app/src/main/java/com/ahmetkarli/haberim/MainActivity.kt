package com.ahmetkarli.haberim

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.ahmetkarli.haberim.databinding.ActivityMainBinding
import com.google.android.gms.ads.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Haberim)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val testDeviceIds = Arrays.asList("E1722F1CBDFEC77C8126A98071926CE3")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            // do something after 1000ms
        }, 1000)


        MobileAds.initialize(this) {}

        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdClicked() {
                Log.d("TAG", "onAdClicked")
            }

            override fun onAdClosed() {
                Log.d("TAG", "onAdClosed")
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                Log.d("TAG", "onAdFailedToLoad adError : $adError")
            }

            override fun onAdImpression() {
                Log.d("TAG", "onAdImpression")
            }

            override fun onAdLoaded() {
                Log.d("TAG", "onAdLoaded")
            }

            override fun onAdOpened() {
                Log.d("TAG", "onAdOpened")
            }
        }

        val navController = findNavController(R.id.nav_host_fragment)
        setupBottomNavMenu(navController)


    }



    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.setupWithNavController(navController)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) || super.onOptionsItemSelected(item)
    }

}