package com.example.wijaya_pc.myapplicationmedcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    public static final String AUTH_SESSION = "PERF_AUTH_SESSION";
    SharedPreferences preferences;

    EditText edtEmail, edtPassword;
    Spinner logRole;

    public static final String USER_ROLE = "role";
    public static final String USER_NAME = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(AUTH_SESSION, Context.MODE_PRIVATE);
        String token = preferences.getString("token", null);

        if(token != null){
            Intent it = new Intent(this, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }

        edtEmail = (EditText) findViewById(R.id.log_email);
        edtPassword = (EditText) findViewById(R.id.log_password);
        logRole = (Spinner) findViewById(R.id.log_role);

        String type [] = {"Dokter", "User"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        logRole.setAdapter(adapter);

        Log.d("cek oncreat login", "onCreate: login activity");
    }

    public void saveToken(int uid, String token, String email, String role){
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("token", token);
        editor.putString("email", email);
        editor.putString("role", role);
        editor.putInt("uid", uid);
        editor.commit();
    }
    public void sendRequest(){
        final String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String role = logRole.getSelectedItem().toString();
        HashMap<String, String> param = new HashMap<>();
        param.put("email", email);
        param.put("password", password);
        param.put("role", role);
        if (!(email.isEmpty() || password.isEmpty()))
        {
            JsonObjectRequest request = new JsonObjectRequest(EndPoint.LOGIN_URL, new JSONObject(param), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    int userid = 0;
                    String token = "0";
                    String role = "";
                    String email = "";
                    if ( response.toString() != null){
                        Log.d("response.login", response.toString());
                    }

                    try {
                        userid = response.getInt("uid");
                        token = response.getString("token");
                        role = response.getString("role");
                        email = response.getString("email");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(userid != 0)
                    {
                        saveToken(userid, token, email, role);
                        Intent it = new Intent(getBaseContext(), MainActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        it.putExtra(USER_ROLE, role);
                        it.putExtra(USER_NAME, email);
                        Toast.makeText(getBaseContext(), "Login Sukses, Selamat Datang " + role + " " + email, Toast.LENGTH_SHORT).show();
                        startActivity(it);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Username / Password Salah BosQuee", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("json.response", "" + error.getMessage());
                            Toast.makeText(getBaseContext(), "Username / Password Salah BosQuee", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            RequestQueue rq = Volley.newRequestQueue(this);
            rq.add(request);
        }
       else
        {
            Toast.makeText(this, "Email dan Password Ada yang Kosong", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginAct(View view) {
        sendRequest();
    }


    public void toRegister(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
