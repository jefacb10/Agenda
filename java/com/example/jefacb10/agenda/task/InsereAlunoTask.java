package com.example.jefacb10.agenda.task;

import android.os.AsyncTask;

import com.example.jefacb10.agenda.WebClient;
import com.example.jefacb10.agenda.converter.AlunoConverter;
import com.example.jefacb10.agenda.modelo.Aluno;

/**
 * Created by jefacb10 on 01/09/2017.
 */
public class InsereAlunoTask extends AsyncTask{

    private final Aluno aluno;

    public InsereAlunoTask(Aluno aluno){
        this.aluno = aluno;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        String json = new AlunoConverter().converterParaJSONCompleto(aluno);
        new WebClient().insere(json);
        return null;
    }
}
