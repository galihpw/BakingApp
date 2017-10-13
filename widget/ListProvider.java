package com.galihpw.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.galihpw.bakingapp.R;
import com.galihpw.bakingapp.model.Ingredients;
import com.galihpw.bakingapp.util.LocalPref;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by hidayatasep43 on 9/19/2017.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<String> mArrayList;
    private Context mContext;
    //private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        mContext = context;
        //appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
         //       AppWidgetManager.INVALID_APPWIDGET_ID);
        mArrayList = new ArrayList<String>();
        populateListItem();
    }

    private void populateListItem() {
        mArrayList.clear();

        LocalPref localPref = null;

        try {
            localPref = LocalPref.getInstance();
        }catch (Exception e){
            Timber.e(e.toString());
        }

        if(localPref != null){
            Gson gson = new Gson();
            String json = localPref.getString(LocalPref.Key.LIST_INGRIDIENT);

            Type listType = new TypeToken<ArrayList<Ingredients>>(){}.getType();

            List<Ingredients> ingredientsList = gson.fromJson(json, listType);
            if(ingredientsList != null){
                for (int i = 0; i < ingredientsList.size(); i++) {
                    Ingredients ingredients = ingredientsList.get(i);
                    String content = ingredients.getIngredient() + " " + ingredients.getQuantity() + " " + ingredients.getMeasure();
                    mArrayList.add(content);
                }
            }

        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Timber.d("Data change");
        populateListItem();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.list_item_detail_recipe);
        String content = mArrayList.get(i);
        remoteView.setTextViewText(R.id.tv_detail_recipe, content);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
