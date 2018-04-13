package github.com.myapplication.activity.add.product;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import github.com.myapplication.R;
import github.com.myapplication.adapters.PlanetAdapter;
import github.com.myapplication.firebase.helper.DocketCrud;
import github.com.myapplication.firebase.helper.DocketInfoCrud;
import github.com.myapplication.model.Product;
import github.com.myapplication.model.ProductInfo;

public class AddProductToList extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CatalogProducts catalogProductsFragment = new CatalogProducts();
    private PopularProducUser popularProducUserFragment = new PopularProducUser();
    private int lastIndex = 0;
    private String docketInfoUID;
    private String docketUID;
    RecyclerView recyclerView;
    Intent intent;
    PlanetAdapter adapter;

    private ArrayList<String> planetList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_list);
        Intent detailsIntent = getIntent();
        docketInfoUID = (String) detailsIntent.getSerializableExtra("docketInfoUID");
        docketUID = (String) detailsIntent.getSerializableExtra("docketUID");

        viewPager = (ViewPager) findViewById(R.id.viewpager_product);
        setupViewPager(viewPager);

        SearchView searchView = (SearchView) findViewById(R.id.sv);
        createSearchView(searchView);

        tabLayout = (TabLayout) findViewById(R.id.tabs_add_product);
        tabLayout.setupWithViewPager(viewPager);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_product);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_action_back_red);
        upArrow.setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void createSearchView(SearchView searchView) {
        searchView.setIconified(false);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        final ImageView searchbutton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchbutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_add_action));
        searchbutton.setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_ATOP);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorGray));
        searchEditText.setHint("Добавить продукт");
        if (searchEditText.getText().equals("")){
            viewPager.setVisibility(View.VISIBLE);
        }
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String searchStr = s.toString();
                if (!searchStr.isEmpty())viewPager.setVisibility(View.INVISIBLE);
                else viewPager.setVisibility(View.VISIBLE);

                setAdapter(searchStr);
            }
        });
    }

    private void setAdapter(final String searchStr){
        planetList.clear();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String product = snapshot.child("name").getValue(String.class);
                    assert product != null;
                    if(product.toLowerCase().contains(searchStr.toLowerCase())){
                        planetList.add(product);
                    }
                }
                if (planetList.isEmpty()){
                    planetList.add(searchStr);
                }
                adapter = new PlanetAdapter(planetList, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle args = new Bundle();
        args.putString("docketInfoUID", docketInfoUID);
        catalogProductsFragment.setArguments(args);

        adapter.addFragment(popularProducUserFragment, "Часто используемые");
        adapter.addFragment(catalogProductsFragment, "Каталог продуктов");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DocketInfoCrud.addProduct(docketUID, docketInfoUID, adapter.getProducts());
        super.onBackPressed();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
