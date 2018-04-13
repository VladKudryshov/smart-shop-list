package github.com.myapplication.util;

import java.util.HashMap;

import github.com.myapplication.model.Shop;

/**
 * Created by Владислав on 22.03.2018.
 */

public class ShopMapper {
    public static Shop mapper(HashMap hashMap){
        Shop shop = new Shop();
        shop.setNameShop(hashMap.get("name").toString());
        return shop;
    }
}
