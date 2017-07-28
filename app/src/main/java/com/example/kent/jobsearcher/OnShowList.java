package com.example.kent.jobsearcher;

import android.support.v4.app.Fragment;

import com.example.kent.jobsearcher.Model.Vacancy;

import java.util.List;

/**
 * Created by Kent on 06.07.2017.
 */

public interface OnShowList {
    void OnSearchCompleted(Fragment fragment);
    void OnSearchCompleted(String count);
}
