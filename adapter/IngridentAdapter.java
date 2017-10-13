package com.galihpw.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.galihpw.bakingapp.R;
import com.galihpw.bakingapp.model.Ingredients;
import com.galihpw.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hidayatasep43 on 9/15/2017.
 */

public class IngridentAdapter extends RecyclerView.Adapter<IngridentAdapter.MyViewHolder>{

    private List<Ingredients> mIngredientsList;

    public IngridentAdapter(Recipe recipe) {
        mIngredientsList = recipe.getIngredientsList();
    }

    @Override
    public IngridentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_detail_recipe, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngridentAdapter.MyViewHolder holder, int position) {
        Ingredients ingredients = mIngredientsList.get(position);
        holder.mTextView.setText((position + 1) + ". " + ingredients.getIngredient() +
                " " + ingredients.getQuantity() + " " + ingredients.getMeasure());
    }

    @Override
    public int getItemCount() {
        return mIngredientsList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_detail_recipe) TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
