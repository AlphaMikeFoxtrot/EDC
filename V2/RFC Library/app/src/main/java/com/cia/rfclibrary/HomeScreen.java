package com.cia.rfclibrary;

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
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    android.support.v7.widget.Toolbar toolbar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    public static final String LOG_TAG = "home_screen_log";

    ArrayList<String> subscriberNames = new ArrayList<>();
    ArrayList<String> bookIds = new ArrayList<>();
    ArrayList<String> toyIds = new ArrayList<>();

    CardView issueBook,
            issueToy,
            returnBook,
            returnToy,
            viewIssuedBooks,
            viewIssuedToys,
            viewSubscribers,
            viewBooks,
            viewToys,
            viewSummary;

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

        progressDialog = new ProgressDialog(this);

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
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.issue_book_card:
                // Toast.makeText(this, "issue_book_card", Toast.LENGTH_SHORT).show();
                issueBookClicked();
                break;

            case R.id.issue_toy_card:
                // Toast.makeText(this, "issue_toy_card", Toast.LENGTH_SHORT).show();
                issueToyClicked();
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
                Toast.makeText(this, "view_issued_books_card", Toast.LENGTH_SHORT).show();break;

            case R.id.view_issued_toys_card:
                Toast.makeText(this, "view_issued_toys_card", Toast.LENGTH_SHORT).show();
                break;

            case R.id.view_subscribers_card:
                Toast.makeText(this, "view_subscribers_card", Toast.LENGTH_SHORT).show();
                break;

            case R.id.view_books_card:
                Toast.makeText(this, "view_books_card", Toast.LENGTH_SHORT).show();
                break;

            case R.id.view_toys_card:
                Toast.makeText(this, "view_toys_card", Toast.LENGTH_SHORT).show();
                break;

            case R.id.view_summary_card:
                Toast.makeText(this, "view_summary_card", Toast.LENGTH_SHORT).show();break;

        }

    }

    private void issueBookClicked(){

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.issue_return_pormpt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(prompt);

        final AutoCompleteTextView toyId = prompt.findViewById(R.id.prompt_id);
        toyId.setHint("issued book id...");
        toyId.setText("SB-");
        toyId.setSelection(toyId.getText().toString().length());

        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        subscriberId.setHint("Subscriber Name...");

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.issue);

        builder
                .setTitle("Issue Book")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "submit clicked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "cancel clicked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void returnBookClicked(){

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.issue_return_pormpt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(prompt);

        final AutoCompleteTextView toyId = prompt.findViewById(R.id.prompt_id);
        toyId.setHint("returned book id...");
        toyId.setText("SB-");
        toyId.setSelection(toyId.getText().toString().length());

        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        subscriberId.setHint("Subscriber Name...");

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.return_arrow);

        builder
                .setTitle("Return Book")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "submit clicked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "cancel clicked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void issueToyClicked(){

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.issue_return_pormpt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(prompt);

        final AutoCompleteTextView toyId = prompt.findViewById(R.id.prompt_id);
        toyId.setHint("issued toy id...");
        toyId.setText("TL/SB/");
        toyId.setSelection(toyId.getText().toString().length());

        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        subscriberId.setHint("Subscriber Name...");

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.issue);

        builder
                .setTitle("Issue Toy")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "submit clicked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "cancel clicked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void returnToyClicked(){

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.issue_return_pormpt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(prompt);

        final AutoCompleteTextView toyId = prompt.findViewById(R.id.prompt_id);
        toyId.setHint("returned toy id...");
        toyId.setText("TL/SB/");
        toyId.setSelection(toyId.getText().toString().length());

        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        subscriberId.setHint("Subscriber Name...");

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.return_arrow);

        builder
                .setTitle("Return Toy")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "submit clicked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "cancel clicked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void viewIssuedBooksClicked(){

    }

    private void viewIssuedToysClicked(){

    }

    private void viewBooksClicked(){

    }

    private void viewToysClicked(){

    }

    private void viewSubscribersClicked(){

    }

    private void viewSummaryClicked(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.hs_logout){
            // Toast.makeText(this, "LOGOUT clicked", Toast.LENGTH_SHORT).show();
            logOut();
        }
        return true;
    }

    public void logOut(){

        new LogoutAST().execute();

    }

    private class LogoutAST extends AsyncTask<Void, Void, String> {

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
            Intent toLogin = new Intent(HomeScreen.this, LoginActivity.class);
            toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toLogin);
        }
    }

    private class ProtocolAST extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("running protocol");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String protocol = strings[0];
            String type = strings[1];
            String obj_id = strings[2];
            String sub_id = strings[3];

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(getString(R.string.protocol_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));

                String data = URLEncoder.encode("protocol", "UTF-8") +"="+ URLEncoder.encode(protocol, "UTF-8") +"&"+
                        URLEncoder.encode("type", "UTF-8") +"="+ URLEncoder.encode(type, "UTF-8") +"&"+
                        URLEncoder.encode("id", "UTF-8") +"="+ URLEncoder.encode(obj_id, "UTF-8") +"&"+
                        URLEncoder.encode("sub", "UTF-8") +"="+ URLEncoder.encode(sub_id, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return "URL: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return "IO: " + e.toString();
            } finally {
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
                if(bufferedReader != null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.contains("success")){
                Toast.makeText(HomeScreen.this, "success", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetBookIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    private class GetToyIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    private class GetSubscriberNamesAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    private class GetBooksAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    private class GetToysAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    private class GetSubscribersAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    private class GetIssuedBookIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    private class GetIssuedToyIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    private class GetIssuedToSubscriberNamesAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

}
