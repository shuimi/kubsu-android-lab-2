package com.example.android_lab_2.faculty;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Group implements Parcelable {

    private Long mID;
    private String mName;
    private ArrayList<Student> mStudents;

    public Group(String name) {
        mName = name;
        mStudents = new ArrayList<Student>();
        mID = CommonValues.getNewGroupID();
    }

    protected Group(Parcel in) {
        if (in.readByte() == 0) {
            mID = null;
        } else {
            mID = in.readLong();
        }
        mName = in.readString();
        mStudents = in.createTypedArrayList(Student.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mID);
        }
        dest.writeString(mName);
        dest.writeTypedList(mStudents);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public Student getStudent(Long ID) {
        for (Student student : mStudents) {
            if (student.getID() == ID) {
                return student;
            }
        }
        return null;
    }

    public void setStudents(ArrayList<Student> students) {
        mStudents = students;
    }

    public boolean hasStudent(Student student) {
        for (Student _student : mStudents) {
            if (_student.getID() == student.getID()) {
                return true;
            }
        }
        return false;
    }

    public void addStudent(Student student) {
        mStudents.add(student);
    }

    public Long getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<Student> getStudents() {
        return mStudents;
    }

    @NonNull
    @Override
    public String toString() {
        return mName;
    }

    public void setStudent(long id, Student student) {
        for (Student s : mStudents) {
            if (s.getID() == student.getID()) {
                s.setFirstname(student.getFirstname());
                s.setSurname(student.getSurname());
                s.setMiddlename(student.getMiddlename());
                s.setBirthdate(student.getBirthdate());
                break;
            }
        }
    }
}
