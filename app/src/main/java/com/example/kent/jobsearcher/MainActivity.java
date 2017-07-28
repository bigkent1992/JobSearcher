package com.example.kent.jobsearcher;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

    public void setUpdateListener(UpdateList updateListener) {
        this.updateListener = updateListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentSearch()).commit();
    }


/*    public void clickSearch(View view) {
        TutByStrategy strategy = new TutByStrategy(this);
        Provider provider = new Provider(strategy);
        provider.getAllVacancies("Минск");
        //strategy.execute("Минск");
       // vacancies.addAll(provider.getAllVacancies("Минск"));
    }*/

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
        tvCount = (TextView) toolbar.findViewById(R.id.tvCount);
        tvCount.setText(count);
    }

    @Override
    public synchronized void OnSearchCompleted(Fragment fragment) {
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

       // setUpdateListener((UpdateList) fragment);
       // updateListener.loadMoreToList(list);


          //  args = new Bundle();
           // args.putStringArrayList("names", names);
            //args.putParcelableArrayList("v_tut.by", (ArrayList<? extends Parcelable>) list);
            //fragment.setArguments(args);
            //getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).addToBackStack("one").commit();
       // } else {
            //Bundle args = new Bundle();
           // args.putParcelableArrayList("v_praca.by", (ArrayList<? extends Parcelable>) list);
           // fragment.setArguments(args);
      //  }

      //  Integer pages = count/50;
       // if (count%50 != 0) pages = pages + 1;
      //  args.putInt("pages", pages);

/*        FragmentVacancies fragment = new FragmentVacancies();
        Bundle args = new Bundle();
        args.putParcelableArrayList("vacancies", (ArrayList<? extends Parcelable>) list);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).addToBackStack("one").commit();*/
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
}
