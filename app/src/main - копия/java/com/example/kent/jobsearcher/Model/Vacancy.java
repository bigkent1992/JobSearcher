package com.example.kent.jobsearcher.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kent on 04.07.2017.
 */

public class Vacancy implements Parcelable {
    private String title, salary, city, companyName, url;

    public Vacancy() {
    }

    protected Vacancy(Parcel in) {
        title = in.readString();
        salary = in.readString();
        city = in.readString();
        companyName = in.readString();
        url = in.readString();
    }

    public static final Creator<Vacancy> CREATOR = new Creator<Vacancy>() {
        @Override
        public Vacancy createFromParcel(Parcel in) {
            return new Vacancy(in);
        }

        @Override
        public Vacancy[] newArray(int size) {
            return new Vacancy[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(salary);
        dest.writeString(city);
        dest.writeString(companyName);
        dest.writeString(url);
    }
}
