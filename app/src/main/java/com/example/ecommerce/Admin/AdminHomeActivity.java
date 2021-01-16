package com.example.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecommerce.Buyer.HomeActivity;
import com.example.ecommerce.Buyer.MainActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.Seller.SellerProductCategoryActivity;

public class AdminHomeActivity extends AppCompatActivity
{
    private Button LogoutBtn, CheckOrdersBtn, maintainProductsBtn, checkApproveProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn = (Button) findViewById(R.id.maintain_products_btn);
        checkApproveProductsBtn = (Button) findViewById(R.id.check_approve_orders_btn);


        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);


            }
        });


        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);

            }
        });

        checkApproveProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminHomeActivity.this, AdminCheckNewProductsActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);

            }
        });


    }
}
