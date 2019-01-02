package reagodjj.example.com.sqlitestudent;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
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

public class SecondActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etAge;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private EditText etNumber;
    private ListView lvSelectItem;

    private SQLiteDatabase sqLiteDatabase;
    private String gender = "男";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initView();

//        String path = Environment.getExternalStorageDirectory() + "/student.db";

        SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper(this, "student.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                Toast.makeText(SecondActivity.this, R.string.add_database, Toast.LENGTH_SHORT).show();

                String createTable = "create table info_student (_id integer primary key autoincrement,"
                        + "name varchar(20), age integer, gender varchar(2))";
                db.execSQL(createTable);
                Toast.makeText(SecondActivity.this, R.string.add_chart, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Toast.makeText(SecondActivity.this, R.string.database_update, Toast.LENGTH_SHORT).show();
            }
        };

        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
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
                if (checkedId == R.id.rb_male || checkedId == R.id.rb_female)
                    gender = rbFemale.getText().toString();
            }
        });
    }

    public void operate(View view) {
        String name = etName.getText().toString();
        String age = etAge.getText().toString();
        final String condition_id = etNumber.getText().toString();
        switch (view.getId()) {
            case R.id.bt_insert:
                //在SqliteDatabase类下，提供四个方法
                //insert（添加）、delete（删除）、update（修改）、query（查询）
                //都不需要写sql语句
                //参数1：你所要操作的数据库表的名称
                //参数2：可以为空的列.  如果第三个参数是null或者说里面没有数据
                //那么我们的sql语句就会变为insert into info_tb () values ()  ，在语法上就是错误的
                //此时通过参数3指定一个可以为空的列，语句就变成了insert into info_tb (可空列) values （null）
                ContentValues values = new ContentValues();
                //insert into 表明(列1，列2) values（值1，值2）
                values.put("name", name);
                values.put("age", age);
                values.put("gender", gender);
                long id = sqLiteDatabase.insert("info_student", null, values);
                Toast.makeText(this, getString(R.string.add_student_id, id), Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_delete:
//                delete： 返回值：count表示影响了多少行
//                参数1：表名
//                参数2：条件列（“_id=? and name = ?”）
//                参数3：条件值（new String[]{}）
                int count = sqLiteDatabase.delete("info_student", "age<?", new String[]{age});
                if (count > 0)
                    Toast.makeText(SecondActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_update:
                String update = "update info_student set name = ?, age = ?, gender = ? where _id = ?";
                sqLiteDatabase.execSQL(update, new String[]{name, age, gender, condition_id});
                Toast.makeText(SecondActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_select:
//                query:
//                参数1：表名
//                参数2：查询列（String数组），查询所有传入null或{“*”}
//            参数3：条件列（不需要传入null） “列1=？and 列2 =？”
//            参数4：条件值（不需要传入null） （数组）
//            参数5：分组（不需要传入null） group by
//            参数6：去除不符合条件的组（不需要传入null） having
//            参数7：排序（不需要传入null） order by
                Cursor cursor = sqLiteDatabase.query("info_student", new String[]{"_id, name, age, gender"},
                        "age>?", new String[]{age}, null, null, "age desc");

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
