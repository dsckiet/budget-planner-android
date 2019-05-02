package tech.dsckiet.budgetbucket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionsActivity extends AppCompatActivity {


    private RecyclerView recyclerViewTransactionMore;
    private MoreTransactionAdapter mMoreAdapter;
    private ArrayList<MoreTransactionItem> mMoreTransactionList;
    private RequestQueue mQueue;
    private FirebaseAuth mAuth;

    private String mail() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String mail = user.getEmail();
        return mail;
    }

    String URL_POST = "https://tranquil-coast-71727.herokuapp.com/api/v1/transactions_in_last_month/" + mail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        mMoreTransactionList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(this);
        parseJSON();

    }

    private void parseJSON() {

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_POST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("transactions");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String type = hit.getString("type");
                                String amount = hit.getString("amount");

                                Log.e("Skipping", "onResponse: " + "YES");
                                mMoreTransactionList.add(new MoreTransactionItem(type, amount));

                            }
                            recyclerViewTransactionMore = findViewById(R.id.recycler_view_more_transaction);
                            recyclerViewTransactionMore.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerViewTransactionMore.setLayoutManager(llm);


                            mMoreAdapter = new MoreTransactionAdapter(TransactionsActivity.this, mMoreTransactionList);

                            recyclerViewTransactionMore.setAdapter(mMoreAdapter);
                            mMoreAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);

    }
}

