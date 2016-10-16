package com.cao.lei.fit.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class TrainingSetsProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TrainingSetsDbHelper mTrainingsetsHelper;

    static final int TRAININGSETS = 100;

    private static final SQLiteQueryBuilder sTrainingsetQueryBuild;

    static {
        sTrainingsetQueryBuild = new SQLiteQueryBuilder();

        sTrainingsetQueryBuild.setTables(TrainingSetsContract.TrainingsetsEntry.TABLE_NAME);
    }


    private static final String sTrainingsetsSelection =
            TrainingSetsContract.TrainingsetsEntry.TABLE_NAME+
                    "." + TrainingSetsContract.TrainingsetsEntry._ID + " = ? ";

    private Cursor getTrainingsetsByID(Uri uri, String[] projection, String sortOrder) {
        String ID = TrainingSetsContract.TrainingsetsEntry.getTrainingSetsIDFromUri(uri);

        String[] selectionArgs = new String[]{ID};
        String selection = sTrainingsetsSelection;

        return sTrainingsetQueryBuild.query(mTrainingsetsHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TrainingSetsContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, TrainingSetsContract.PATH_TRAININGSETS, TRAININGSETS);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mTrainingsetsHelper = new TrainingSetsDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case TRAININGSETS:
                return TrainingSetsContract.TrainingsetsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "trainingsets"
            case TRAININGSETS: {
                retCursor = mTrainingsetsHelper.getReadableDatabase().query(
                        TrainingSetsContract.TrainingsetsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mTrainingsetsHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TRAININGSETS: {
                long _id = db.insert(TrainingSetsContract.TrainingsetsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TrainingSetsContract.TrainingsetsEntry.buildLocationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mTrainingsetsHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case TRAININGSETS:
                rowsDeleted = db.delete(
                        TrainingSetsContract.TrainingsetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mTrainingsetsHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case TRAININGSETS:
                rowsUpdated = db.update(TrainingSetsContract.TrainingsetsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mTrainingsetsHelper.close();
        super.shutdown();
    }

}
