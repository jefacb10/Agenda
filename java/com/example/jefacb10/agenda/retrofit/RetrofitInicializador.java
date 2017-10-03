package com.example.jefacb10.agenda.retrofit;

/**
 * Created by jefacb10 on 04/09/2017.
 */

import com.example.jefacb10.agenda.services.AlunoService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitInicializador {
    private final Retrofit retrofit;


    public RetrofitInicializador(){
        //Cria interceptor para verificar o que ocorre no ciclo de requisição ao servidor
        //O Retrofit usa o OkHttpClient para criar um logging-interruptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);

        //O Retrofit necessita de um endereço URL base para usar como padrão de comunicação
        //É necessário usar o conversor Jackson para transformar os objetos em JSON
        //Construir um client para fazer a requisição
        //E por fim construir o Retrofit com todas as informações passadas
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.15.150:8080/api/")
                                            .addConverterFactory(JacksonConverterFactory.create())
                                            .client(client.build())
                                            .build();
    }

    public AlunoService getAlunoService(){
        return retrofit.create(AlunoService.class);
    }
}
