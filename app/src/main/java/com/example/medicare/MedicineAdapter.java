package com.example.medicare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicare.database.Medicine;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;

    public MedicineAdapter(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine_home, parent, false);

        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);

        holder.tvMedicineName.setText(medicine.name);
        holder.tvMedicineDetails.setText(
                medicine.dosage + " • " + medicine.time
        );

        holder.btnTake.setOnClickListener(v -> {
            // Take button will be connected later with history/reminder
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public void updateList(List<Medicine> newList) {
        medicineList = newList;
        notifyDataSetChanged();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {

        TextView tvMedicineName;
        TextView tvMedicineDetails;
        Button btnTake;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMedicineName = itemView.findViewById(R.id.tvMedicineName);
            tvMedicineDetails = itemView.findViewById(R.id.tvMedicineDetails);
            btnTake = itemView.findViewById(R.id.btnTake);
        }
    }
}