package github.com.myapplication.activity.details;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import github.com.myapplication.R;
import github.com.myapplication.activity.add.product.AddProductToList;
import github.com.myapplication.firebase.helper.DocketInfoCrud;
import github.com.myapplication.model.Docket;

public class DocketDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String docketInfoUID ="";
    private String docketUID ="";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docket_details);

        Intent detailsIntent = getIntent();
        Docket docket = (Docket) detailsIntent.getSerializableExtra("docket");
        docketUID = docket.getUid();
        docketInfoUID = docket.getDocket_info_uid();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleToolbar = findViewById(R.id.toolbar_title);
        titleToolbar.setText(docket.getDocketName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = new Intent(this, AddProductToList.class);
        intent.putExtra("docketInfoUID",docketInfoUID);
        intent.putExtra("docketUID", docketUID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_product_in_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle productNeed = new Bundle();
        productNeed.putString("docketInfoUID",docketInfoUID);
        productNeed.putString("docketUID",docketUID);

        ProductNeedBuy fragmentProductNeedBuy = new ProductNeedBuy();
        fragmentProductNeedBuy.setArguments(productNeed);

        ProductsPaid fragmentProductsPaid = new ProductsPaid();
        fragmentProductsPaid.setArguments(productNeed);

        adapter.addFragment(fragmentProductNeedBuy, "Купить");
        adapter.addFragment(fragmentProductsPaid, "Куплено");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_popup_menu, menu);
        return true;
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
