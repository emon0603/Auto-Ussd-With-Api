package com.example.autoussdwithapi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.autoussdwithapi.Service.Forground;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;

    private FrameLayout fragment;
    private NavigationView navigationView;

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;
    private static final String CHANNEL_ID = "my_channel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fragment = findViewById(R.id.content_frame);





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            } else {
                // Permission already granted

            }
        } else {
            // For devices below Android 13, no need to request notification permission

        }



        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_close,
                R.string.navigation_drawer_open
        );

        drawerLayout.addDrawerListener(toggle);




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() ==R.id.nav_settings ) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new AboutFragment()).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }


                return false;
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted

            } else {
                // Permission denied
                // Handle the case where the user denied the permission
            }
        }
    }









}
