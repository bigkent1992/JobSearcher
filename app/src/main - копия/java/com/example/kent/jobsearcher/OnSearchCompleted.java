package com.example.kent.jobsearcher;

import com.example.kent.jobsearcher.Model.Vacancy;

import java.util.List;

/**
 * Created by Kent on 06.07.2017.
 */

public interface OnSearchCompleted {
    void OnSearchCompleted(List<Vacancy> list);
    void OnSearchCompleted(String count);
}
