package com.galihpw.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by hidayatasep43 on 8/23/2017.
 */

public class Recipe implements Parcelable{

    private long mId;
    private String mName;
    private List<Steps> mStepsList;
    private List<Ingredients> mIngredientsList;

    public Recipe() {
        mStepsList = new ArrayList<>();
        mIngredientsList = new ArrayList<>();
    }

    public Recipe(JSONObject jsonObject) {
        try {
            mId = jsonObject.getLong("id");
            mName = jsonObject.getString("name");
        } catch (JSONException e) {
            Timber.e(e.toString());
        }
        mIngredientsList = new ArrayList<>();
        mStepsList = new ArrayList<>();
    }

    public Recipe(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
        mIngredientsList = new ArrayList<>();
        mStepsList = new ArrayList<>();
        in.readTypedList(mStepsList, Steps.CREATOR);
        in.readTypedList(mIngredientsList, Ingredients.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<Steps> getStepsList() {
        return mStepsList;
    }

    public void setStepsList(List<Steps> stepsList) {
        mStepsList = stepsList;
    }

    public List<Ingredients> getIngredientsList() {
        return mIngredientsList;
    }

    public void setIngredientsList(List<Ingredients> ingredientsList) {
        mIngredientsList = ingredientsList;
    }

    public void AddSteps(Steps steps){
        mStepsList.add(steps);
    }

    public void AddIngridients(Ingredients ingredients){
        mIngredientsList.add(ingredients);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mName);
        parcel.writeTypedList(mStepsList);
        parcel.writeTypedList(mIngredientsList);
    }
}
