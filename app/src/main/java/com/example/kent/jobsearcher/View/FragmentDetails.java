package com.example.kent.jobsearcher.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.kent.jobsearcher.Model.Vacancy;
import com.example.kent.jobsearcher.R;

import java.util.List;

/**
 * Created by Kent on 09.07.2017.
 */

public class FragmentDetails extends Fragment {
    TextView tvCaption;
    TextView tvCompany;
    TextView tvCity;
    TextView tvSalary;
    TextView tvExp;
    WebView webView;
    TextView tvKey_skills, name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details, null);
        List<String> key_skills;
        name = (TextView) getActivity().findViewById(R.id.toolbar).findViewById(R.id.tvCount);
        name.setText("TUT.BY");
        tvCaption = (TextView) view.findViewById(R.id.tvCaption);
        tvCompany = (TextView) view.findViewById(R.id.tvCompany);
        tvCity = (TextView) view.findViewById(R.id.tvCity);
        tvSalary = (TextView) view.findViewById(R.id.tvSalary);
        tvExp = (TextView) view.findViewById(R.id.tvExp);
       // TextView tvMain = (TextView) view.findViewById(R.id.tvMain);
        webView = (WebView) view.findViewById(R.id.webView);
        tvKey_skills = (TextView) view.findViewById(R.id.tvKey_skills);

        Vacancy vacancy = getArguments().getParcelable("vacancy");
        if (vacancy != null) {
            tvCaption.setText(vacancy.getTitle());
            tvCompany.setText(vacancy.getCompanyName());
            tvCity.setText(vacancy.getCity());
            tvExp.setText(vacancy.getExperience());
            tvSalary.setText(vacancy.getSalary());
           // tvMain.setText(Html.fromHtml(vacancy.getMain_text()));
            webView.loadDataWithBaseURL(null, vacancy.getMain_text(), "text/html", "utf-8", null);
            key_skills = vacancy.getKey_skills();
            if (key_skills != null) {
                StringBuilder builder = new StringBuilder();
                for (String key : key_skills)
                    builder.append(key).append(" | ");
                tvKey_skills.setText(builder.toString());
            } else {
                TextView tvKey_skills_title = (TextView) view.findViewById(R.id.tvKey_skills_title);
                tvKey_skills_title.setVisibility(View.GONE);
                tvKey_skills.setVisibility(View.GONE);
            }

        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
