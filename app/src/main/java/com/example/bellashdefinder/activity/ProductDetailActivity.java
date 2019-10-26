package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.AnswerListAdapter;
import com.example.bellashdefinder.model.Answer;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.DataSet;
import com.example.bellashdefinder.util.NumberUtil;

import java.io.IOException;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String KEY_PRODUCT = "product";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    private AppCompatEditText edtName, edtPrice;
    private AppCompatSpinner categorySpinner;
    private RecyclerView rvSkinType, rvFinishFits, rvShadeFamily;
    private AppCompatImageView imgView;

    private AnswerListAdapter skinTypeAdapter, finishFitsAdapter, shadeFamilyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        edtName = findViewById(R.id.edt_name);
        edtPrice = findViewById(R.id.edt_price);
        categorySpinner = findViewById(R.id.spn_category);
        rvSkinType = findViewById(R.id.rv_skin_type);
        rvFinishFits = findViewById(R.id.rv_finish_fits);
        rvShadeFamily = findViewById(R.id.rv_shade_family);
        imgView = findViewById(R.id.img_v_product);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_PRODUCT)) {
            Product product = (Product) b.get(KEY_PRODUCT);

            assert product != null;

            edtName.setText(product.getName());
            edtPrice.setText(String.valueOf(NumberUtil.getOneDigit(product.getPrice())));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                DataSet.categoryList);
        categorySpinner.setAdapter(adapter);

        skinTypeAdapter = new AnswerListAdapter(DataSet.getSkinTypeList());
        rvSkinType.setAdapter(skinTypeAdapter);

        finishFitsAdapter = new AnswerListAdapter(DataSet.getFinishFitsList());
        rvFinishFits.setAdapter(finishFitsAdapter);

        shadeFamilyAdapter = new AnswerListAdapter(DataSet.getShadeFamilyList());
        rvShadeFamily.setAdapter(shadeFamilyAdapter);
    }

    public void saveProduct(View v) {
        Answer skinTypeAnswer = skinTypeAdapter.getSelectedAnswer();
        Answer finishFitAnswer = finishFitsAdapter.getSelectedAnswer();
        Answer shadeFamilyAnswer = shadeFamilyAdapter.getSelectedAnswer();

        if (skinTypeAnswer == null || finishFitAnswer == null || shadeFamilyAnswer == null) {
            Toast.makeText(this, "Please answer all question!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this,
                skinTypeAnswer.getAnswer() +
                        "\n" + finishFitAnswer.getAnswer() +
                        "\n" + shadeFamilyAnswer.getAnswer(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data.getExtras() != null) {
                Bundle b = data.getExtras();
                Bitmap bitmap = (Bitmap) b.get("data");
                imgView.setImageBitmap(bitmap);
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imgView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void dispatchTakePictureIntent(View v) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Checking can current activity handle the intent?
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void dispatchPickImageIntent(View v) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, REQUEST_PICK_IMAGE);
    }
}
