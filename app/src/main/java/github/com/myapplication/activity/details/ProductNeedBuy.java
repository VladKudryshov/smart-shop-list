package github.com.myapplication.activity.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;

import github.com.myapplication.R;
import github.com.myapplication.firebase.helper.DocketCrud;
import github.com.myapplication.firebase.helper.DocketInfoCrud;
import github.com.myapplication.model.ProductInfo;

public class ProductNeedBuy extends Fragment {

    private ListView mlistProductsNeedPaid;
    private DocketInfoCrud docketInfoCrud = new DocketInfoCrud(this);
    private int quantity = 0;
    private String docketInfoUID;
    private String docketUID;

    public ProductNeedBuy() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_not_paid, container, false);
        mlistProductsNeedPaid = view.findViewById(R.id.list_product_not_paid);
        docketInfoUID = getArguments().getString("docketInfoUID");
        docketUID = getArguments().getString("docketUID");
        final FirebaseListAdapter<ProductInfo> fadapter = docketInfoCrud.getAllProducts(docketInfoUID, false);

        mlistProductsNeedPaid.setAdapter(fadapter);
        mlistProductsNeedPaid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductInfo item = fadapter.getItem(position);
                DocketCrud.updateQuantity(docketUID, -item.getQuantity());
                DocketInfoCrud.payProduct(getArguments().getString("docketInfoUID"), item.getId(), true);
            }
        });
        return view;
    }
}
