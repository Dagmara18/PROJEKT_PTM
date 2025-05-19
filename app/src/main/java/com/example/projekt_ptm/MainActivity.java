package com.example.projekt_ptm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private MapView mapView;
    private IMapController mapController;
    private LocationManager locationManager;
    private Marker userLocationMarker;
    private EditText etPointName, etPointDesc;
    private Database dbHelper;
    private String encodedImage = "";
    private GeoPoint lastUserLocation;

    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_main);

        dbHelper = new Database(this);

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(16.0);

        etPointName = findViewById(R.id.etPointName);
        etPointDesc = findViewById(R.id.etPointDesc);

        Button btnAddPoint = findViewById(R.id.btnAddPoint);
        Button btnViewPoints = findViewById(R.id.btnViewPoints);

        btnAddPoint.setOnClickListener(v -> {
            if (lastUserLocation != null) {
                openCamera();
            } else {
                Toast.makeText(this, "Lokalizacja nieznana. Proszę zaczekać...", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewPoints.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PointListActivity.class);
            startActivity(intent);
        });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                        encodedImage = encodeImage(imageBitmap);
                        addPoint();
                    }
                }
        );

        requestPermissions();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            startLocationUpdates();
            loadMarkersFromDatabase(); // Wczytanie markerów przy starcie aplikacji
        }
    }

    private void startLocationUpdates() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            lastUserLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
            updateUserLocationMarker(lastUserLocation);

            // Zawsze zoomuj do lokalizacji użytkownika po jej ustaleniu
            mapController.setZoom(18.0);
            mapController.animateTo(lastUserLocation);
        }
    };

    private void updateUserLocationMarker(GeoPoint location) {
        if (userLocationMarker == null) {
            userLocationMarker = new Marker(mapView);
            userLocationMarker.setIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_mylocation));
            userLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            mapView.getOverlays().add(userLocationMarker);
        }
        userLocationMarker.setPosition(location);
        mapView.invalidate();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "Brak aplikacji aparatu", Toast.LENGTH_SHORT).show();
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void addPoint() {
        String pointName = etPointName.getText().toString().trim();
        String description = etPointDesc.getText().toString().trim();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (pointName.isEmpty()) {
            Toast.makeText(this, "Podaj nazwę punktu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lastUserLocation == null) {
            Toast.makeText(this, "Lokalizacja nieznana. Spróbuj ponownie.", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.insertPoint(pointName, description, timestamp, lastUserLocation.getLatitude(), lastUserLocation.getLongitude(), encodedImage);

        if (id != -1) {
            Toast.makeText(this, "Punkt zapisany do bazy", Toast.LENGTH_SHORT).show();
            addMarkerToMap(pointName, lastUserLocation);
        } else {
            Toast.makeText(this, "Błąd zapisu do bazy", Toast.LENGTH_SHORT).show();
        }

        encodedImage = "";
        etPointName.setText("");
        etPointDesc.setText("");
    }

    private void addMarkerToMap(String name, GeoPoint location) {
        Marker marker = new Marker(mapView);
        marker.setPosition(location);
        marker.setTitle(name);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    private void loadMarkersFromDatabase() {
        Cursor cursor = dbHelper.getAllPoints();
        while (cursor.moveToNext()) {
            double lat = cursor.getDouble(cursor.getColumnIndex("x"));
            double lon = cursor.getDouble(cursor.getColumnIndex("y"));
            String name = cursor.getString(cursor.getColumnIndex("nazwa"));

            GeoPoint location = new GeoPoint(lat, lon);
            addMarkerToMap(name, location);
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
