package com.kingdevelopers.www.vehicledatabase.Ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kingdevelopers.www.vehicledatabase.Constants;
import com.kingdevelopers.www.vehicledatabase.R;
import com.kingdevelopers.www.vehicledatabase.Utils;
import com.kingdevelopers.www.vehicledatabase.database.DBManager;

import java.io.File;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent in=new Intent();
        Constants.DATABASE_ROOT_PATH=getExternalFilesDir(null).getAbsolutePath();
        (findViewById(R.id.bt_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in.setClass(ActivityMain.this,ActivityAdd1.class);
                initializeDatabase();
                startActivity(in);
                Utils.logMsg("ActivityMain bt_Add");
                finish();
            }
        });
        (findViewById(R.id.bt_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in.setClass(ActivityMain.this,ActivityView.class);
                initializeDatabase();
                startActivity(in);
                Utils.logMsg("ActivityMain bt_View");
                finish();
            }
        });
    }
    private void initializeDatabase() {
        /*creating database files and log file*/
        DBManager.initializeInstance(this);

        File databaseFolder = new File(Constants.EXTERNAL_ROOT_PATH, Constants.FILES_FOLDER);
        if (!databaseFolder.exists()) {
            databaseFolder.mkdir();
        }
    }
}
