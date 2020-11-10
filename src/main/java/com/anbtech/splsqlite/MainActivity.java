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
    public String cursorMode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sqLiteDB = init_database() ;
        cursorMode = "init" ;
        init_tables() ;
        load_values(cursorMode) ;

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

        Button buttonPrev = (Button)findViewById(R.id.buttonPrev) ;
        buttonPrev.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                cursorMode = "prev" ;
                load_values(cursorMode);
            }
        });

        Button buttonNext = (Button)findViewById(R.id.buttonNext) ;
        buttonPrev.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                cursorMode = "next" ;
                load_values(cursorMode);
            }
        });

        Button buttonQuery = (Button)findViewById(R.id.buttonQuery) ;
        buttonQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                query_values();
            }
        });

    }

    private SQLiteDatabase init_database() {
        SQLiteDatabase db = null ;

        // File file = getDatabasePath("contact.db") ;
        File file = new File(getFilesDir(),"contact1.db") ;

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

    private void load_values(String mode){
        //if(sqLiteDB != null){
        //    String sqlQueryTbl = "SELECT * FROM CONTACT_T" ;
        //    Cursor cursor = null ;

            //퀴리 실행
        //    cursor = sqLiteDB.rawQuery(sqlQueryTbl, null) ;
        int num = 0, over20 = 0;
        String name = null, phone = null ;

        SQLiteDatabase db = dbHelper.getReadableDatabase() ;
        Cursor cursor = db.rawQuery(ContactDBCtrct.SQL_SELECT, null ) ;

        if(mode == "init" ){
            if( cursor.moveToFirst()){
                num = cursor.getInt(0);
                name = cursor.getString(1);
                phone = cursor.getString(2);
                over20 = cursor.getInt(3);
                // num(int) 값 가져오기
                EditText editTextNo = (EditText) findViewById(R.id.editTextNo);
                editTextNo.setText(Integer.toString(num));

                // name(text) 값 가져오기
                EditText editTextName = (EditText) findViewById(R.id.editTextName);
                editTextName.setText(name);

                // phone(TEXT) 값 가져오기
                EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);
                editTextPhone.setText(phone);

                // over20 (integer) 값 가져오기
                CheckBox checkBoxOver20 = (CheckBox) findViewById(R.id.checkBoxOver20);
                if (over20 == 0) {
                    checkBoxOver20.setChecked(false);
                } else {
                    checkBoxOver20.setChecked(true);
                }
            }
        }

    }

    private void query_values(){
        SQLiteDatabase db = dbHelper.getReadableDatabase() ;
        Cursor cursor = db.rawQuery(ContactDBCtrct.SQL_SELECT, null) ;

        String strQuery = "DB List \r\n" ;
        while(cursor.moveToNext()){
            strQuery += cursor.getInt(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getInt(3) + "\r\n" ;
        }

        EditText editQuery = (EditText)findViewById(R.id.editTextQuery) ;
        editQuery.setText(strQuery);

        cursor.close() ;
        db.close() ;
    }

    private void save_values() {
        //if(sqLiteDB != null) {
            //delete
            //sqLiteDB.execSQL("DELETE FROM CONTACT_T");
            SQLiteDatabase db = dbHelper.getWritableDatabase() ;

            //db.execSQL(ContactDBCtrct.SQL_DELETE);

            EditText editTextNo = (EditText)findViewById(R.id.editTextNo) ;
            int num = Integer.parseInt(editTextNo.getText().toString()) ;

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