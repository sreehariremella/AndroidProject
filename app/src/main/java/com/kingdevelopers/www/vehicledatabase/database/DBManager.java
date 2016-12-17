package com.kingdevelopers.www.vehicledatabase.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.kingdevelopers.www.vehicledatabase.Constants;
import com.kingdevelopers.www.vehicledatabase.Utils;
import com.kingdevelopers.www.vehicledatabase.model.Owner;
import com.kingdevelopers.www.vehicledatabase.model.Vehicle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by saibaba on 6/22/2016.
 */
public class DBManager {
    private AtomicInteger openCounter = new AtomicInteger();
    private static DBManager dbManager;
    private DBhelper dbHelper;
    private SQLiteDatabase database;
    boolean isDbCreated = false;
    Cursor gcursor = null;
    String gquery = "";

    public static synchronized void initializeInstance(Context context) {
        Utils.logMsg("DBManager: initializeInstance()");
        if (dbManager == null) {
            dbManager = new DBManager();
            dbManager.openCounter.set(0);
            File fileData = new File(Constants.DATABASE_ROOT_PATH + File.separator + Constants.DATABASE_FOLDER
                    + File.separator + Constants.DATABASE_NAME);
            dbManager.dbHelper = new DBhelper(context, fileData.getAbsolutePath(), null, Constants.DATABASE_VERSION);
            if (!fileData.exists()) {
                Utils.logMsg("DBManager: Database file does not exist, to be created");
                dbManager.createDataBaseFile();
                dbManager.isDbCreated = true;
            } else
                Utils.logMsg("DBManager: Database file already exists");
        }
        dbManager.createTables();
    }

    public static synchronized DBManager getInstance() {
        if (dbManager == null) {
            throw new IllegalStateException(DBManager.class.getSimpleName()
                    + " is not initialized, call initializeInstance() method first.");
        }
        return dbManager;
    }

    public synchronized void forceCloseDatabase() {
        // call this method in Gloabal, Uncaught Exception
        openCounter.set(0);
        database.close();
    }

    public synchronized SQLiteDatabase openDatabase(String callerMessage) {
        try {
            if (openCounter.incrementAndGet() == 1) {
                database = dbHelper.getWritableDatabase();
                Utils.logMsg("DBManager.openDatabase(): Got Writable Database " + callerMessage + " "
                        + openCounter.get());
            } else
                Utils.logMsg("DBManager.openDatabase(): Connection Already Open, Got the same " + callerMessage + " "
                        + +openCounter.get());
        } catch (SQLiteException e) {
            Utils.logMsg("DBManager.openDatabase(): " + callerMessage + " " + e.getMessage());
            database = dbHelper.getReadableDatabase();
            Utils.logMsg("DBManager.openDatabase(): getReadableDatabase " + openCounter.get());
        }
        return database;
    }

    public synchronized void closeDatabase(String callerMessage) {
        if (openCounter.decrementAndGet() == 0) {
            database.close();
            Utils.logMsg("DBManager.closeDatabase(): Closed Database " + callerMessage + " " + +openCounter.get());
        } else
            Utils.logMsg("DBManager.closeDatabase(): Skiped Closing " + callerMessage + " " + +openCounter.get());
        if (openCounter.get() < 0)
            openCounter.set(0);
    }

    private void createDataBaseFile() {
        Utils.logMsg("DBManager: createDataBaseFile()");
        try {
            File directory = new File(Constants.DATABASE_ROOT_PATH, Constants.DATABASE_FOLDER);
            if (!directory.exists()) {
                boolean result = directory.mkdir();
                if (result)
                    Utils.logMsg("DBManager: Directory Created");
                else
                    Utils.logMsg("DBManager: directory result = " + result);
            }
            File dbfile = new File(directory.getAbsolutePath(), Constants.DATABASE_NAME);
            boolean result = dbfile.createNewFile();
            if (result)
                Utils.logMsg("DBManager: DataBase File Created");
            else
                Utils.logMsg("DBManager: database result = " + result);
        } catch (IOException ex) {
            Utils.LogError("createDataBase", ex);
        }
    }

    private void createTables() {
        Utils.logMsg("DBManager.createTables(): Start");
        // open database
        SQLiteDatabase database = DBManager.getInstance().openDatabase("createTables");
        try {
            // all database operations using the same connection
            if (isDbCreated)
                executeCreateTableScript(database);
            else {
                executeCreateTableScript(database);
                executeAlterTableScript(database);
            }
        } catch (Exception e) {
            Utils.logMsg("DBManager.createTables(): " + e.getMessage());
        } finally {
            // close databaseE
            DBManager.getInstance().closeDatabase("createTables");
        }
    }

    private void executeAlterTableScript(SQLiteDatabase database) {

    }

