package com.example.jefacb10.agenda;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jefacb10.agenda.adapter.AlunosAdapter;
import com.example.jefacb10.agenda.converter.AlunoConverter;
import com.example.jefacb10.agenda.dao.alunoDAO;
import com.example.jefacb10.agenda.dto.AlunoSync;
import com.example.jefacb10.agenda.modelo.Aluno;
import com.example.jefacb10.agenda.retrofit.RetrofitInicializador;
import com.example.jefacb10.agenda.task.EnviarAlunosTask;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.jar.Manifest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaAlunosActivity extends AppCompatActivity {
    private static final int CODIGO_SMS = 586;

    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        carregaAlunos();

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        Button btnSalvar = (Button) findViewById(R.id.lista_adicionar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int posicao, long id) {
                //Pega o aluno na posição selecionada
                Aluno aluno = (Aluno) lista.getItemAtPosition(posicao);
                //Faz a conexão com a activity a ser aberta passando um MAP(chave, valor) para que a chave seja encontrada do outro lado
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intent.putExtra("aluno", aluno);
                startActivity(intent);
            }
        });

        //Para dizer qual componente da tela vai possuir um menu de contexto (aquele que recebe o menu ao ser clicado)
        registerForContextMenu(listaAlunos);

        //Para perguntar ao usuário se ele quer permitir recebimento de SMS
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.RECEIVE_SMS } , CODIGO_SMS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_enviar_notas:
                new EnviarAlunosTask(this).execute();
                break;
            case R.id.menu_baixar_provas:
                Intent intent = new Intent(this, ProvasActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_mapa:
                Intent intentMapa = new Intent(this, MapaActivity.class);
                startActivity(intentMapa);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        //Necessário utilizar um adapterView para pegar a posição
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        //Pega o aluno da posição selecionada
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        //Para fazer ligação o procedimento é diferente:
        //1 - adiciona o item do menu 'Ligar'
        //2 - Criar um 'Ouvinte' para tratar o clique
        final MenuItem ligacao = menu.add("Ligar");
        ligacao.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Deve-se verificar se o usuário permitiu que o app tenha permissão de acesso a chamada de telefone
                //Antes de tudo a permissão é liberada pelo app no MANIFEST

                //Verificar se a permissão pelo usuário foi concedida ou não:
                //(NÃO) - o Android pergunta ao usuário se ele concede ou não a permissão (tela, array de Strings com as permissões e o Request Code)
                //(SIM) - a intent é criada para chamadas telefônicas mandando o número como dado e abrindo o discador
                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 123);
                }
                else{
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentLigar);
                }

                return false;
            }
        });


        //Cria item do menu > Cria a intent para visualizar outro app do ANDROID > Utiliza o protocolo do serviço > chama a intent
        MenuItem enviarSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:"+aluno.getTelefone()));
        enviarSMS.setIntent(intentSMS);

        //O mapa usa o protocolo 'geo:'; a lat. e long. como 0 e 0, respectivamente; e a sintaxe '?q=' para usar no Google Maps. (Para zoom: ?z=14&q=)
        MenuItem buscarMapa = menu.add("Buscar no mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q="+aluno.getEndereco()));
        buscarMapa.setIntent(intentMapa);



        MenuItem visitarSite = menu.add("Visitar site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String site = aluno.getSite();
        if(!site.startsWith("http://"))
            site = "http://"+ site;
        intentSite.setData(Uri.parse(site));
        visitarSite.setIntent(intentSite);
        //Para selecionar um item da lista e atribuir um menu de itens ao mesmo. No caso aqui terá apenas o "Deletar"
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                alunoDAO dao = new alunoDAO(ListaAlunosActivity.this);
                dao.deletar(aluno);
                dao.close();
                //Para os dados da Activity serem atualizados após a exclusão do item
                carregaAlunos();
                return false;
            }
        });
    }

    private void carregaAlunos() {
        alunoDAO dao = new alunoDAO(this);
        List<Aluno> alunos = dao.consulta();

        for (Aluno aluno : alunos) {
            Log.i("id do aluno", String.valueOf(aluno.getNumeroMatricula()));
        }
        dao.close();
        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        AlunosAdapter adapter = new AlunosAdapter(this, alunos);
        listaAlunos.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Pega os alunos do servidor o traz os dados em forma de lista
        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().lista();
        call.enqueue(new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();
                alunoDAO dao = new alunoDAO(ListaAlunosActivity.this);
                dao.sincroniza(alunoSync.getAlunos());
                dao.close();
                carregaAlunos();
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
            }
        });
        carregaAlunos();
    }
}
