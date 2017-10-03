package com.example.jefacb10.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jefacb10.agenda.modelo.Aluno;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jefacb10 on 19/07/2017.
 */

public class alunoDAO extends SQLiteOpenHelper {

    public alunoDAO(Context context) {
        //Construtor que pega o contexto, o nome do DATABASE, CursorFactory que é para modificação no banco de dados, e o número da versão.
        super(context, "Agenda", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Cria uma String com uma query para criar a tabela e o banco de dados executa essa instrução especificada na String
        String sql = "CREATE TABLE tbAluno (numeroMatricula INTEGER PRIMARY KEY NOT NULL, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, nota REAL, caminhoFoto TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Cria a String sql vazia e de acordo com o versionamento ela é modificada e executada. Não deve ser usado o 'break;' para que o
        // código continue até a última alteração no banco
        String sql = "";
        switch (oldVersion){
            case 1:
                sql = "ALTER TABLE tbAluno ADD COLUMN caminhoFoto TEXT";
                db.execSQL(sql); //indo para a versão 2

            case 2:
                //Criar tabela nova para a 3a versão para utilizar o UUID na aplicação
                String criandoTabelaNovaParaUUID = "CREATE TABLE tbAluno_novo "+
                        "(numeroMatricula CHAR(36) PRIMARY KEY, "+
                        "nome TEXT NOT NULL, "+
                        "endereco TEXT, "+
                        "telefone TEXT, "+
                        "site TEXT, "+
                        "nota REAL, "+
                        "caminhoFoto TEXT);";
                db.execSQL(criandoTabelaNovaParaUUID);

                //Migra os dados da antiga tabela para a nova
                String inserindoNaTabelaNova = "INSERT INTO tbAluno_novo "+
                        "(numeroMatricula, nome, endereco, telefone, site, nota, caminhoFoto) "+
                        "SELECT numeroMatricula, nome, endereco, telefone, site, nota, caminhoFoto "+
                                "FROM tbAluno;";
                db.execSQL(inserindoNaTabelaNova);

                //Após dados serem migrados a tabela antiga é excluída
                String deletaTabelaAntiga = "DROP TABLE tbAluno;";
                db.execSQL(deletaTabelaAntiga);

                //Para evitar modificações em grande parte do código, basta renomear a tabela para o nome da tabela antiga
                String alteraNomeTabela = "ALTER TABLE tbAluno_novo RENAME TO tbAluno;";
                db.execSQL(alteraNomeTabela);

            case 3:
                //Para alterar todos os numeros de matrícula dos alunos para UUID
                String buscaAlunos = "SELECT * FROM tbAluno";
                Cursor cursor = db.rawQuery(buscaAlunos, null);
                List<Aluno> alunos = populaAlunos(cursor);

                String mudaNumMatriculaParaUUID = "UPDATE tbAluno SET numeroMatricula=? WHERE numeroMatricula=?";
                for(Aluno aluno : alunos){
                    db.execSQL(mudaNumMatriculaParaUUID, new String[] { geraUUID(), aluno.getNumeroMatricula()});
                }



        }


        //Cria uma query para apagar uma tabela se já estiver uma criada a fim de criar uma outra com alterações necessárias
        //String sql = "DROP TABLE IF EXISTS tbAluno;";
        //db.execSQL(sql);
        //onCreate(db);
    }

    private String geraUUID() {
        return UUID.randomUUID().toString();
    }

    public void insere(Aluno aluno){
        //Pega o banco de dados editável, um MAP de dados(CHAVE, VALOR) e faz a inserção(nomeTabela, null para modificações, ContentValue)
        SQLiteDatabase db = getWritableDatabase();
        //verifica se o aluno já possui UUID e gera um caso não tenha
        if(aluno.getNumeroMatricula() == null)
            aluno.setNumeroMatricula(geraUUID());
        ContentValues dados = getDadosAluno(aluno);
        db.insert("tbAluno", null, dados);

    }

    @NonNull
    private ContentValues getDadosAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();
        dados.put("numeroMatricula", aluno.getNumeroMatricula());
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("caminhoFoto", aluno.getCaminhoFoto());
        return dados;
    }

    public List<Aluno> consulta() {
        //1 - Criar uma string da query de seleção
        //2 - Pegar o banco de dados para leitura
        //3 - Criar um cursor do banco que vai apontar para a linha(query, parâmetro) que será lida
        //4 - Criar uma lista e enquanto houver próxima linha no cursor colocar os dados referentes a coluna indexada pela 'chave'
        //5 - Adicionar o aluno com os dados preenchidos na lista e retorná-la
        String sql = "SELECT * FROM tbAluno;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<Aluno> alunos = populaAlunos(cursor);
        cursor.close();
        return alunos;
    }

    @NonNull
    private List<Aluno> populaAlunos(Cursor cursor) {
        List<Aluno> alunos = new ArrayList<Aluno>();
        while(cursor.moveToNext()){
            Aluno aluno = new Aluno();
            aluno.setNumeroMatricula(cursor.getString(cursor.getColumnIndex("numeroMatricula")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            aluno.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));
            alunos.add(aluno);
        }
        cursor.close();
        return alunos;
    }

    public void deletar(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        String[] parametros = {String.valueOf(aluno.getNumeroMatricula())};
        db.delete("tbAluno", "numeroMatricula = ?",parametros);
    }

    public void atualiza(Aluno aluno) {
        ContentValues dados = getDadosAluno(aluno);
        SQLiteDatabase db = getWritableDatabase();
        String[] parametros = {String.valueOf(aluno.getNumeroMatricula())};
        db.update("tbAluno",dados, "numeroMatricula = ?", parametros);
    }

    public boolean verificaTelAluno(String telefone) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tbAluno WHERE telefone = ?", new String[]{telefone});
        int resultado = c.getCount();
        c.close();
        return resultado > 0;
    }
    //Baixar dados do servidor e colocar no SQLite
    public void sincroniza(List<Aluno> alunos) {
        Log.i("aluno", String.valueOf(alunos));
        for(Aluno aluno : alunos){
            if(existe(aluno)){
                atualiza(aluno);
            }
            else{
                insere(aluno);
            }
        }
    }

    private boolean existe(Aluno aluno) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT numeroMatricula FROM tbAluno WHERE numeroMatricula=? LIMIT 1";
        Cursor cursor = db.rawQuery(sql, new String[]{aluno.getNumeroMatricula()});
        int quantidade = cursor.getCount();
        cursor.close();
        return quantidade > 0;
    }
}
