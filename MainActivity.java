package com.galihpw.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.galihpw.bakingapp.adapter.RecipeAdapter;
import com.galihpw.bakingapp.base.BaseActivity;
import com.galihpw.bakingapp.idlingresource.RecipeIdlingResource;
import com.galihpw.bakingapp.model.Ingredients;
import com.galihpw.bakingapp.model.Recipe;
import com.galihpw.bakingapp.model.Steps;
import com.galihpw.bakingapp.util.CustomItemOffset;
import com.galihpw.bakingapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements RecipeAdapter.OnRecipeItemClickListener{

    @BindView(R.id.recyler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_empty) TextView mTextViewEmpty;

    public ArrayList<Recipe> mRecipeList;
    RecipeAdapter mAdapter;

    public static final String RECIPE = "recipe";

    // The Idling Resource which will be null in production.
    @Nullable private RecipeIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if(savedInstanceState != null){
            mRecipeList = savedInstanceState.getParcelableArrayList(RECIPE);
        }else{
            mRecipeList = new ArrayList<Recipe>();
            doGetRecipeData();
        }

        //inisiate recyclee view
        mAdapter = new RecipeAdapter(mRecipeList);
        mAdapter.setListener(this);
        int column = Util.calculateNumberOfColumn(this, 132);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, column);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new CustomItemOffset(MainActivity.this, R.dimen.margin_item));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        //set up content
        setUpContent();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doGetRecipeData();
            }
        });
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE, mRecipeList);
    }


    private void setUpContent() {
        if(mRecipeList.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mTextViewEmpty.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mTextViewEmpty.setVisibility(View.GONE);
        }
    }

    private void doGetRecipeData() {
        if(Util.isNetworkConnected(this)){
            getRecipeData();
        }else{
            if(mProgressDialog.isShowing()) dismissProgress();
            if(mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    public void getRecipeData(){
        if(Util.isNetworkConnected(this)){

            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mProgressDialog.isShowing()) dismissProgress();
                            if(mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(MainActivity.this, R.string.error_label, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    Timber.d("result = " + result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mProgressDialog.isShowing()) dismissProgress();
                            if(mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);

                            if(result.isEmpty()){
                                Toast.makeText(MainActivity.this, R.string.error_label, Toast.LENGTH_SHORT).show();
                            }else{
                                try {
                                    mRecipeList.clear();
                                    mAdapter.notifyDataSetChanged();

                                    JSONArray jsonArray = new JSONArray(result);
                                    for(int i = 0; i < jsonArray.length(); i++) {
                                        //get resep
                                        final JSONObject recipeObject = jsonArray.getJSONObject(i);
                                        Recipe recipe = new Recipe(recipeObject);
                                        //get ingredients
                                        final JSONArray ingredientObject = recipeObject.getJSONArray("ingredients");
                                        for(int j = 0; j < ingredientObject.length(); j++){
                                            Ingredients ingredients = new Ingredients(ingredientObject.getJSONObject(j));
                                            recipe.AddIngridients(ingredients);
                                        }
                                        //get step
                                        final JSONArray stepObject = recipeObject.getJSONArray("steps");
                                        for(int j = 0; j < stepObject.length(); j++){
                                            Steps steps= new Steps(stepObject.getJSONObject(j));
                                            recipe.AddSteps(steps);
                                        }
                                        mRecipeList.add(recipe);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    setUpContent();

                                } catch (JSONException e) {
                                    Timber.e(e.toString());
                                }
                            }

                        }
                    });
                }
            });
        }else {
            showToast(R.string.no_internet_connection);
        }
    }

    @Override
    public void onRecipeClicked(int position) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra(RECIPE, mRecipeList.get(position));
        startActivity(intent);
    }

    /**
     * Only called from test, creates and returns a new {@link RecipeIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new RecipeIdlingResource(2000);
        }
        return mIdlingResource;
    }

}
