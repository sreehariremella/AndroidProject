package com.kingdevelopers.www.vehicledatabase.model;

/**
 * Created by saibaba on 12/15/2016.
 */
public class Owner {
    int OwnerId;
    String OwnerName;
    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public int getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(int ownerId) {
        OwnerId = ownerId;
    }
}
