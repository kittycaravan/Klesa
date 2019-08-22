package com.peisia.klesa;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

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
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private View mViewToolbar;
    private TextView mToolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFm = getSupportFragmentManager();
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ////    툴바 커스텀
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mViewToolbar = getLayoutInflater().inflate(R.layout.tool_bar_custom, null);
        mActionBar.setCustomView(mViewToolbar, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        mToolbarTitle = mViewToolbar.findViewById(R.id.tool_bar_custom_title);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        changeFragment(new FragmentHome(), "홈");
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
            changeFragment(new FragmentHome(), "홈");
        } else if (id == R.id.nav_player_equip) {
            changeFragment(new FragmentEquip(), "장비");
        } else if (id == R.id.nav_player_belogings) {
            changeFragment(new FragmentBelogings(), "소지품");
        } else if (id == R.id.nav_map) {
            changeFragment(new FragmentMap(), "맵");
        } else if (id == R.id.nav_base) {
            changeFragment(new FragmentBase(), "기지");
        } else if (id == R.id.nav_current_table_forces) {
            changeFragment(new FragmentTableForces(), "세력 정보");
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void changeFragment(Fragment fragment, String title){
        mFm.beginTransaction().replace(R.id.main_frame, fragment).commit();
        mToolbarTitle.setText(title);
    }
}
