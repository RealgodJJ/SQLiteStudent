package reagodjj.example.com.sqlitestudent.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import reagodjj.example.com.sqlitestudent.R;
import reagodjj.example.com.sqlitestudent.SecondActivity;
import reagodjj.example.com.sqlitestudent.entity.Student;

public class StudentDao {
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public StudentDao(final Context context) {
        this.context = context;
        SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper(context, "student.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                Toast.makeText(context, R.string.add_database, Toast.LENGTH_SHORT).show();

                String createTable = "create table info_student (_id integer primary key autoincrement,"
                        + "name varchar(20), age integer, gender varchar(2))";
                db.execSQL(createTable);
                Toast.makeText(context, R.string.add_chart, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Toast.makeText(context, R.string.database_update, Toast.LENGTH_SHORT).show();
            }
        };

        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
    }

    public void addStudent(Student student) {
        //在SqliteDatabase类下，提供四个方法
        //insert（添加）、delete（删除）、update（修改）、query（查询）
        //都不需要写sql语句
        //参数1：你所要操作的数据库表的名称
        //参数2：可以为空的列.  如果第三个参数是null或者说里面没有数据
        //那么我们的sql语句就会变为insert into info_tb () values ()  ，在语法上就是错误的
        //此时通过参数3指定一个可以为空的列，语句就变成了insert into info_tb (可空列) values （null）
        ContentValues values = new ContentValues();
        //insert into 表明(列1，列2) values（值1，值2）
        values.put("name", student.getName());
        values.put("age", student.getAge());
        values.put("gender", student.getGender());
        long id = sqLiteDatabase.insert("info_student", null, values);
        Toast.makeText(context, context.getString(R.string.add_student_id, id), Toast.LENGTH_SHORT).show();
    }

    public Cursor selectStudent(String... strs) {
//        //1.查询所有(没有参数)
//        String sql = "select * from info_tb ";
//        //2.含条件查询（姓名/年龄/编号）（参数形式：第一个参数指明条件，第二个参数指明条件值）
//        if(strs.length != 0){
//            sql += " where " + strs[0] + "='" + strs[1] + "'";
//        }
//        Cursor c = db.rawQuery(sql,null);
//        return c;

        Cursor cursor;
        if (strs.length != 0) {
            cursor = sqLiteDatabase.query("info_student", new String[]{"_id, name, age, gender"},
                    strs[0] + "= ?", new String[]{strs[1]}, null, null, "age desc");
        } else {
            cursor = sqLiteDatabase.query("info_student", null,
                    null, null, null, null, "age desc");
        }
        return cursor;
    }

    public int updateStudent(Student student, String ...strs) {
        ContentValues values_1 = new ContentValues();
        //insert into 表明(列1，列2) values（值1，值2）
        values_1.put("name", student.getName());
        values_1.put("age", student.getAge());
        values_1.put("gender", student.getGender());
        return sqLiteDatabase.update("info_student", values_1,
                strs[0] + " = ?", new String[]{strs[1]});
    }

    public int deleteStudent(String... strs) {
        //delete： 返回值：count表示影响了多少行
        //参数1：表名
        //参数2：条件列（“_id=? and name = ?”）
        //参数3：条件值（new String[]{}）
        int count;
        if (!strs[0].equals("")) {
            //统计影响数据的行数
            count = sqLiteDatabase.delete("info_student", strs[0] + "= ?", new String[]{strs[1]});
        } else {
            count = sqLiteDatabase.delete("info_student", null, null);
        }
        return count;
    }
}
