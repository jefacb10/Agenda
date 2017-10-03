package com.example.jefacb10.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jefacb10.agenda.modelo.Prova;

import java.util.ArrayList;
import java.util.List;

public class DetalhesProvaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_prova);

        Intent intent = getIntent();
        Prova prova = (Prova) intent.getSerializableExtra("prova");

        TextView campoMateria = (TextView) findViewById(R.id.detalhe_prova_materia);
        campoMateria.setText(prova.getMateria());

        TextView campoData = (TextView) findViewById(R.id.detalhe_prova_data);
        campoData.setText(prova.getData());

        ListView lista_topicos = (ListView) findViewById(R.id.detalhe_prova_lista);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, prova.getTopicos());
        lista_topicos.setAdapter(adapter);

    }
}
