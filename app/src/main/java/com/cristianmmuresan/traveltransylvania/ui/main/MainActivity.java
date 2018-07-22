package com.cristianmmuresan.traveltransylvania.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;
import com.cristianmmuresan.traveltransylvania.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPlaceItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SPAN_COUNT = 2;
    private ActivityMainBinding binding;
    private PlacesAdapter placesAdapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupRecyclerView();
        setupViewModel();
    }

    private void setupRecyclerView() {
        placesAdapter = new PlacesAdapter(this);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return placesAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
                    }
                }
        );

        binding.placesRecyclerView.setHasFixedSize(true);
        final int marginSize = getResources().getDimensionPixelOffset(R.dimen.places_margin);
        binding.placesRecyclerView.addItemDecoration(new MarginDecoration(marginSize, SPAN_COUNT));
        binding.placesRecyclerView.setLayoutManager(gridLayoutManager);
        binding.placesRecyclerView.setAdapter(placesAdapter);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getPlaces().observe(this, new Observer<List<PlaceEntry>>() {
            @Override
            public void onChanged(@Nullable List<PlaceEntry> placeEntries) {
                if (placeEntries == null) {
                    Log.d(TAG, "onChanged: NULL");
                    return;
                }
                List<Object> listWithHeaders = viewModel.getListWithHeaders(placeEntries);
                placesAdapter.setPlaces(listWithHeaders);
                Log.d(TAG, "onChanged: size = " + placeEntries.size() +
                        "  Updating list of places from LiveData in ViewModel ");
            }
        });
    }

    @Override
    public void onClick(int placeId) {
        viewModel.startPlaceActivity(placeId);
    }
}
