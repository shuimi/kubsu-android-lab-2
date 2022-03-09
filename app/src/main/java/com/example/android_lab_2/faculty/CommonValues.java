package com.example.android_lab_2.faculty;

public class CommonValues {

    private static Long mLastStudentID;
    private static Long mLastGroupID;

    public static Long getNewStudentID() {
        if (mLastStudentID == null) mLastStudentID = 0L;
        return ++mLastStudentID;
    }

    public static Long getNewGroupID() {
        if (mLastGroupID == null) mLastGroupID = 0L;
        return ++mLastGroupID;
    }

}
