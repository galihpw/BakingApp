package com.galihpw.bakingapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.galihpw.bakingapp.R;
import com.galihpw.bakingapp.adapter.DetailStepAdapter;
import com.galihpw.bakingapp.base.BaseFragment;
import com.galihpw.bakingapp.model.Recipe;
import com.galihpw.bakingapp.model.Steps;
import com.galihpw.bakingapp.util.VerticalSpaceitemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class DetailStepFragment extends BaseFragment {

    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_POSITION = "position";

    private Recipe mRecipe;
    private int mPosition;

    @BindView(R.id.recyler_view) RecyclerView mRecyclerView;
    @BindView(R.id.tv_title) TextView mTextViewTitle;

    long playbackPosition = 0;

    DetailStepAdapter mAdapter;

    public DetailStepFragment() {
        // Required empty public constructor
    }

    public static DetailStepFragment newInstance(Recipe recipe, int position) {
        DetailStepFragment fragment = new DetailStepFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(ARG_RECIPE);
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    protected View onBaseCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Steps steps = mRecipe.getStepsList().get((mPosition - 1));
        mTextViewTitle.setText(steps.getShortDescription());
        mAdapter = new DetailStepAdapter(getActivity(), steps.getVideoUrl(), steps.getDescription());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new VerticalSpaceitemDecoration(24));
        mAdapter.setPlaybackPosition(playbackPosition);

        return view;
    }

    //update view
    public void UpdateDetail(Recipe recipe, int position){
        //release video
        releseExoPlayer();

        mRecipe = recipe;
        mPosition = position;
        Steps steps = recipe.getStepsList().get((position - 1));
        if(mTextViewTitle != null){
            mTextViewTitle.setText(steps.getShortDescription());
        }

        DetailStepAdapter adapter = new DetailStepAdapter(getActivity(), steps.getVideoUrl(), steps.getDescription());
        if(mRecyclerView == null){
            Timber.d("null");
            return;
        }
        mRecyclerView.setAdapter(null);
        mRecyclerView.setAdapter(adapter);
    }

    public void UpdateDetail(int position){
        releseExoPlayer();

        mPosition = position;
        Steps steps = mRecipe.getStepsList().get((position - 1));
        mTextViewTitle.setText(steps.getShortDescription());
        DetailStepAdapter adapter = new DetailStepAdapter(getActivity(), steps.getVideoUrl(), steps.getDescription());
        if(mRecyclerView == null){
            Timber.d("null");
            return;
        }
        mRecyclerView.setAdapter(null);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.releaseExoPlayer();
        playbackPosition = mAdapter.getPlaybackPosition();
    }

    public long getPlaybackPosition() {
        return playbackPosition;
    }

    public void setPlaybackPosition(long playbackPosition) {
        this.playbackPosition = playbackPosition;
    }

    public void releseExoPlayer(){
        if(mAdapter != null) mAdapter.releaseExoPlayer();
        playbackPosition = 0;
        Timber.d("release exoplayer");
    }
}