    public void executeCreateTableScript(SQLiteDatabase database) {
        String CREATE_OwnerTable = "CREATE TABLE IF NOT EXISTS OWNER_TABLE (Id INTEGER PRIMARY KEY, OwnerName TEXT)";
        String CREATE_VehicleTable = "CREATE TABLE IF NOT EXISTS VEHICLE_TABLE (Id INTEGER PRIMARY KEY, Model TEXT, Make Text, Number Text, Owner_Id Integer, FOREIGN KEY(Owner_Id) REFERENCES OWNER_TABLE(Id))";
        try {
            database.execSQL(CREATE_OwnerTable);
            database.execSQL(CREATE_VehicleTable);
        } catch (Exception e) {
            Utils.logMessage("Error while creating Tables" + e.toString());
        }
    }

    public long insertRecord(String tableName, ContentValues newTaskValue) {
        long result = -1;
        // open database
        SQLiteDatabase database = DBManager.getInstance().openDatabase("insertRecord");
        try {
            result = database.insert(tableName, null, newTaskValue);
        } catch (SQLiteException e) {
            Utils.logMsg("DBManager.insertRecord(): " + e.getMessage());
        } finally {
            // close database
            DBManager.getInstance().closeDatabase("insertRecord");
        }
        return result;
    }

    public long addOwner(String OwnerName, int OwnerId) {
        Utils.logMsg("DBManager.saveOwnerData" + OwnerId + "," + OwnerName);
//        Id INTEGER PRIMARY KEY, OwnerName TEXT
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id", OwnerId);
        contentValues.put("OwnerName", OwnerName);
        return DBManager.getInstance().insertRecord("OWNER_TABLE", contentValues);
    }

    public long addVehicles(ArrayList<Vehicle> vehicles) {
//        Id INTEGER PRIMARY KEY, Model TEXT, Make Text, Number Text, Owner_Id Integer
        long v = 0;
        for (int i = 0; i < vehicles.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Owner_Id", vehicles.get(i).getOid());
            contentValues.put("Id", vehicles.get(i).getuId());
            contentValues.put("Model", vehicles.get(i).getCarModel());
            contentValues.put("Make", vehicles.get(i).getMake());
            contentValues.put("Number", vehicles.get(i).getNumber());
            if (DBManager.getInstance().insertRecord("VEHICLE_TABLE", contentValues) == -1) {
                v = DBManager.getInstance().insertRecord("VEHICLE_TABLE", contentValues);
                break;
            }
        }
        return v;
    }

    public Cursor getRawQuery(SQLiteDatabase database, String query) {
        return database.rawQuery(query, null);
    }

    public ArrayList<Owner> getOwnerData() {
        Utils.logMsg("getOwnerData.Entry");
        ArrayList<Owner> owners = new ArrayList<>();
        SQLiteDatabase db = DBManager.getInstance().openDatabase("gettingOwnerData");
        String q = "SELECT * FROM OWNER_TABLE";
        Cursor c = DBManager.getInstance().getRawQuery(db, q);
        Utils.logMsg("getOwnerData.Entry.Cursor" + q);
        Owner ow;
        try {
            if (c.moveToFirst()) {
                do {
                    ow = new Owner();
                    ow.setOwnerId(c.getInt(c.getColumnIndex("Id")));
                    ow.setOwnerName(c.getString(c.getColumnIndex("OwnerName")));
                    Utils.logMsg("getOwnerData.Loop" + String.valueOf(ow.getOwnerId()) + ow.getOwnerName());
                    owners.add(ow);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Utils.logMsg("DBManager.getOwnerData()Exception:-" + e.toString());
        } finally {
            c.close();
            DBManager.getInstance().closeDatabase("gettingOwnerData");
        }
        return owners;
    }

    public ArrayList<Vehicle> getVehicleData() {
        //        Id INTEGER PRIMARY KEY, Model TEXT, Make Text, Number Text, Owner_Id Integer
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        SQLiteDatabase db = DBManager.getInstance().openDatabase("gettingVehicleData");
        String q = "SELECT * FROM VEHICLE_TABLE";
        Cursor c = DBManager.getInstance().getRawQuery(db, q);
        Vehicle ve;
        try {
            int i = 1;
            if (c.moveToFirst()) {
                do {
                    ve = new Vehicle();
                    ve.setOid(c.getInt(c.getColumnIndex("Owner_Id")));
                    ve.setuId(c.getInt(c.getColumnIndex("Id")));
                    ve.setCarModel(c.getString(c.getColumnIndex("Model")));
                    ve.setMake(c.getString(c.getColumnIndex("Make")));
                    ve.setNumber(c.getString(c.getColumnIndex("Number")));
                    ve.setsNo(i);
                    i++;
                    vehicles.add(ve);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Utils.logMsg("DBManager.getOwnerData()Exception:-" + e.toString());
        } finally {
            c.close();
            DBManager.getInstance().closeDatabase("gettingVehicleData");
        }
        return vehicles;
    }
}
