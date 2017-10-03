package com.example.jefacb10.agenda.services;

import com.example.jefacb10.agenda.dto.AlunoSync;
import com.example.jefacb10.agenda.modelo.Aluno;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by jefacb10 on 04/09/2017.
 */

public interface AlunoService {
    @POST("aluno")
    Call<Void> insere(@Body Aluno aluno);

    @GET("aluno")
    Call<AlunoSync> lista();
}
