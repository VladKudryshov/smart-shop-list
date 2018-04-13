package github.com.myapplication.firebase.helper;

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

import github.com.myapplication.R;
import github.com.myapplication.model.Docket;
import github.com.myapplication.model.ProductInfo;
import github.com.myapplication.popup.menu.PopupMenuDocket;

public class DocketCrud {

    private static DatabaseReference reference;
    private static FirebaseListOptions<Docket> options;
    private static FirebaseListAdapter<Docket> fadapter;
    private LifecycleOwner life;
    private Context context;

    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    static {
        database.keepSynced(true);
    }

    public DocketCrud(LifecycleOwner life, Context context) {
        this.life = life;
        this.context = context;
    }

    public static void add(Docket model){
        String docketsInfo_uid = database.push().getKey();

        model.setDocket_info_uid(docketsInfo_uid);

        DatabaseReference dockets = database.child("dockets");
        String dockets_uid = database.push().getKey();
        model.setUid(dockets_uid);
        dockets.child(dockets_uid).setValue(model);

    }

    public static void moveToBasket(Docket model, int position){

        fadapter.getRef(position).removeValue();
        database.child("basket").push().setValue(model);
    }

    public static void delete(int position) {
        fadapter.getRef(position).removeValue();
    }

    public FirebaseListAdapter<Docket> getAllDockets(){
        reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);

        options = createListOptionsFirebase();

        fadapter = new FirebaseListAdapter<Docket>(options) {
            @Override
            protected void populateView(View v, final Docket model, final int position) {
                Button menu = v.findViewById(R.id.show_menu);
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenuDocket.showPopupMenu(context,v,position, model);
                    }
                });

                TextView titleDocket = v.findViewById(R.id.title_docket);
                titleDocket.setText(model.getDocketName());
                TextView quantityNotPurchased = v.findViewById(R.id.list_quantity_not_purchased);
                quantityNotPurchased.setText(String.valueOf(model.getQuantity()));
            }
        };

        return fadapter;
    }

    private FirebaseListOptions<Docket> createListOptionsFirebase() {
        return new FirebaseListOptions.Builder<Docket>()
                .setLayout(R.layout.docket)
                .setQuery(createQuery(), Docket.class)
                .setLifecycleOwner(life)
                .build();
    }

    private Query createQuery() {
        return reference.child("dockets");
    }

    public static void setQuantity(final String docketUID, final int quantity){
        database.child("dockets").child(docketUID).child("quantity").setValue(quantity);
    }

    public static void updateQuantity(final String docketUID, final int quantity) {
        database.child("dockets").child(docketUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ProductInfo product = snapshot.getValue(ProductInfo.class);
                database.child("dockets").child(snapshot.getKey()).child("quantity").setValue(product.getQuantity()+quantity);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


}
