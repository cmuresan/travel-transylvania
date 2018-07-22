package com.cristianmmuresan.traveltransylvania.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_PLACE = 1;
    private List<Object> places;

    public boolean isHeader(int position) {
        return getItemViewType(position) == VIEW_TYPE_HEADER;
    }

    public void setPlaces(List<Object> listWithHeaders) {
        places = listWithHeaders;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (places != null) {
            Object item = places.get(position);
            if (item instanceof String) {
                return VIEW_TYPE_HEADER;
            }
            if (item instanceof PlaceEntry) {
                return VIEW_TYPE_PLACE;
            }
        }
        return Adapter.IGNORE_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_HEADER:
                View viewHeader = inflater.inflate(R.layout.item_header, parent, false);
                return new HeaderViewHolder(viewHeader);

            case VIEW_TYPE_PLACE:
                View viewPlace = inflater.inflate(R.layout.item_place, parent, false);
                return new PlaceViewHolder(viewPlace);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                HeaderViewHolder statusViewHolder = (HeaderViewHolder) holder;
                String status = (String) places.get(position);
                statusViewHolder.bindData(status);
                break;
            case VIEW_TYPE_PLACE:
                PlaceViewHolder resortViewHolder = (PlaceViewHolder) holder;
                PlaceEntry place = (PlaceEntry) places.get(position);
                resortViewHolder.bindData(place);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return places == null ? 0 : places.size();
    }
}
