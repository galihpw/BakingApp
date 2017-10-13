package com.galihpw.bakingapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.galihpw.bakingapp.base.BaseActivity;
import com.galihpw.bakingapp.fragment.DetailStepFragment;
import com.galihpw.bakingapp.fragment.RecipeFragment;
import com.galihpw.bakingapp.model.Ingredients;
import com.galihpw.bakingapp.model.Recipe;
import com.galihpw.bakingapp.util.LocalPref;
import com.galihpw.bakingapp.widget.MyWidgetProvider;

import java.util.List;

import timber.log.Timber;


public class RecipeActivity extends BaseActivity implements RecipeFragment.OnRecipeInteractionListener {

    Recipe mRecipe;
    DetailStepFragment mDetailStepFragment;
    RecipeFragment mRecipeFragment;

    int mPosition = 0;
    long positionVideo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if(getIntent() != null){
            mRecipe = getIntent().getParcelableExtra(MainActivity.RECIPE);
        }

        if(mRecipe == null){
            finish();
        }

        if(savedInstanceState != null){
            mRecipe = savedInstanceState.getParcelable("recipe");
            mPosition = savedInstanceState.getInt("position");
            positionVideo = savedInstanceState.getLong("positionvideo", 0);

            mRecipeFragment = (RecipeFragment) getFragmentManager().findFragmentByTag("receipe");
            mRecipeFragment.setListener(this);

            mDetailStepFragment = (DetailStepFragment) getFragmentManager().findFragmentByTag("detailrecipe");
            if(mDetailStepFragment != null){
                mDetailStepFragment.UpdateDetail(mRecipe, mPosition);
                mDetailStepFragment.setPlaybackPosition(positionVideo);
            }

        }else{
            mRecipeFragment = RecipeFragment.newInstance(mRecipe);
            mRecipeFragment.setListener(this);
            getFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, mRecipeFragment, "receipe")
                    .commit();

            if(findViewById(R.id.frame_detail_layout) != null){
                mDetailStepFragment = DetailStepFragment.newInstance(mRecipe, 1);
                getFragmentManager().beginTransaction()
                        .add(R.id.frame_detail_layout, mDetailStepFragment, "detailrecipe")
                        .commit();

                mPosition = 1;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", mRecipe);
        outState.putInt("position", mPosition);

        DetailStepFragment detailStepFragment = (DetailStepFragment) getFragmentManager().findFragmentByTag("detailrecipe");
        if(detailStepFragment != null){
            outState.putLong("positionvideo", detailStepFragment.getPlaybackPosition());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.menu_add_to_widget){
            //add to shared preferences
            List<Ingredients> ingredientsList= mRecipe.getIngredientsList();
            Gson gson = new Gson();
            String json = gson.toJson(ingredientsList);
            localPref.put(LocalPref.Key.LIST_INGRIDIENT, json);

            try {
                Intent updateWidget = new Intent(this, MyWidgetProvider.class);
                updateWidget.setAction(MyWidgetProvider.UPDATE_LIST);
                PendingIntent pending = PendingIntent.getBroadcast(this, 0, updateWidget, PendingIntent.FLAG_CANCEL_CURRENT);
                pending.send();
            } catch (PendingIntent.CanceledException e) {
                Timber.e("Error widgetTrial()="+e.toString());
            }
            return true;
        }
        return false;
    }

    @Override
    public void onRecipeInteraction(int position) {
        mPosition = position;
        Timber.d("masuk");
        if(findViewById(R.id.frame_detail_layout) != null){
            if(mDetailStepFragment != null){
                mDetailStepFragment.UpdateDetail(position);
            }
        }else{
            DetailStepFragment detailStepFragment = DetailStepFragment.newInstance(mRecipe, position);
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, detailStepFragment, "detailrecipe")
                    .addToBackStack(null)
                    .commit();
        }
    }
}
