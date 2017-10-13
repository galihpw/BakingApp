package com.galihpw.bakingapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.galihpw.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by hidayatasep43 on 9/17/2017.
 */

public class DetailStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private String mVideoUrl = "";
    private String mDetailStep = "";
    private int count = 0;

    SimpleExoPlayer mSimpleExoPlayer;
    private long playbackPosition = 0;


    public DetailStepAdapter(Context context) {
        mContext = context;
        if(mVideoUrl.isEmpty()){
            count = 1;
        }else{
            count = 2;
        }
    }

    public DetailStepAdapter(Context context, String videoUrl, String detailStep) {
        mContext = context;
        mVideoUrl = videoUrl;
        mDetailStep = detailStep;
        if(mVideoUrl.isEmpty()){
            count = 1;
        }else{
            count = 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(count == 2){
            if(viewType == 0){
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_video_detail_recipe, parent, false);
                return new VideoViewHolder(view);
            }else{
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_detail_recipe, parent, false);
                return new TextViewHolder(view);
            }
        }else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_detail_recipe, parent, false);
            return new TextViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(count == 2){
            if(holder.getItemViewType() == 0){
                Timber.d("panggil bind view holder");

                VideoViewHolder viewHolder = (VideoViewHolder) holder;

                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                DataSource.Factory  mediaDataSourceFactory = new DefaultDataSourceFactory(mContext,
                        Util.getUserAgent(mContext, "mediaPlayerSample"),
                        (TransferListener<? super DataSource>) bandwidthMeter);

                viewHolder.mSimpleExoPlayerView.requestFocus();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
                mSimpleExoPlayer = player;

                viewHolder.mSimpleExoPlayerView.setPlayer(player);
                player.seekTo(playbackPosition);
                player.setPlayWhenReady(true);

                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mVideoUrl),
                        mediaDataSourceFactory, extractorsFactory, null, null);
                player.prepare(mediaSource);


            }else{
                TextViewHolder viewHolder = (TextViewHolder) holder;
                viewHolder.mTextView.setText(mDetailStep);
            }
        }else{
            TextViewHolder viewHolder = (TextViewHolder) holder;
            viewHolder.mTextView.setText(mDetailStep);
        }

    }

    public void releaseExoPlayer(){
        if(mSimpleExoPlayer != null){
            playbackPosition = mSimpleExoPlayer.getCurrentPosition();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public void setPlaybackPosition(long playbackPosition) {
        this.playbackPosition = playbackPosition;
        if(mSimpleExoPlayer != null){
            mSimpleExoPlayer.seekTo(this.playbackPosition);
        }
    }

    public long getPlaybackPosition() {
        return playbackPosition;
    }

    public void updateData(String videoUrl, String detailStep){
        mVideoUrl = videoUrl;
        mDetailStep = detailStep;
        if(mVideoUrl.isEmpty()){
            count = 1;
        }else{
            count = 2;
        }
        notifyDataSetChanged();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.video_player) SimpleExoPlayerView mSimpleExoPlayerView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class TextViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_detail_recipe) TextView mTextView;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
