package com.example.android_lab_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.android_lab_2.faculty.Group;
import com.example.android_lab_2.faculty.Student;


public class StudentListAdapter extends BaseAdapter {

    private Group mGroup;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    @Nullable AdapterView.OnItemClickListener mEditCallback;
    @Nullable AdapterView.OnItemClickListener mDeleteCallback;

    public StudentListAdapter(
            Group group,
            Context context,
            @Nullable AdapterView.OnItemClickListener edit,
            @Nullable AdapterView.OnItemClickListener delete
    ) {
        mGroup = group;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mEditCallback = edit;
        mDeleteCallback = delete;
    }

    @Override
    public int getCount() {
        return mGroup.getStudents().size();
    }

    @Override
    public Object getItem(int i) {
        return mGroup.getStudents().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(mGroup.getStudents().isEmpty()) return view;

        View newView = mLayoutInflater
                .inflate(R.layout.element_view_student, viewGroup, false);
        TextView nameText = newView.findViewById(R.id.evsNameText);
        TextView dateText = newView.findViewById(R.id.evsDateText);
        Button editButton = newView.findViewById(R.id.evsEditButton);
        Button deleteButton = newView.findViewById(R.id.evsDeleteButton);

        Student currentStudent = mGroup.getStudents().get(i);

        nameText.setText(currentStudent.getNameAndInitials());
        dateText.setText(currentStudent.getBirthDateString());

        editButton.setOnClickListener(v -> {
            mEditCallback.onItemClick((AdapterView<?>) viewGroup, view, i, 0);
        });
        deleteButton.setOnClickListener(v -> {
            mDeleteCallback.onItemClick((AdapterView<?>) viewGroup, view, i, 0);
        });

        return newView;
    }
}
