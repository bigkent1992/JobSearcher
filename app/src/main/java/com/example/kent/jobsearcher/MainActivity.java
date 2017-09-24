package com.example.kent.jobsearcher;

import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.kent.jobsearcher.Model.PracaByStrategy;
import com.example.kent.jobsearcher.Model.Provider;
import com.example.kent.jobsearcher.Model.RabotaByStrategy;
import com.example.kent.jobsearcher.Model.TutByStrategy;
import com.example.kent.jobsearcher.View.FragmentPage;
import com.example.kent.jobsearcher.View.FragmentSearch;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnShowList {
    private static final String LOG_TAG = "MyLog";
    Toolbar toolbar;
    TextView tvCount;
    Integer count;
    ArrayList<String> names;
    public ArrayList<Fragment> fragments = new ArrayList<>();
    public FragmentPage pageFragment;
    Bundle args;
    public TutByStrategy tutByStrategy;
    public PracaByStrategy pracaByStrategy;
    public RabotaByStrategy rabotaByStrategy;
    Provider provider;
    int countFr = 0;
    private UpdateList updateListener;
    private FragmentSearch fragmentSearch;

    public void setUpdateListener(UpdateList updateListener) {
        this.updateListener = updateListener;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
      //  getSupportFragmentManager().putFragment(outState, "FragmentSearch", fragmentSearch);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "OnCreate");

        MainActivity mainActivity = (MainActivity) getLastCustomNonConfigurationInstance();
        if (mainActivity != null) {
            if (mainActivity.provider != null)
                this.provider = mainActivity.provider;
            if (mainActivity.tutByStrategy != null)
                this.tutByStrategy = mainActivity.tutByStrategy;
            if (mainActivity.pracaByStrategy != null)
                this.pracaByStrategy = mainActivity.pracaByStrategy;
            if (mainActivity.rabotaByStrategy != null)
                this.rabotaByStrategy = mainActivity.rabotaByStrategy;
        }
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        names = new ArrayList<>();
        names.add("tut.by");
        names.add("praca.by");
        names.add("rabota.by");
        if (savedInstanceState == null) {
            //   fragmentSearch = (FragmentSearch) getSupportFragmentManager().getFragment(savedInstanceState, "FragmentSearch");
            //  getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragmentSearch).commit();
            //  } else
            fragmentSearch = new FragmentSearch();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragmentSearch).commit();
        }
       /*// if (savedInstanceState != null) {
            Object object = getLastNonConfigurationInstance();
            if (object instanceof FragmentSearch) {
            fragmentSearch = (FragmentSearch) getLastNonConfigurationInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragmentSearch).commit();
        }
        else {
            fragmentSearch = new FragmentSearch();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragmentSearch).commit();
        }*/
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

    @Override
    public void OnSearchCompleted(String count) {
       // tvCount = (TextView) toolbar.findViewById(R.id.tvCount);
      //  tvCount.setText(count);
    }

    @Override
    public void OnSearchCompleted(Fragment fragment) {
       /* if (list.isEmpty()) {
            Toast.makeText(this, "Вакансий нет", Toast.LENGTH_LONG).show();
            return;
        }*/
        if (pageFragment == null) {
            pageFragment = new FragmentPage();
        }
        fragments.add(fragment);

        //countFr++;
        if (fragments.size() == names.size()) {
            pageFragment.fill(names, fragments);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, pageFragment).addToBackStack(null).commit();
        }
    }

    public void startSearching(String searchString, String name, boolean isNextPage) {
        if (provider == null)
            provider = new Provider();
        switch (name) {
            case "tut.by":
                if (tutByStrategy == null)
                    tutByStrategy = new TutByStrategy(this);
                provider.setStrategy(tutByStrategy);
                break;
            case "praca.by":
                if (pracaByStrategy == null)
                    pracaByStrategy = new PracaByStrategy(this);
                provider.setStrategy(pracaByStrategy);
                break;
            case "rabota.by":
                if (rabotaByStrategy == null)
                    rabotaByStrategy = new RabotaByStrategy(this);
                rabotaByStrategy.setNextPage(isNextPage);
                provider.setStrategy(rabotaByStrategy);
                break;
        }
        provider.getAllVacancies(searchString);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentSearch()).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       // Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume ");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // Log.d(LOG_TAG, "onSaveInstanceState");
    }

    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }
}
