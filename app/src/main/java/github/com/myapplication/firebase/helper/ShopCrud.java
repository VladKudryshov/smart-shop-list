package github.com.myapplication.firebase.helper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.com.myapplication.activity.add.product.CatalogProducts;
import github.com.myapplication.model.Location;
import github.com.myapplication.model.ProductInfo;
import github.com.myapplication.model.Shop;
import github.com.myapplication.util.ProductInfoMapper;
import github.com.myapplication.util.ShopMapper;

/**
 * Created by Владислав on 22.03.2018.
 */

public class ShopCrud {

    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    static {
        database.keepSynced(true);
    }
/*
1721348912,
    3644745511,
    3644745510,
    1721348913,
    1721348915,
    3644745512,
    4986442025,
    5022309848,
    1721348917,
    3644745514,
    3644745513,
    1721348912
 */

/*
1548371681,
    5469365189,
    1448430768,
    1448430767,
    1448430771,
    5469365190,
    1548371681
 */

/*
  1216085798,
    3458277419,
    5404295280,
    1216085831,
    3458277414,
    3458277418,
    3458277415,
    3458277421,
    3458277416,
    3458277420,
    1216085823,
    1216085802,
    3458277422,
    3458277417,
    5404295279,
    1216085798
 */
    public static void createProducts() {
        database.child("shops").push().setValue(new Shop("Евроопт", new Location(53.6666730,53.6666730)));
        database.child("shops").push().setValue(new Shop("Алми", new Location(53.6719813,23.8630651)));
        database.child("shops").push().setValue(new Shop("Белмаркет", new Location(53.7128867,23.8152902)));
        database.child("shops").push().setValue(new Shop("Рублевский", new Location(53.6445590, 23.8583757)));
        database.child("shops").push().setValue(new Shop("Алми", new Location( 53.6436464, 23.8514241)));
        database.child("shops").push().setValue(new Shop("Торговый центр \"Веста\"", new Location( 53.6409832, 23.8445588)));
    }


    public static void getAllShops(final List<Shop> shops){
        database.child("shops").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    Shop value = data.getValue(Shop.class);
                    shops.add(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
