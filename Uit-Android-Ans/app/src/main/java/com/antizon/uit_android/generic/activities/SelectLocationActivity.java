package com.antizon.uit_android.generic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.antizon.uit_android.R;
import com.antizon.uit_android.utilities.LocationHelper;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, View.OnClickListener{
    Context context;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10;
    boolean locationPermissionGranted = false;
    GoogleMap mMap;
    Location lastKnownLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final String TAG = "SelectLocationActivity";
    double longitude;
    double latitude;
    String postalCode, address, city, state, country, knownName;
    ImageView imageMyLocation;
    Button btnPickLocation;

    AutocompleteSupportFragment autocompleteSupportFragment1;
    String apiKey, selectedLocationAddress = "notSelected";
    LatLng latlng1, myLatLng;
    LocationHelper placeLocation;
    PlacesClient placesClient;
    MarkerOptions markerOptions;

    Double oldLatitude, oldLongitude;

    RelativeLayout layout_btnBack,  layout_main, layout_permissionDenied, btnSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        context = SelectLocationActivity.this;
        // Construct a FusedLocationProviderClient.
        Utilities.setCustomStatusAndNavColor(SelectLocationActivity.this, R.color.white_dash, R.color.white_dash);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        oldLatitude = getIntent().getDoubleExtra("latitude", 0);
        oldLongitude = getIntent().getDoubleExtra("longitude", 0);

        imageMyLocation = findViewById(R.id.imageMyLocation);
        btnPickLocation = findViewById(R.id.btnPickLocation);
        layout_btnBack = findViewById(R.id.layout_btnBack);
        layout_main = findViewById(R.id.layout_main);
        layout_permissionDenied = findViewById(R.id.layout_permissionDenied);
        btnSettings = findViewById(R.id.btnSettings);

        layout_btnBack.setOnClickListener(v -> onBackPressed());

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(this);
                    //Search Places Code
                    apiKey = "AIzaSyDGe23sEjTcgvDIu_afjIsmhlFSvxOCNEA";
                    if (!Places.isInitialized()) {
                        Places.initialize(SelectLocationActivity.this, apiKey);
                    }
                    placesClient = Places.createClient(SelectLocationActivity.this);

                    autocompleteSupportFragment1 = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment1);
                    if (autocompleteSupportFragment1 != null) {
                        autocompleteSupportFragment1.setHint("Search Location");
                        autocompleteSupportFragment1.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
                        autocompleteSupportFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                            @Override
                            public void onPlaceSelected(@NonNull Place place) {
                                latlng1 = place.getLatLng();
                                markerOptions = new MarkerOptions();
                                mMap.clear();
                                markerOptions.position(latlng1);
                                markerOptions.title("Location");
                                markerOptions.icon((BitmapDescriptorFactory.fromResource(R.drawable.location_ic)));
                                placeLocation = new LocationHelper(
                                        latlng1.longitude,
                                        latlng1.latitude
                                );

                                latitude = latlng1.latitude;
                                longitude = latlng1.longitude;
                                selectedLocationAddress = getAddress(SelectLocationActivity.this, placeLocation.getLatitude(), placeLocation.getLongitude());
                                placeLocation = new LocationHelper(placeLocation.getLatitude(), placeLocation.getLongitude());

                                mMap.clear();
                                moveMap(latitude, longitude, "searchPlaces");
                            }

                            @Override
                            public void onError(@NonNull Status status) {
                            }
                        });
                    }
                }
                btnPickLocation.setOnClickListener(v -> moveMap(latitude, longitude, "pickLocation"));
                imageMyLocation.setOnClickListener(v -> getDeviceLocation());

            } else {
                locationPermissionGranted = false;
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }else {
            Utilities.setWhiteBars(SelectLocationActivity.this);
            layout_main.setVisibility(View.GONE);
            layout_permissionDenied.setVisibility(View.VISIBLE);
            btnSettings.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            locationPermissionGranted = false;
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Utilities.setCustomStatusAndNavColor(SelectLocationActivity.this, R.color.white_dash, R.color.white_dash);
                locationPermissionGranted = true;
                layout_main.setVisibility(View.VISIBLE);
                layout_permissionDenied.setVisibility(View.GONE);
                updateLocationUI();
            }else {
                Utilities.setWhiteBars(SelectLocationActivity.this);
                locationPermissionGranted = false;
                layout_main.setVisibility(View.GONE);
                layout_permissionDenied.setVisibility(View.VISIBLE);
                btnSettings.setOnClickListener(v -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                });
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapLongClick(@NonNull @NotNull LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }

    @Override
    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(@NonNull @NotNull Marker marker) {

    }

    @Override
    public void onMarkerDrag(@NonNull @NotNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull @NotNull Marker marker) {
        // getting the Co-ordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //move to current position
        moveMap(latitude, longitude, "drag");
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG", "Can't find style. Error: ", e);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation();
            getMarkerPosition();
        }else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }


    }

    public void getMarkerPosition() {
        mMap.setOnCameraIdleListener(() -> {
            latitude = mMap.getCameraPosition().target.latitude;
            longitude = mMap.getCameraPosition().target.longitude;
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    postalCode = addresses.get(0).getPostalCode();
                    city = addresses.get(0).getLocality();
                    address = addresses.get(0).getAddressLine(0);
                    state = addresses.get(0).getAdminArea();
                    country = addresses.get(0).getCountryName();
                    knownName = addresses.get(0).getFeatureName();
                    Log.d("LocationDetail", postalCode + city + address + state + knownName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            LatLng latLng1 = new LatLng(latitude, longitude);
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(latLng1)
                    .title(city + " " + country));
        });

    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        float zoomLevel = 18.0f; //This goes up to 21
                        if (oldLatitude != 0 && oldLongitude != 0) {
                            //For Zoom MAp
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(oldLatitude, oldLongitude), zoomLevel));
                            //
                            myLatLng = new LatLng(oldLatitude, oldLongitude);
                        } else {
                            //For Zoom MAp
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), zoomLevel));
                            //
                            myLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        }
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(SelectLocationActivity.this, Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
                            postalCode = addresses.get(0).getPostalCode();
                            city = addresses.get(0).getLocality();
                            address = addresses.get(0).getAddressLine(0);
                            state = addresses.get(0).getAdminArea();
                            country = addresses.get(0).getCountryName();
                            knownName = addresses.get(0).getFeatureName();
                            Log.d("LocationDetail", postalCode + city + address + state + knownName);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mMap.addMarker(new MarkerOptions().position(myLatLng).draggable(false).title(city + " " + country));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, zoomLevel));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        mMap.getUiSettings().setZoomControlsEnabled(false);
                        mMap.getUiSettings().setZoomGesturesEnabled(true);
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        float zoomLevel = 18.0f; //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.852, 151.211), zoomLevel));
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }

    }

    private void moveMap(double latitude, double longitude, String callFor) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(SelectLocationActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            postalCode = addresses.get(0).getPostalCode();
            city = addresses.get(0).getLocality();
            address = addresses.get(0).getAddressLine(0);
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            knownName = addresses.get(0).getFeatureName();
            Log.d("LocationDetail", postalCode + city + address + state + knownName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(city + " " + country + state + knownName));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (callFor.equals("pickLocation")) {
            Intent intent = new Intent();
            intent.putExtra("selectedAddress", address);
            intent.putExtra("selectedLocation", city + " , " + country);
            intent.putExtra("selectedCity", city);
            intent.putExtra("selectedState", state);
            intent.putExtra("selectedCountry", country);
            intent.putExtra("selectedLatitude", latitude);
            intent.putExtra("selectedLongitude", longitude);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }

    }

    public String getAddress(Context context, double LATITUDE, double LONGITUDE) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            double finalLat = LATITUDE / 100000;
            double finalLong = LONGITUDE / 100000;
            List<Address> addresses = geocoder.getFromLocation(finalLat, finalLong, 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}