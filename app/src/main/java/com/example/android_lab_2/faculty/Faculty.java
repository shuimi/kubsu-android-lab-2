package com.example.android_lab_2.faculty;

import java.util.ArrayList;

public class Faculty {

    private String mName;
    private final ArrayList<Group> mGroups;
    private final Group mUndistributedGroup;

    public Faculty(String name) {
        mName = name;
        mGroups = new ArrayList<Group>();
        mUndistributedGroup = new Group("Не распределены");
    }

    public Group findStudentGroup(Student student) {
        for (Group group : mGroups) {
            if (group.hasStudent(student)) {
                return group;
            }
        }
        return null;
    }

    public Group getGroup(int position) {
        return mGroups.get(position);
    }

    public void addGroup(Group group) {
        mGroups.add(group);
    }

    public ArrayList<Group> getGroups() {
        return mGroups;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void clearGroups() {
        for (Group g : mGroups) {
            g = null;
        }
    }

    public void deleteGroup(int id) {
        mGroups.remove(id);
    }

    public Group getUndistributedGroup() {
        return mUndistributedGroup;
    }

    public void setGroup(Long groupID, Group group) {
        for (Group g : mGroups) {
            if (g.getID().equals(groupID)) {
                g.setName(group.getName());
                g.setStudents(group.getStudents());
                break;
            }
        }
    }

    public void setUndistributedGroup(Group group) {
        mUndistributedGroup.setStudents(group.getStudents());
    }
}
