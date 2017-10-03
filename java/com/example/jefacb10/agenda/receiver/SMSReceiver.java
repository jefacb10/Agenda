package com.example.jefacb10.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.jefacb10.agenda.R;
import com.example.jefacb10.agenda.dao.alunoDAO;

/**
 * Created by jefacb10 on 26/07/2017.
 */

public class SMSReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        //As PDUs(Protocol Data Unit) são formatos de mensagens trafegados entre os mais novos e velhos celulares
        //1 - Cria um array de objetos recebendo as pdus da intent
        //2 - Cria um array de bytes que pega o conteúdo da primeira posição da array de pdus
        //3 - Pega o formato da mensagem
        //4 - Cria o objeto de mensagem através da PDU
        //5 - Pega o número de telefone do remetente
        //6 - Instancia o dao e verifica se o número de telefone pertence a um aluno ou não
        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];
        String formato = (String) intent.getSerializableExtra("format");
        SmsMessage sms = SmsMessage.createFromPdu(pdu, formato);
        String telefone = sms.getOriginatingAddress();
        alunoDAO dao = new alunoDAO(context);
        if(dao.verificaTelAluno(telefone)) {
            Toast.makeText(context, "SMS recebido de um aluno", Toast.LENGTH_SHORT).show();
            //Para tocar som ao receber mensagem
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }
        dao.close();
    }
}
