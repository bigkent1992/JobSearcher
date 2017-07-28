package com.example.kent.jobsearcher.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Kent on 04.07.2017.
 */

public class Vacancy implements Parcelable {
    private String title, salary, city, companyName, url, experience, main_text;
    private List<String> key_skills;

    public Vacancy() {
    }

    protected Vacancy(Parcel in) {
        title = in.readString();
        salary = in.readString();
        city = in.readString();
        companyName = in.readString();
        url = in.readString();
        experience = in.readString();
        main_text = in.readString();
        key_skills = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(salary);
        dest.writeString(city);
        dest.writeString(companyName);
        dest.writeString(url);
        dest.writeString(experience);
        dest.writeString(main_text);
        dest.writeStringList(key_skills);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<String> getKey_skills() {
        return key_skills;
    }

    public void setKey_skills(List<String> key_skills) {
        this.key_skills = key_skills;
    }

    public String getMain_text() {
        return main_text;
    }

    public void setMain_text(String main_text) {
        this.main_text = main_text;
    }
}
