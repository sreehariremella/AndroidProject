package com.kingdevelopers.www.vehicledatabase.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingdevelopers.www.vehicledatabase.R;
import com.kingdevelopers.www.vehicledatabase.model.DisView;

import java.util.ArrayList;

/**
 * Created by saibaba on 12/15/2016.
 */
public class RecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int Owner = 0;
    private static final int Car = 1;
    private ArrayList<DisView> displayViews;

    private RecAdapter() {
    }

    public RecAdapter(ArrayList<DisView> displayViews) {
        this.displayViews = displayViews;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Owner:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner, parent, false);
                return new OwnerView(view);
            case Car:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car, parent, false);
                return new CarView(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisView object = displayViews.get(position);
        if (object != null) {
            switch (object.getType()) {
                case Owner:
                    ((OwnerView) holder).tv_owner.setText(object.getDisplayText());
                    break;
                case Car:
                    ((CarView) holder).tv_car.setText(object.getDisplayText());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return displayViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (displayViews != null) {
            DisView obj = displayViews.get(position);
            if (obj != null) {
                return obj.getType();
            }
        }
        return 0;
    }

    public static class OwnerView extends RecyclerView.ViewHolder {
        TextView tv_owner;

        public OwnerView(View itemView) {
            super(itemView);
            tv_owner = (TextView) itemView.findViewById(R.id.tv_owner_name);
        }
    }

    public static class CarView extends RecyclerView.ViewHolder {
        TextView tv_car;

        public CarView(View itemView) {
            super(itemView);
            tv_car = (TextView) itemView.findViewById(R.id.tv_car_number);
        }
    }
}
