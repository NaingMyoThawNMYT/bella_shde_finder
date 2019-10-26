package com.example.bellashdefinder.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.AnswerListAdapter;
import com.example.bellashdefinder.model.Answer;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.DataSet;
import com.example.bellashdefinder.util.NumberUtil;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String KEY_PRODUCT = "product";

    private AppCompatEditText edtName, edtPrice;
    private AppCompatSpinner categorySpinner;
    private RecyclerView rvSkinType, rvFinishFits, rvShadeFamily;

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
}
