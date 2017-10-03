package com.example.jefacb10.agenda;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by jefacb10 on 27/07/2017.
 */

//Classe criada para comunicação com servidor
public class WebClient {
    //método para enviar dados para o servidor
    public String post(String json){
        URL url = null;
        String endereco = "https://www.caelum.com.br/mobile";
        String resposta = realizaConexao(json, endereco);
        if (resposta != null) return resposta;

        return null;
    }

    @Nullable
    private String realizaConexao(String json, String endereco) {
        URL url;
        try {
            //1 - Cria uma URL
            //2 - Cria uma conexão HTTP usando a url e prioriza a forma de dados que serão usados na ida e retorno dos dados, que é json
            //3 - Como os dados serão de saída, então deve ser especificado que a saída é verdadeira: "setDoOutput(true)"
            //4 - Deve-se criar um PrinstStream para o envio de dados em um Stream e um Scanner para receber a resposta do servidor

            url = new URL(endereco);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setDoOutput(true);

            PrintStream output = new PrintStream(connection.getOutputStream());
            output.println(json);

            connection.connect();

            Scanner scanner = new Scanner(connection.getInputStream());
            String resposta = scanner.next();
            return resposta;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insere(String json) {
        String endereco = "http://192.168.15.6:8080/api/aluno";
        realizaConexao(json, endereco);
    }
}
