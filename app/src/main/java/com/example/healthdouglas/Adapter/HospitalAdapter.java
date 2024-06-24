package com.example.healthdouglas.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthdouglas.Hospital;
import com.example.healthdouglas.R;

import org.w3c.dom.Text;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {
    List<Hospital> hospitalList;
    public HospitalAdapter(List<Hospital> hospitalList){
        this.hospitalList = hospitalList;
    }
    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital,parent,false);
        return new HospitalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        Hospital hospital = hospitalList.get(position);
        holder.hospitalName.setText(hospital.getName());
        holder.hospitalVicinity.setText(hospital.getVicinity());
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    public class HospitalViewHolder extends RecyclerView.ViewHolder{
        TextView hospitalName;
        TextView hospitalVicinity;
        HospitalViewHolder(@NonNull View itemView){
            super(itemView);
            hospitalName = itemView.findViewById(R.id.hospital_name);
            hospitalVicinity = itemView.findViewById(R.id.hospital_vicinity);
        }
    }
}
