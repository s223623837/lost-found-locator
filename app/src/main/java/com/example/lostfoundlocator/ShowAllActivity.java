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
