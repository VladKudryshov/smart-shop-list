package github.com.myapplication.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import github.com.myapplication.R;
import github.com.myapplication.firebase.helper.DocketCrud;
import github.com.myapplication.model.Docket;

public class AddListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final EditText nameList = findViewById(R.id.name_list);

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String docketName = nameList.getText().toString();

                if(TextUtils.isEmpty(docketName.trim())) {
                    Snackbar.make(v, "Name is empty!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                Docket docket = new Docket();
                docket.setDocketName(docketName);
                docket.setQuantity(0);
                docket.setUser("current");

                DocketCrud.add(docket);

                onBackPressed();

            }
        });


    }

}
