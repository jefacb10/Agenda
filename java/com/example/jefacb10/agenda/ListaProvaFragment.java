package com.example.jefacb10.agenda;//Prestar atenção em qual pacote Fragment está sendo importado
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jefacb10.agenda.DetalhesProvaActivity;
import com.example.jefacb10.agenda.ProvasActivity;
import com.example.jefacb10.agenda.R;
import com.example.jefacb10.agenda.modelo.Prova;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jefacb10 on 02/08/2017.
 */

public class ListaProvaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Para inflar o layout deverá ter três parâmetros: o fragment, o ViewGroup que ocupará a função de 'pai' e false para não ocupar a função ainda
        View view = inflater.inflate(R.layout.fragment_lista_provas, container, false);
        List<String> topicosPort = Arrays.asList("Trovadorismo", "Parnasianismo", "Modernismo");
        Prova provaPortugues = new Prova("Português", "25/10/2017", topicosPort);

        List<String> topicosMat = Arrays.asList("Probabilidade", "Logaritmo", "Trigonometria", "Geometria");
        Prova provaMatematica = new Prova("Matemática", "26/10/2017", topicosMat);

        final List<Prova> provas = Arrays.asList(provaPortugues, provaMatematica);
        ListView lista_provas = (ListView) view.findViewById(R.id.item_lista_prova);

        ArrayAdapter<Prova> adapter = new ArrayAdapter<Prova>(getContext(), android.R.layout.simple_list_item_1, provas);
        lista_provas.setAdapter(adapter);
        lista_provas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prova prova = (Prova) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), "Você clicou em "+ prova, Toast.LENGTH_SHORT).show();

                ProvasActivity provasActivity = (ProvasActivity) getActivity();
                provasActivity.selecionaProvas(prova);

            }
        });
        return view;
    }
}
