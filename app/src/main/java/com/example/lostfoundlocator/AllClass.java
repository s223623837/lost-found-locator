package com.example.lostfoundlocator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.lostfoundlocator.database.DBHelper;
import com.example.lostfoundlocator.models.Advert;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AllClass {
}
package com.example.lostfoundlocator;

        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.Button;

        import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button createNewAdvertButton, showAll, showAllItemsOnMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNewAdvertButton = findViewById(R.id.createNewAdvertButton);
        showAll = findViewById(R.id.showAll);
        showAllItemsOnMapButton = findViewById(R.id.showAllItemsOnMapButton);

        createNewAdvertButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateAdvertActivity.class)));

        showAll.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ShowAllActivity.class))); // Assume PickPlaceActivity is implemented

        showAllItemsOnMapButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ShowMapActivity.class)));
    }
}
package com.example.lostfoundlocator;

        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.Toast;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import com.example.lostfoundlocator.database.DBHelper;
        import com.example.lostfoundlocator.models.Advert;

public class CreateAdvertActivity extends AppCompatActivity {

    private static final int PICK_PLACE_REQUEST_CODE = 2;

    private Spinner postTypeSpinner;
    private EditText nameEditText, phoneEditText, descriptionEditText, locationEditText;
    private Button getCurrentLocation, saveAdvertButton;
    private double pickedLatitude;
    private double pickedLongitude;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        postTypeSpinner = findViewById(R.id.postTypeSpinner);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        locationEditText = findViewById(R.id.locationEditText);
        getCurrentLocation = findViewById(R.id.getCurrentLocation);
        saveAdvertButton = findViewById(R.id.saveAdvertButton);

        dbHelper = new DBHelper(this);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.post_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postTypeSpinner.setAdapter(adapter);

        getCurrentLocation.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAdvertActivity.this, PickPlaceActivity.class);
            startActivityForResult(intent, PICK_PLACE_REQUEST_CODE);
        });

        saveAdvertButton.setOnClickListener(v -> saveAdvert());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PLACE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String pickedLocation = data.getStringExtra("pickedLocation");
            String address = data.getStringExtra("address");
            locationEditText.setText(pickedLocation + "\n" + address);

            // Parse latitude and longitude from the pickedLocation string
            String[] latLng = pickedLocation.replace("Lat: ", "").replace("Lng: ", "").split(", ");
            pickedLatitude = Double.parseDouble(latLng[0]);
            pickedLongitude = Double.parseDouble(latLng[1]);
        }
    }

    private void saveAdvert() {
        // Retrieve user input
        String postType = postTypeSpinner.getSelectedItem().toString();
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String location = locationEditText.getText().toString();

        // Create a new Advert object
        Advert advert = new Advert(postType, name, phone, description, location, pickedLatitude, pickedLongitude);

        // Save the advert to the database
        dbHelper.addAdvert(advert);

        // Show a confirmation message
        Toast.makeText(this, "Advert saved!", Toast.LENGTH_SHORT).show();

        // Clear fields
        postTypeSpinner.setSelection(0);
        nameEditText.setText("");
        phoneEditText.setText("");
        descriptionEditText.setText("");
        locationEditText.setText("");

        // Redirect to ShowMapActivity
        Intent intent = new Intent(CreateAdvertActivity.this, ShowAllActivity.class);
        startActivity(intent);
    }
}
package com.example.lostfoundlocator;

        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Address;
        import android.location.Geocoder;
        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import androidx.fragment.app.FragmentActivity;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.GoogleApiAvailability;
        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;

        import java.io.IOException;
        import java.util.List;
        import java.util.Locale;

