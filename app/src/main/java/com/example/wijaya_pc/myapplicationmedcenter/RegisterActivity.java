package com.example.wijaya_pc.myapplicationmedcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private GoogleApiClient client;

    EditText txtEmail, txtPassword, txtConfirm;
    Spinner spnRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        txtEmail = (EditText) findViewById(R.id.reg_email);
        txtPassword = (EditText) findViewById(R.id.reg_password);
        txtConfirm = (EditText) findViewById(R.id.reg_confirm);
       // txtRole = (EditText) findViewById(R.id.reg_role);
          spnRole = (Spinner) findViewById(R.id.spnRole);

        String type [] = {"Dokter", "User"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRole.setAdapter(adapter);
    }

    public void register(View view){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String confirm = txtConfirm.getText().toString();
        String role = spnRole.getSelectedItem().toString();

        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);
        data.put("role", role);

        if (!(email.isEmpty() || password.isEmpty()))
            if(password.equals(confirm)){
                JsonObjectRequest request = new JsonObjectRequest(EndPoint.REGISTER_URL,
                        new JSONObject(data),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("register.resp", response.toString());
                                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getBaseContext(), "Register Error", Toast.LENGTH_SHORT);
                                error.printStackTrace();
                            }
                        }
                );
                RequestQueue rq = Volley.newRequestQueue(this);
                rq.add(request);
            } else {
                Toast.makeText(this, "Password dan Konfirmasi harus sama", Toast.LENGTH_SHORT).show();
                txtPassword.selectAll();
            }
        else
        {
            Toast.makeText(this, "Email dan Password Ada yang Kosong", Toast.LENGTH_SHORT).show();
        }
    }

    public void toLogin(View view){
        finish();
    }


}
