package com.example.cbr__fitness.databasehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FitnessDBSqliteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FitnessDB.db";
    public static final int DATABASE_VERSION = 1;

    public FitnessDBSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("ON CREATE IST CALLED");
        db.execSQL(FitnessDBContract.SQL_CREATE_USER);
        db.execSQL(FitnessDBContract.SQL_CREATE_EXERCISE);
        db.execSQL(FitnessDBContract.SQL_CREATE_PLAN);
        db.execSQL(FitnessDBContract.SQL_CREATE_LIMITATIONS);
        db.execSQL(FitnessDBContract.SQL_CREATE_USER_PLAN_RELATION);
        db.execSQL(FitnessDBContract.SQL_CREATE_PLAN_EXERCISE_RELATION);
        db.execSQL(FitnessDBContract.SQL_CREATE_LIMITATION_USER_RELATION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FitnessDBContract.SQL_DELETE_EXERCISE);
        db.execSQL(FitnessDBContract.SQL_DELETE_USER);
        db.execSQL(FitnessDBContract.SQL_DELETE_PLAN);
        db.execSQL(FitnessDBContract.SQL_DELETE_LIMITATIONS);
        db.execSQL(FitnessDBContract.SQL_DELETE_PLAN_EXERCISE_RELATION);
        db.execSQL(FitnessDBContract.SQL_DELETE_USER_PLAN_RELATION);
        db.execSQL(FitnessDBContract.SQL_DELETE_LIMITATION_USER_RELATION);
        onCreate(db);
    }

}
