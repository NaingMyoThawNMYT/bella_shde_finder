package com.example.bellashdefinder.util;

import android.graphics.Bitmap;

import com.example.bellashdefinder.model.Answer;
import com.example.bellashdefinder.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSet {

    public static final String[] categoryList = new String[]{"Foundation",
            "Bronzer",
            "Primer",
            "Powder"};

    public static final String[] questionList = new String[]{"What\'s your skin type?",
            "Which finish fits your look?",
            "Which shade family best fits your skin tone?"};

    public static final List<Product> cart = new ArrayList<>();

    public static final Map<String, Bitmap> photos = new HashMap<>();

    public static boolean isMember = false;

    public static int getSelectedCategoryPosition(String category) {
        for (int i = 0; i < categoryList.length; i++) {
            if (category.equals(categoryList[i])) {
                return i;
            }
        }

        return 0;
    }

    public static List<Answer> getSkinTypeList() {
        Answer a = new Answer();
        Answer b = new Answer();
        Answer c = new Answer();

        a.setAnswer("Dry");
        b.setAnswer("Combination");
        c.setAnswer("Oily");

        List<Answer> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);

        return list;
    }

    public static List<Answer> getFinishFitsList() {
        Answer a = new Answer();
        Answer b = new Answer();

        a.setAnswer("Matte");
        b.setAnswer("Dewy");

        List<Answer> list = new ArrayList<>();
        list.add(a);
        list.add(b);

        return list;
    }

    public static List<Answer> getShadeFamilyList() {
        Answer a = new Answer();
        Answer b = new Answer();
        Answer c = new Answer();
        Answer d = new Answer();
        Answer e = new Answer();
        Answer f = new Answer();

        a.setAnswer("Fair");
        a.setColor("#FCFBC1");
        b.setAnswer("Light");
        b.setColor("#F3D980");
        c.setAnswer("Light Medium");
        c.setColor("#F29F4D");
        d.setAnswer("Medium");
        d.setColor("#C76A06");
        e.setAnswer("Medium Deep");
        e.setColor("#C74C06");
        f.setAnswer("Deep");
        f.setColor("#4B3C2C");

        List<Answer> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        list.add(f);

        return list;
    }

    public static List<String> getCartProductIdList() {
        List<String> list = new ArrayList<>();

        for (Product product : cart) {
            list.add(product.getId());
        }

        return list;
    }
}
