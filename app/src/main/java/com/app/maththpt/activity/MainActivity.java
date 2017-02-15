package com.app.maththpt.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.maththpt.BuildConfig;
import com.app.maththpt.R;
import com.app.maththpt.adapter.NavigationAdapter;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.databinding.ActivityMainBinding;
import com.app.maththpt.databinding.NavHeaderMainBinding;
import com.app.maththpt.fragment.CategoryFragment;
import com.app.maththpt.fragment.KiemTraFragment;
import com.app.maththpt.model.ItemMenu;
import com.app.maththpt.viewmodel.MainViewModel;
import com.app.maththpt.viewmodel.NavHeaderMainViewModel;
import com.app.maththpt.widget.CRecyclerView;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOGIN_CODE = 11;
    private CRecyclerView rvSlide;
    private NavigationAdapter navigationAdapter;
    private List<ItemMenu> itemMenus;
    private boolean isClicked = false;
    private LinearLayout lnHeader;
    private SharedPreferences sharedPreferences;
    //    private ImageView imgAvatar;
//    private TextView tvName, tvEmail;
    private MainViewModel mainViewModel;
    ActivityMainBinding activityMainBinding;
    private NavHeaderMainBinding navHeaderMainBinding;
    private NavHeaderMainViewModel navHeaderMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new MainViewModel(this);
        activityMainBinding.setViewModelMain(mainViewModel);
        initUI();
        bindData();
        event();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    BuildConfig.APPLICATION_ID,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void event() {
        navHeaderMainBinding.lnHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_CODE);
            }
        });
    }

    private void bindData() {
        changeFragment(new CategoryFragment());
        bindNav();

    }

    private void bindNav() {
        sharedPreferences = getSharedPreferences(Configuaration.Pref, MODE_PRIVATE);
        String id = sharedPreferences.getString(Configuaration.KEY_IDFB, "");
        String name = sharedPreferences.getString(Configuaration.KEY_NAME, "");
        String email = sharedPreferences.getString(Configuaration.KEY_EMAIL, "");
        String avatar = sharedPreferences.getString(Configuaration.KEY_AVATAR, "");
        if (!id.isEmpty()) {

            if (!name.isEmpty()) {
//                tvName.setText(name);
                navHeaderMainViewModel.getUserName = name;
            }
            if (!email.isEmpty()) {
//                tvEmail.setText(email);
                navHeaderMainViewModel.getEmail = email;
            }
            if (!avatar.isEmpty()) {
//                Picasso.with(MainActivity.this).load(avatar).placeholder(R.mipmap.ic_avatar).into(imgAvatar);
                navHeaderMainViewModel.getUserAvatar = avatar;
            }
            navHeaderMainViewModel.notifyChange();
        } else {
            navHeaderMainViewModel.getUserName = getString(R.string.login);
            navHeaderMainViewModel.getEmail = getString(R.string.app_name);
            navHeaderMainViewModel.getUserAvatar = "";
            navHeaderMainViewModel.notifyChange();
        }
    }

    @BindingAdapter("android:srcUrl")
    public static void setImageUrl(ImageView view, String url) {
        if (url != null && !url.trim().isEmpty())
            Picasso.with(view.getContext()).load(url).placeholder(R.mipmap.ic_avatar).into(view);
    }

    private void changeFragment(Fragment targetFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    Toolbar toolbar;

    private void initUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavigation();

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        rvSlide = (CRecyclerView) findViewById(R.id.rvSlide);
//        navigationAdapter = new NavigationAdapter(this, new ArrayList<ItemMenu>());
//        rvSlide.setAdapter(navigationAdapter);
        View headerView = getLayoutInflater().inflate(R.layout.nav_header_main, navigationView, false);
        navigationView.addHeaderView(headerView);
        lnHeader = (LinearLayout) headerView.findViewById(R.id.lnHeader);
        tvName = (TextView) headerView.findViewById(R.id.tvName);
        tvEmail = (TextView) headerView.findViewById(R.id.tvEmail);
        imgAvatar = (ImageView) headerView.findViewById(R.id.imgAvatar);*/

    }

    private void setupNavigation() {
        navHeaderMainBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.nav_header_main, activityMainBinding.navView, false);
        navHeaderMainViewModel = new NavHeaderMainViewModel();
        navHeaderMainBinding.setNavHeaderMainViewModel(navHeaderMainViewModel);
        navHeaderMainViewModel.getUserName = getString(R.string.login).toUpperCase();
        navHeaderMainViewModel.getEmail = getString(R.string.app_name).toUpperCase();
        navHeaderMainViewModel.notifyChange();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, activityMainBinding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityMainBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        activityMainBinding.navView.addHeaderView(navHeaderMainBinding.getRoot());
        activityMainBinding.navView.setNavigationItemSelectedListener(this);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_home) {
//            clearBackStack(getSupportFragmentManager());
//            changeFragment(new HomeFragment());
//        } else
        if (id == R.id.nav_chuyenDe) {
            clearBackStack(getSupportFragmentManager());
            changeFragment(new CategoryFragment());
        } else if (id == R.id.nav_kiemTra) {
            clearBackStack(getSupportFragmentManager());
            changeFragment(new KiemTraFragment());
        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            sharedPreferences.edit().clear().commit();
            bindNav();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void clearBackStack(FragmentManager manager) {
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager
                    .getBackStackEntryAt(0);
            manager.popBackStack(first.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage(getString(R.string.confirmQuitApp));
//            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });
//            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LOGIN_CODE) {
                String name = sharedPreferences.getString(Configuaration.KEY_NAME, "");
                String email = sharedPreferences.getString(Configuaration.KEY_EMAIL, "");
                String avatar = sharedPreferences.getString(Configuaration.KEY_AVATAR, "");
                if (!name.isEmpty()) {
//                tvName.setText(name);
                    navHeaderMainViewModel.getUserName = name;
                }
                if (!email.isEmpty()) {
//                tvEmail.setText(email);
                    navHeaderMainViewModel.getEmail = email;
                }
                if (!avatar.isEmpty()) {
//                Picasso.with(MainActivity.this).load(avatar).placeholder(R.mipmap.ic_avatar).into(imgAvatar);
                    navHeaderMainViewModel.getUserAvatar = avatar;
                }
                navHeaderMainViewModel.notifyChange();
            }
        }
    }
}
