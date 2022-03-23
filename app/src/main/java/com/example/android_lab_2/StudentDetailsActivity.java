package com.example.android_lab_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_lab_2.faculty.Student;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StudentDetailsActivity extends AppCompatActivity {

    private Student mStudent;

    private EditText EditFirstName;
    private EditText EditSurname;
    private EditText EditMiddleName;
    private CalendarView EditBirthDate;

    private Button SubmitButton;
    private Button CancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        Bundle bundle = getIntent().getExtras();

        mStudent = (Student) bundle.getParcelable("Student");

        EditFirstName = findViewById(R.id.studentEditFirstName);
        EditMiddleName = findViewById(R.id.studentEditSecondName);
        EditSurname = findViewById(R.id.studentEditLastName);
        EditBirthDate = findViewById(R.id.studentEditBirthDate);

        SubmitButton = findViewById(R.id.studentEditSubmitButton);
        CancelButton = findViewById(R.id.studentEditCancel);

        EditBirthDate.setOnDateChangeListener((view, year, month, day) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            EditBirthDate.setDate(c.getTimeInMillis());
            mStudent.setBirthdate(c);
        });

        EditFirstName.setText(mStudent.getFirstname());
        EditSurname.setText(mStudent.getSurname());
        EditMiddleName.setText(mStudent.getMiddlename());
        EditBirthDate.setDate(mStudent.getBirthdate().getTimeInMillis());

        SubmitButton.setOnClickListener(view -> {
            editUser();
            finishActivity();
        });

        CancelButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });
    }

    private void editUser() {

        Calendar birthDate = Calendar.getInstance();
        birthDate.setTimeInMillis(EditBirthDate.getDate());

        String firstName = EditFirstName.getText().toString();
        if (firstName.isEmpty()) {
            EditFirstName.setError("Некорректный ввод");
            return;
        }

        String surName = EditSurname.getText().toString();
        if (surName.isEmpty()) {
            EditSurname.setError("Некорректный ввод");
            return;
        }

        String lastName = EditMiddleName.getText().toString();
        if (lastName.isEmpty()) {
            EditMiddleName.setError("Некорректный ввод");
            return;
        }

        mStudent.setFirstname(firstName);
        mStudent.setSurname(surName);
        mStudent.setMiddlename(lastName);
        mStudent.setBirthdate(birthDate);
    }

    private void finishActivity(){
        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putParcelable("Student", mStudent);
        intent.putExtras(bundle);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
        super.onBackPressed();
    }

}