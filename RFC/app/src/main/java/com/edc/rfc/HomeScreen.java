package com.edc.rfc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.edc.rfc.Classes.Book;
import com.edc.rfc.Classes.Subscriber;
import com.edc.rfc.Classes.Toy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    android.support.v7.widget.Toolbar toolbar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    public static final String LOG_TAG = "home_screen_log";

    ArrayList<Subscriber> subscribers = new ArrayList<>();
    ArrayList<String> subscriberNames = new ArrayList<>();

    ArrayList<Toy> toys = new ArrayList<Toy>();
    ArrayList<String> toyIds = new ArrayList<>();

    ArrayList<Book> books = new ArrayList<>();

    CardView issueBook, issueToy, returnBook, returnToy, viewIssuedBooks, viewIssuedToys, viewSubscribers, viewBooks, viewToys, viewSummary;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        toolbar = findViewById(R.id.home_screen_toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences(getString(R.string.sp_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        issueBook = findViewById(R.id.issue_book_card);
        issueToy = findViewById(R.id.issue_toy_card);
        returnBook = findViewById(R.id.return_book_card);
        returnToy = findViewById(R.id.return_toy_card);
        viewIssuedBooks = findViewById(R.id.view_issued_books_card);
        viewIssuedToys = findViewById(R.id.view_issued_toys_card);
        viewSubscribers = findViewById(R.id.view_subscribers_card);
        viewBooks = findViewById(R.id.view_books_card);
        viewToys = findViewById(R.id.view_toys_card);
        viewSummary = findViewById(R.id.view_summary_card);

        issueBook.setOnClickListener(this);
        issueToy.setOnClickListener(this);
        returnBook.setOnClickListener(this);
        returnToy.setOnClickListener(this);
        viewIssuedBooks.setOnClickListener(this);
        viewIssuedToys.setOnClickListener(this);
        viewSubscribers.setOnClickListener(this);
        viewBooks.setOnClickListener(this);
        viewToys.setOnClickListener(this);
        viewSummary.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.log_out){
            logOut();
        }

        return true;
    }

    public void logOut(){

        new LogoutAST().execute();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.issue_book_card:
                // Toast.makeText(this, "issue_book_card", Toast.LENGTH_SHORT).show();
                issueBookClicked();
                break;

            case R.id.issue_toy_card:
                // Toast.makeText(this, "issue_toy_card", Toast.LENGTH_SHORT).show();
                try {
                    issueToyClicked();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "button unavailable!\n" + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.v(LOG_TAG, e.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "button unavailable!\n" + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.v(LOG_TAG, e.toString());
                }
                break;

            case R.id.return_book_card:
                // Toast.makeText(this, "return_book_card", Toast.LENGTH_SHORT).show();
                returnBookClicked();
                break;

            case R.id.return_toy_card:
                // Toast.makeText(this, "return_toy_card", Toast.LENGTH_SHORT).show();
                returnToyClicked();
                break;

            case R.id.view_issued_books_card:
                // Toast.makeText(this, "view_issued_books_card", Toast.LENGTH_SHORT).show();
                viewIssuedBooksClicked();
                break;

            case R.id.view_issued_toys_card:
                // Toast.makeText(this, "view_issued_toys_card", Toast.LENGTH_SHORT).show();
                viewIssuedToysClicked();
                break;

            case R.id.view_subscribers_card:
                // Toast.makeText(this, "view_subscribers_card", Toast.LENGTH_SHORT).show();
                viewSubscribersClicked();
                break;

            case R.id.view_books_card:
                // Toast.makeText(this, "view_books_card", Toast.LENGTH_SHORT).show();
                viewBooksClicked();
                break;

            case R.id.view_toys_card:
                // Toast.makeText(this, "view_toys_card", Toast.LENGTH_SHORT).show();
                viewToysClicked();
                break;

            case R.id.view_summary_card:
                // Toast.makeText(this, "view_summary_card", Toast.LENGTH_SHORT).show();
                viewSummaryClicked();
                break;

        }

    }

    private void issueToyClicked() throws ExecutionException, InterruptedException {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.issue_return_prompt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );

        builder.setView(prompt);

        final AutoCompleteTextView toyId = prompt.findViewById(R.id.prompt_id);
        toyId.setHint("issued toy id...");
        toyId.setText("TL/SB/");

        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        subscriberId.setHint("Subscriber Name...");

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.issue);

        final ArrayAdapter<String> adapterSubscriber = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new GetSubscribers().execute().get());
        subscriberId.setAdapter(adapterSubscriber);

        final ArrayAdapter<String> adapterToy = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new GetToys().execute().get());
        toyId.setAdapter(adapterToy);

        builder
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(HomeScreen.this, "" + subscriberNames.get(which), Toast.LENGTH_SHORT).show();
                        Toast.makeText(HomeScreen.this, "" + subscriberId.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }

    private void issueBookClicked() {

    }

    private void returnBookClicked() {

    }

    private void returnToyClicked() {

    }

    private void viewIssuedBooksClicked() {

    }

    private void viewIssuedToysClicked() {

    }

    private void viewSubscribersClicked() {

    }

    private void viewBooksClicked() {

    }

    private void viewToysClicked() {

    }

    private void viewSummaryClicked() {

    }

    private class LogoutAST extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(HomeScreen.this);
            progressDialog.setMessage("logging you out...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            editor.putBoolean(getString(R.string.sp_session), false);
            editor.putString(getString(R.string.sp_username), "");
            editor.commit();

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Intent toLogin = new Intent(HomeScreen.this, LoginScreen.class);
            toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toLogin);
        }
    }

    private class GetSubscribers extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(getString(R.string.get_subscribers_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

                if (response.toString().isEmpty()){
                    return subscriberNames;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject subscriber = root.getJSONObject(i);
                        Subscriber current = new Subscriber();
                        current.setId(subscriber.getString("subscriber_id"));
                        current.setName(subscriber.getString("subscriber_name"));

                        subscriberNames.add(subscriber.getString("subscriber_name"));

                        subscribers.add(current);

                    }

                    return subscriberNames;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return subscriberNames;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return subscriberNames;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return subscriberNames;
            }

        }

    }

    private class GetToys extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(getString(R.string.get_toys_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

                if (response.toString().isEmpty()){
                    return toyIds;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject toy = root.getJSONObject(i);
                        Toy current = new Toy();
                        current.setToyId(toy.getString("toy_id"));
                        current.setToyName(toy.getString("toy_name"));

                        toyIds.add(toy.getString("toy_id"));

                        toys.add(current);

                    }

                    return toyIds;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return toyIds;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return toyIds;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return toyIds;
            }

        }

    }
}
