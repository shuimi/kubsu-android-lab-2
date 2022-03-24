package com.example.android_lab_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_lab_2.faculty.Faculty;
import com.example.android_lab_2.faculty.Group;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {

    private Faculty mFaculty;
    private Menu mMenu;
    private ListView mGroupsList;

    private ActivityResultLauncher mActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mGroupsList = findViewById(R.id.GroupsList);

        SharedPreferences sharedPreferences = getSharedPreferences("faculty", MODE_PRIVATE);
        mFaculty = new Gson().fromJson(sharedPreferences.getString("faculty", ""), Faculty.class);

        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {

                        Intent intent = result.getData();
                        Bundle bundle = intent.getExtras();

                        Group group = (Group) bundle.getParcelable("Group");

                        mFaculty.setGroup(group.getID(), group);

                        Toast.makeText(
                                getApplicationContext(),
                                "Успешно вернулись",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = getSharedPreferences("faculty", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(mFaculty);
        editor.putString("faculty", json).apply();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.faculty_menu, menu);
        mMenu = menu;
        mMenu.findItem(R.id.miAddGroup).setVisible(false);
        menu.findItem(R.id.miDeleteFaculty).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    public void exitDialog() {
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(MainActivity.this);
        exitDialog.setTitle("Точно выйти?");
        exitDialog.setMessage("Точно-точно выйти?");
        exitDialog.setCancelable(false);
        exitDialog.setPositiveButton("Выйти!", (dialogInterface, i) -> finish());
        exitDialog.setNegativeButton("Неа!", null);
        exitDialog.show();
    }

    public void aboutDialog() {
        AlertDialog.Builder infoDialog = new AlertDialog.Builder(MainActivity.this);
        infoDialog.setTitle("О программе");
        infoDialog.setMessage("Это программа.");
        infoDialog.setCancelable(true);
        infoDialog.setPositiveButton("Круто!", null);
        infoDialog.show();
    }

    boolean deleteGroupDialog(int position) {
        AlertDialog.Builder infoDialog = new AlertDialog.Builder(MainActivity.this);
        infoDialog.setTitle("Подтвердить");
        infoDialog.setMessage("Удалить группу?");
        infoDialog.setCancelable(true);

        infoDialog.setPositiveButton("Удалить", (dialog, which) -> {
            mFaculty.deleteGroup(position);
            ((ArrayAdapter) mGroupsList.getAdapter()).notifyDataSetChanged();
        });
        infoDialog.setNegativeButton("Отмена", null);
        infoDialog.show();
        return true;
    }

    boolean editGroupActivityInvoke(int position) {

        Group selectedGroup = mFaculty.getGroup(position);

        Intent intent = new Intent(MainActivity.this, GroupDetailsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("Group", selectedGroup);
        intent.putExtras(bundle);

        mActivityResultLauncher.launch(intent);

        return true;
    }

    void tweakNoFaculty() {
        setTitle(mFaculty.getName());
        mMenu.findItem(R.id.miCreateFaculty).setVisible(false);
        mMenu.findItem(R.id.miDeleteFaculty).setVisible(true);
        mMenu.findItem(R.id.miAddGroup).setVisible(true);
    }

    void tweakFacultyExists() {
        setTitle("");
        mMenu.findItem(R.id.miCreateFaculty).setVisible(true);
        mMenu.findItem(R.id.miDeleteFaculty).setVisible(false);
        mMenu.findItem(R.id.miAddGroup).setVisible(false);
    }

    void connectGroupsList() {
        mGroupsList.setAdapter(
                new ArrayAdapter<Group>(
                        this,
                        android.R.layout.simple_list_item_1,
                        mFaculty.getGroups()
                )
        );
        mGroupsList.setOnItemLongClickListener(
                (parent, view, position, id) -> deleteGroupDialog(position)
        );
        mGroupsList.setOnItemClickListener(
                (parent, view, position, id) -> editGroupActivityInvoke(position)
        );
    }

    void unmountGroupsList() {
        mGroupsList.setAdapter(null);
        mGroupsList.setOnItemClickListener(null);
        mGroupsList.setOnItemLongClickListener(null);
    }

    void updateFaculty() {
        if (mFaculty != null) {
            tweakNoFaculty();
            connectGroupsList();
        } else {
            tweakFacultyExists();
            unmountGroupsList();
        }
    }

    public void facultyCreateDialog() {
        AlertDialog facultyInputDialog =
                new AlertDialog.Builder(MainActivity.this).create();
        facultyInputDialog.setTitle("Введите название факультета");
        facultyInputDialog.setCancelable(true);

        View dialogView = getLayoutInflater().inflate(
                R.layout.faculty_input_layout,
                null
        );

        dialogView.findViewById(R.id.filAcceptButton).setOnClickListener(view -> {

            EditText facultyInput = dialogView.findViewById(R.id.filFacultyInput);
            String facultyName = facultyInput.getText().toString();

            if (!facultyName.isEmpty()) {
                mFaculty = new Faculty(facultyName);

                mMenu.findItem(R.id.miDeleteFaculty).setVisible(true);

                updateFaculty();
                facultyInputDialog.cancel();
            } else {
                facultyInput.setError("Неправильное название факультета");
            }

        });

        dialogView.findViewById(R.id.filCancelButton).setOnClickListener(view -> {
            facultyInputDialog.cancel();
        });

        facultyInputDialog.setView(dialogView);
        facultyInputDialog.show();
    }

    public void deleteFacultyDialog() {
        AlertDialog.Builder deleteFacultyDialog = new AlertDialog.Builder(MainActivity.this);
        deleteFacultyDialog.setTitle("Точно удалить?");
        deleteFacultyDialog.setMessage("Точно-точно удалить?");
        deleteFacultyDialog.setCancelable(false);

        deleteFacultyDialog.setPositiveButton("Удалить!", (dialogInterface, i) -> {
            mFaculty.clearGroups();
            mFaculty = null;
            updateFaculty();
        });

        deleteFacultyDialog.setNegativeButton("Неа!", null);
        deleteFacultyDialog.show();
    }

    public void groupAddDialog() {

        AlertDialog groupInputDialog =
                new AlertDialog.Builder(MainActivity.this).create();
        groupInputDialog.setTitle("Введите название группы");
        groupInputDialog.setCancelable(false);

        View dialogView = getLayoutInflater().inflate(
                R.layout.faculty_input_layout,
                null
        );

        dialogView.findViewById(R.id.filAcceptButton).setOnClickListener(view -> {

            EditText facultyInput = dialogView.findViewById(R.id.filFacultyInput);
            String groupName = facultyInput.getText().toString();

            if (!groupName.isEmpty()) {

                Group group = new Group(groupName);
                mFaculty.addGroup(group);

                ((ArrayAdapter) mGroupsList.getAdapter()).notifyDataSetChanged();

                updateFaculty();
                groupInputDialog.cancel();
            } else {
                facultyInput.setError("Неправильное название группы");
            }

        });

        dialogView.findViewById(R.id.filCancelButton).setOnClickListener(view -> {
            groupInputDialog.cancel();
        });

        groupInputDialog.setView(dialogView);
        groupInputDialog.show();
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.miAbout: {
                aboutDialog();
                return true;
            }
            case R.id.miExit: {
                exitDialog();
                return true;
            }
            case R.id.miCreateFaculty: {
                facultyCreateDialog();
                return true;
            }
            case R.id.miDeleteFaculty: {
                deleteFacultyDialog();
                return true;
            }
            case R.id.miAddGroup: {
                groupAddDialog();
                return true;
            }
            default: {
            }
        }

        return super.onOptionsItemSelected(item);
    }
}