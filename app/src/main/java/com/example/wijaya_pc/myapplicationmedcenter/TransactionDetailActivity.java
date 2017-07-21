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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.wijaya_pc.myapplicationmedcenter.R.id.btn_list_user;

public class TransactionDetailActivity extends AppCompatActivity {

    private Penyakit transaction;
    private TextView txtName;
    private TextView txtType;
    private TextView txtDescription;
    private TextView txtGejala;
    private TextView txtObat;

    Integer penyakitTypeInt;

    public static final String PENYAKIT_NAME_GEJALA = "name";
    public static final String PENYAKIT_ID_GEJALA = "id";

   /* EditText editTextUpdateName;
    EditText editTextUpdateDesc;
    EditText editTextUpdateGejala;
    EditText editTextUpdateObat;
    Spinner spinnerType;
    Button buttonUpdate;*/

    int success;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        preferences = getSharedPreferences(LoginActivity.AUTH_SESSION, Context.MODE_PRIVATE);

        txtName = (TextView) findViewById(R.id.txt_name);
        txtType = (TextView) findViewById(R.id.txt_type);
        txtDescription = (TextView) findViewById(R.id.txt_description);
        txtGejala = (TextView) findViewById(R.id.txt_gejala);
        txtObat = (TextView) findViewById(R.id.txt_Obat);

        Intent intent = getIntent();
       // transaction = (Penyakit) intent.getSerializableExtra("transaction.detail");

        final String id = intent.getStringExtra(TransactionListActivity.PENYAKIT_ID);
        final String name = intent.getStringExtra(TransactionListActivity.PENYAKIT_NAME);
        final String desc = intent.getStringExtra(TransactionListActivity.PENYAKIT_DESCRIPTION);
        final String gejala = intent.getStringExtra(TransactionListActivity.PENYAKIT_GEJALA);
        final String obat = intent.getStringExtra(TransactionListActivity.PENYAKIT_OBAT);
        final String type = intent.getStringExtra(TransactionListActivity.PENYAKIT_TYPE);

        txtName.setText(name);
        txtType.setText(type);
        txtDescription.setText(desc);
        txtGejala.setText(gejala);
        txtObat.setText(obat);

        /*txtName.setText(transaction.getName());
        txtType.setText(transaction.stringType());
        txtDescription.setText(transaction.getDescription());
        txtGejala.setText(transaction.getGejala());
        txtObat.setText(transaction.getObat());*/

