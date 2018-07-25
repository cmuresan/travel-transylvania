package com.cristianmmuresan.traveltransylvania.ui.main;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;
import com.cristianmmuresan.traveltransylvania.databinding.ItemPlaceBinding;
import com.squareup.picasso.Picasso;

class PlaceViewHolder extends RecyclerView.ViewHolder {
    private ItemPlaceBinding binding;
    private final OnPlaceItemClickListener onPlaceItemClickListener;

    PlaceViewHolder(View viewPlace, OnPlaceItemClickListener onPlaceItemClickListener) {
        super(viewPlace);
        binding = DataBindingUtil.bind(viewPlace);
        this.onPlaceItemClickListener = onPlaceItemClickListener;
    }

    void bindData(final PlaceEntry place) {
        binding.placeName.setText(place.getName());
        Picasso.get().load(place.getImageResources()).placeholder(R.drawable.ic_image).into(binding.placeImage);

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPlaceItemClickListener != null) {
                    onPlaceItemClickListener.onClick(place.getId());
                }
            }
        });
    }
}
