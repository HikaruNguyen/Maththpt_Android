package com.app.maththpt.activity;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.app.maththpt.BuildConfig;
import com.app.maththpt.R;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.databinding.ActivityMainBinding;
import com.app.maththpt.databinding.NavHeaderMainBinding;
import com.app.maththpt.fragment.CategoryFragment;
import com.app.maththpt.fragment.HistoryFragment;
import com.app.maththpt.fragment.KiemTraFragment;
import com.app.maththpt.fragment.TestsFragment;
import com.app.maththpt.utils.FacebookUtils;
import com.app.maththpt.viewmodel.MainViewModel;
import com.app.maththpt.viewmodel.NavHeaderMainViewModel;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CODE_LOGIN = 11;

    private SharedPreferences sharedPreferences;
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
        sharedPreferences = getSharedPreferences(Configuaration.Pref, MODE_PRIVATE);
        initUI();
        bindData();
        event();
        if (BuildConfig.DEBUG) {
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

    }

    private void event() {
        navHeaderMainBinding.lnHeader.setOnClickListener(view -> {
            String token = sharedPreferences.getString(Configuaration.KEY_TOKEN, "");
            if (!FacebookUtils.getFacebookID().isEmpty() || !token.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, CODE_LOGIN);
            }

        });
    }

    private void bindData() {
        changeFragment(new TestsFragment());
        bindNav();
    }

    private void bindNav() {
        String name = sharedPreferences.getString(Configuaration.KEY_NAME, "");
        String email = sharedPreferences.getString(Configuaration.KEY_EMAIL, "");
        String token = sharedPreferences.getString(Configuaration.KEY_TOKEN, "");
        if (!FacebookUtils.getFacebookID().isEmpty() || !token.isEmpty()) {
            if (!name.isEmpty()) {
                navHeaderMainViewModel.getUserName = name;
            }
            if (!email.isEmpty()) {
                navHeaderMainViewModel.getEmail = email;
            }
            if (!FacebookUtils.getAvatarFBFromId().isEmpty()) {
                navHeaderMainViewModel.getUserAvatar = FacebookUtils.getAvatarFBFromId();
            }
            navHeaderMainViewModel.notifyChange();
            activityMainBinding.navView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            activityMainBinding.navView.getMenu().findItem(R.id.nav_history).setVisible(true);
        } else {
            navHeaderMainViewModel.getUserName = getString(R.string.login);
            navHeaderMainViewModel.getEmail = getString(R.string.app_name);
            navHeaderMainViewModel.getUserAvatar = "";
            navHeaderMainViewModel.notifyChange();
            activityMainBinding.navView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            activityMainBinding.navView.getMenu().findItem(R.id.nav_history).setVisible(false);
        }

    }

    @BindingAdapter("android:srcUrl")
    public static void setImageUrl(ImageView view, String url) {
        if (url != null && !url.trim().isEmpty()) {
            Picasso.with(view.getContext()).load(url).placeholder(R.mipmap.ic_avatar).into(view);
        } else {
            Picasso.with(view.getContext()).load(R.mipmap.ic_avatar).into(view);
        }

    }

    public void changeFragment(Fragment targetFragment) {
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
        activityMainBinding.navView.setItemIconTintList(null);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tests) {
            clearBackStack(getSupportFragmentManager());
            changeFragment(new TestsFragment());
        } else if (id == R.id.nav_category) {
            clearBackStack(getSupportFragmentManager());
            changeFragment(new CategoryFragment());
        } else if (id == R.id.nav_kiemTra) {
            clearBackStack(getSupportFragmentManager());
            changeFragment(new KiemTraFragment());
        } else if (id == R.id.nav_history) {
            clearBackStack(getSupportFragmentManager());
            changeFragment(new HistoryFragment());
        } else if (id == R.id.nav_logout) {
            logout();

        }
        if (id != R.id.nav_logout) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    public void setMenuSelect(int id) {
        activityMainBinding.navView.setCheckedItem(id);
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.logout));
        builder.setMessage(getString(R.string.confirm_logout));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginManager.getInstance().logOut();
                sharedPreferences.edit().clear().apply();
                bindNav();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void clearBackStack(FragmentManager manager) {
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
            if (requestCode == CODE_LOGIN) {
                bindNav();
            }
        }
    }

}
