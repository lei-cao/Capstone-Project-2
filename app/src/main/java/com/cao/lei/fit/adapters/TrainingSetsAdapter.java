package com.cao.lei.fit.adapters;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ARNAUD FRUGIER
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cao.lei.fit.R;
import com.cao.lei.fit.data.TrainingSetsContract;
import com.cao.lei.fit.models.TrainingSet;
import com.squareup.picasso.Picasso;

public class TrainingSetsAdapter extends CursorRecyclerAdapter<SimpleViewHolder> {

    Context mContext;
    private int mLayout;
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;

    public TrainingSetsAdapter (Context context, int layout, Cursor c, String[] from, int[] to) {
        super(c);
        mContext = context;
        mLayout = layout;
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(mLayout, parent, false);
        return new SimpleViewHolder(v, mTo);
    }

    @Override
    public void onBindViewHolder (SimpleViewHolder holder, Cursor cursor) {
        final int count = mTo.length;
        final int[] from = mFrom;

        for (int i = 0; i < count; i++) {
            ((TextView)holder.views[i].findViewById(R.id.training_title)).setText(cursor.getString(from[1]));
            ImageView imageView = (ImageView) holder.views[i].findViewById(R.id.thumbnail);
            imageView.setImageResource(R.mipmap.ic_launcher);
            Picasso.with(mContext)
                    .load(cursor.getString(from[2]))
                    .noFade()
                    .into(imageView);

        }
    }

    public TrainingSet getItem(int position) {
        TrainingSet ts = new TrainingSet();
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        ts.title= cursor.getString(cursor.getColumnIndex(TrainingSetsContract.TrainingsetsEntry.COLUMN_TRAININGSETS_TITLE));
        ts.thumbnail= cursor.getString(cursor.getColumnIndex(TrainingSetsContract.TrainingsetsEntry.COLUMN_TRAININGSETS_THUMBNAIL));
        ts.description = cursor.getString(cursor.getColumnIndex(TrainingSetsContract.TrainingsetsEntry.COLUMN_TRAININGSETS_DESCRIPTION));
        ts.videourl = cursor.getString(cursor.getColumnIndex(TrainingSetsContract.TrainingsetsEntry.COLUMN_TRAININGSETS_VIDEO_URL));
        return ts;
    }

    /**
     * Create a map from an array of strings to an array of column-id integers in cursor c.
     * If c is null, the array will be discarded.
     *
     * @param c the cursor to find the columns from
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        findColumns(c, mOriginalFrom);
        return super.swapCursor(c);
    }
}

class SimpleViewHolder extends RecyclerView.ViewHolder
{
    public RelativeLayout[] views;

    public SimpleViewHolder (View itemView, int[] to)
    {
        super(itemView);
        views = new RelativeLayout[to.length];
        for(int i = 0 ; i < to.length ; i++) {
            views[i] = (RelativeLayout) itemView.findViewById(to[i]);
        }
    }
}
