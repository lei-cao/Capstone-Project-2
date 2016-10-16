package com.cao.lei.fit;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.cao.lei.fit.adapters.TrainingSetsAdapter;
import com.cao.lei.fit.data.TrainingSetsContract.TrainingsetsEntry;
import com.cao.lei.fit.models.TrainingSet;
import com.cao.lei.fit.services.FitResponses;
import com.cao.lei.fit.services.FitServices;
import com.cao.lei.fit.utils.ItemClickSupport;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context mContext;
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
        mContext = getApplicationContext();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.trining_sets);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // First, check if the location with this city name exists in the db
        final Cursor trainingSetsCursor = mContext.getContentResolver().query(
                TrainingsetsEntry.CONTENT_URI,
                TRAININGSETS_COLUMNS,
                null,
                null,
                null);


        mAdapter = new TrainingSetsAdapter(mContext, R.layout.training_set_item, trainingSetsCursor, TRAININGSETS_COLUMNS, new int[]{R.id.trainingSet});
        mRecyclerView.setAdapter(mAdapter);

        FitServices service = new FitServices();
        Call<FitResponses.TrainingSetsResponse> call = service.service.trainingSets();
        call.enqueue(new Callback<FitResponses.TrainingSetsResponse>() {
            @Override
            public void onResponse(Response<FitResponses.TrainingSetsResponse> response, Retrofit retrofit) {
                if (response.body() != null && response.body().results.size() != 0) {
                    for (TrainingSet t : response.body().results) {
                        addLocation(t.title, t.thumbnail, t.description, t.videourl);
                    }

                    // First, check if the location with this city name exists in the db
                    final Cursor newTrainingSetsCursor = mContext.getContentResolver().query(
                            TrainingsetsEntry.CONTENT_URI, TRAININGSETS_COLUMNS, null, null, null);

                    ((TrainingSetsAdapter)mAdapter).swapCursor(newTrainingSetsCursor);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                String message = t.getMessage();
                Log.d("failure", message);
            }
        });

        ItemClickSupport.addTo(mRecyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        TrainingSet ts = ((TrainingSetsAdapter)mAdapter).getItem(position);
                        Intent intent = new Intent(mContext, DetailActivity.class).putExtra(DetailActivity.DETAIL_TRAININGSET, ts);
                        startActivity(intent);
                    }
                });

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
        Cursor trainingSetsCursor = mContext.getContentResolver().query(
                TrainingsetsEntry.CONTENT_URI,
                new String[]{TrainingsetsEntry._ID},
                TrainingsetsEntry.COLUMN_TRAININGSETS_TITLE + " = ?",
                new String[]{title},
                null);

        if (trainingSetsCursor.moveToFirst()) {
            int idIndex = trainingSetsCursor.getColumnIndex(TrainingsetsEntry._ID);
            id = trainingSetsCursor.getLong(idIndex);
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

        trainingSetsCursor.close();
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