        Button button_lihat_gejala = (Button) findViewById(R.id.btnGejala);
        Button button_delete = (Button) findViewById(R.id.button_delete);
        Button button_update = (Button) findViewById(R.id.button_update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog(id, name, desc, gejala, obat, type);
            }
        });

        button_lihat_gejala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddGejalaActivity.class);
                intent.putExtra(PENYAKIT_ID_GEJALA, id);
                intent.putExtra(PENYAKIT_NAME_GEJALA, name);
                Log.d("idpenyakit", "onClick: "+id);
                Log.d("namapenyakit", "onClick: "+name);
                startActivity(intent);
            }
        });

        String role = preferences.getString("role", "");
        if ( role.toString().trim().equals("User") ) //( txtRole.getText().toString().trim().equals("User") )
        {
            button_update.setVisibility(View.INVISIBLE);
            button_delete.setVisibility(View.INVISIBLE);
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

    private void showUpdateDialog(final String penyakitID, String penyakitName, String penyakitDesc, String penyakitGejala, String penyakitObat, String penyakitType){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextUpdateName = (EditText) dialogView.findViewById(R.id.edt_update_name);
        final EditText editTextUpdateDesc = (EditText) dialogView.findViewById(R.id.edt_update_description);
        final EditText editTextUpdateGejala = (EditText) dialogView.findViewById(R.id.edt_update_gejala);
        final EditText editTextUpdateObat = (EditText) dialogView.findViewById(R.id.edt_update_obat);
        final Spinner spinnerType = (Spinner) dialogView.findViewById(R.id.spn_update_type);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);

        dialogBuilder.setTitle("Updating Data Penyakit "+penyakitName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        if ( penyakitType.toString().trim().equals("Ringan") )
        {
            penyakitTypeInt = 0;
        }
        else if ( penyakitType.toString().trim().equals("Sedang") )
        {
            penyakitTypeInt = 1;
        }
        else if (penyakitType.toString().trim().equals("Berat") )
        {
            penyakitTypeInt = 2;
        }
        else
        {
            penyakitTypeInt = 3;
        }

        editTextUpdateName.setText(penyakitName);
        editTextUpdateDesc.setText(penyakitDesc);
        editTextUpdateGejala.setText(penyakitGejala);
        editTextUpdateObat.setText(penyakitObat);
        String type [] = {"Ringan", "Sedang", "Berat", "Parah"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setSelection(penyakitTypeInt);

        Log.d("print id", "updTransaction: "+penyakitID);
        Log.d("print name", "updTransaction: "+penyakitName);
        Log.d("print description", "updTransaction: "+penyakitDesc);
        Log.d("print gejala", "updTransaction: "+penyakitGejala);
        Log.d("print Obat", "updTransaction: "+penyakitObat);
        Log.d("print type", "updTransaction: "+penyakitType);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namex = editTextUpdateName.getText().toString();
                String descriptionx = editTextUpdateDesc.getText().toString();
                String gejalax = editTextUpdateGejala.getText().toString();
                String Obatx = editTextUpdateObat.getText().toString();
                int typex = spinnerType.getSelectedItemPosition()+1;

                updTransaction(penyakitID, namex, descriptionx, gejalax, Obatx, typex);

                finish();
                alertDialog.dismiss();
            }
        });
    };

    public void delTransaction(View view){
        Intent intent = getIntent();
        // transaction = (Penyakit) intent.getSerializableExtra("transaction.detail");

        String id = intent.getStringExtra(TransactionListActivity.PENYAKIT_ID);
        StringRequest request = new StringRequest(Request.Method.DELETE, EndPoint.TRANSACTION_URL +"/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("delete.response", response.toString());
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("delete.error", error.getMessage());
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void updTransaction(String penyakitID, String penyakitName, String penyakitDesc, String penyakitGejala, String penyakitObat, int penyakitType){
   // public void updTransaction(View view){
       //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

       // LayoutInflater inflater = getLayoutInflater();
        //final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        //dialogBuilder.setView(dialogView);

      //  EditText editTextUpdateName = (EditText) dialogView.findViewById(R.id.edt_update_name);
      //  EditText editTextUpdateDesc = (EditText) dialogView.findViewById(R.id.edt_update_description);
      //  EditText editTextUpdateGejala = (EditText) dialogView.findViewById(R.id.edt_update_gejala);
      //  EditText editTextUpdateObat = (EditText) dialogView.findViewById(R.id.edt_update_obat);
      //  Spinner spinnerType = (Spinner) dialogView.findViewById(R.id.spn_update_type);

       // Intent intent = getIntent();
        // transaction = (Penyakit) intent.getSerializableExtra("transaction.detail");

        Log.d("print id", "updTransaction: "+penyakitID);
        Log.d("print name", "updTransaction: "+penyakitName);
        Log.d("print description", "updTransaction: "+penyakitDesc);
        Log.d("print gejala", "updTransaction: "+penyakitGejala);
        Log.d("print Obat", "updTransaction: "+penyakitObat);
        Log.d("print type", "updTransaction: "+penyakitType);

        HashMap<String, String> data = new HashMap<>();
        data.put("id", penyakitID);
        data.put("name", penyakitName);
        data.put("description", penyakitDesc);
        data.put("gejala", penyakitGejala);
        data.put("Obat", penyakitObat);
        data.put("type", Integer.toString(penyakitType));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, EndPoint.TRANSACTIONEDIT_URL +"/"+penyakitID,
                new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("transaction.resp", response.toString());
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

        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, EndPoint.TRANSACTIONEDIT_URL +"/"+id,
                new JSONObject(data),
                new Response.Listener<JSONObject>() {
                @Override
                public void  onResponse(JSONObject response) {
                    Log.d("update.response", response.toString());
                    //finish();

                   // Log.d("transaction.resp", response.toString());
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    /*try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt(TAG_SUCCESS);

                        // Cek error node pada json
                        if (success == 1) {
                            Log.d("Add/update", jObj.toString());
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                    }
                }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("update.error", error.getMessage());
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);*/
    }
}
