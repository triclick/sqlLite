package com.anbtech.splsqlite;

public class ContactDBCtrct {

    private ContactDBCtrct() {} ;

    public static final String TBL_CONTACT = "CONTACT_T" ;
    public static final String COL_NUM = "NUM" ;
    public static final String COL_NAME = "NAME" ;
    public static final String COL_PHONE = "PHONE" ;
    public static final String COL_OVER20 = "OVER20" ;

    // CREATE TABLE
    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + TBL_CONTACT + " " +
            "("  +
                COL_NUM +       " INTEGER NOT NULL" + ", " +
                COL_NAME +      " TEXT"             + ", " +
                COL_PHONE +     " TEXT"             + ", " +
                COL_OVER20 +    " INTEGER"          +
            ")" ;

    // DROP TABLE
    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS " + TBL_CONTACT ;

    // SELECT TABLE
    public static final String SQL_SELECT = "SELECT * FROM " + TBL_CONTACT ;

    // INSERT or REPLACE
    // DROP TABLE
    public static final String SQL_INSERT = "INSERT OR REPLACE INTO " + TBL_CONTACT + " " +
            "(" + COL_NUM + ", " + COL_NAME + ", " + COL_PHONE + ", " + COL_OVER20 + ") VALUES " ;

    // DELETE TABLE
    public static final String SQL_DELETE = "DELETE FROM " + TBL_CONTACT ;

}
