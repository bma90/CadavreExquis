package com.bma.gon.cadavreexquis.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import com.bma.gon.cadavreexquis.R
import com.bma.gon.cadavreexquis.fragments.ProfilFragment
import com.bma.gon.cadavreexquis.fragments.Stories
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // ------------------ Override AppCompat classe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //toolbar
        setSupportActionBar(toolbar as Toolbar)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onStart() {
        super.onStart()
        Log.d("connection", ""+FirebaseAuth.getInstance().currentUser?.email+"")
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut()
        }
    }

    //------------------ Gesture function
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                //message.setText(R.string.title_home)
                loadFragment(Stories())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                //message.setText(R.string.title_notifications)
                loadFragment(ProfilFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //------------------ Functions
    private fun loadFragment(frag: Fragment){
        var transaction:FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, frag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
