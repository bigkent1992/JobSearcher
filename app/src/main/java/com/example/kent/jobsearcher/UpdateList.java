package com.example.kent.jobsearcher;

import com.example.kent.jobsearcher.Model.Strategy;
import com.example.kent.jobsearcher.Model.Vacancy;

import java.util.List;

/**
 * Created by Kent on 14.07.2017.
 */

public interface UpdateList {
    void loadMoreToList(List<Vacancy> list, String nextPage);
}
