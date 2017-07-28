package com.example.kent.jobsearcher;

import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kent.jobsearcher.Model.Provider;
import com.example.kent.jobsearcher.Model.TutByStrategy;
import com.example.kent.jobsearcher.Model.Vacancy;
import com.example.kent.jobsearcher.View.FragmentPage;
import com.example.kent.jobsearcher.View.FragmentSearch;
import com.example.kent.jobsearcher.View.FragmentVacancies;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnSearchCompleted {
    Toolbar toolbar;
    TextView tvCount;
    Integer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);
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
    public void OnSearchCompleted(String count) {
        if (!count.equals("")) {
            tvCount = (TextView) toolbar.findViewById(R.id.tvCount);
            tvCount.setText(count);
            String[] strings =  count.split(" ");
            this.count = Integer.parseInt(strings[1].replaceAll("\\s+",""));
        }
    }

    @Override
    public void OnSearchCompleted(List<Vacancy> list) {
        if (list.isEmpty()) {
            Toast.makeText(this, "Вакансий нет", Toast.LENGTH_LONG).show();
            return;
        }
        getSupportActionBar().hide();
        FragmentPage fragment = new FragmentPage();
        Bundle args = new Bundle();

        Integer pages = count/20 + 1;
        args.putInt("pages", pages);
        args.putParcelableArrayList("vacancies", (ArrayList<? extends Parcelable>) list);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).addToBackStack("one").commit();
/*        FragmentVacancies fragment = new FragmentVacancies();
        Bundle args = new Bundle();
        args.putParcelableArrayList("vacancies", (ArrayList<? extends Parcelable>) list);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).addToBackStack("one").commit();*/
    }
}
