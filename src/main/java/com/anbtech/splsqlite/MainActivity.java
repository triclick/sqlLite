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
        SQLiteDatabase db = dbHelper.getReadableDatabase() ;
        Cursor cursor = db.rawQuery(ContactDBCtrct.SQL_SELECT, null) ;
        load_values(cursor, cursorMode) ;

        Button buttonSave = (Button)findViewById(R.id.buttonSave) ;
        buttonSave.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                cursor.close();
                db.close();
                save_values() ;
            }
        });

        Button buttonClear = (Button)findViewById(R.id.buttonClear) ;
        buttonClear.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                cursor.close();
                db.close();
                delete_values();
            }
        });

        Button buttonPrev = (Button)findViewById(R.id.buttonPrev) ;
        buttonPrev.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if( cursor == null ){
                    SQLiteDatabase db = dbHelper.getReadableDatabase() ;
                    Cursor cursor = db.rawQuery(ContactDBCtrct.SQL_SELECT, null) ;
                }
                if( !cursor.isFirst() ) {
                    cursorMode = "prev" ;
                    load_values(cursor, cursorMode);
                }
            }
        });

        Button buttonNext = (Button)findViewById(R.id.buttonNext) ;
        buttonNext.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if( cursor == null ){
                    SQLiteDatabase db = dbHelper.getReadableDatabase() ;
                    Cursor cursor = db.rawQuery(ContactDBCtrct.SQL_SELECT, null) ;
                }
                if( !cursor.isLast() ){
                    cursorMode = "next" ;
                    load_values(cursor, cursorMode);
                }
            }
        });

        Button buttonQuery = (Button)findViewById(R.id.buttonQuery) ;
        buttonQuery.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                if( cursor == null ){
                    SQLiteDatabase db = dbHelper.getReadableDatabase() ;
                    Cursor cursor = db.rawQuery(ContactDBCtrct.SQL_SELECT, null) ;
                }
                query_values(cursor);
            }
        });
    }

    private void init_tables() {
        dbHelper = new ContactDBHelper(this) ;
    }

    private void load_values(Cursor cursor, String mode){
        int num = 0, over20 = 0;
        String name = null, phone = null ;

        EditText editTextNo = (EditText) findViewById(R.id.editTextNo);
        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        CheckBox checkBoxOver20 = (CheckBox) findViewById(R.id.checkBoxOver20);

        if(mode == "init" ){
            if( cursor.moveToFirst()){
                num = cursor.getInt(0);
                name = cursor.getString(1);
                phone = cursor.getString(2);
                over20 = cursor.getInt(3);
            }
        }
        else if(mode == "next" ){
            if( cursor.moveToNext()){
                num = cursor.getInt(0);
                name = cursor.getString(1);
                phone = cursor.getString(2);
                over20 = cursor.getInt(3);
            }
        }
        else if(mode == "prev" ){
            if( cursor.moveToPrevious()){
                num = cursor.getInt(0);
                name = cursor.getString(1);
                phone = cursor.getString(2);
                over20 = cursor.getInt(3);
            }
        }

        // num(int), name(text),phone(TEXT),over20 (integer) 값 가져오기
        editTextNo.setText(Integer.toString(num));
        editTextName.setText(name);
        editTextPhone.setText(phone);
        if (over20 == 0) {
            checkBoxOver20.setChecked(false);
        } else {
            checkBoxOver20.setChecked(true);
        }
    }

    private void query_values(Cursor cursor){
        String strQuery = "DB List \r\n" ;
        if( cursor.moveToFirst()){
            do {
                strQuery += cursor.getInt(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getInt(3) + "\r\n";
            }
            while(cursor.moveToNext()) ;
        }
        cursor.moveToLast() ;
        EditText editQuery = (EditText)findViewById(R.id.editTextQuery) ;
        editQuery.setText(strQuery);
    }

    private void save_values() {
            SQLiteDatabase db = dbHelper.getWritableDatabase() ;

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