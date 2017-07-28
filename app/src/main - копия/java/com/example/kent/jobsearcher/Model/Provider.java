package com.example.kent.jobsearcher.Model;

import java.util.List;

/**
 * Created by Kent on 04.07.2017.
 */

public class Provider {
    private Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /*public List<Vacancy> getAllVacancies(String searchString) {
       return strategy.searchExecute(searchString);
    }*/
    public void getAllVacancies(String searchString) {
        strategy.searchExecute(searchString);
    }
}
