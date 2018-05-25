package br.com.geekstorm.sussemfila;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Widgets do Layout
    ImageView agendamento, consAgendadas;
    TextView nomeusuario, cpfusuario, btsair;
    //Sistema de Sessão
    private UsuarioSessao sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Efetuando ligação dos objetos do layout com objetos da classe
        nomeusuario = (TextView) findViewById(R.id.Main_nomeuser);
        cpfusuario = (TextView) findViewById(R.id.Main_cpfuser);
        btsair = (TextView) findViewById(R.id.Main_sair);
        agendamento = findViewById(R.id.Main_btagendamento);
        consAgendadas = findViewById(R.id.Main_btconsultaagendamento);

        this.sessao = new UsuarioSessao(getApplicationContext());

        //Abrindo consultas agendadas
        consAgendadas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, AgendadasActivity.class);
                startActivity(it);
            }
        });

        //Restaurando Intent e seus dados
        Intent a = getIntent();
        String cpfdocara = a.getStringExtra("nomedocara");
        String nomedocara = a.getStringExtra("cpfdocara");

        //Checando se o usuario esta logado
        if(sessao.checkLogin())
            finish();

        //Restaura dados do Usuario, caso esteja logado
        HashMap<String, String> user = sessao.getUserDetails();
        String nome = user.get(UsuarioSessao.KEY_NOME);
        String cpf = user.get(UsuarioSessao.KEY_CPF);

        //Se o usuario estiver logado pegar seus dados; se não, pegar dados da intent login
        if(this.sessao.isUserLoggedIn()){
            nomeusuario.setText(nome);
            cpfusuario.setText("CPF: " + cpf);
        }else{
            nomeusuario.setText(cpfdocara);
            cpfusuario.setText("CPF: " + nomedocara);
        }

        //Botao sair
        btsair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               logoff();
            }
        });

        //Botao Agendamento
        agendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AgendamentoActivity.class);
                startActivity(i);
          }
        });
    }

    //Função sair e deletar sessão
    private void logoff(){
        finish();
        this.sessao.logoutUser();
    }
    }



