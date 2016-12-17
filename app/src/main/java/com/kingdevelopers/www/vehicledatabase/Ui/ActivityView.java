package com.kingdevelopers.www.vehicledatabase.Ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.kingdevelopers.www.vehicledatabase.R;
import com.kingdevelopers.www.vehicledatabase.Utils;
import com.kingdevelopers.www.vehicledatabase.adapter.RecAdapter;
import com.kingdevelopers.www.vehicledatabase.database.DBManager;
import com.kingdevelopers.www.vehicledatabase.model.DisView;
import com.kingdevelopers.www.vehicledatabase.model.Owner;
import com.kingdevelopers.www.vehicledatabase.model.Vehicle;

import java.util.ArrayList;

public class ActivityView extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    ArrayList<Owner> owners = new ArrayList<>();
    ArrayList<DisView> disvie = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);
        Utils.logMsg("ViewActivity OnCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        getDataFromDB();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecAdapter adapter = new RecAdapter(disvie);
        recyclerView.setAdapter(adapter);

    }

    public void getDataFromDB() {
        Utils.logMsg("ViewActivity getDataFromDB");
        owners = DBManager.getInstance().getOwnerData();
        vehicles = DBManager.getInstance().getVehicleData();
        for (int i = 0; i < owners.size(); i++) {
            disvie.add(new DisView((owners.get(i).getOwnerName()), 0));
            for (int k = 0; k < vehicles.size(); k++) {
                if (owners.get(i).getOwnerId() == vehicles.get(k).getOid()) {
                    disvie.add(new DisView((vehicles.get(k).getNumber()), 1));
                }
            }
        }
        Utils.logMsg("getDataFromDB length:-" + String.valueOf(disvie.size()));
    }

    @Override
    public void onBackPressed() {
                finish();
    }
}
