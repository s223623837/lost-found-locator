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
