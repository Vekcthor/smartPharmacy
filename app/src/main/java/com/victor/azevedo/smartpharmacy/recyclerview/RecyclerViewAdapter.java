package com.victor.azevedo.smartpharmacy.recyclerview;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.victor.azevedo.smartpharmacy.R;
import com.victor.azevedo.smartpharmacy.activities.MainActivity;
import com.victor.azevedo.smartpharmacy.activities.NewAlarm;
import com.victor.azevedo.smartpharmacy.data.Alarm;
import com.victor.azevedo.smartpharmacy.data.AlarmModel;
import com.victor.azevedo.smartpharmacy.data.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<AlarmModel> alarmModelList;

    public RecyclerViewAdapter(List<AlarmModel> alarmModelList) {
        this.alarmModelList = alarmModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_line_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.medicationName.setText(alarmModelList.get(position).getMedicationName());
        holder.medicationTime.setText(new SimpleDateFormat("HH:mm").format(new Date(alarmModelList.get(position).getTime())));
        holder.medicationDose.setText("Dose: " + alarmModelList.get(position).getDose());
        holder.medicationWeekdays.setText(alarmModelList.get(position).getWeekdays());
        holder.medicationInventory.setText("  Inventário: " + alarmModelList.get(position).getInventory());

        holder.switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(compoundButton.getContext(), "Alarme Ativado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(compoundButton.getContext(), "Alarme Desativado", Toast.LENGTH_LONG).show();
                }

            }
        });

        holder.imageButton.setOnClickListener(view -> removerItem(holder.itemView.getContext(),position));
    }

    @Override
    public int getItemCount() {
        return alarmModelList != null ? alarmModelList.size() : 0;
    }
    /**
     * Método publico chamado para atualziar a lista.
     * @param alarmModel Novo objeto que será incluido na lista.
     */
    public void updateList(AlarmModel alarmModel) {
        insertItem(alarmModel);
    }

    // Método responsável por inserir um novo usuário na lista
    //e notificar que há novos itens.
    private void insertItem(AlarmModel alarmModel) {
        alarmModelList.add(alarmModel);
        notifyItemInserted(getItemCount());
    }

    // Método responsável por atualizar um usuário já existente na lista.
    private void updateItem(int position) {
        AlarmModel alarmModel = alarmModelList.get(position);
        notifyItemChanged(position);
    }

    // Método responsável por remover um usuário da lista.
    private void removerItem(Context context, int position) {
        AsyncTask.execute(new Runnable() {
          @Override
          public void run() {
              AppDatabase appDatabase = AppDatabase.getInstance(context);
              Alarm alarm = appDatabase.alarmDao().loadById(alarmModelList.get(position).getId());
              appDatabase.alarmDao().delete(alarm);
          }
        });
        alarmModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, alarmModelList.size());
    }

}
