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

public class ProductsPaid extends Fragment {

    private ListView mlistProductsPaid;
    private DocketInfoCrud docketInfoCrud = new DocketInfoCrud(this);
    private String docketInfoUID;
    private String docketUID;


    public ProductsPaid() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_paid,container,false);
        mlistProductsPaid = view.findViewById(R.id.list_product_paid);
        docketInfoUID = getArguments().getString("docketInfoUID");
        docketUID = getArguments().getString("docketUID");
        final FirebaseListAdapter<ProductInfo> fadapter = docketInfoCrud.getAllProducts(docketInfoUID,true);
        mlistProductsPaid.setAdapter(fadapter);
        mlistProductsPaid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductInfo item = fadapter.getItem(position);
                DocketCrud.updateQuantity(docketUID, item.getQuantity());
                DocketInfoCrud.payProduct(getArguments().getString("docketInfoUID"), item.getId(),false);
            }
        });
        return view;
    }
}
