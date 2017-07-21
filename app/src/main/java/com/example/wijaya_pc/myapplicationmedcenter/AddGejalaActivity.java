package com.example.wijaya_pc.myapplicationmedcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGejalaActivity extends AppCompatActivity {

    TextView textViewPenyakitName;
    EditText editTextGejala;

    Button buttonAddGejala;

    ArrayAdapter adapter;

    ListView listViewGejala;
    private ArrayList<Gejala> ListGejala = new ArrayList<>();

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gejala);

        preferences = getSharedPreferences(LoginActivity.AUTH_SESSION, Context.MODE_PRIVATE);

        textViewPenyakitName = (TextView) findViewById(R.id.textViewPenyakitName);
        editTextGejala = (EditText) findViewById(R.id.editTextGejala);
        buttonAddGejala = (Button) findViewById(R.id.buttonAddGejala);
        listViewGejala = (ListView) findViewById(R.id.listViewGejala);

        dummyData();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ListGejala);
        //adapter = new SearchableAdapter(this, android.R.layout.simple_list_item_1, transList);
        listViewGejala.setAdapter(adapter);

        Intent intent = getIntent();
        final String id = intent.getStringExtra(TransactionDetailActivity.PENYAKIT_ID_GEJALA);
        final String name = intent.getStringExtra(TransactionDetailActivity.PENYAKIT_NAME_GEJALA);
        Log.d("idpenyakit", "onClick: "+id);
        Log.d("namapenyakit", "onClick: "+name);
        textViewPenyakitName.setText(name);

        buttonAddGejala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGejala(id);
            }
        });

        String role = preferences.getString("role", "");
        if ( role.toString().trim().equals("User") ) //( txtRole.getText().toString().trim().equals("User") )
        {
            buttonAddGejala.setVisibility(View.GONE);
            editTextGejala.setVisibility(View.GONE);
            //txtRole.setText("Rolenya adalaahhhhh " + role);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu); // set your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            preferences.edit().clear().commit();
            Intent it = new Intent(this, LoginActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveGejala(final String id) {
        String idPenyakit = id;
        String gejala = editTextGejala.getText().toString().trim();

        HashMap<String, String> data = new HashMap<>();
        data.put("idPenyakit", idPenyakit);
        data.put("gejala", gejala);

        JsonObjectRequest request = new JsonObjectRequest(EndPoint.ADDGEJALA_URL,
                new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("transaction.resp", response.toString());
                        /*Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);*/
                        sendRequest();
                        editTextGejala.setText("");
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String token = preferences.getString("token", null);
                String uid = Integer.toString(preferences.getInt("uid", 0));
                params.put("token", token);
                params.put("uid", uid);
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(request);
    }

    protected void onResume() {
        super.onResume();
        sendRequest();
    }

    public void sendRequest(){
        Intent intent = getIntent();
        final String id = intent.getStringExtra(TransactionDetailActivity.PENYAKIT_ID_GEJALA);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, EndPoint.GETGEJALA_URL +"/"+id, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response) {
                requestAction(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String token = preferences.getString("token", null);
                String uid = Integer.toString(preferences.getInt("uid", 0));
                params.put("token", token);
                params.put("uid", uid);
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(request);
    }

    private void requestAction(JSONArray response){
        Log.d("jobs.result",response.toString());
        Gejala trans;
        ListGejala = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject objRes = (JSONObject) response.get(i);
                trans = new Gejala(objRes.getString("id"),
                        objRes.getString("gejala"),
                        objRes.getString("name"),
                        objRes.getString("idPenyakit"),
                        objRes.getString("description"),
                        objRes.getString("gejalaUmum"),
                        objRes.getString("obat"),
                        objRes.getInt("type"));
                ListGejala.add(trans);
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ListGejala);
            listViewGejala.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    private void dummyData(){
        ListGejala.add(new Gejala("1", "Badan Panas", "Demam Berdarah", "1", "asd", "dsa", "qwe", Penyakit.PARAH));
        ListGejala.add(new Gejala("2", "Batuk berdarah", "Demam Berdarah", "1", "asd", "dsa", "qwe", Penyakit.PARAH));
        ListGejala.add(new Gejala("3", "Uhuk Uhuk", "Demam Berdarah", "1", "asd", "dsa", "qwe", Penyakit.PARAH));
        ListGejala.add(new Gejala("4", "Ingusan", "Demam Berdarah", "1", "asd", "dsa", "qwe", Penyakit.PARAH));
        ListGejala.add(new Gejala("5", "Batuk Pilek Bersamaan", "Demam Berdarah", "1", "asd", "dsa", "qwe", Penyakit.PARAH));
    }
}
