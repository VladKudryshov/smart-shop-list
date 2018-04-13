package github.com.myapplication.model;

import java.util.List;

/**
 * Created by Владислав on 04.03.2018.
 */

public class DocketInfo {
    private List<ProductInfo> products;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductInfo> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInfo> products) {
        this.products = products;
    }
}
