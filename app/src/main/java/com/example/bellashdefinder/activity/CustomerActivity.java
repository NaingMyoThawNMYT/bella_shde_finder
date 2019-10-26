package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.util.DataSet;

public class CustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
    }

    public void goToSearchActivity(View v) {
        switch (v.getId()) {
            case R.id.btn_foundation: {
                goToSearchActivity(DataSet.categoryList[0]);
                break;
            }
            case R.id.btn_bronzer: {
                goToSearchActivity(DataSet.categoryList[1]);
                break;
            }
            case R.id.btn_primer: {
                goToSearchActivity(DataSet.categoryList[2]);
                break;
            }
            case R.id.btn_powder: {
                goToSearchActivity(DataSet.categoryList[3]);
                break;
            }
        }
    }

    private void goToSearchActivity(String category) {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra(SearchActivity.KEY_CATEGORY, category);
        startActivity(i);
    }
}
