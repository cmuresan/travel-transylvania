package com.cristianmmuresan.traveltransylvania.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
                    handleLiveDataChange(placeEntries);
            }
        });
    }

    private void handleLiveDataChange(@Nullable List<PlaceEntry> placeEntries) {
        if (placeEntries == null) {
            Log.d(TAG, "onChanged: NULL");
            return;
        }
        List<Object> listWithHeaders = viewModel.getListWithHeaders(placeEntries);
        placesAdapter.setPlaces(listWithHeaders);
        Log.d(TAG, "onChanged: size = " + placeEntries.size() +
                "  Updating list of places from LiveData in ViewModel ");
    }

    @Override
    public void onClick(int placeId) {
        viewModel.startPlaceActivity(placeId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                viewModel.setQuery(query);
                viewModel.getPlacesBySearch().observe(MainActivity.this, new Observer<List<PlaceEntry>>() {
                    @Override
                    public void onChanged(@Nullable List<PlaceEntry> placeEntries) {
                        int entries = placeEntries != null ? placeEntries.size() : 0;
                        Log.d(TAG, "onChanged: placeEntriesSize = " + entries);
                        handleLiveDataChange(placeEntries);
                    }
                });
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setScreenNameEvent(this);
    }
}
