package github.com.myapplication.firebase.helper;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

import github.com.myapplication.R;
import github.com.myapplication.model.ProductInfo;

/**
 * Created by Владислав on 04.03.2018.
 */

public class DocketInfoCrud {

    private static FirebaseListOptions<ProductInfo> options;
    private static FirebaseListAdapter<ProductInfo> fadapter;
    private static int quantityProductNotPaid = 0;
    private static LifecycleOwner life;
    private static Context context;
    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    public DocketInfoCrud() {
    }

    public DocketInfoCrud(LifecycleOwner life) {
        DocketInfoCrud.life = life;
    }


    public static void addProduct(final String uid, final String infouid, final List<ProductInfo> list) {
        if (list.isEmpty()) return;
        database.child("docketsInfo").child(infouid).child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int quantity = 0;
                List<ProductInfo> products = new ArrayList<>();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    ProductInfo value = data.getValue(ProductInfo.class);
                    assert value != null;
                    quantity+=value.getQuantity();
                    products.add(value);
                }
                products.addAll(list);
                database.child("docketsInfo").child(infouid).child("products").setValue(products);

                DocketCrud.setQuantity(uid, quantity);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void payProduct(String uid, Integer position, boolean paid) {
        database.child("docketsInfo")
                .child(uid)
                .child("products")
                .child(String.valueOf(position))
                .child("paid")
                .setValue(paid);
    }

    public static void deleteProductFromDocket(String uid, int position){
        database.child("docketsInfo")
                .child(uid)
                .child("products")
                .child(String.valueOf(position))
                .removeValue();
    }

    public FirebaseListAdapter<ProductInfo> getAllProducts(final String uid, final boolean paid) {
        options = createListOptionsFirebase(uid, paid, R.layout.product_for_details);
        quantityProductNotPaid = 0;
        fadapter = new FirebaseListAdapter<ProductInfo>(options) {

            @SuppressLint("ResourceAsColor")
            @Override
            protected void populateView(View v, ProductInfo model, final int position) {

                TextView titleDocket = v.findViewById(R.id.title_product_not_paid);
                titleDocket.setText(model.getName());
                TextView quantity = v.findViewById(R.id.product_quantity);
                quantity.setText(String.valueOf(model.getQuantity()));
                if (paid){
                    v.setAlpha(0.8F);
                }
                Button deleteProduct = v.findViewById(R.id.delete_product);
                deleteProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocketInfoCrud.deleteProductFromDocket(uid,position);
                    }
                });
            }
        };


        return fadapter;
    }

    private static FirebaseListOptions<ProductInfo> createListOptionsFirebase(String uid, boolean paid, int layout) {
        return new FirebaseListOptions.Builder<ProductInfo>()
                .setLayout(layout)
                .setQuery(createQuery(uid, paid), ProductInfo.class)
                .setLifecycleOwner(life)
                .build();
    }

    private static Query createQuery(String uid, boolean paid) {
        return database.child("docketsInfo").child(uid).child("products").orderByChild("paid").equalTo(paid);
    }

    public static int getQuantityProductNotPaid() {
        return quantityProductNotPaid;
    }
}
