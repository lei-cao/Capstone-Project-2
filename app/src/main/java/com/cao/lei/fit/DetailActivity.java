package com.cao.lei.fit;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.cao.lei.fit.models.TrainingSet;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_TRAININGSET = "TRAININGSET";

    private TrainingSet mTrainingSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);



        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            mTrainingSet = arguments.getParcelable(DETAIL_TRAININGSET);
            getSupportActionBar().setTitle(mTrainingSet.title);


            WebView detail = (WebView) findViewById(R.id.detail);
            detail.loadData(mTrainingSet.description, "text/html; charset=utf-8", "utf-8");


            VideoView video = (VideoView) findViewById(R.id.video);
            Uri uri = Uri.parse(mTrainingSet.videourl);

            video.setMediaController(new MediaController(this));
            video.setVideoURI(uri);
            video.requestFocus();
            video.start();

            video.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });

        }

    }
}
