package com.example.kent.jobsearcher.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details, null);
        List<String> key_skills;
        TextView tvCaption = (TextView) view.findViewById(R.id.tvCaption);
        TextView tvCompany = (TextView) view.findViewById(R.id.tvCompany);
        TextView tvCity = (TextView) view.findViewById(R.id.tvCity);
        TextView tvSalary = (TextView) view.findViewById(R.id.tvSalary);
        TextView tvExp = (TextView) view.findViewById(R.id.tvExp);
       // TextView tvMain = (TextView) view.findViewById(R.id.tvMain);
        WebView webView = (WebView) view.findViewById(R.id.webView);
        TextView tvKey_skills = (TextView) view.findViewById(R.id.tvKey_skills);

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
                    builder.append(key);
                tvKey_skills.setText(builder.toString());
            }

        }
        return view;
    }
}
