package com.cao.lei.fit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cao.lei.fit.data.TrainingSetsContract.TrainingsetsEntry;

public class TrainingSetsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "fit.db";

    public TrainingSetsDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold training sets.  A training sets consists of the
        // Title, thumbnail, description, video url,
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + TrainingsetsEntry.TABLE_NAME + " (" +
                TrainingsetsEntry._ID + " INTEGER PRIMARY KEY," +
                TrainingsetsEntry.COLUMN_TRAININGSETS_TITLE + " TEXT NOT NULL, " +
                TrainingsetsEntry.COLUMN_TRAININGSETS_THUMBNAIL + " TEXT NOT NULL, " +
                TrainingsetsEntry.COLUMN_TRAININGSETS_DESCRIPTION + " TEXT NOT NULL, " +
                TrainingsetsEntry.COLUMN_TRAININGSETS_VIDEO_URL + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrainingsetsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
