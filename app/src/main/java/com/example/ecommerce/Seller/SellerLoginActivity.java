package com.example.ecommerce.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity
{

    private Button loginSellerBtn;
    private EditText emailInput, passwordInput;
    private Button sellerRegisterBegin;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);


        emailInput = findViewById(R.id.seller_login_email);
        passwordInput = findViewById(R.id.seller_login_password);
        loginSellerBtn = findViewById(R.id.seller_login_btn);
        sellerRegisterBegin = findViewById(R.id.seller_register_login_btn);

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();


        sellerRegisterBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerLoginActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });


        loginSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginSeller();
            }
        });


    }



    private void LoginSeller()
    {
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();



        if (!email.equals("") && !password.equals(""))
        {
            loadingBar.setTitle("Loging Seller Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            loadingBar.dismiss();
                            Toast.makeText(SellerLoginActivity.this, "You are Logged in successfully.", Toast.LENGTH_SHORT).show();

                            Intent intent =  new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                    });

        }
        else
        {
            Toast.makeText(this, "Please complete the login form.", Toast.LENGTH_SHORT).show();

        }
    }
}
