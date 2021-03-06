package com.cristianmmuresan.traveltransylvania.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.analytics.AnalyticsConstants;
import com.cristianmmuresan.traveltransylvania.analytics.AnalyticsEventsFactory;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;
import com.cristianmmuresan.traveltransylvania.ui.place.PlaceActivity;
import com.cristianmmuresan.traveltransylvania.ui.place.PlaceViewModel;
import com.cristianmmuresan.traveltransylvania.ui.place.PlaceViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 200;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location lastLocation;
    private PlaceViewModel viewModel;
    private PlaceEntry currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        int placeId = getIntent().getIntExtra(PlaceActivity.PLACE_ID_KEY, 1);
        setupViewModel(placeId);

        setupLocationServices();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupViewModel(final int placeId) {
        viewModel = ViewModelProviders.of(this, new PlaceViewModelFactory(getApplication(), placeId)).get(PlaceViewModel.class);
        viewModel.getPlace().observe(this, new Observer<PlaceEntry>() {
            @Override
            public void onChanged(@Nullable PlaceEntry placeEntry) {
                if (placeEntry == null) {
                    return;
                }
                currentPlace = placeEntry;
                moveMap();
            }
        });
    }

    private void moveMap() {
        if (currentPlace == null) return;
        if (mMap == null) return;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(currentPlace.getLatitude(),
                        currentPlace.getLongitude()),
                currentPlace.getZoom()));
    }

    private void setupLocationServices() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
            setMyLocationEnabled();
            moveMap();
        }
    }

    @SuppressLint("MissingPermission")
    private void setMyLocationEnabled() {
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) return;
                lastLocation = location;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setMyLocationEnabled();
        }
        moveMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            AnalyticsEventsFactory.getInstance().setScreenName(this, AnalyticsConstants.SCREEN_MAPS);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
