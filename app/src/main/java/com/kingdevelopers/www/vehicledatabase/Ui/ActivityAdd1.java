package com.kingdevelopers.www.vehicledatabase.Ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kingdevelopers.www.vehicledatabase.R;
import com.kingdevelopers.www.vehicledatabase.Utils;

public class ActivityAdd1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add1);
        setTitle("Add Owner");
        Utils.logMsg("On create");
    }
    public void onStart()
    {
        super.onStart();
        Utils.logMsg("On start");
        ((Button)findViewById(R.id.bt_add_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent();
                in.putExtra("Ow",((EditText)findViewById(R.id.et_OwnerName)).getText().toString());
                in.putExtra("Id",String.valueOf(Utils.getRowID()));
                in.putExtra("NoOfCars",((EditText)findViewById(R.id.et_NumberOfCars)).getText().toString());
                in.setClass(ActivityAdd1.this,ActivityAdd2.class);
                startActivity(in);
                finish();
            }
        });
    }
}
