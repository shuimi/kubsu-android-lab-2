package com.example.android_lab_2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android_lab_2.faculty.Group;
import com.example.android_lab_2.faculty.Student;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupDetailsActivity extends AppCompatActivity {

    private Menu mMenu;

    private Group mGroup;
    private ArrayList<Student> mStudents;
    private ListView mStudentsListView;

    private ActivityResultLauncher mActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        Bundle bundle = getIntent().getExtras();

        mGroup = (Group) bundle.getSerializable("Group");
        mStudents = mGroup.getStudents();

        mStudentsListView = findViewById(R.id.StudentsList);


        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){

                        Intent intent = result.getData();
                        Bundle _bundle = intent.getExtras();

                        Student student = (Student) _bundle.getSerializable("Student");

                        mGroup.setStudent(student.getID(), student);

                        Toast.makeText(
                                getApplicationContext(),
                                "Успешно вернулись",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );


        setTitle(mGroup.getName());
    }

    private void createStudent(String firstName, String secondName, String lastName, String date) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = format.parse(date);

        Student student = Student.Create(
                firstName,
                secondName,
                lastName,
                birthDate
        );

        mStudents.add(student);
        ((ArrayAdapter) mStudentsListView.getAdapter()).notifyDataSetChanged();
        updateStudentsList();
    }

    private void addStudentDialog(){

        AlertDialog studentInputDialog =
                new AlertDialog.Builder(GroupDetailsActivity.this).create();
        studentInputDialog.setTitle("Введите данные студента");
        studentInputDialog.setCancelable(true);

        View dialogView = getLayoutInflater().inflate(
                R.layout.student_input_layout,
                null
        );

        dialogView.findViewById(R.id.studentEditSubmitButton).setOnClickListener(view -> {

            EditText studentFirstNameInput = dialogView
                    .findViewById(R.id.studentEditFirstName3);
            EditText studentLastNameInput = dialogView
                    .findViewById(R.id.studentEditLastName3);
            EditText studentSecondNameInput = dialogView
                    .findViewById(R.id.studentEditSecondName3);
            EditText studentBirthDateNameInput = dialogView
                    .findViewById(R.id.studentEditBirthDate3);

            String studentFirstName = studentFirstNameInput.getText().toString();
            String studentLastName = studentLastNameInput.getText().toString();
            String studentSecondName = studentSecondNameInput.getText().toString();
            String studentBirthDate = studentBirthDateNameInput.getText().toString();

            if (!studentFirstName.isEmpty()) {
                if (!studentLastName.isEmpty()) {
                    if (!studentSecondName.isEmpty()) {
                        if (!studentBirthDate.isEmpty()) {
                            try {
                                createStudent(
                                        studentFirstName,
                                        studentLastName,
                                        studentSecondName,
                                        studentBirthDate
                                );

                                studentInputDialog.cancel();

                            } catch (ParseException e) {
                                studentBirthDateNameInput.setError("Неправильная дата");
                            }
                        } else {
                            studentBirthDateNameInput.setError("Неправильная дата");
                        }
                    } else {
                        studentSecondNameInput.setError("Неправильное отчество");
                    }
                } else {
                    studentLastNameInput.setError("Неправильная фамилия");
                }
            } else {
                studentFirstNameInput.setError("Неправильное имя");
            }

        });

        dialogView.findViewById(R.id.studentEditCancel).setOnClickListener(view -> {
            studentInputDialog.cancel();
        });

        studentInputDialog.setView(dialogView);
        studentInputDialog.show();
    }

    private boolean deleteStudentDialog(int position){
        AlertDialog.Builder deleteStudentDialog = new AlertDialog.Builder(
                GroupDetailsActivity.this
        );
        deleteStudentDialog.setTitle("Точно удалить?");
        deleteStudentDialog.setMessage("Точно-точно удалить?");
        deleteStudentDialog.setCancelable(false);

        deleteStudentDialog.setPositiveButton("Удалить!", (dialogInterface, i) -> {
            mStudents.remove(position);
            updateStudentsList();
        });

        deleteStudentDialog.setNegativeButton("Неа!", null);
        deleteStudentDialog.show();

        return true;
    }

    private void clearStudentsDialog(){
        AlertDialog.Builder clearStudentsDialog = new AlertDialog.Builder(
                GroupDetailsActivity.this
        );
        clearStudentsDialog.setTitle("Точно удалить?");
        clearStudentsDialog.setMessage("Точно-точно удалить?");
        clearStudentsDialog.setCancelable(false);

        clearStudentsDialog.setPositiveButton("Удалить!", (dialogInterface, i) -> {
            mStudents = null;
            updateStudentsList();
        });

        clearStudentsDialog.setNegativeButton("Неа!", null);
        clearStudentsDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.miAddStudent: {
                addStudentDialog();
                break;
            }
            case R.id.miClearStudents: {
                clearStudentsDialog();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu, menu);
        mMenu = menu;
        updateStudentsList();
        return super.onCreateOptionsMenu(menu);
    }

    void tweakStudentsExist(){
        mMenu.findItem(R.id.miAddStudent).setVisible(true);
        mMenu.findItem(R.id.miClearStudents).setVisible(true);
    }

    void tweakStudentsEmpty(){
        mMenu.findItem(R.id.miAddStudent).setVisible(true);
        mMenu.findItem(R.id.miClearStudents).setVisible(false);
    }

    void connectStudentsList() {
        mStudentsListView.setAdapter(
                new ArrayAdapter<Student>(
                        this,
                        android.R.layout.simple_list_item_1,
                        mStudents
                )
        );
        mStudentsListView.setOnItemLongClickListener(
                (parent, view, position, id) -> deleteStudentDialog(position)
        );
        mStudentsListView.setOnItemClickListener(
                (parent, view, position, id) -> editStudentActivityInvoke(position)
        );
    }

    void unmountStudentsList(){
        mStudentsListView.setAdapter(null);
        mStudentsListView.setOnItemClickListener(null);
        mStudentsListView.setOnItemLongClickListener(null);
    }

    private void editStudentActivityInvoke(int position) {

        Student selectedStudent = mStudents.get(position);

        Intent intent = new Intent(GroupDetailsActivity.this, StudentDetailsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Student", selectedStudent);
        intent.putExtras(bundle);

        mActivityResultLauncher.launch(intent);
    }

    private void updateStudentsList(){
        if (mStudents != null){
            tweakStudentsExist();
            connectStudentsList();
        }
        else{
            unmountStudentsList();
            tweakStudentsEmpty();
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putSerializable("Group", mGroup);
        intent.putExtras(bundle);

        setResult(RESULT_OK, intent);
        finish();

        super.onBackPressed();
    }
}