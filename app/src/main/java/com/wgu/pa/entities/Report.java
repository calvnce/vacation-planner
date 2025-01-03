package com.wgu.pa.entities;

import java.util.List;

public class Report {
    private Vacation vacation;
    private List<Excursion> excursions;

    public Report(Vacation vacation, List<Excursion> excursions) {
        this.vacation = vacation;
        this.excursions = excursions;
    }

    public Vacation getVacation() {
        return vacation;
    }

    public List<Excursion> getExcursions() {
        return excursions;
    }
}
