package com.example.jefacb10.agenda;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jefacb10.agenda.dao.alunoDAO;
import com.example.jefacb10.agenda.modelo.Aluno;
import com.example.jefacb10.agenda.retrofit.RetrofitInicializador;
import com.example.jefacb10.agenda.task.InsereAlunoTask;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioActivity extends AppCompatActivity {
    public static final int CODIGO_CAMERA = 567;
    FormularioHelper helper;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        helper = new FormularioHelper(this);

        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");
        if(aluno != null)
            helper.preencheDados(aluno);

        Button btnFoto = (Button) findViewById(R.id.formulario_botao_foto);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" +System.currentTimeMillis() +".jpg";
                File arquivoFoto = new File(caminhoFoto);
                intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                //Se fosse no Android 7:
                // intent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", arquivoFoto));
                startActivityForResult(intentFoto, CODIGO_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CODIGO_CAMERA){
                helper.carregaFoto(caminhoFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Gera-se um inflador para colocar na Action Bar e infla o menu escolhido
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Todos os itens do menu terão os mesmos efeitos do código abaixo e por isso deve usar switch case para saber qual item usar.
       switch (item.getItemId()) {
            case R.id.menu_formulario_ok:
                Aluno aluno = helper.getAluno();
                 //Insere no banco de dados e fecha a conexão
                alunoDAO dao = new alunoDAO(this);
                if(aluno.getNumeroMatricula() != null){
                    dao.atualiza(aluno);
                    Toast.makeText(FormularioActivity.this, "Dados do "+ aluno.getNome()+" atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(FormularioActivity.this, "Dados do "+ aluno.getNome()+" salvos com sucesso!", Toast.LENGTH_SHORT).show();
                    dao.insere(aluno);
                }
                dao.close();
                // new InsereAlunoTask(aluno).execute();
                // Faz requisição de inserção de dados no servidor via Retrofit
                Call call = new RetrofitInicializador().getAlunoService().insere(aluno);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.i("onResponse", "Requisicao feita com sucesso");
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("onFailure", "Requisicao falhou");
                    }
                });

                finish();
                break;
       }
            return super.onOptionsItemSelected(item);

    }
}
