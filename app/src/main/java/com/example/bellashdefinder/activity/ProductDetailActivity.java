package com.example.bellashdefinder.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.bellashdefinder.storage.FirebaseDatabaseHelper;
import com.example.bellashdefinder.util.BitmapUtil;
import com.example.bellashdefinder.util.DataSet;
import com.example.bellashdefinder.util.NumberUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    private ProgressDialog dialog;

    private StorageReference mStorageRef;
    private Product product;

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

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_PRODUCT)) {
            product = (Product) b.get(KEY_PRODUCT);

            if (product != null) {
                edtName.setText(product.getName());
                edtPrice.setText(String.valueOf(NumberUtil.getOneDigit(product.getPrice())));

                categorySpinner.setSelection(DataSet.getSelectedCategoryPosition(product.getCategory()));
                skinTypeAdapter.setSelectedAnswer(product.getSkinType());
                finishFitsAdapter.setSelectedAnswer(product.getFinishFit());
                shadeFamilyAdapter.setSelectedAnswer(product.getShadeFamily());
            }
        }

        fetchAndSetPhoto(product == null ? null : product.getId());
    }

    public void saveProduct(View v) {
        Answer skinTypeAnswer = skinTypeAdapter.getSelectedAnswer();
        Answer finishFitAnswer = finishFitsAdapter.getSelectedAnswer();
        Answer shadeFamilyAnswer = shadeFamilyAdapter.getSelectedAnswer();

        if (skinTypeAnswer == null || finishFitAnswer == null || shadeFamilyAnswer == null) {
            Toast.makeText(this, "Please answer all question!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtName.getText() == null) {
            Toast.makeText(this, "Please enter name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtPrice.getText() == null) {
            Toast.makeText(this, "Please enter price!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (product == null) {
            product = new Product();
        }

        product.setName(edtName.getText().toString());
        product.setPrice(Double.valueOf(edtPrice.getText().toString()));
        product.setCategory((String) categorySpinner.getSelectedItem());
        product.setSkinType(skinTypeAnswer.getAnswer());
        product.setFinishFit(finishFitAnswer.getAnswer());
        product.setShadeFamily(shadeFamilyAnswer.getAnswer());

        DatabaseReference myRef = FirebaseDatabaseHelper.getTableProduct();

        if (TextUtils.isEmpty(product.getId())) {
            product.setId(myRef.push().getKey());
        }

        dialog.show();

        myRef.child(product.getId()).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    uploadImage(product.getId());
                } else {
                    dialog.dismiss();
                }
            }
        });
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

    public void discard(View v) {
        finish();
    }

    private void uploadImage(String key) {
        // Get the data from an ImageView as bytes
        imgView.setDrawingCacheEnabled(true);
        imgView.buildDrawingCache();
        Drawable drawable = imgView.getDrawable();
        if (drawable == null || TextUtils.isEmpty(key)) {
            showSuccessToastAndFinishActivity();
            return;
        }

        Bitmap bitmap;
        try {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } catch (ClassCastException e) {
            showSuccessToastAndFinishActivity();
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageReference = mStorageRef.child(key);
        UploadTask uploadTask = storageReference.putBytes(data);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    showSuccessToastAndFinishActivity();
                } else {
                    Toast.makeText(ProductDetailActivity.this,
                            "Fail to upload photo",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSuccessToastAndFinishActivity() {
        Toast.makeText(this,
                "Saved successfully.",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private void fetchAndSetPhoto(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        if (DataSet.photos.get(key) != null) {
            imgView.setImageBitmap(DataSet.photos.get(key));
            return;
        }

        mStorageRef.child(product.getId()).getBytes(Long.MAX_VALUE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    DataSet.photos.put(product.getId(), BitmapUtil.byteArrayToBitmap(task.getResult()));
                    imgView.setImageBitmap(DataSet.photos.get(product.getId()));
                }
            }
        });
    }
}
