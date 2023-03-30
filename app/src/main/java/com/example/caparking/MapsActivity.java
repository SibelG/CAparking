package com.example.caparking;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.caparking.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class MapsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{


    private View header;
    SessionManager sessionManager;
    BottomNavigationView navView;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //navigation drawer manager
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);

        navView = findViewById(R.id.bottom_navigation);
        setupBottomNavigationView();


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handles navigation view item on clicks
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment frag = null; // create a Fragment Object
        int itemId = item.getItemId(); // get selected menu item's id

                    // check selected menu item's id and replace a Fragment Accordingly
        if (itemId == R.id.nav_itinerary) {
            frag = new PurchaseFragment();
        } else if (itemId == R.id.nav_profile) {
            frag = new ProfileFragment();
        } else if (itemId == R.id.nav_security) {
            frag = new MapsFragment();
        } else if (itemId == R.id.nav_card) {
            frag = new CardsFragment();

        } else if (itemId == R.id.nav_about) {
            frag = new AboutFragment();


        } else if (itemId == R.id.nav_logout) {

            //getApplicationContext().getSharedPreferences(LoginActivity.MY_PREFERENCES, 0).edit().clear().commit();
            sessionManager.logOut();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();

        }
        // display a toast message with menu item's title
        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void setNavigationVisibility(boolean visible) {
        if (navView.isShown() && !visible) {
            navView.setVisibility(View.GONE);
        }
        else if (!navView.isShown() && visible){
            navView.setVisibility(View.VISIBLE);
        }
    }

    private void setupBottomNavigationView() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navView, navController);
    }

        /*if (id == R.id.nav_itinerary) {
            Intent intent = new Intent(this, PurchaseFragment.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileFragment.class);
            startActivity(intent);
        } else if (id == R.id.nav_security) {
            Intent intent = new Intent(this, SecurityFragment.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutFragment.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {

            //getApplicationContext().getSharedPreferences(LoginActivity.MY_PREFERENCES, 0).edit().clear().commit();
            sessionManager.logOut();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;*/

}
