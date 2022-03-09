package com.example.android_lab_2.faculty;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {

    private final long mID;

    private String mSurname, mFirstname, mMiddlename;
    private Date mBirthdate;

    private Student(String surname, String firstname, String middlename, Date birthdate) {
        mSurname = surname;
        mFirstname = firstname;
        mMiddlename = middlename;
        mBirthdate = birthdate;
        mID = CommonValues.getNewStudentID();
    }

    public static Student Create(String surname, String firstname, String middlename, Date birthdate) {
        return new Student(surname, firstname, middlename, birthdate);
    }

    public Group getGroup(Faculty faculty) throws GroupNotFoundException {
        Group group = faculty.findStudentGroup(this);
        if (group == null) {
            throw new GroupNotFoundException();
        }
        return group;
    }

    public String getSurname() {
        return mSurname;
    }

    public void setSurname(String surname) {
        mSurname = surname;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public void setFirstname(String firstname) {
        mFirstname = firstname;
    }

    public String getMiddlename() {
        return mMiddlename;
    }

    public void setMiddlename(String middlename) {
        mMiddlename = middlename;
    }

    public long getID() {
        return mID;
    }

    public Date getBirthdate() {
        return mBirthdate;
    }

    public void setBirthdate(Date birthdate) {
        mBirthdate = birthdate;
    }

    @Override
    public String toString() {
        return mSurname + " " + mFirstname;
    }
}
