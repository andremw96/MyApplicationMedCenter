package com.example.wijaya_pc.myapplicationmedcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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

public class TransactionListGejalaActivity extends AppCompatActivity {

    private ArrayList<Gejala> ListGejala = new ArrayList<>();
    SharedPreferences preferences;

    ArrayAdapter adapter;
    ListView listViewGejala;

    android.widget.SearchView search_view;

    public static final String PENYAKIT_NAME = "name";
    public static final String PENYAKIT_ID = "id";
    public static final String PENYAKIT_DESCRIPTION = "description";
    public static final String PENYAKIT_GEJALA = "gejala";
    public static final String PENYAKIT_OBAT = "obat";
    public static final String PENYAKIT_TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list_gejala);

        preferences = getSharedPreferences(LoginActivity.AUTH_SESSION, Context.MODE_PRIVATE);

        listViewGejala = (ListView) findViewById(R.id.list_transactions_gejala);

        search_view  = (android.widget.SearchView) findViewById(R.id.search_view);

        dummyData();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ListGejala);
        listViewGejala.setAdapter(adapter);

        search_view.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // adapter.getFilter().filter(newText);
                // return false;
                listViewGejala.setTextFilterEnabled(true);

                if (TextUtils.isEmpty(newText)) {
                    listViewGejala.clearTextFilter();
                }
                else {
                    listViewGejala.setFilterText(newText.toString());
                }

                return true;
            }
        });

        listViewGejala.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Intent intent = new Intent(getBaseContext(),
                        TransactionDetailActivity.class);
                intent.putExtra("transaction.detail", transListGejala.get(position));
                startActivity(intent);*/

                Gejala penyakit = ListGejala.get(position);

                Intent intent = new Intent(getApplicationContext(), TransactionDetailActivity.class);
                intent.putExtra(PENYAKIT_ID, penyakit.getIdPenyakit());
                intent.putExtra(PENYAKIT_NAME, penyakit.getNamaPenyakit());
                intent.putExtra(PENYAKIT_DESCRIPTION, penyakit.getDescriptionPenyakit());
                intent.putExtra(PENYAKIT_GEJALA, penyakit.getGejalaPenyakit());
                intent.putExtra(PENYAKIT_OBAT, penyakit.getObatPenyakit());
                intent.putExtra(PENYAKIT_TYPE, penyakit.stringType());
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        sendRequest();
    }

    public void sendRequest(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, EndPoint.GETALLGEJALA_URL, null, new Response.Listener<JSONArray>()
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
}
