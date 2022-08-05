package com.example.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // PORTRAIT
        if(findViewById(R.id.container) != null) {

            Toolbar toolbar = findViewById(R.id.toolbar_port);
            setSupportActionBar(toolbar);

            // NAVIGACIONI MENI/BAR - nudi opcije
            // Prikaz navigacionog bara, nav_view_port -> nav_menu.xml - opcije navigacionog bara
            BottomNavigationView navView = findViewById(R.id.nav_view_port);
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_profile, R.id.nav_plan, R.id.nav_services)
                    .build();

            // NAVIGACIONI FRAGMENT/KONTROLOR - kontrolise prikaz fragmenata na osnovu opcija bara
            // nav_host_fragment_port -> mobile_navigation.xml - vezuje opcije menija za fragment klase
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_port);

            // NAVIGACIONI KORISNICKI INTERFEJS
            // Povezivanje opcija navigacionog bara sa kontrolorom
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            // Povezivanje izgleda navigacionog bara sa kontrolorom
            NavigationUI.setupWithNavController(navView, navController);
        }
        // LANDSCAPE
        else {
            Toolbar toolbar = findViewById(R.id.toolbar_land);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view_land);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_profile, R.id.nav_plan, R.id.nav_services)
                    .setDrawerLayout(drawer)
                    .build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_land);

            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

    }

    // Sluzi samo da prihvati rezultat kada se iz PlanFragmenta pozove AddActivity
    // Greskom upozorava nullable jer trazi anotacije JetBranis-a umesto Androida
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_CANCELED) {
                recreate(); //  ponovo iscrta/kreira novu instancu aktivnosti
                // finish() - zatvara tekucu instancu aktivnosti aktivnost
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_land);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
