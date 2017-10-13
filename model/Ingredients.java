package com.galihpw.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by hidayatasep43 on 8/23/2017.
 */

public class Ingredients implements Parcelable{

    private int mQuantity;
    private String mMeasure;
    private String mIngredient;

    public Ingredients() {}

    public Ingredients(JSONObject object){
        try {
            mQuantity = object.getInt("quantity");
            mMeasure = object.getString("measure");
            mIngredient = object.getString("ingredient");
        } catch (JSONException e) {
            Timber.e(e.toString());
        }
    }

    public Ingredients(Parcel in) {
        mQuantity = in.readInt();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String measure) {
        mMeasure = measure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String ingredient) {
        mIngredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mQuantity);
        parcel.writeString(mMeasure);
        parcel.writeString(mIngredient);
    }
}
