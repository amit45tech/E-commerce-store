package com.example.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminHomeActivity;
import com.example.ecommerce.Seller.SellerProductCategoryActivity;
import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private EditText InputPhoneNumber, InputPassword;
    private TextView AdminLink, NonAdminLink, ForgetPasswordLink;

    private ProgressDialog loadingBar;

    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button)findViewById(R.id.login_btn);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NonAdminLink = (TextView) findViewById(R.id.non_admin_panel_link);
        ForgetPasswordLink = (TextView) findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);



        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);

            }
        });



        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NonAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NonAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NonAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

    }

    private void LoginUser()
    {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

         if(TextUtils.isEmpty(phone))
         {
             Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
         }
        else if(TextUtils.isEmpty(password))
         {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
         }
        else
         {
             loadingBar.setTitle("Logging In");
             loadingBar.setMessage("Please wait, while we are checking the credentials...");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();

             AllowAccessToAccount(phone, password);

         }

    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if (chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);

        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome Admin you are Logged in successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password incorrect!", Toast.LENGTH_SHORT).show();

                        }

                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Phone Number is incorrect!.", Toast.LENGTH_SHORT).show();

                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with"+ phone+"does't exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Check the  Credentials", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });


    }

    public static class ConfirmFinalOrderActivity extends AppCompatActivity
    {

        private EditText nameEditText, phoneEditText, addressEditText, cityEditText, stateEditText, pincodeEditText;
        private Button confirmOrderBtn;


        private String totalAmount = "";



        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_confirm_final_order);


            totalAmount =  getIntent().getStringExtra("Total Price");


            confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
            nameEditText = (EditText) findViewById(R.id.shippment_name);
            phoneEditText = (EditText) findViewById(R.id.shippment_phone_number);
            addressEditText = (EditText) findViewById(R.id.shippment_address);
            cityEditText = (EditText) findViewById(R.id.shippment_city);
            stateEditText = (EditText) findViewById(R.id.shippment_state);
            pincodeEditText = (EditText) findViewById(R.id.shippment_pincode);


            confirmOrderBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Check();

                }
            });

        }

        private void Check()
        {
            if (TextUtils.isEmpty(nameEditText.getText().toString()))
            {
                Toast.makeText(this, "Please provide your Full name", Toast.LENGTH_SHORT).show();

            }
            else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
            {
                Toast.makeText(this, "Please provide your phone number", Toast.LENGTH_SHORT).show();

            }
            else if (TextUtils.isEmpty(addressEditText.getText().toString()))
            {
                Toast.makeText(this, "Please provide your address", Toast.LENGTH_SHORT).show();

            }
            else if (TextUtils.isEmpty(stateEditText.getText().toString()))
            {
                Toast.makeText(this, "Please provide your state", Toast.LENGTH_SHORT).show();

            }
            else if (TextUtils.isEmpty(pincodeEditText.getText().toString()))
            {
                Toast.makeText(this, "Please provide your pincode", Toast.LENGTH_SHORT).show();

            }
            else
            {
                ConfirmOrder();

            }
        }



        private void ConfirmOrder()
        {
            final String saveCurrentTime, saveCurrentDate;

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());


            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentDate.format(calForDate.getTime());

            final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                    .child("Orders")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> ordersMap =  new HashMap<>();
            ordersMap.put("totalAmount", totalAmount);
            ordersMap.put("name", nameEditText.getText().toString());
            ordersMap.put("phone", phoneEditText.getText().toString());
            ordersMap.put("address", addressEditText.getText().toString());
            ordersMap.put("city", cityEditText.getText().toString());
            ordersMap.put("state", stateEditText.getText().toString());
            ordersMap.put("pincode", pincodeEditText.getText().toString());
            ordersMap.put("date", saveCurrentDate);
            ordersMap.put("time", saveCurrentTime);
            ordersMap.put("status", "not shipped");

            ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        FirebaseDatabase.getInstance().getReference()
                                .child("Cart List")
                                .child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(ConfirmFinalOrderActivity.this, "Your final order is placed successfully.", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                    }

                }
            });

        }


    }
}
