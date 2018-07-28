package com.cristianmmuresan.traveltransylvania.ui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;
import com.cristianmmuresan.traveltransylvania.databinding.ItemPlaceWidgetBinding;

import java.util.List;

class WidgetPlacesAdapter extends RecyclerView.Adapter<WidgetPlacesAdapter.RecipeViewHolder> {
    private final Context context;
    private List<PlaceEntry> places;
    private OnPlaceItemClickListener onRecipeItemClickListener;

    WidgetPlacesAdapter(Context context) {
        this.context = context;
    }

    public void setPlaces(List<PlaceEntry> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    public void setClickListener(OnPlaceItemClickListener onRecipeItemClickListener) {
        this.onRecipeItemClickListener = onRecipeItemClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_place_widget, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {
        if (holder.binding != null) {
            holder.binding.placeName.setText(places.get(position).getName());

            holder.binding.placeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRecipeItemClickListener != null) {
                        onRecipeItemClickListener.onItemClick(holder.getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return places != null ? places.size() : 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlaceWidgetBinding binding;

        RecipeViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
