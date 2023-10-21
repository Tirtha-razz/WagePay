package com.example.wagepay;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //defining variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;

    Button addCategory;
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    AttendanceFragment attendanceFragment = new AttendanceFragment();

    SummaryFragment summaryFragment = new SummaryFragment();

    private static final long SESSION_DURATION_MS = 7 * 24 * 60 * 60 * 1000; // 1 week
    private static final long LOGOUT_DELAY_MS = 5 * 60 * 1000; // 5 minutes delay for logout

    private SessionManager sessionManager;
    private final Handler logoutHandler = new Handler();
    private final Runnable logoutRunnable = this::logout;

    private String selectedCategoryId;

    WorkRecyclerAdapter workRecyclerAdapter;
    RecyclerView recyclerView;

    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        sessionManager.startSession(SESSION_DURATION_MS);

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

        //for home navigation clicked while app is opened
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
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment, homeFragment).commit();
                floatingActionButton.setVisibility(View.VISIBLE); // Show FAB in HomeFragment
                return true;
            } else if (item.getItemId() == R.id.attendance) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment, attendanceFragment).commit();
                floatingActionButton.setVisibility(View.GONE); // Hide FAB in AttendanceFragment
                return true;
            }else if (item.getItemId() == R.id.summary) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FirstFragment, summaryFragment).commit();
                floatingActionButton.setVisibility(View.GONE); // Hide FAB in SummaryFragment
                return true;
            }
            return false;
        });
        //bottom navigation and fragment finish here

        // Retrieve the user's phone number as the userId
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
             phoneNo = currentUser.getPhoneNumber();
        }

        //for recycler view of work
        recyclerView = findViewById(R.id.work_recyclerView);
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<WorkRecyclerModel> options =
                new FirebaseRecyclerOptions.Builder<WorkRecyclerModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Categories"), WorkRecyclerModel.class)
                        .build();
        workRecyclerAdapter = new WorkRecyclerAdapter(options);
        recyclerView.setAdapter(workRecyclerAdapter);

        // Set up the click listener for the RecyclerView items
        workRecyclerAdapter.setOnItemClickListener(new WorkRecyclerAdapter.OnItemClickListener() {
            public void onItemClick(String categoryId, WorkRecyclerModel model) {
                // Handle the item click event here
                String categoryName = model.getCategoryName();
                selectedCategoryId = categoryId; // Set the selected category ID using model.getCategoryId()

                // Store selectedCategoryId in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedCategoryId", selectedCategoryId);
                editor.apply();
            }
        });

        // insertion of data in category
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Inside your activity
        addCategory = findViewById(R.id.add_category);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a dialog to get user input
                showDialog();
            }
        });

        //for floating action button
        floatingActionButton = findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(v -> {
            // Start Activity 2
            startActivity(new Intent(MainActivity.this, WorkerFormActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        workRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        workRecyclerAdapter.stopListening();
    }

    //for custom dialog
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        builder.setView(dialogView);

        final EditText inputField = dialogView.findViewById(R.id.cat_Name);

        builder.setTitle("Add Category");

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = inputField.getText().toString();

                    if (phoneNo != null && !phoneNo.isEmpty()) {
                        // Create a reference to the user's "categories" node
                        DatabaseReference userCategoriesRef = databaseReference.child(phoneNo).child("Categories");

                        // Generate a unique category ID
                        String categoryId = userCategoriesRef.push().getKey();

                        // Create a Category object and set its properties (you can customize your Category class)
                        WorkRecyclerModel category = new WorkRecyclerModel(categoryId, userInput);

                        // Store the category under the user's "categories" node
                        userCategoriesRef.child(categoryId).setValue(category);
                    }
                }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
        } else if (itemId == R.id.logout) {
            logout();
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return false;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        // Reset the session expiration timestamp and cancel any pending logout
        sessionManager.startSession(SESSION_DURATION_MS);
        logoutHandler.removeCallbacks(logoutRunnable);
        logoutHandler.postDelayed(logoutRunnable, LOGOUT_DELAY_MS);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Schedule logout when the session expires
        logoutHandler.postDelayed(logoutRunnable, SESSION_DURATION_MS);

        if (!sessionManager.isSessionValid()) {
            logout();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove the pending logout callbacks when the activity is paused
        logoutHandler.removeCallbacks(logoutRunnable);
    }

     private void logout() {
        // Log out the user and redirect to the login screen
        sessionManager.endSession();
        finish(); // Close the current activity
   }
}