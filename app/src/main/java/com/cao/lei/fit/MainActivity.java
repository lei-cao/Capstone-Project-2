package com.cao.lei.fit;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cao.lei.fit.adapters.TrainingSetsAdapter;
import com.cao.lei.fit.data.TrainingSetsContract.TrainingsetsEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int TRAININGSETS_LOADER = 0;

    private static final String[] TRAININGSETS_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            TrainingsetsEntry.TABLE_NAME + "." + TrainingsetsEntry._ID,
            TrainingsetsEntry.COLUMN_TRAININGSETS_TITLE,
            TrainingsetsEntry.COLUMN_TRAININGSETS_THUMBNAIL,
            TrainingsetsEntry.COLUMN_TRAININGSETS_DESCRIPTION,
            TrainingsetsEntry.COLUMN_TRAININGSETS_VIDEO_URL,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportLoaderManager().initLoader(TRAININGSETS_LOADER, null, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.trining_sets);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        addLocation("haha", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("nim", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("123", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxx1", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxx2", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxx3j", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxx3", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxx4", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxx5", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxx5", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxa", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxb", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxc", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxd", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxe", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxf", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxg", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxh", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxi", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxgj", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxjk", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxl", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxm", "lala", "sfdsfdsf", "sdfsdfds");
        addLocation("xxxn", "lala", "sfdsfdsf", "sdfsdfds");
        // specify an adapter (see also next example)



        // First, check if the location with this city name exists in the db
        Cursor locationCursor = getApplicationContext().getContentResolver().query(
                TrainingsetsEntry.CONTENT_URI,
                new String[]{TrainingsetsEntry._ID, TrainingsetsEntry.COLUMN_TRAININGSETS_TITLE},
                null,
                null,
                null);


        mAdapter = new TrainingSetsAdapter(R.layout.training_set_item, locationCursor, new String[]{TrainingsetsEntry.COLUMN_TRAININGSETS_TITLE}, new int[]{R.id.training_title});
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     *
     * @param title
     * @param thumbnail
     * @param description
     * @param videoUrl
     * @return
     */
    long addLocation(String title, String thumbnail, String description, String videoUrl) {
        long id;



        // First, check if the location with this city name exists in the db
        Cursor locationCursor = getApplicationContext().getContentResolver().query(
                TrainingsetsEntry.CONTENT_URI,
                new String[]{TrainingsetsEntry._ID},
                TrainingsetsEntry.COLUMN_TRAININGSETS_TITLE + " = ?",
                new String[]{title},
                null);

        if (locationCursor.moveToFirst()) {
            int idIndex = locationCursor.getColumnIndex(TrainingsetsEntry._ID);
            id = locationCursor.getLong(idIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues values = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            values.put(TrainingsetsEntry.COLUMN_TRAININGSETS_TITLE, title);
            values.put(TrainingsetsEntry.COLUMN_TRAININGSETS_THUMBNAIL, thumbnail);
            values.put(TrainingsetsEntry.COLUMN_TRAININGSETS_DESCRIPTION, description);
            values.put(TrainingsetsEntry.COLUMN_TRAININGSETS_VIDEO_URL, videoUrl);

            // Finally, insert location data into the database.
            Uri insertedUri = getContentResolver().insert(
                    TrainingsetsEntry.CONTENT_URI,
                    values
            );

            id = ContentUris.parseId(insertedUri);
        }

        locationCursor.close();
        // Wait, that worked?  Yes!
        return id;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = TrainingsetsEntry._ID + " ASC";

        return new CursorLoader(this,
                TrainingsetsEntry.CONTENT_URI,
                TRAININGSETS_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
