package com.cao.lei.fit.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class TrainingSetsContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.cao.lei.fit";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.cao.lei.fit/trainingsets is a valid path for
    public static final String PATH_TRAININGSETS = "trainingsets";

    /* Inner class that defines the table contents of the location table */
    public static final class TrainingsetsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAININGSETS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAININGSETS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAININGSETS;

        // Table name
        public static final String TABLE_NAME = "trainingsets";

        // The trainingsets title
        public static final String COLUMN_TRAININGSETS_TITLE = "trainingsets_title";

        // The trainingsets thumbnail
        public static final String COLUMN_TRAININGSETS_THUMBNAIL = "trainingsets_thumbnail";

        // The trainingsets description
        public static final String COLUMN_TRAININGSETS_DESCRIPTION = "trainingsets_description";

        // The trainingsets video url
        public static final String COLUMN_TRAININGSETS_VIDEO_URL = "trainingsets_video_url";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getTrainingSetsIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

}
