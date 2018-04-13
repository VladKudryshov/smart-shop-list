package github.com.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import github.com.myapplication.R;
import github.com.myapplication.model.ProductInfo;

public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder> {

    ArrayList<String> planetList;
    private List<ProductInfo> products = new ArrayList<>();
    private int lastIndex = 0;

    public List<ProductInfo> getProducts() {
        if (products!=null) {
            return products;
        }
        return new ArrayList<>(1);
    }

    public PlanetAdapter(ArrayList<String> planetList, Context context) {
        this.planetList = planetList;
    }

    @Override
    public PlanetAdapter.PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.product,parent,false);
        PlanetViewHolder viewHolder=new PlanetViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PlanetAdapter.PlanetViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductInfo productInfo = new ProductInfo();
                productInfo.setName(planetList.get(position));
                if (products.contains(productInfo)){
                    products.get(products.indexOf(productInfo)).incQuantity();
                }else {
                    productInfo.incQuantity();
                    productInfo.setId(lastIndex++);
                    products.add(productInfo);
                }
            }
        });
        holder.text.setText(planetList.get(position));
    }

    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public static class PlanetViewHolder extends RecyclerView.ViewHolder{

        protected TextView text;

        PlanetViewHolder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.title_product);
        }
    }
}