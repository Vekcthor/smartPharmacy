package com.victor.azevedo.smartpharmacy.recyclerview;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.victor.azevedo.smartpharmacy.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView medicationName, medicationTime, medicationWeekdays, medicationDose, medicationInventory;
    public SwitchMaterial switchMaterial;
    public ImageButton imageButton;

    public ViewHolder(View itemView){
        super(itemView);
        medicationTime = (TextView) itemView.findViewById(R.id.textViewTimeMedicationRecyclerView);
        medicationName = (TextView) itemView.findViewById(R.id.textViewNameMedicationRecyclerView);
        medicationWeekdays = (TextView) itemView.findViewById(R.id.textViewWeekdaysMedicationRecyclerView);
        medicationDose = (TextView) itemView.findViewById(R.id.textViewDoseMedicationRecyclerView);
        medicationInventory = (TextView) itemView.findViewById(R.id.textViewInventoryMedicationRecyclerView);
        switchMaterial = (SwitchMaterial) itemView.findViewById(R.id.switchRecyclerview);
        imageButton = (ImageButton) itemView.findViewById(R.id.imageButtonDeleteAlarm);
    }
}
