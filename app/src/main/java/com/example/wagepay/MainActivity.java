package com.example.wagepay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //defining variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;

    ArrayList<WorkRecyclerModel> arrWork = new ArrayList<>();

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    AttendanceFragment attendanceFragment = new AttendanceFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
        }

        //set bottom status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.green));
        }



        // Set the status bar icons' color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //set bottom status bar icons' color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            int flags = decorView.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            decorView.setSystemUiVisibility(flags);
        }

        //hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNav);

        //for home navigation clicked while app is opned
        Menu menu = navigationView.getMenu();
        MenuItem homeMenuItem = menu.findItem(R.id.home); // Replace nav_home with the ID of your home menu item
        homeMenuItem.setChecked(true); // Set the home menu item as checked

        // toolbar
        toolbar.setTitle("Sudip Baral");
        setSupportActionBar(toolbar);



        //navigation drawer
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        //for bottom navigation and fragments


        getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment, homeFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.attendance) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment, attendanceFragment).commit();
                    return true;
                }
                return false;
            }
        });
        //bottom navigation and fragment finish here

        //for recycler view of work
        RecyclerView recyclerView = findViewById(R.id.work_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this ,LinearLayoutManager.HORIZONTAL,false));

        arrWork.add(new WorkRecyclerModel("work 1"));
        arrWork.add(new WorkRecyclerModel("work 2"));
        arrWork.add(new WorkRecyclerModel("work 3"));
        arrWork.add(new WorkRecyclerModel("work 4"));
        arrWork.add(new WorkRecyclerModel("work 5"));
        arrWork.add(new WorkRecyclerModel("work 6"));
        arrWork.add(new WorkRecyclerModel("work 7"));
        arrWork.add(new WorkRecyclerModel("+"));

        WorkRecyclerAdapter adapter = new WorkRecyclerAdapter(this,arrWork);
        recyclerView.setAdapter(adapter);

        //for floating action button
        floatingActionButton = findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Activity 2


                startActivity(new Intent(MainActivity.this, WorkerFormActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle item selection here
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            // Start Activity1
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (itemId == R.id.profile) {
            // Start Activity2
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        return false;
    }
}