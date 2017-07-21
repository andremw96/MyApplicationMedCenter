package com.example.wijaya_pc.myapplicationmedcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    ArrayList transList;

    TextView txtRole, txtuname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtRole = (TextView) findViewById(R.id.textView6);
        txtuname = (TextView) findViewById(R.id.textView8);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TransactionFormActivity.class);
                startActivity(intent);
            }
        });

        Button btnListTransactions = (Button) findViewById(R.id.btn_list_transactions);
        btnListTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TransactionListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_list_gejala = (Button) findViewById(R.id.btn_list_gejala);
        btn_list_gejala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TransactionListGejalaActivity.class);
                startActivity(intent);
            }
        });

       Button btn_list_user = (Button) findViewById(R.id.btn_list_user);
        btn_list_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TransactionListDoctor.class);
                startActivity(intent);
            }
        });

        preferences = getSharedPreferences(LoginActivity.AUTH_SESSION, Context.MODE_PRIVATE);
        String token = preferences.getString("token", null);
        if(token == null){
            Intent it = new Intent(this, LoginActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }

        //String uid = Integer.toString(preferences.getInt("uid", 0));
        //Intent intent = getIntent();
        //String role = intent.getStringExtra(LoginActivity.USER_ROLE);
        //String uname = intent.getStringExtra(LoginActivity.USER_NAME);
        String role = preferences.getString("role", "");
        String email = preferences.getString("email", "");
        txtRole.setText("Halaman Utama " + role);
        txtuname.setText("Halo, " + email);
        //txtRole.setVisibility(View.GONE);

        if ( role.toString().trim().equals("User") ) //( txtRole.getText().toString().trim().equals("User") )
        {
            fab.setVisibility(View.INVISIBLE);
            btn_list_user.setVisibility(View.GONE);
            //txtRole.setText("Rolenya adalaahhhhh " + role);
        }

        Log.d("cek on create", "onCreate: main activity");
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

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest();
    }

    public void sendRequest(){
        JsonArrayRequest request = new JsonArrayRequest(EndPoint.TRANSACTION_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        requestAction(response);
                        //updateStat(transList);
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
        Penyakit trans;
        transList = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject objRes = (JSONObject) response.get(i);
                trans = new Penyakit(objRes.getString("id"),
                        objRes.getString("name"),
                        objRes.getString("description"),
                        objRes.getString("gejala"),
                        objRes.getString("obat"),
                        objRes.getInt("type"));
                transList.add(trans);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
