package com.example.bellashdefinder.activity;

import android.os.Bundle;
import android.text.TextUtils;
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

    private RecyclerView rvQuestion;
    private AnswerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rvQuestion = findViewById(R.id.rv_question);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_CATEGORY)) {
            String category = b.getString(KEY_CATEGORY);

            if (TextUtils.isEmpty(category)) {
                return;
            }

            Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
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
    }

    private void saveAnswer(String question) {
        if (adapter != null) {
            answerMap.put(question, adapter.getSelectedAnswer());
        }
    }

    private void changeQuestion(final int questionIndex) {
        findViewById(R.id.btn_back).setVisibility(questionIndex <= 0 ? View.INVISIBLE : View.VISIBLE);

        ((AppCompatButton) findViewById(R.id.btn_next_or_finish)).setText(getString(questionIndex == DataSet.questionList.length - 1 ? R.string.finish : R.string.next));

        findViewById(R.id.btn_next_or_finish).setOnClickListener(new View.OnClickListener() {
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

        adapter = new AnswerListAdapter(questionMap.get(DataSet.questionList[questionIndex]));
        rvQuestion.setAdapter(adapter);
    }
}
