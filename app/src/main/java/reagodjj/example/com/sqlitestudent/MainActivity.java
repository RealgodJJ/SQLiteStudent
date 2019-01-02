package reagodjj.example.com.sqlitestudent;

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

public class MainActivity extends AppCompatActivity {
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

                if (condition_id.equals("")) {
                    final String delete = "delete from info_student";
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setIcon(R.mipmap.ic_launcher).setTitle(R.string.warning)
                            .setMessage(R.string.sure_to_delete_all)
                            .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sqLiteDatabase.execSQL(delete);
                                }
                            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog alertDialog = builder.show();
                            alertDialog.dismiss();
                        }
                    }).show();
                } else {
                    String delete = "delete from info_student where _id = ?";
                    sqLiteDatabase.execSQL(delete, new String[]{condition_id});
                    Toast.makeText(MainActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
//                    String delete = "delete from info_student where _id = " + condition_id;
//                    sqLiteDatabase.execSQL(delete);
                }
                break;

            case R.id.bt_update:
                break;

            case R.id.bt_select:
                String select = "select * from info_student";

                if (!condition_id.equals("")) {
                    select += " where _id = " + condition_id;
                }
                Cursor cursor = sqLiteDatabase.rawQuery(select, null);

//                if (!condition_id.equals("")) {
//                    select += " where _id = ?";
//                }
//                Cursor cursor = sqLiteDatabase.rawQuery(select, new String[]{condition_id});

                SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
                        R.layout.select_item, cursor, new String[]{"_id", "name", "age", "gender"},
                        new int[]{R.id.tv_id, R.id.tv_name, R.id.tv_age, R.id.tv_gender},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                lvSelectItem.setAdapter(simpleCursorAdapter);
                break;
        }
    }
}
