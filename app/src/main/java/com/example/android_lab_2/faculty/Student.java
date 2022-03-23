package com.example.android_lab_2.faculty;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Student implements Parcelable {

    private final long mID;

    private String mSurname, mFirstname, mMiddlename;
    private Calendar mBirthdate;

    private Student(String surname, String firstname, String middlename, Calendar birthdate) {
        mSurname = surname;
        mFirstname = firstname;
        mMiddlename = middlename;
        mBirthdate = birthdate;
        mID = CommonValues.getNewStudentID();
    }

    protected Student(Parcel in) {
        mID = in.readLong();
        mSurname = in.readString();
        mFirstname = in.readString();
        mMiddlename = in.readString();
        mBirthdate = Calendar.getInstance();
        mBirthdate.setTimeInMillis(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mID);
        dest.writeString(mSurname);
        dest.writeString(mFirstname);
        dest.writeString(mMiddlename);
        dest.writeLong(mBirthdate.getTimeInMillis());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public static Student Create(String surname, String firstname, String middlename, Calendar birthdate) {
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

    public Calendar getBirthdate() {
        return mBirthdate;
    }

    public void setBirthdate(Calendar birthdate) {
        mBirthdate = birthdate;
    }

    public String getNameAndInitials() {
        return mSurname + " " + mFirstname.charAt(0) + ". " + mMiddlename.charAt(0) + ". ";
    }

    public String getBirthDateString() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(mBirthdate.getTime());
    }

    @Override
    public String toString() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return
                mSurname + " " +
                mFirstname.charAt(0) + ". " +
                mMiddlename.charAt(0) + ". " +
                format.format(mBirthdate.getTime());
    }
}
