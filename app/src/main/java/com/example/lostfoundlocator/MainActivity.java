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
