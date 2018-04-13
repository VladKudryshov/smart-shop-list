package github.com.myapplication.firebase.helper;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import github.com.myapplication.R;
import github.com.myapplication.activity.add.product.CatalogProducts;
import github.com.myapplication.model.Product;
import github.com.myapplication.model.ProductInfo;
import github.com.myapplication.util.ProductInfoMapper;

public class ProductCrud {
    private static FirebaseListOptions<Product> options;
    private static FirebaseListAdapter<Product> fadapter;
    private static LifecycleOwner life;
    private static Context context;
    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private static List<ProductInfo> list = new ArrayList<>();

    public ProductCrud(LifecycleOwner life) {
        this.life = life;
    }

    public static void createProducts() {
        database.child("products").push().setValue(new Product("Молоко"));
        database.child("products").push().setValue(new Product("Хлеб"));
        database.child("products").push().setValue(new Product("Виски"));
        database.child("products").push().setValue(new Product("Кола"));
        database.child("products").push().setValue(new Product("Сыр"));
        database.child("products").push().setValue(new Product("Дошик"));
        database.child("products").push().setValue(new Product("Батон"));
    }

    public FirebaseListAdapter<Product> getAllProducts() {
        options = createListOptionsFirebase();
        fadapter = new FirebaseListAdapter<Product>(options) {
            @Override
            protected void populateView(View v, Product model, int position) {
                TextView titleDocket = v.findViewById(R.id.title_product);
                titleDocket.setText(model.getName());
            }
        };

        return fadapter;
    }

    private static FirebaseListOptions<Product> createListOptionsFirebase() {
        return new FirebaseListOptions.Builder<Product>()
                .setLayout(R.layout.product)
                .setQuery(createQuery(), Product.class)
                .setLifecycleOwner(life)
                .build();
    }

    private static Query createQuery() {
        return database.child("products");
    }

    public void getProducts(String uid, final CatalogProducts catalogProducts) {

        database.child("docketsInfo").child(uid).child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<HashMap> data = (List<HashMap>)snapshot.getValue();
                if (data==null)return;
                List<ProductInfo> infoList = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    ProductInfo product = ProductInfoMapper.mapper(data.get(i));

                    infoList.add(product);
                }
                catalogProducts.setProducts(infoList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
