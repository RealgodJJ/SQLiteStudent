package reagodjj.example.com.sqlitestudent;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import reagodjj.example.com.sqlitestudent.dao.StudentDao;
import reagodjj.example.com.sqlitestudent.entity.Student;

public class SecondActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etAge;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private EditText etNumber;
    private ListView lvSelectItem;

    private StudentDao studentDao;
    private SQLiteDatabase sqLiteDatabase;
    private String gender = "男";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initView();

//        String path = Environment.getExternalStorageDirectory() + "/student.db";

        studentDao = new StudentDao(this);
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        rgGender = findViewById(R.id.rg_gender);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        etNumber = findViewById(R.id.et_number);
        lvSelectItem = findViewById(R.id.lv_select_item);

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_male)
                    gender = rbMale.getText().toString();
                else if (checkedId == R.id.rb_female)
                    gender = rbFemale.getText().toString();
            }
        });
    }

    public void operate(View view) {
        String name = etName.getText().toString();
        String age = etAge.getText().toString();
        String condition_id = etNumber.getText().toString();
        switch (view.getId()) {
            case R.id.bt_insert:
                if (!age.equals("")) {
                    Student student = new Student(name, Integer.parseInt(age), gender);
                    studentDao.addStudent(student);
                }
                break;

            case R.id.bt_delete:
                String d_key = "";
                String d_value = "";
                if (!age.equals("")) {
                    d_key = "age";
                    d_value = age;
                } else if (!condition_id.equals("")) {
                    d_key = "_id";
                    d_value = condition_id;
                } else if (!name.equals("")) {
                    d_key = "name";
                    d_value = name;
                }

                int count;
                if (d_key.equals(""))
                    count = studentDao.deleteStudent();
                else
                    count = studentDao.deleteStudent(d_key, d_value);
                if (count > 0)
                    Toast.makeText(SecondActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_update:
                Student student = new Student(Integer.parseInt(condition_id), name, Integer.parseInt(age),
                        gender);

                int count_1 = studentDao.updateStudent(student, "_id", condition_id);

                if (count_1 > 0)
                    Toast.makeText(SecondActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_select:
                String s_key = "";
                String s_value = "";
                if (!age.equals("")) {
                    s_key = "age";
                    s_value = age;
                } else if (!condition_id.equals("")) {
                    s_key = "_id";
                    s_value = condition_id;
                } else if (!name.equals("")) {
                    s_key = "name";
                    s_value = name;
                }

                Cursor cursor;
                if (s_key.equals(""))
                    cursor = studentDao.selectStudent();
                else {
                    cursor = studentDao.selectStudent(s_key, s_value);
                }

                SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
                        R.layout.select_item, cursor, new String[]{"_id", "name", "age", "gender"},
                        new int[]{R.id.tv_id, R.id.tv_name, R.id.tv_age, R.id.tv_gender},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                lvSelectItem.setAdapter(simpleCursorAdapter);
                break;
        }
        etName.setText("");
        etAge.setText("");
        etNumber.setText("");
        rbMale.setChecked(true);
    }
}
