package com.example.android_lab_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_lab_2.faculty.Group;
import com.example.android_lab_2.faculty.Student;

import java.util.ArrayList;
import java.util.Calendar;

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

        mGroup = (Group) bundle.getParcelable("Group");
        mStudents = mGroup.getStudents();

        mStudentsListView = findViewById(R.id.StudentsList);


        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){

                        Intent intent = result.getData();
                        Bundle _bundle = intent.getExtras();

                        Student student = (Student) _bundle.getParcelable("Student");

                        mGroup.setStudent(student.getID(), student);

                        Toast.makeText(
                                getApplicationContext(),
                                "Успешно вернулись",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                    ((StudentListAdapter)mStudentsListView.getAdapter())
                            .notifyDataSetChanged();
                }
        );


        setTitle(mGroup.getName());
    }

    private void createStudent(String firstName, String secondName, String lastName, Long date) {

        Calendar birthDate = Calendar.getInstance();
        birthDate.setTimeInMillis(date);

        Student student = Student.Create(
                firstName,
                secondName,
                lastName,
                birthDate
        );

        mStudents.add(student);
        ((StudentListAdapter) mStudentsListView.getAdapter()).notifyDataSetChanged();
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

        CalendarView studentBirthDateNameInput = dialogView
                .findViewById(R.id.studentEditBirthDate3);

        studentBirthDateNameInput.setOnDateChangeListener((_view, year, month, day) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            studentBirthDateNameInput.setDate(c.getTimeInMillis());
        });

        dialogView.findViewById(R.id.studentEditSubmitButton).setOnClickListener(view -> {

            EditText studentFirstNameInput = dialogView
                    .findViewById(R.id.studentEditFirstName3);
            EditText studentLastNameInput = dialogView
                    .findViewById(R.id.studentEditLastName3);
            EditText studentSecondNameInput = dialogView
                    .findViewById(R.id.studentEditSecondName3);

            String studentFirstName = studentFirstNameInput.getText().toString();
            String studentLastName = studentLastNameInput.getText().toString();
            String studentSecondName = studentSecondNameInput.getText().toString();
            long studentBirthDate = studentBirthDateNameInput.getDate();

            if (!studentFirstName.isEmpty()) {
                if (!studentLastName.isEmpty()) {
                    if (!studentSecondName.isEmpty()) {

                        createStudent(
                                studentFirstName,
                                studentLastName,
                                studentSecondName,
                                studentBirthDate
                        );

                        studentInputDialog.cancel();

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
                new StudentListAdapter(
                        mGroup,
                        this,
                        (parent, view, position, id) -> editStudentActivityInvoke(position),
                        (parent, view, position, id) -> deleteStudentDialog(position)
                )
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
        bundle.putParcelable("Student", selectedStudent);
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
        bundle.putParcelable("Group", mGroup);
        intent.putExtras(bundle);

        setResult(RESULT_OK, intent);
        finish();

        super.onBackPressed();
    }
}