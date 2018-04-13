package github.com.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;

import github.com.myapplication.R;
import github.com.myapplication.activity.details.DocketDetailsActivity;
import github.com.myapplication.firebase.helper.DocketCrud;
import github.com.myapplication.model.Docket;
import github.com.myapplication.service.MyService;

public class MainActivity extends AppCompatActivity {

    private Intent detailsItent;
    private Intent addListActivityIntent;

    private ListView mListDockets;
    private DatabaseReference reference;
    private FirebaseListOptions<Docket> options;
    private DocketCrud crudOperation = new DocketCrud(this,this);
    private FirebaseListAdapter<Docket> fadapter = crudOperation.getAllDockets();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detailsItent = new Intent(this, DocketDetailsActivity.class);
        addListActivityIntent = new Intent(this, AddListActivity.class);
        mListDockets = findViewById(R.id.listview);
        mListDockets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Docket", String.valueOf(fadapter.getItem(position)));
                detailsItent.putExtra("docket",fadapter.getItem(position));
                startActivity(detailsItent);
            }
        });

        //Service notification distance
        final Intent service = new Intent(this, MyService.class);

        FloatingActionButton fab = findViewById(R.id.fab_add_list);
        fab.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                startActivity(addListActivityIntent);
            }
        });

        mListDockets.setAdapter(fadapter);
    }

}
