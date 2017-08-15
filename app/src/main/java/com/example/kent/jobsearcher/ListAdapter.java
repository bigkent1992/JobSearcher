package com.example.kent.jobsearcher;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kent.jobsearcher.Model.TutByDetails;
import com.example.kent.jobsearcher.Model.Vacancy;

import java.util.List;

/**
 * Created by Kent on 12.08.2017.
 */

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<Vacancy> vacancies;
    private CardClick cardClick;

    public ListAdapter(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public void setCardClick(CardClick cardClick) {
        this.cardClick = cardClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
            return new VacancyHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VacancyHolder) {
            final Vacancy vacancy = vacancies.get(position);
            VacancyHolder vacancyHolder = (VacancyHolder) holder;
            vacancyHolder.title.setText(vacancy.getTitle());
            vacancyHolder.url.setText(vacancy.getUrl());
            vacancyHolder.company.setText(vacancy.getCompanyName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardClick.onCardClick(vacancy);
                }
            });
        } else if (holder instanceof LoadingHolder) {
            LoadingHolder loadingHolder = (LoadingHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return vacancies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return vacancies.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private static class LoadingHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    private static class VacancyHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView url;
        TextView company;

        VacancyHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv1);
            url = (TextView) view.findViewById(R.id.tv2);
            company = (TextView) view.findViewById(R.id.tv3);
        }
    }
}
