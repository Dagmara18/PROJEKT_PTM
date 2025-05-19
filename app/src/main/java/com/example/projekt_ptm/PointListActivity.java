package com.example.projekt_ptm;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PointListActivity extends AppCompatActivity {

    private Database dbHelper;
    private LinearLayout pointsContainer;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);

        dbHelper = new Database(this);
        pointsContainer = findViewById(R.id.pointsContainer);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());  // Powrót do MainActivity

        loadPoints();
    }

    private void loadPoints() {
        pointsContainer.removeAllViews();
        Cursor cursor = dbHelper.getAllPoints();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Brak zapisanych punktów", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("nazwa"));
            String desc = cursor.getString(cursor.getColumnIndex("opis"));
            String imageBase64 = cursor.getString(cursor.getColumnIndex("zdjecie"));

            LinearLayout pointRow = new LinearLayout(this);
            pointRow.setOrientation(LinearLayout.HORIZONTAL);
            pointRow.setPadding(8, 16, 8, 16);
            pointRow.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvName = new TextView(this);
            tvName.setText(name);
            tvName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            tvName.setPadding(8, 0, 8, 0);

            TextView tvDesc = new TextView(this);
            tvDesc.setText(desc);
            tvDesc.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));
            tvDesc.setPadding(8, 0, 8, 0);

            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            imageView.setPadding(8, 0, 8, 0);

            if (imageBase64 != null && !imageBase64.isEmpty()) {
                byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decodedImage);
            }

            pointRow.addView(tvName);
            pointRow.addView(tvDesc);
            pointRow.addView(imageView);

            pointsContainer.addView(pointRow);
        }

        cursor.close();
    }
}
