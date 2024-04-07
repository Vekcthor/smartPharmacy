package com.victor.azevedo.smartpharmacy.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.victor.azevedo.smartpharmacy.R;
import com.victor.azevedo.smartpharmacy.data.Alarm;
import com.victor.azevedo.smartpharmacy.data.AlarmDao;
import com.victor.azevedo.smartpharmacy.data.AlarmModel;
import com.victor.azevedo.smartpharmacy.data.AppDatabase;
import com.victor.azevedo.smartpharmacy.recyclerview.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonAddNewMedication;
    RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    SwitchMaterial switchMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAlarmsList);
        buttonAddNewMedication = (Button) findViewById(R.id.buttonAddNewMedication);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //Insert Data
                List<Alarm> alarmsList = AppDatabase.getInstance(MainActivity.this).alarmDao().getAll();

                final int size = alarmsList.size();
                for (int i = 0; i < size; i++)
                {
                    recyclerViewAdapter.updateList(new AlarmModel(alarmsList.get(i).uid,alarmsList.get(i).medicationName, alarmsList.get(i).time,
                            alarmsList.get(i).weekdays, alarmsList.get(i).dailyFrequency, alarmsList.get(i).dose, alarmsList.get(i).inventory));
                    //do something with i
                }
                // Get Data   AppDatabase.getInstance(context).userDao().getAllUsers();
            }
        });

        buttonAddNewMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewAlarm.class);
                startActivity(intent);
                //finish();
            }
        });

        setupRecycler();
    }

    private void setupRecycler() {

        // Configurando o gerenciador de layout para ser uma lista.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<>(0));
        recyclerView.setAdapter(recyclerViewAdapter);

        // Configurando um dividr entre linhas, para uma melhor visualização.
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

}