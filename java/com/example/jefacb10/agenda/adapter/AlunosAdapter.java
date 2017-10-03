package com.example.jefacb10.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jefacb10.agenda.ListaAlunosActivity;
import com.example.jefacb10.agenda.R;
import com.example.jefacb10.agenda.modelo.Aluno;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by jefacb10 on 26/07/2017.
 */

public class AlunosAdapter extends BaseAdapter{
    private List<Aluno> alunos;
    private Context context;

    public AlunosAdapter(Context context, List<Aluno> alunos) {
        this.context = context;
        this.alunos = alunos;
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Aluno aluno = alunos.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView nome = (TextView) view.findViewById(R.id.item_nome);
        nome.setText(aluno.getNome());

        TextView telefone = (TextView) view.findViewById(R.id.item_telefone);
        telefone.setText(aluno.getTelefone());

        ImageView campoFoto = (ImageView) view.findViewById(R.id.item_foto);
        String caminhoFoto = aluno.getCaminhoFoto();

        TextView campoEndereco = (TextView) view.findViewById(R.id.item_endereco);
        if(campoEndereco!=null)
            campoEndereco.setText(aluno.getEndereco());

        TextView campoSite = (TextView) view.findViewById(R.id.item_site);
        if(campoSite!=null)
            campoSite.setText(aluno.getSite());

        if(caminhoFoto != null){
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapResult = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmapResult);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return view;
    }
}
