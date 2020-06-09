package com.peisia.klesa;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.peisia.klesa.fragment.FragmentBase;
import com.peisia.klesa.fragment.FragmentBelogings;
import com.peisia.klesa.fragment.FragmentEquip;
import com.peisia.klesa.fragment.FragmentHome;
import com.peisia.klesa.fragment.FragmentMap;
import com.peisia.klesa.fragment.FragmentTableForces;
import com.peisia.klesa.service.ServiceWorldTime;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Messenger mServiceMessenger = null;

    private FragmentManager mFm;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private View mViewToolbar;
    private TextView mToolbarTitle;
    private boolean mIsBound;
    private Fragment mCurrentFragment;
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

//        changeFragment(new FragmentHome(), "홈");
        mCurrentFragment = new FragmentHome();
        changeFragment(mCurrentFragment, "홈");
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
            mCurrentFragment = new FragmentHome();
//            changeFragment(new FragmentHome(), "홈");
            changeFragment(mCurrentFragment, "홈");
        } else if (id == R.id.nav_player_equip) {
            mCurrentFragment = new FragmentEquip();
            changeFragment(mCurrentFragment, "장비");
        } else if (id == R.id.nav_player_belogings) {
            mCurrentFragment = new FragmentBelogings();
            changeFragment(mCurrentFragment, "소지품");
        } else if (id == R.id.nav_map) {
            mCurrentFragment = new FragmentMap();
            changeFragment(mCurrentFragment, "맵");
        } else if (id == R.id.nav_base) {
            mCurrentFragment = new FragmentBase();
            changeFragment(mCurrentFragment, "기지");
        } else if (id == R.id.nav_current_table_forces) {
            mCurrentFragment = new FragmentTableForces();
            changeFragment(mCurrentFragment, "세력 정보");
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void changeFragment(Fragment fragment, String title){
        mFm.beginTransaction().replace(R.id.main_frame, fragment).commit();
        mToolbarTitle.setText(title);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        procIntent(intent);
        super.onNewIntent(intent);
    }
    private void procIntent(Intent intent) {
        String intentValue = intent.getStringExtra(MyApp.INTENT_VALUE_SERVICE_TIME_NOTICE_TO_ACTIVITY);
        Log.v("ASM","==== ==== 메우!! :"+intentValue);
    }

    //todo

    /** 서비스 정지 */
    private void setStopService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        stopService(new Intent(ActivityMain.this, ServiceWorldTime.class));
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("test","onServiceConnected");
            mServiceMessenger = new Messenger(iBinder);
            try {
                Message msg = Message.obtain(null, ServiceWorldTime.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mServiceMessenger.send(msg);
            }
            catch (RemoteException e) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    /** 메신저 객체를 선언한다.
     * Service 로 부터 message를 받으므로 여기에 처리 코딩 */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ServiceWorldTime.MSG_SEND_TO_ACTIVITY_WORLD_TIME_TICK:
                    Log.v("ASM","==== ==== 서비스로 부터 메시지를 받았음 메시지는:"+msg.what);
                    //todo 시간 업데이트 신호를 받았음. 이걸로 할 처리를..
                    //todo 틱선녀? 이뿐 틱선녀를 등장시키자
//                    if(TextUtils.equals(mCurrentFragment.getClass().getSimpleName(), FragmentHome.class.getSimpleName())){
                    if(mCurrentFragment instanceof FragmentHome){
                        Log.v("ASM","==== ==== 여기오냥");
                        ((FragmentHome)mCurrentFragment).displayTickGodness();
                    }



/*                    int value1 = msg.getData().getInt("fromService");
                    String value2 = msg.getData().getString("test");
                    Log.i("test","act : value1 "+value1);
                    Log.i("test","act : value2 "+value2);*/
                    break;
            }
            return false;
        }
    }));

    /** Service 로 메시지를 보냄 */
    private void sendMessageToService(String str) {
        if (mIsBound) {
            if (mServiceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, ServiceWorldTime.MSG_REGISTER_CLIENT, str);
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) {
                }
            }
        }
    }
    public ServiceConnection getConnection() {
        return mConnection;
    }
}
