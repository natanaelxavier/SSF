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

    ImageView agendamento;
    TextView nomeusuario, cpfusuario, btsair;

    private RequestQueue requestQueue;
    private static final String URLespecialidade = "http://sussemfila.000webhostapp.com/especialidades.php ";
    private StringRequest request;

    ArrayList<Especialidade> especialidadesArray = new ArrayList<>();
    Especialidade especialidade;

    int ag = 1;
    private UsuarioSessao sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomeusuario = (TextView) findViewById(R.id.Main_nomeuser);
        cpfusuario = (TextView) findViewById(R.id.Main_cpfuser);
        btsair = (TextView) findViewById(R.id.Main_sair);
        agendamento = findViewById(R.id.Main_btagendamento);
        this.sessao = new UsuarioSessao(getApplicationContext());

        requestQueue = Volley.newRequestQueue(this);

        Intent a = getIntent();

        String cpfdocara = a.getStringExtra("nomedocara");
        String nomedocara = a.getStringExtra("cpfdocara");

        if(sessao.checkLogin())
            finish();


        // get user data from session

        HashMap<String, String> user = sessao.getUserDetails();

        String nome = user.get(UsuarioSessao.KEY_NOME);
        String cpf = user.get(UsuarioSessao.KEY_CPF);

        if(this.sessao.isUserLoggedIn()){
            nomeusuario.setText(nome);
            cpfusuario.setText("CPF: " + cpf);
        }else{
            nomeusuario.setText(cpfdocara);
            cpfusuario.setText("CPF: " + nomedocara);
        }

        btsair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               logoff();
            }
        });

        agendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request = new StringRequest(Request.Method.POST, URLespecialidade, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject objeto = jsonArray.getJSONObject(i);
                                especialidade = new Especialidade(objeto.getInt("id"),objeto.getString("descricao"));
                                especialidadesArray.add(especialidade);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent agendamento = new Intent(MainActivity.this, AgendamentoActivity.class);
                        agendamento.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        agendamento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);



                        Bundle bundle = new Bundle();
                        bundle.putSerializable("especialidade", especialidadesArray);
                        agendamento.putExtra("bundle", bundle);

                        //Fim do metodo
                        startActivity(agendamento);
                        especialidadesArray.clear();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        return null;
                    }
                };
                requestQueue.add(request);


          }
        });
    }

    private void logoff(){
        finish();
        this.sessao.logoutUser();
    }
    }



