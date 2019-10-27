package com.example.bellashdefinder.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.DataSet;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void registerAndOrder(View v) {
        // TODO: 10/27/2019 register and order

        showOrderSuccessDialog();
    }

    private void showOrderSuccessDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");

        StringBuilder msg = new StringBuilder();
        for (Product product : DataSet.cart) {
            msg.append(product.getName());
        }

        msg.append(DataSet.cart.size() == 1 ? " is" : " are").append(" ordered.");

        DataSet.cart.clear();

        builder.setMessage(msg.toString());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}
