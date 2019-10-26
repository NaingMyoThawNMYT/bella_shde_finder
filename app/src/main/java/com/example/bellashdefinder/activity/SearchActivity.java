package com.example.bellashdefinder.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bellashdefinder.R;

public class SearchActivity extends AppCompatActivity {
    public static final String KEY_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_CATEGORY)) {
            String category = b.getString(KEY_CATEGORY);

            if (TextUtils.isEmpty(category)) {
                return;
            }

            Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
        }
    }
}
