package com.example.ecommerce.Seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminCheckNewProductsActivity;
import com.example.ecommerce.Buyer.MainActivity;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ItemViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference unverifiedProductRef;


    private  BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item)
        {


            switch (item.getItemId())
            {
                case R.id.navigation_home:

                    Intent intentHome =  new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                    startActivity(intentHome);
                    break;


                case R.id.navigation_add:

                    Intent intentAdd =  new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                    startActivity(intentAdd);

                    break;


                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent intent =  new Intent(SellerHomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


                   break;
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemReselectedListener(mOnNavigationItemSelectedListener);

        unverifiedProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.seller_home_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unverifiedProductRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductStatus.setText(model.getProductState());
                        holder.txtProductPrice.setText("Price: " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                final  String productID = model.getPid();

                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };

                                AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Do you want to Delete this Product?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int position)
                                    {
                                        if (position == 0)
                                        {
                                            DeleteProduct(productID);

                                        }
                                        if (position == 1)
                                        {

                                        }

                                    }
                                });
                                builder.show();
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                        ItemViewHolder holder = new ItemViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }

    private void DeleteProduct(String productID)
    {


        unverifiedProductRef.child(productID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        Toast.makeText(SellerHomeActivity.this, "Item has been approved, and Available for the sale.", Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
