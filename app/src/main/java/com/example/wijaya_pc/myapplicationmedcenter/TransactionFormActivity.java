package com.example.wijaya_pc.myapplicationmedcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransactionFormActivity extends AppCompatActivity {

    EditText edtName;
    EditText edtDescription;
    EditText edtGejala;
    EditText edtObat;
    Spinner spnType;

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_form);
        preferences = getSharedPreferences(LoginActivity.AUTH_SESSION, Context.MODE_PRIVATE);

        spnType = (Spinner) findViewById(R.id.spn_type);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtDescription = (EditText) findViewById(R.id.edt_description);
        edtGejala = (EditText) findViewById(R.id.edt_gejala);
        edtObat = (EditText) findViewById(R.id.edt_obat);

        String type [] = {"Ringan", "Sedang", "Berat", "Parah"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(adapter);
    }

    public void saveTransaction(View view){
        String name = edtName.getText().toString();
        String description = edtDescription.getText().toString();
        String gejala = edtGejala.getText().toString();
        String Obat = edtObat.getText().toString();
        int type = spnType.getSelectedItemPosition()+1;
        HashMap<String, String> data = new HashMap<>();
        if ( name.toString().trim().equals("TBC"))
        {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.update_dialog, null);
            dialogBuilder.setView(dialogView);

            final EditText editTextUpdateName = (EditText) dialogView.findViewById(R.id.edt_update_name);

            dialogBuilder.setTitle("Updating Data Penyakit TBC");
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            editTextUpdateName.setText("TBC");
        }
        else
        {
            data.put("name", name);
            data.put("type", Integer.toString(type));
            data.put("description", description);
            data.put("gejala", gejala);
            data.put("Obat", Obat);
        }

        JsonObjectRequest request = new JsonObjectRequest(EndPoint.TRANSACTION_URL,
                new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("transaction.resp", response.toString());
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
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
}
