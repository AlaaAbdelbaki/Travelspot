package com.travelspot.travelspot.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.travelspot.travelspot.Models.Post;
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.Trip;
import com.travelspot.travelspot.Models.TripServices;
import com.travelspot.travelspot.R;
import com.travelspot.travelspot.UserSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder>{

    private List<Trip> trips;
    private Context mContext;


    public TripAdapter(Context mContext, List<Trip> trips){
        this.trips = trips;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TripAdapter.TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.trip_item,parent,false);
        return new TripAdapter.TripHolder(item,this);
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.TripHolder holder, int position) {
        World.init(mContext);
        final Trip singleItem = trips.get(position);
        Log.e( "onBindViewHolder: ",singleItem.toString() );
        holder.countryName.setText(singleItem.getLocation());
        holder.countryFlag.setImageResource(World.getFlagOf(singleItem.getLocation()));
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd/MM/yyyy");
        holder.tripDuration.setText("From: "+sdf.format(singleItem.getStartDate())+" until: "+sdf.format(singleItem.getEndDate()));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class TripHolder extends RecyclerView.ViewHolder{

        public ImageView countryFlag;
        public TextView countryName;
        public TextView tripDuration;
        final TripAdapter mAdapter;

        public TripHolder(@NonNull View itemView,TripAdapter mAdapter) {
            super(itemView);

            this.countryFlag = itemView.findViewById(R.id.countryFlag);
            this.countryName = itemView.findViewById(R.id.countryName);
            this.tripDuration = itemView.findViewById(R.id.tripDuration);
            this.mAdapter = mAdapter;
        }
    }
}
