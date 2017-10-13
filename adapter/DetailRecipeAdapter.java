package com.galihpw.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.galihpw.bakingapp.R;
import com.galihpw.bakingapp.model.Ingredients;
import com.galihpw.bakingapp.model.Recipe;
import com.galihpw.bakingapp.model.Steps;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hidayatasep43 on 8/20/2017.
 */

public class DetailRecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Recipe mRecipe;
    private final int TYPE_INGRIDIENT = 1;
    private final int TYPE_STEP = 2;
    private Context mContext;

    static OnReceipeClickListener mListener;

    public interface OnReceipeClickListener{
        public void onReceipeItemClick(int position);
    }

    public DetailRecipeAdapter(Context context, Recipe recipe) {
        mRecipe = recipe;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_INGRIDIENT;
        }else {
            return TYPE_STEP;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_INGRIDIENT){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_ingridient, parent, false);
            return new IngridientViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_step, parent, false);
            return new StepViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == TYPE_INGRIDIENT){
            IngridientViewHolder ingridientHolder = (IngridientViewHolder) holder;
            String ingridient = "";
            for(Ingredients ingredients: mRecipe.getIngredientsList()){
                ingridient += "- " + ingredients.getQuantity() + " " + ingredients.getMeasure() + " of " + ingredients.getIngredient();
                ingridient += "\n";
            }
            ingridientHolder.mTextViewIngridient.setText(ingridient);
        }else{
            StepViewHolder stepViewHolder = (StepViewHolder) holder;
            Steps steps = mRecipe.getStepsList().get((position-1));
            stepViewHolder.mTextViewStep.setText(steps.getShortDescription());
            if(!steps.getThumbnailUrl().isEmpty()){
                Picasso
                    .with(mContext)
                    .load(steps.getThumbnailUrl())
                    .placeholder(R.drawable.ic_cake_black_36dp)
                    .error(R.drawable.ic_cake_black_36dp)
                    .into(stepViewHolder.mImageViewStep);
            }
        }
    }

    @Override
    public int getItemCount() {
        return  (mRecipe.getStepsList().size() + 1);
    }

    public void setListener(OnReceipeClickListener mListener) {
        DetailRecipeAdapter.mListener = mListener;
    }

    class IngridientViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_ingridient) TextView mTextViewIngridient;

        public IngridientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class StepViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        @BindView(R.id.tv_step) TextView mTextViewStep;
        @BindView(R.id.image_step) ImageView mImageViewStep;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener != null){
                mListener.onReceipeItemClick(getAdapterPosition());
            }
        }
    }

}
