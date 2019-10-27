package com.example.bellashdefinder.storage;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseHelper {

    public static DatabaseReference getTableProduct() {
        return FirebaseDatabase.getInstance().getReference("table_product");
    }
}
