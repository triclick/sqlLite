package com.anbtech.splsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //SQLiteDatabase sqLiteDB ;
    ContactDBHelper dbHelper = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sqLiteDB = init_database() ;
        init_tables() ;
        load_values() ;

        Button buttonSave = (Button)findViewById(R.id.buttonSave) ;
        buttonSave.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                save_values() ;
            }
        });

        Button buttonClear = (Button)findViewById(R.id.buttonClear) ;
        buttonClear.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                delete_values() ;
            }
        });

    }

    private SQLiteDatabase init_database() {
        SQLiteDatabase db = null ;

        // File file = getDatabasePath("contact.db") ;
        File file = new File(getFilesDir(),"contact.db") ;

        System.out.println("PATH :" + file.toString()) ;
        try{
            db = SQLiteDatabase.openOrCreateDatabase(file, null) ;
        } catch(SQLiteException e){
            e.printStackTrace();
        }

        if(db == null){
            System.out.println("DB creation failed. " + file.getAbsolutePath()) ;
        }
        return db ;
    }

    private void init_tables() {

        dbHelper = new ContactDBHelper(this) ;
        /* if(sqLiteDB != null) {
            String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS CONTACT_T (" +
                    "NUM "    +   "INTEGER NOT NULL," +
                    "NAME "    +   "TEXT," +
                    "PHONE "    +   "TEXT," +
                    "OVER20 "    +   "INTEGER" + ")" ;
            System.out.println(sqlCreateTbl);
            sqLiteDB.execSQL(sqlCreateTbl);
        }*/
    }

    private void load_values() {
        //if(sqLiteDB != null){
        //    String sqlQueryTbl = "SELECT * FROM CONTACT_T" ;
        //    Cursor cursor = null ;

            //퀴리 실행
        //    cursor = sqLiteDB.rawQuery(sqlQueryTbl, null) ;
        SQLiteDatabase db = dbHelper.getReadableDatabase() ;
        Cursor cursor = db.rawQuery(ContactDBCtrct.SQL_SELECT, null ) ;

        //    if(cursor.moveToNext()){
        if(cursor.moveToFirst()){
                // no (integer) 값 가져오기
                int num = cursor.getInt(0) ;
                EditText editTextNo = (EditText)findViewById(R.id.editTextNo) ;
                editTextNo.setText(Integer.toString(num));

                // name(text) 값 가져오기
                String name = cursor.getString(1) ;
                EditText editTextName = (EditText)findViewById(R.id.editTextName) ;
                editTextName.setText(name);

                // phone(TEXT) 값 가져오기
                String phone = cursor.getString(2) ;
                EditText editTextPhone = (EditText)findViewById(R.id.editTextPhone) ;
                editTextPhone.setText(phone);

                // over20 (integer) 값 가져오기
                int over20 = cursor.getInt(3) ;
                CheckBox checkBoxOver20 = (CheckBox)findViewById(R.id.checkBoxOver20) ;
                if(over20 == 0){
                    checkBoxOver20.setChecked(false);
                } else {
                    checkBoxOver20.setChecked(true);
                }
        //    }
        }
    }

    private void save_values() {
        //if(sqLiteDB != null) {
            //delete
            //sqLiteDB.execSQL("DELETE FROM CONTACT_T");
            SQLiteDatabase db = dbHelper.getWritableDatabase() ;

            db.execSQL(ContactDBCtrct.SQL_DELETE);

            EditText editTextNo = (EditText)findViewById(R.id.editTextNo) ;
            int num = Integer.parseInt(editTextNo.getText().toString()) ;

        //    String noText = editTextNo.getText().toString() ;
        //    int num = 0 ;
        //    if( noText != null && !noText.isEmpty()){
        //        num = Integer.parseInt(noText) ;
        //   }

            EditText editTextName = (EditText)findViewById(R.id.editTextName) ;
            String name = editTextName.getText().toString() ;

            EditText editTextPhone = (EditText)findViewById(R.id.editTextPhone) ;
            String phone = editTextPhone.getText().toString() ;

            CheckBox checkBoxOver20 = (CheckBox)findViewById(R.id.checkBoxOver20) ;
            boolean isOver20 = checkBoxOver20.isChecked() ;

            String sqlInsert = "INSERT INTO CONTACT_T " +
                    "( NUM, NAME, PHONE, OVER20 ) VALUES (" +
                    Integer.toString(num) + "," +
                    "'" + name + "'," +
                    "'" + phone + "'," +
                    ((isOver20 == true) ? "1" : "0" ) + ")" ;
            System.out.println(sqlInsert);
            db.execSQL(sqlInsert);
            //sqLiteDB.execSQL(sqlInsert);
        //}
    }

    private void delete_values(){
        //if(sqLiteDB != null ){
            //String sqlDelete = "DELETE FROM CONTACT_T" ;
            //sqLiteDB.execSQL(sqlDelete);
            SQLiteDatabase db = dbHelper.getWritableDatabase() ;

            db.execSQL(ContactDBCtrct.SQL_DELETE);

            EditText editTextNo = (EditText)findViewById(R.id.editTextNo) ;
            editTextNo.setText("");

            EditText editTextName = (EditText)findViewById(R.id.editTextName) ;
            editTextName.setText("");

            EditText editTextPhone = (EditText)findViewById(R.id.editTextPhone) ;
            editTextPhone.setText("");

            CheckBox checkBoxOver20 = (CheckBox) findViewById(R.id.checkBoxOver20) ;
            checkBoxOver20.setChecked(false);
        //}
    }
}