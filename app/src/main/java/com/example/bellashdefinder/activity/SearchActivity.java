package com.example.bellashdefinder.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.AnswerListAdapter;
import com.example.bellashdefinder.model.Answer;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.storage.FirebaseDatabaseHelper;
import com.example.bellashdefinder.util.DataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    public static final String KEY_CATEGORY = "category";
    private int questionIndex = 0;
    private Map<String, Answer> answerMap = new HashMap<>();
    private Map<String, List<Answer>> questionMap = new HashMap<>();
    private String category;
    private DatabaseReference tableProduct;
    private ProgressDialog dialog;

    private RecyclerView rvQuestion;
    private AnswerListAdapter adapter;
    private AppCompatButton btnNextOrFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rvQuestion = findViewById(R.id.rv_question);
        btnNextOrFinish = findViewById(R.id.btn_next_or_finish);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_CATEGORY)) {
            category = b.getString(KEY_CATEGORY);
        }

        for (String question : DataSet.questionList) {
            answerMap.put(question, null);
        }

        questionMap.put(DataSet.questionList[0], DataSet.getSkinTypeList());
        questionMap.put(DataSet.questionList[1], DataSet.getFinishFitsList());
        questionMap.put(DataSet.questionList[2], DataSet.getShadeFamilyList());

        changeQuestion(questionIndex);

        tableProduct = FirebaseDatabaseHelper.getTableProduct();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
    }

    public void back(View v) {
        if (questionIndex == DataSet.questionList.length) {
            questionIndex--;
        } else {
            saveAnswer(DataSet.questionList[questionIndex]);
        }

        questionIndex--;

        changeQuestion(questionIndex);
    }

    public void next(View v) {
        if (questionIndex < DataSet.questionList.length) {
            saveAnswer(DataSet.questionList[questionIndex]);

            questionIndex++;

            changeQuestion(questionIndex);
        }
    }

    private void finish(int questionIndex) {
        saveAnswer(DataSet.questionList[questionIndex]);

        dialog.show();

        Query query = tableProduct.orderByChild("category").equalTo(category);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();

                List<Product> productList = ProductListActivity.parseProductList((Map<String, Object>) dataSnapshot.getValue());
                Product product = filterProduct(category,
                        answerMap.get(DataSet.questionList[0]).getAnswer(),
                        answerMap.get(DataSet.questionList[1]).getAnswer(),
                        answerMap.get(DataSet.questionList[2]).getAnswer(),
                        productList);

                if (product == null) {
                    Toast.makeText(SearchActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                Intent i = new Intent(SearchActivity.this, ResultActivity.class);
                i.putExtra(ProductDetailActivity.KEY_PRODUCT, product);
                startActivity(i);

                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    private void saveAnswer(String question) {
        if (adapter != null) {
            answerMap.put(question, adapter.getSelectedAnswer());
        }
    }

    private void changeQuestion(final int questionIndex) {
        btnNextOrFinish.setEnabled(answerMap.get(DataSet.questionList[questionIndex]) != null);

        findViewById(R.id.btn_back).setVisibility(questionIndex <= 0 ? View.INVISIBLE : View.VISIBLE);

        btnNextOrFinish.setText(getString(questionIndex == DataSet.questionList.length - 1 ? R.string.finish : R.string.next));

        btnNextOrFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionIndex == DataSet.questionList.length - 1) {
                    finish(questionIndex);
                } else {
                    next(v);
                }
            }
        });


        ((AppCompatTextView) findViewById(R.id.tv_question)).setText(DataSet.questionList[questionIndex]);

        adapter = new AnswerListAdapter(questionMap.get(DataSet.questionList[questionIndex]),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnNextOrFinish.setEnabled(true);
                    }
                });

        rvQuestion.setAdapter(adapter);
    }

    private Product filterProduct(String category,
                                  String skinType,
                                  String finishFit,
                                  String shadeFamily,
                                  List<Product> productList) {
        for (Product product : productList) {
            if (product.getCategory().equals(category) &&
                    product.getSkinType().equals(skinType) &&
                    product.getFinishFit().equals(finishFit) &&
                    product.getShadeFamily().equals(shadeFamily)) {
                return product;
            }
        }

        return null;
    }
}
