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
//                delete： 返回值：count表示影响了多少行
//                参数1：表名
//                参数2：条件列（“_id=? and name = ?”）
//                参数3：条件值（new String[]{}）
                int count;
                if (!age.equals("")) {
                    //统计影响数据的行数
                    count = sqLiteDatabase.delete("info_student", "age<?", new String[]{age});
                } else {
                    count = sqLiteDatabase.delete("info_student", null, null);
                }
                if (count > 0)
                    Toast.makeText(SecondActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_update:
                ContentValues values_1 = new ContentValues();
                //insert into 表明(列1，列2) values（值1，值2）
                values_1.put("name", name);
                values_1.put("age", age);
                values_1.put("gender", gender);
                int count_1 = sqLiteDatabase.update("info_student", values_1,
                        "name like ?", new String[]{name});
                if (count_1 > 0)
                    Toast.makeText(SecondActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_select:
                String key = "";
                String value = "";
                if (!age.equals("")) {
                    key = "age";
                    value = age;
                } else if (!condition_id.equals("")) {
                    key = "_id";
                    value = condition_id;
                } else if (!name.equals("")) {
                    key = "name";
                    value = name;
                }

                Cursor cursor;
                if (key.equals(""))
                    cursor = studentDao.selectStudent();
                else {
                    cursor = studentDao.selectStudent(key, value);
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
