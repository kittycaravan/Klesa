package com.peisia.klesa;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.peisia.klesa.fragment.FragmentBase;
import com.peisia.klesa.fragment.FragmentBelogings;
import com.peisia.klesa.fragment.FragmentEquip;
import com.peisia.klesa.fragment.FragmentHome;
import com.peisia.klesa.fragment.FragmentMap;
import com.peisia.klesa.fragment.FragmentTableForces;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager mFm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFm = getSupportFragmentManager();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        changeFragment(new FragmentHome());
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            changeFragment(new FragmentHome());
        } else if (id == R.id.nav_player_equip) {
            changeFragment(new FragmentEquip());
        } else if (id == R.id.nav_player_belogings) {
            changeFragment(new FragmentBelogings());
        } else if (id == R.id.nav_map) {
            changeFragment(new FragmentMap());
        } else if (id == R.id.nav_base) {
            changeFragment(new FragmentBase());
        } else if (id == R.id.nav_current_table_forces) {
            changeFragment(new FragmentTableForces());
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void changeFragment(Fragment fragment){
        mFm.beginTransaction().replace(R.id.main_frame, fragment).commit();
    }
}
