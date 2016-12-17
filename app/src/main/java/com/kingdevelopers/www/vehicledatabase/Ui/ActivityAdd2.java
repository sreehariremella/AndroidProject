package com.kingdevelopers.www.vehicledatabase.Ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingdevelopers.www.vehicledatabase.R;
import com.kingdevelopers.www.vehicledatabase.Utils;
import com.kingdevelopers.www.vehicledatabase.database.DBManager;
import com.kingdevelopers.www.vehicledatabase.model.Vehicle;

import java.util.ArrayList;

public class ActivityAdd2 extends AppCompatActivity {
    LinearLayout ll;
    Vehicle v;
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    int noOfVehicle;
    String ownerName;
    int ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add2);
        setTitle("Add Vehicle's");
        noOfVehicle = Integer.parseInt(getIntent().getStringExtra("NoOfCars"));
        ownerName = getIntent().getStringExtra("Ow");
        ownerId = Integer.parseInt(getIntent().getStringExtra("Id"));
        ll = (LinearLayout) findViewById(R.id.lv_details);
        generateList(noOfVehicle);
        ((Button) findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkListData();
                finish();
            }
        });
    }

    private void generateList(int noOfVehicle) {
        for (int i = 1; i <= noOfVehicle; i++) {
            v = new Vehicle();
            v.setsNo(i);
            v.setOid(Integer.parseInt(getIntent().getStringExtra("Id")));
            v.setuId(Utils.getRowID());
            LayoutInflater inflater = this.getLayoutInflater();
            View itemview = inflater.inflate(R.layout.vehicle_details, null, true);
            ((TextView) itemview.findViewById(R.id.tv_sNo)).setText(String.valueOf(v.getsNo()));
            itemview.setTag(v);
            ll.addView(itemview);
        }
    }

    private void checkListData() {
        boolean dataOk = false;
        for (int i = 0; i < noOfVehicle; i++) {
            View childView = ll.getChildAt(i);
            v = (Vehicle) childView.getTag();
            v.setCarModel(((EditText) childView.findViewById(R.id.et_carModel)).getText().toString());
            v.setMake(((EditText) childView.findViewById(R.id.et_Make)).getText().toString());
            v.setNumber(((EditText) childView.findViewById(R.id.et_Number)).getText().toString());
            Utils.logMsg("\nOwnerId:-" + v.getOid() + "\nCarId:-" + v.getuId() + "\nMake:-" + v.getMake() + "\nModel:-" + v.getCarModel() + "\nNumber:-" + v.getNumber());
            if (dataValid(childView, v)) {
                vehicles.add(v);
                dataOk = true;
            } else {
                dataOk = false;
                break;
            }
            Utils.logMsg(String.valueOf(v.getsNo()) + "," + String.valueOf(v.getOid()) + "," + String.valueOf(v.getuId()));
        }
        if (dataOk) {
            DBManager.getInstance().openDatabase("saving data");
            if (DBManager.getInstance().addOwner(ownerName, ownerId) > -1 && DBManager.getInstance().addVehicles(vehicles) > -1) {
                Intent in = new Intent();
                in.setClass(this, ActivityMain.class);
                startActivity(in);
                finish();
                Utils.showMsg("DataSaved", getApplicationContext());
                vehicles.clear();
            }
            DBManager.getInstance().closeDatabase("saving data");
        } else {
            Utils.showMsg("DataNotSaved", getApplicationContext());
            vehicles.clear();
        }
    }

    private boolean dataValid(View childView, Vehicle v) {
        boolean flag = true;
        if (v.getCarModel().trim().equals("")) {
            ((EditText) childView.findViewById(R.id.et_carModel)).setError("Please EnterCar Model");
            flag = false;
        }
        if (v.getMake().trim().equals("")) {
            ((EditText) childView.findViewById(R.id.et_Make)).setError("Please EnterCar Make");
            flag = false;
        }
        if (v.getNumber().trim().equals("")) {
            ((EditText) childView.findViewById(R.id.et_Number)).setError("Please EnterCar Number");
            flag = false;
        } else {
            if (v.getNumber().matches("[a-zA-Z]{2}[0-9]{2}[a-zA-Z]{2}[0-9]{4}")) {
                flag = true;
            } else {
                flag = false;
                ((EditText) childView.findViewById(R.id.et_Number)).setError("Please Enter Valid Car Number");
            }
        }
        return flag;
    }

}
