package github.com.myapplication.activity.add.product;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;

import java.util.ArrayList;
import java.util.List;

import github.com.myapplication.R;
import github.com.myapplication.firebase.helper.ProductCrud;
import github.com.myapplication.model.Product;
import github.com.myapplication.model.ProductInfo;


public class CatalogProducts extends Fragment {


    private ListView mListDockets;
    private ProductCrud productCrud = new ProductCrud(this);
    private FirebaseListAdapter<Product> fadapter = productCrud.getAllProducts();
    private List<ProductInfo> products = new ArrayList<>();
    private int lastIndex = 0;
    private int quantity = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_products,container,false);
        mListDockets = view.findViewById(R.id.list_product);
        String docketInfoUID = getArguments().getString("docketInfoUID");
        productCrud.getProducts(docketInfoUID,this);
        mListDockets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product item = fadapter.getItem(position);
                ++quantity;
                ProductInfo productInfo = new ProductInfo();
                productInfo.setName(item.getName());
                if (products.contains(productInfo)){
                    products.get(products.indexOf(productInfo)).incQuantity();
                }else {
                    productInfo.incQuantity();
                    productInfo.setId(lastIndex++);
                    products.add(productInfo);
                }
            }
        });


        mListDockets.setAdapter(fadapter);
        return view;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<ProductInfo> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInfo> products) {
        if (products!=null) {
            this.products = products;
        } else {
            this.products = new ArrayList<>();
        }
    }
}
