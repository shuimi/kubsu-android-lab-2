package com.example.android_lab_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.android_lab_2.faculty.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentDetailsActivity extends AppCompatActivity {

    private Student mStudent;

    private EditText EditFirstName;
    private EditText EditSurname;
    private EditText EditMiddleName;
    private EditText EditBirthDate;

    private Button SubmitButton;
    private Button CancelButton;

    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        Bundle bundle = getIntent().getExtras();

        mStudent = (Student) bundle.getSerializable("Student");

        EditFirstName = findViewById(R.id.studentEditFirstName);
        EditMiddleName = findViewById(R.id.studentEditSecondName);
        EditSurname = findViewById(R.id.studentEditLastName);
        EditBirthDate = findViewById(R.id.studentEditBirthDate);

        SubmitButton = findViewById(R.id.studentEditSubmitButton);
        CancelButton = findViewById(R.id.studentEditCancel);

        EditFirstName.setText(mStudent.getFirstname());
        EditSurname.setText(mStudent.getSurname());
        EditMiddleName.setText(mStudent.getMiddlename());
        EditBirthDate.setText(
                format.format(mStudent.getBirthdate()).toString()
        );

        SubmitButton.setOnClickListener(view -> {
            try {
                editUser();
                finishActivity();
            } catch (ParseException e) {
                EditBirthDate.setError("Некорректный ввод");
            }
        });

        CancelButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });
    }

    private void editUser() throws ParseException {

        Date birthDate = format.parse(EditBirthDate.getText().toString());

        String firstName = EditFirstName.getText().toString();
        if(firstName.isEmpty()) {
            EditFirstName.setError("Некорректный ввод");
            return;
        }

        String surName = EditSurname.getText().toString();
        if(surName.isEmpty()) {
            EditSurname.setError("Некорректный ввод");
            return;
        }

        String lastName = EditMiddleName.getText().toString();
        if(lastName.isEmpty()) {
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
        bundle.putSerializable("Student", mStudent);
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