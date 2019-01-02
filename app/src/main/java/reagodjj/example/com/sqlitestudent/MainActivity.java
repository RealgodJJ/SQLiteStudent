package reagodjj.example.com.sqlitestudent;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etAge;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private EditText etNumber;

    private SQLiteDatabase sqLiteDatabase;
    private String gender = "男";
//    private Button btInsert;
//    private Button btSelect;
//    private Button btDelete;
//    private Button btUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

//        String path = Environment.getExternalStorageDirectory() + "/student.db";

        SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper(this, "student.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                Toast.makeText(MainActivity.this, R.string.add_database, Toast.LENGTH_SHORT).show();

                String createTable = "create table info_student (_id integer primary key autoincrement,"
                        + "name varchar(20), age integer, gender varchar(2))";
                db.execSQL(createTable);
                Toast.makeText(MainActivity.this, R.string.add_chart, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Toast.makeText(MainActivity.this, R.string.database_update, Toast.LENGTH_SHORT).show();
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

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_male || checkedId == R.id.rb_female)
                    gender = rbFemale.getText().toString();
            }
        });
    }

    public void operate(View view) {
        switch (view.getId()) {
            case R.id.bt_insert:
                String name = etName.getText().toString();
                String age = etAge.getText().toString();

//                String insert = "insert into info_student(name, age, gender) values ('" + name +
//                        "', " + age + ", '" + gender + "')";
//                sqLiteDatabase.execSQL(insert);

                String insert = "insert into info_student(name, age, gender) values (?, ?, ?)";
                sqLiteDatabase.execSQL(insert, new String[]{name, age, gender});

                etName.setText("");
                etAge.setText("");

                Toast.makeText(MainActivity.this, "添加数据成功！", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_delete:
                break;

            case R.id.bt_update:
                break;

            case R.id.bt_select:
                break;
        }
    }
}