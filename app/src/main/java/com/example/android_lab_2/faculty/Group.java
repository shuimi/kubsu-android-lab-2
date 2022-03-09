package com.example.android_lab_2.faculty;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {

    private Long mID;
    private String mName;
    private ArrayList<Student> mStudents;

    public Group(String name) {
        mName = name;
        mStudents = new ArrayList<Student>();
        mID = CommonValues.getNewGroupID();
    }

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
