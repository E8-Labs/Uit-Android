package com.antizon.uit_android.activities.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.activities.comments.CommentsActivity;
import com.antizon.uit_android.generic.activities.WebViewActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.CommunityPostDataModel;
import com.antizon.uit_android.utilities.DoubleClickListener;
import com.antizon.uit_android.utilities.UitApplicationClass;
import com.antizon.uit_android.utilities.Utilities;
import com.antizon.uit_android.utilities.cache.PlayerUtilities;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Objects;

public class CommunityPostDetailActivity extends AppCompatActivity {
    Context context;
    SessionManagement sessionManagement;
    boolean playWhenReady = true;
    int currentWindow = 0;
    long playbackPosition = 0;

    ImageView btnBack, postImage, uitImage;
    RelativeLayout layout_video, layout_image, layout_bottom;
    StyledPlayerView playerView;
    AVLoadingIndicatorView video_loading, image_loading;
    TextView text_title, text_description, text_overView, text_comments, text_visitWebsite;
    RoundedImageView user_ProfileImage;

    ExoPlayer player;
    CommunityPostDataModel postData;
    String videoUrl;

    FloatingActionButton btnPlay;
    View layoutShowButton;
    boolean isVideoPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_post_detail);
        Utilities.setWhiteBars(CommunityPostDetailActivity.this);
        context = CommunityPostDetailActivity.this;
        sessionManagement = new SessionManagement(context);

        postData = getIntent().getParcelableExtra("postData");

        btnBack = findViewById(R.id.btnBack);
        postImage = findViewById(R.id.postImage);
        layout_video = findViewById(R.id.layout_video);
        layout_image = findViewById(R.id.layout_image);
        image_loading = findViewById(R.id.image_loading);
        layout_bottom = findViewById(R.id.layout_bottom);
        playerView = findViewById(R.id.playerView);
        video_loading = findViewById(R.id.video_loading);
        text_title = findViewById(R.id.text_title);
        text_description = findViewById(R.id.text_description);
        text_overView = findViewById(R.id.text_overView);
        text_comments = findViewById(R.id.text_comments);
        text_visitWebsite = findViewById(R.id.text_visitWebsite);
        uitImage = findViewById(R.id.uitImage);
        user_ProfileImage = findViewById(R.id.user_ProfileImage);
        btnPlay = findViewById(R.id.btnPlay);
        layoutShowButton = findViewById(R.id.layoutShowButton);


        if (sessionManagement.getRole().equals("1")){
            uitImage.setVisibility(View.VISIBLE);
            user_ProfileImage.setVisibility(View.GONE);
        }else {
            uitImage.setVisibility(View.GONE);
            user_ProfileImage.setVisibility(View.VISIBLE);
            Utilities.loadImage(context, sessionManagement.getProfileImage(), user_ProfileImage);
        }
        showPostDetails(postData);

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void showPostDetails(CommunityPostDataModel postDataModel){
        if (postDataModel.getVideo_url() != null){
            layout_video.setVisibility(View.VISIBLE);
            layout_image.setVisibility(View.GONE);
            initPlayer(postDataModel);
        }else {
            layout_video.setVisibility(View.GONE);
            layout_image.setVisibility(View.VISIBLE);
            Utilities.loadImage(context, postDataModel.getImage_path() , postImage, image_loading);

            postImage.setOnClickListener(v -> {
                Intent intent = new Intent(context, ImageViewerActivity.class);
                ViewCompat.setTransitionName(postImage, postDataModel.getImage_path());
                String animationName = ViewCompat.getTransitionName(postImage);
                intent.putExtra("animationName", animationName);
                intent.putExtra("IMAGE",postDataModel.getImage_path());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(CommunityPostDetailActivity.this, postImage, Objects.requireNonNull(animationName));
                startActivity(intent, options.toBundle());
            });
        }

        if (postDataModel.getPost_title() !=null){
            text_title.setText(postDataModel.getPost_title());
        }

        if (postDataModel.getPost_description() !=null){
            text_description.setText(postDataModel.getPost_description());
        }

        if (postDataModel.getPost_url() !=null){
            text_visitWebsite.setVisibility(View.VISIBLE);
            text_visitWebsite.setOnClickListener(v -> {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("pageTitle", "Post Website");
                intent.putExtra("pageUrl", postDataModel.getPost_url());
                startActivity(intent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            });
        }else {
            text_visitWebsite.setVisibility(View.GONE);
        }

        text_comments.setOnClickListener(v -> moveToComments(String.valueOf(postDataModel.getId())));
        layout_bottom.setOnClickListener(v -> moveToComments(String.valueOf(postDataModel.getId())));
    }

    private void initPlayer(CommunityPostDataModel postDataModel) {
        videoUrl = postDataModel.getVideo_url();

        RenderersFactory renderersFactory = PlayerUtilities.buildRenderersFactory(context, false);
        MediaSource.Factory mediaSourceFactory = new DefaultMediaSourceFactory(UitApplicationClass.dataSourceFactory);
        player = new ExoPlayer.Builder(context, renderersFactory).setMediaSourceFactory(mediaSourceFactory).setTrackSelector(UitApplicationClass.trackSelectorDef).setLoadControl(new DefaultLoadControl()).build();
        Uri uriOfContentUrl = Uri.parse(videoUrl);
        MediaItem mediaItem = new MediaItem.Builder().setUri(uriOfContentUrl).setMimeType(MimeTypes.VIDEO_MP4).build();
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(UitApplicationClass.dataSourceFactory).createMediaSource(mediaItem);
        player.setMediaSource(mediaSource);
        player.setPlayWhenReady(true);
        player.prepare();
        player.setVolume(1);
        playerView.requestFocus();
        playerView.setPlayer(player);
        playerView.setShowBuffering(StyledPlayerView.SHOW_BUFFERING_NEVER);
        playerView.showController();

        stopPlayer();

        layoutShowButton.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (isVideoPlaying){

                    btnPlay.setVisibility(View.VISIBLE);
                    btnPlay.show();
                }
            }
        });

        btnPlay.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!isVideoPlaying){
                    resumePlayer();
                }else {
                    stopPlayer();
                }
            }
        });

        setListeners();
    }

    private void setListeners() {
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_ENDED) {
                
                    //The player finished playing all media
                    player.seekTo(0);
                    player.setPlayWhenReady(true);
                } else if (state == Player.STATE_BUFFERING) {//show thumbnail when buffering
                    video_loading.setVisibility(View.VISIBLE);
                } else if (state == Player.STATE_READY) {
                    video_loading.setVisibility(View.GONE);
                }
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isVideoPlaying){
            resumePlayer();
        }else {
            stopPlayer();
        }

    }


    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentMediaItemIndex();
            player.release();
            player = null;
        }
    }

    private void stopPlayer(){
        if (player !=null){
            player.pause();
            player.setVolume(0);
            isVideoPlaying = false;
            video_loading.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
            btnPlay.show();
            btnPlay.setImageResource(R.drawable.play_video_ic);
        }
    }

    private void resumePlayer(){
        if (player !=null){
            player.setPlayWhenReady(true);
            player.setVolume(1);
            player.play();
            isVideoPlaying = true;
            btnPlay.setVisibility(View.GONE);
            btnPlay.hide();
            btnPlay.setImageResource(R.drawable.pause_ic);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void moveToComments(String postId){
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}