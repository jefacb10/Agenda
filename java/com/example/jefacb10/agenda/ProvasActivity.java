package com.example.jefacb10.agenda;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jefacb10.agenda.modelo.Prova;

import java.util.Arrays;
import java.util.List;

public class ProvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.replace(R.id.frame_principal, new ListaProvaFragment());
        if(modoPaisagem())
            tx.replace(R.id.frame_secundario, new DetalhesProvaFragment());
        tx.commit();


    }

    private boolean modoPaisagem() {
        return getResources().getBoolean(R.bool.modoPaisagem);
    }

    public void selecionaProvas(Prova prova) {
        FragmentManager manager = getSupportFragmentManager();
        if (!modoPaisagem()){
            FragmentTransaction tx = manager.beginTransaction();
            DetalhesProvaFragment detalhesFragment = new DetalhesProvaFragment();
            Bundle parametros = new Bundle();
            parametros.putSerializable("prova", prova);
            detalhesFragment.setArguments(parametros);
            tx.replace(R.id.frame_principal, detalhesFragment);
            tx.addToBackStack(null);
            tx.commit();

        } else {
            DetalhesProvaFragment detalhesFragment = (DetalhesProvaFragment) manager.findFragmentById(R.id.frame_secundario);
            detalhesFragment.populaCampos(prova);
        }
    }
}
