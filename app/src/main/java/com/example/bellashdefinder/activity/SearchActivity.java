package com.example.bellashdefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.adapter.AnswerListAdapter;
import com.example.bellashdefinder.model.Answer;
import com.example.bellashdefinder.util.DataSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    public static final String KEY_CATEGORY = "category";
    private int questionIndex = 0;
    private Map<String, Answer> answerMap = new HashMap<>();
    private Map<String, List<Answer>> questionMap = new HashMap<>();
    private String category;

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

        Toast.makeText(this,
                answerMap.get(DataSet.questionList[0]) +
                        "\n" + answerMap.get(DataSet.questionList[1]) +
                        "\n" + answerMap.get(DataSet.questionList[2]),
                Toast.LENGTH_SHORT).show();

        // TODO: 10/27/2019 find result and pass category and product to result activity
        Intent i = new Intent(this, ResultActivity.class);
        startActivity(i);

        finish();
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
}
