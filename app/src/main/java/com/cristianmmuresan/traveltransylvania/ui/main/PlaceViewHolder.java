package com.cristianmmuresan.traveltransylvania.ui.main;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;
import com.cristianmmuresan.traveltransylvania.databinding.ItemPlaceBinding;

class PlaceViewHolder extends RecyclerView.ViewHolder {
    private ItemPlaceBinding binding;

    PlaceViewHolder(View viewPlace) {
        super(viewPlace);
        binding = DataBindingUtil.bind(viewPlace);
    }

    void bindData(PlaceEntry place) {
        binding.placeName.setText(place.getName());
        binding.placeImage.setImageResource(place.getImageResources());
    }
}
