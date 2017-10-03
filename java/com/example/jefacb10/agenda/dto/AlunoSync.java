package com.example.jefacb10.agenda.dto;

import com.example.jefacb10.agenda.modelo.Aluno;

import java.util.List;

/**
 * Created by jefacb10 on 25/09/2017.
 */

public class AlunoSync {
    private List<Aluno> alunos;
    private String momentoDaUltimaModificacao;

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public String getMomentoDaUltimaModificacao() {
        return momentoDaUltimaModificacao;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public void setMomentoDaUltimaModificacao(String momentoDaUltimaModificacao) {
        this.momentoDaUltimaModificacao = momentoDaUltimaModificacao;
    }
}
