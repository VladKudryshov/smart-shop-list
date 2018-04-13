package github.com.myapplication.util;

import java.util.HashMap;

import github.com.myapplication.model.Product;
import github.com.myapplication.model.ProductInfo;

/**
 * Created by Владислав on 05.03.2018.
 */

public class ProductInfoMapper {
    public static ProductInfo mapper(HashMap hashMap){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName(hashMap.get("name").toString());
        productInfo.setPaid(Boolean.valueOf(hashMap.get("paid").toString()));
        productInfo.setQuantity(Integer.valueOf(hashMap.get("quantity").toString()));
        return productInfo;
    }
}
