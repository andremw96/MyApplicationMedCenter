package com.example.wijaya_pc.myapplicationmedcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView.OnQueryTextListener;
import android.app.SearchManager;
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

public class TransactionListActivity extends AppCompatActivity  {

    private ArrayList<Penyakit> transList = new ArrayList<>();
    private ArrayList<Penyakit> filteredTransList = new ArrayList<>();
    SharedPreferences preferences;

    ArrayAdapter adapter, filteredAdapter;
   // SearchableAdapter adapter;
    ListView transactionList, filteredList;
    android.widget.SearchView search_view;
    Button buttonCari;

    private TextView txtName;
    private TextView txtType;
    private TextView txtDescription;
    private TextView txtGejala;
    private TextView txtObat;

    public static final String PENYAKIT_NAME = "name";
    public static final String PENYAKIT_ID = "id";
    public static final String PENYAKIT_DESCRIPTION = "description";
    public static final String PENYAKIT_GEJALA = "gejala";
    public static final String PENYAKIT_OBAT = "obat";
    public static final String PENYAKIT_TYPE = "type";
    private GenericListFilter<Penyakit> mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        preferences = getSharedPreferences(LoginActivity.AUTH_SESSION, Context.MODE_PRIVATE);

        transactionList = (ListView) findViewById(R.id.list_transactions);
        search_view  = (android.widget.SearchView) findViewById(R.id.search_view);

        buttonCari = (Button) findViewById(R.id.buttonCari);
        buttonCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        dummyData();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, transList);
        //adapter = new SearchableAdapter(this, android.R.layout.simple_list_item_1, transList);
        transactionList.setAdapter(adapter);

       // filteredAdapter = adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredTransList);
        //filteredList.setAdapter(filteredAdapter);

        search_view.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // adapter.getFilter().filter(newText);
               // return false;
                transactionList.setTextFilterEnabled(true);

                if (TextUtils.isEmpty(newText)) {
                    transactionList.clearTextFilter();
                }
                else {
                    transactionList.setFilterText(newText.toString());
                   /* for (int i = 0 ; i<transList.size();i++)
                    {
                        filteredTransList.add(transList.get(i));
                    }*/
                }

                return true;
            }
        });

        transactionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Penyakit model = (Penyakit) adapter.getItem(position);
                // Intent intent = new Intent(getBaseContext(), TransactionDetailActivity.class);
                //intent.putExtra("transaction.detail", transList.get(position));
                // startActivity(intent);

                Penyakit penyakit = transList.get(position);

                Intent intent = new Intent(getApplicationContext(), TransactionDetailActivity.class);
                intent.putExtra(PENYAKIT_ID, penyakit.getId());
                intent.putExtra(PENYAKIT_NAME, penyakit.getName());
                intent.putExtra(PENYAKIT_DESCRIPTION, penyakit.getDescription());
                intent.putExtra(PENYAKIT_GEJALA, penyakit.getGejala());
                intent.putExtra(PENYAKIT_OBAT, penyakit.getObat());
                intent.putExtra(PENYAKIT_TYPE, penyakit.stringType());
                startActivity(intent);

            }
        });

       /* EditCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TransactionListActivity.this.adapter.getFilter().filter(s);
            };

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

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

    protected void onResume() {
        super.onResume();
        sendRequest();
    }

    public void sendRequest(){
        JsonArrayRequest request = new JsonArrayRequest(EndPoint.TRANSACTION_URL, new Response.Listener<JSONArray>() {
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
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, transList);
            //adapter = new SearchableAdapter(this, android.R.layout.simple_list_item_1, transList);
            transactionList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    private void dummyData(){
        transList.add(new Penyakit("1", "Demam Berdarah",
                "Demam akibat digigit nyamuk", "Badan Panas", "Tidur", Penyakit.PARAH));
        transList.add(new Penyakit("2", "TBC",
                "Batuk parah dan berdarah", "Batuk berdarah", "Obat Batuk", Penyakit.PARAH));
        transList.add(new Penyakit("3", "Batuk",
                "Batuk biasa", "Uhuk Uhuk", "OBH", Penyakit.RINGAN));
        transList.add(new Penyakit("4", "Pilek",
                "Pilek biasa", "Ingusan", "Nezoep", Penyakit.RINGAN));
        transList.add(new Penyakit("5", "Flu",
                "Batuk dan Pilek", "Batuk Pilek Bersamaan", "OBH Campur Nezoep", Penyakit.SEDANG));
    }
}
