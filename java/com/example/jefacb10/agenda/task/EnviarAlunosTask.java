package com.example.jefacb10.agenda.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.jefacb10.agenda.WebClient;
import com.example.jefacb10.agenda.converter.AlunoConverter;
import com.example.jefacb10.agenda.dao.alunoDAO;
import com.example.jefacb10.agenda.modelo.Aluno;

import java.util.List;

/**
 * Created by jefacb10 on 28/07/2017.
 */

//Classe feita para criar uma Thread secundária sem interromper a Thread principal
public class EnviarAlunosTask extends AsyncTask<Void, Void, String> {
    private final Context context;
    private ProgressDialog progresso;

    public EnviarAlunosTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        //Para mostrar barra de progresso
        progresso = ProgressDialog.show(context, "Aguarde", "Enviando alunos...", true, true);
    }

    @Override
    protected String doInBackground(Void... params) {
        alunoDAO dao = new alunoDAO(context);
        List<Aluno> alunos = dao.consulta();
        dao.close();

        AlunoConverter conversor = new AlunoConverter();
        String json = conversor.converterParaJSON(alunos);

        WebClient cliente = new WebClient();
        String resposta = cliente.post(json);

        return resposta;
    }

    @Override
    protected void onPostExecute(String resposta) {
        progresso.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
    }
}
