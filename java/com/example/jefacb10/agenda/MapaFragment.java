package com.example.jefacb10.agenda;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.jefacb10.agenda.dao.alunoDAO;
import com.example.jefacb10.agenda.modelo.Aluno;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by jefacb10 on 04/08/2017.
 */

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //Assim que o mapa estiver pronto o método carrega na tela
        getMapAsync(this);
    }

    //Necessário implementar o método para indicar que o mapa está pronto
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //1 - Pega as coordenadas à partir de um endereço
        //2 - Se não for nulo, constrói um ponto fixo à partir das coordenadas e do tamanho do zoom
        //3 - Instancia o banco de dados
        //4 - Cria um laço pegando as coordenadas de cada aluno
        //5 - Se não forem nulas, instancia um marcador (pinos no mapa) e seta a posição, título ao ser clicado e subtítulo
        //6 - No fim do laço adiciona o marcador no googleMap e saindo do fluxo de repetição fecha o banco de dados
        LatLng enderecoEscola = pegaCoordenadaPosicao("Av. Roque Celestino Pires");
        if(enderecoEscola!=null) {
            centralizaEm(enderecoEscola);
        }
        else{
            Toast.makeText(getContext(), "Endereço não encontrado!", Toast.LENGTH_SHORT).show();
        }
        alunoDAO dao = new alunoDAO(getContext());
        for(Aluno aluno : dao.consulta()){
            LatLng posicaoAluno = pegaCoordenadaPosicao(aluno.getEndereco());
            if(posicaoAluno!=null){
                MarkerOptions marcador = new MarkerOptions();
                marcador.position(posicaoAluno);
                marcador.title(aluno.getNome());
                marcador.snippet(String.valueOf(aluno.getNota()));
                googleMap.addMarker(marcador);
            }
        }
        dao.close();

    }

    private LatLng pegaCoordenadaPosicao(String endereco) {
        //1 - É necessário realizar um try/catch para a IOException que pode ser lançada
        //2 - Cria um Geocoder que devolve uma lista contendo Latitude e Longitude à partir de um endereço e o máximo de resultados
        //3 - Se a lista não estiver vazia, é retornada a posição(Latitude/Longitude)
        //4 - Caso contrário, é retornado nulo
        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> resultados = geocoder.getFromLocationName(endereco, 1);
            if(!resultados.isEmpty()){
                LatLng posicao = new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
                return posicao;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void centralizaEm(LatLng coordenada) {
        if (googleMap != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(coordenada, 17);
            googleMap.moveCamera(update);
        }
    }
}