public class PickPlaceActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private Button confirmLocationButton;
    private EditText pickedLocationEditText;
    private LatLng pickedLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_place);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        confirmLocationButton = findViewById(R.id.confirmLocationButton);
        pickedLocationEditText = findViewById(R.id.pickedLocationEditText);

        if (checkPlayServices()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }

        confirmLocationButton.setOnClickListener(v -> {
            if (pickedLatLng != null) {
                String pickedLocation = "Lat: " + pickedLatLng.latitude + ", Lng: " + pickedLatLng.longitude;
                String address = getAddressFromLatLng(pickedLatLng);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("pickedLocation", pickedLocation);
                resultIntent.putExtra("address", address);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(PickPlaceActivity.this, "Please pick a location on the map", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        mMap.setOnMapClickListener(latLng -> {
            pickedLatLng = latLng;
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Picked Location"));
            pickedLocationEditText.setText("Lat: " + latLng.latitude + ", Lng: " + latLng.longitude);
        });
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                        } else {
                            Toast.makeText(PickPlaceActivity.this, "Failed to get location", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Address not found";
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
package com.example.lostfoundlocator;

        import android.os.Bundle;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import androidx.activity.EdgeToEdge;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.graphics.Insets;
        import androidx.core.view.ViewCompat;
        import androidx.core.view.WindowInsetsCompat;

        import com.example.lostfoundlocator.database.DBHelper;
        import com.example.lostfoundlocator.models.Advert;

        import java.util.List;

public class ShowAllActivity extends AppCompatActivity {

    private ListView advertsListView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_all);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        advertsListView = findViewById(R.id.advertsListView);
        dbHelper = new DBHelper(this);

        List<Advert> adverts = dbHelper.getAllAdverts();

        ArrayAdapter<Advert> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adverts);
        advertsListView.setAdapter(adapter);
    }
}
package com.example.lostfoundlocator;

        import android.os.Bundle;

        import androidx.activity.EdgeToEdge;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.graphics.Insets;
        import androidx.core.view.ViewCompat;
        import androidx.core.view.WindowInsetsCompat;

        import com.example.lostfoundlocator.database.DBHelper;
        import com.example.lostfoundlocator.models.Advert;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;

        import java.util.List;

public class ShowMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Advert> adverts = dbHelper.getAllAdverts();
        for (Advert advert : adverts) {
            LatLng location = new LatLng(advert.getLatitude(), advert.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(advert.getName()).snippet(advert.getDescription()));
        }
        if (!adverts.isEmpty()) {
            LatLng firstLocation = new LatLng(adverts.get(0).getLatitude(), adverts.get(0).getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10));
        }
    }
}
package com.example.lostfoundlocator.models;

public class Advert {
    private String postType;
    private String name;
    private String phone;
    private String description;
    private String location;
    private double latitude;
    private double longitude;

    // Constructor
    public Advert(String postType, String name, String phone, String description, String location, double latitude, double longitude) {
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    @Override
    public String toString() {
        return "Post Type: " + postType + "\nName: " + name + "\nPhone: " + phone + "\nDescription: " + description + "\nLocation: " + location;
    }
}
package com.example.lostfoundlocator.database;

        import android.annotation.SuppressLint;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import com.example.lostfoundlocator.models.Advert;

        import java.util.ArrayList;
        import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LostFoundLocator.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ADVERTS = "adverts";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_POST_TYPE = "postType";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ADVERTS_TABLE = "CREATE TABLE " + TABLE_ADVERTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_POST_TYPE + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_LATITUDE + " REAL,"
                + COLUMN_LONGITUDE + " REAL" + ")";
        db.execSQL(CREATE_ADVERTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVERTS);
        onCreate(db);
    }

    // Method to add an advert
    public void addAdvert(com.example.lostfoundlocator.models.Advert advert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_POST_TYPE, advert.getPostType());
        values.put(COLUMN_NAME, advert.getName());
        values.put(COLUMN_PHONE, advert.getPhone());
        values.put(COLUMN_DESCRIPTION, advert.getDescription());
        values.put(COLUMN_LOCATION, advert.getLocation());
        values.put(COLUMN_LATITUDE, advert.getLatitude());
        values.put(COLUMN_LONGITUDE, advert.getLongitude());

        db.insert(TABLE_ADVERTS, null, values);
        db.close();
    }

    // Method to delete an advert
    public void deleteAdvert(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADVERTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Method to get all adverts
    public List<com.example.lostfoundlocator.models.Advert> getAllAdverts() {
        List<com.example.lostfoundlocator.models.Advert> advertList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ADVERTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") com.example.lostfoundlocator.models.Advert advert = new com.example.lostfoundlocator.models.Advert(
                        cursor.getString(cursor.getColumnIndex(COLUMN_POST_TYPE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE))
                );
                advertList.add(advert);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return advertList;
    }
}
