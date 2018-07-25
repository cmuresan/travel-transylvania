package com.cristianmmuresan.traveltransylvania.ui.place;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;
import com.cristianmmuresan.traveltransylvania.databinding.ActivityPlaceBinding;
import com.squareup.picasso.Picasso;

public class PlaceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = PlaceActivity.class.getSimpleName();
    public static final String PLACE_ID_KEY = "placeIdKey";
    private PlaceViewModel viewModel;
    private ActivityPlaceBinding binding;
    private PlaceEntry placeEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        int placeId = getIntent().getIntExtra(PLACE_ID_KEY, 1);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_place);
        setupViewModel(placeId);
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.readMore.setOnClickListener(this);
        binding.checkMap.setOnClickListener(this);
        binding.favorite.setOnClickListener(this);
        binding.share.setOnClickListener(this);
        binding.directionsFab.setOnClickListener(this);
    }

    private void setupViewModel(final int placeId) {
        viewModel = ViewModelProviders.of(this, new PlaceViewModelFactory(getApplication(), placeId)).get(PlaceViewModel.class);
        viewModel.getPlace().observe(this, new Observer<PlaceEntry>() {
            @Override
            public void onChanged(@Nullable PlaceEntry placeEntry) {
                if (placeEntry == null) {
                    Log.d(TAG, "onChanged: NULL");
                    return;
                }
                updateUi(placeEntry);
            }
        });
    }

    private void updateUi(@NonNull PlaceEntry placeEntry) {
        this.placeEntry = placeEntry;
        binding.description.setText(placeEntry.getDescription());
        Picasso.get().load(placeEntry.getImageResources()).placeholder(R.drawable.ic_image).into(binding.image);
        binding.image.setImageResource(placeEntry.getImageResources());
        binding.mainCollapsing.setTitle(placeEntry.getName());
        if (placeEntry.isFavorite()) {
            binding.favorite.setImageResource(R.drawable.ic_favorite);
        } else {
            binding.favorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_more:
                viewModel.readMore(placeEntry.getReadMoreLink());
                break;
            case R.id.check_map:
                viewModel.checkMap(placeEntry.getLatitude(), placeEntry.getLongitude(), placeEntry.getZoom());
                break;
            case R.id.favorite:
                viewModel.favorite(placeEntry);
                break;
            case R.id.share:
                viewModel.share(placeEntry.toString());
                break;
            case R.id.directions_fab:
                viewModel.getDirections(placeEntry.getLatitude(), placeEntry.getLongitude());
                break;
        }
    }
}
