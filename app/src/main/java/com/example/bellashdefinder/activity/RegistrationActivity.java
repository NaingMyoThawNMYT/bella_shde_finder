package com.example.bellashdefinder.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Customer;
import com.example.bellashdefinder.model.Order;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.storage.FirebaseDatabaseHelper;
import com.example.bellashdefinder.util.DataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public class RegistrationActivity extends AppCompatActivity {
    private DatabaseReference tableOrder, tableCustomer;

    private AppCompatEditText edtName, edtAddress, edtPhone, edtEmail;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        tableOrder = FirebaseDatabaseHelper.getTableOrder();
        tableCustomer = FirebaseDatabaseHelper.getTableCustomer();

        edtName = findViewById(R.id.edt_name);
        edtAddress = findViewById(R.id.edt_address);
        edtPhone = findViewById(R.id.edt_phone);
        edtEmail = findViewById(R.id.edt_email);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
    }

    public void registerAndOrder(View v) {
        Editable edtNameText = edtName.getText();
        Editable edtAddressText = edtAddress.getText();
        Editable edtPhoneText = edtPhone.getText();
        Editable edtEmailText = edtEmail.getText();

        if (TextUtils.isEmpty(edtNameText) ||
                TextUtils.isEmpty(edtAddressText) ||
                TextUtils.isEmpty(edtPhoneText) ||
                TextUtils.isEmpty(edtEmailText)) {
            Toast.makeText(this,
                    "Please, fill form completely!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        final Customer customer = new Customer();
        customer.setName(edtNameText.toString());
        customer.setAddress(edtAddressText.toString());
        customer.setPhone(edtPhoneText.toString());
        customer.setEmail(edtEmailText.toString());
        customer.setId(tableCustomer.push().getKey());

        dialog.show();

        tableCustomer.child(customer.getId()).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Order order = new Order();
                    order.setCustomerId(customer.getId());
                    order.setProductIdList(DataSet.getProductIdList());
                    order.setId(tableOrder.push().getKey());

                    tableOrder.child(order.getId()).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                showOrderSuccessDialog();
                            } else {
                                Toast.makeText(RegistrationActivity.this,
                                        "Fail to order!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    showOrderSuccessDialog();
                } else {
                    dialog.dismiss();

                    Toast.makeText(RegistrationActivity.this,
                            "Fail to order!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
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
