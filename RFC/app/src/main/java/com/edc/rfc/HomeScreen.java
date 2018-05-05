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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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

    ArrayList<Subscriber> subscribers = new ArrayList<>();
    ArrayList<String> subscriberNames = new ArrayList<>();

    ArrayList<Subscriber> issuedToSubscribers = new ArrayList<>();
    ArrayList<String> issuedToSubscriberNames = new ArrayList<>();

    ArrayList<Toy> toys = new ArrayList<Toy>();
    ArrayList<String> toyIds = new ArrayList<>();

    ArrayList<Book> books = new ArrayList<>();
    ArrayList<String> bookIds = new ArrayList<>();

    ArrayList<Book> issuedBooks = new ArrayList<>();
    ArrayList<String> issuedBookIds = new ArrayList<>();

    ArrayList<Toy> issuedToys = new ArrayList<>();
    ArrayList<String> issuedToyIds = new ArrayList<>();

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
                try {
                    issueBookClicked();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                try {
                    returnBookClicked();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.return_toy_card:
                // Toast.makeText(this, "return_toy_card", Toast.LENGTH_SHORT).show();
                try {
                    returnToyClicked();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.view_issued_books_card:
                // Toast.makeText(this, "view_issued_books_card", Toast.LENGTH_SHORT).show();
                try {
                    viewIssuedBooksClicked();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.view_issued_toys_card:
                // Toast.makeText(this, "view_issued_toys_card", Toast.LENGTH_SHORT).show();
                try {
                    viewIssuedToysClicked();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.view_subscribers_card:
                // Toast.makeText(this, "view_subscribers_card", Toast.LENGTH_SHORT).show();
                try {
                    viewSubscribersClicked();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.view_books_card:
                // Toast.makeText(this, "view_books_card", Toast.LENGTH_SHORT).show();
                try {
                    viewBooksClicked();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.view_toys_card:
                // Toast.makeText(this, "view_toys_card", Toast.LENGTH_SHORT).show();
                try {
                    viewToysClicked();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.view_summary_card:
                // Toast.makeText(this, "view_summary_card", Toast.LENGTH_SHORT).show();
                try {
                    viewSummaryClicked();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

        }

    }


    // Done :--------------------------------------------------------------------------- //
    private void issueToyClicked() throws ExecutionException, InterruptedException {

        progressDialog.setMessage("please wait....");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.issue_return_prompt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );

        builder.setView(prompt);

        final AutoCompleteTextView toyId = prompt.findViewById(R.id.prompt_id);
        toyId.setHint("issued toy id...");
        toyId.setText("TL/SB/");
        toyId.setSelection(toyId.getText().toString().length());

        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        subscriberId.setHint("Subscriber Name...");

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.issue);

        final ArrayAdapter<String> adapterSubscriber = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new GetSubscribers().execute().get());
        subscriberId.setAdapter(adapterSubscriber);

        final ArrayAdapter<String> adapterToy = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new GetToys().execute().get());
        toyId.setAdapter(adapterToy);

        progressDialog.dismiss();

        builder
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(HomeScreen.this, "" + subscriberNames.get(which), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(HomeScreen.this, "" + subscriberId.getText().toString(), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(HomeScreen.this, "\n" + toyId.getText().toString(), Toast.LENGTH_SHORT).show();
                        new Protocol().execute("toy", toyId.getText().toString(), subscriberId.getText().toString(), "issue");
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
    // --------------------------------------------------------------------------------- //

    // Done :--------------------------------------------------------------------------- //
    private void issueBookClicked() throws ExecutionException, InterruptedException {

        progressDialog.setMessage("please wait....");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.issue_return_prompt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );

        builder.setView(prompt);

        final AutoCompleteTextView bookId = prompt.findViewById(R.id.prompt_id);
        bookId.setHint("issued book id...");
        bookId.setText("SB-");
        bookId.setSelection(bookId.getText().toString().length());

        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        subscriberId.setHint("Subscriber Name...");

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.issue);

        final ArrayAdapter<String> adapterSubscriber = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new GetSubscribers().execute().get());
        subscriberId.setAdapter(adapterSubscriber);

        final ArrayAdapter<String> adapterToy = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new GetBooks().execute().get());
        bookId.setAdapter(adapterToy);

        progressDialog.dismiss();

        builder
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(HomeScreen.this, "" + subscriberNames.get(which), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(HomeScreen.this, "" + subscriberId.getText().toString(), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(HomeScreen.this, "\n" + bookId.getText().toString(), Toast.LENGTH_SHORT).show();
                        new Protocol().execute("book", bookId.getText().toString(), subscriberId.getText().toString(), "issue");
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
    // --------------------------------------------------------------------------------- //

    // Done :--------------------------------------------------------------------------- //
    private void returnBookClicked() throws ExecutionException, InterruptedException {

        progressDialog.setMessage("please wait....");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.issue_return_prompt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );

        builder.setView(prompt);

        final AutoCompleteTextView bookId = prompt.findViewById(R.id.prompt_id);
        bookId.setHint("returned book id...");
        bookId.setText("SB-");
        bookId.setSelection(bookId.getText().length());

        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        subscriberId.setSelection(subscriberId.getText().length());
        final ArrayAdapter<String> adapterSubscriber = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new GetIssuedToSubscribers().execute().get());
        subscriberId.setAdapter(adapterSubscriber);

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.return_arrow);

        final ArrayAdapter<String> adapterBook = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new GetIssuedBooks().execute().get());
        bookId.setAdapter(adapterBook);

        progressDialog.dismiss();

        builder
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(HomeScreen.this, "" + subscriberNames.get(which), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(HomeScreen.this, "" + bookId.getText().toString(), Toast.LENGTH_SHORT).show();
                        new Protocol().execute("book", bookId.getText().toString(), subscriberId.getText().toString(), "return");
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
    // --------------------------------------------------------------------------------- //

    // Done :--------------------------------------------------------------------------- //
    private void returnToyClicked() throws ExecutionException, InterruptedException {

        progressDialog.setMessage("please wait...");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.issue_return_prompt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );

        builder.setView(prompt);

        final AutoCompleteTextView toyId = prompt.findViewById(R.id.prompt_id);
        toyId.setHint("returned toy id...");
        toyId.setText("TL/SB/");
        toyId.setSelection(toyId.getText().length());

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.return_arrow);

        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        subscriberId.setText("SB/Lib/");
        subscriberId.setSelection(subscriberId.getText().length());
        final ArrayAdapter<String> adapterSubscriber = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new GetIssuedToSubscribers().execute().get());
        subscriberId.setAdapter(adapterSubscriber);

        final ArrayAdapter<String> adapterToy = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new GetIssuedToys().execute().get());
        toyId.setAdapter(adapterToy);

        progressDialog.dismiss();

        builder
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(HomeScreen.this, "" + subscriberNames.get(which), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(HomeScreen.this, "" + toyId.getText().toString(), Toast.LENGTH_SHORT).show();
                        new Protocol().execute("toy", toyId.getText().toString(), subscriberId.getText().toString(), "return");
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
    // --------------------------------------------------------------------------------- //

    private void viewIssuedBooksClicked() throws ExecutionException, InterruptedException {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.view_stuff, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );

        builder.setView(prompt);

        final RecyclerView promptRV = prompt.findViewById(R.id.prompt_recycler_view);
        promptRV.setLayoutManager(new LinearLayoutManager(this));

    }

    private void viewIssuedToysClicked() throws ExecutionException, InterruptedException {

    }

    private void viewSubscribersClicked() throws ExecutionException, InterruptedException {

    }

    private void viewBooksClicked() throws ExecutionException, InterruptedException {

    }

    private void viewToysClicked() throws ExecutionException, InterruptedException {

    }

    private void viewSummaryClicked() throws ExecutionException, InterruptedException {
    }


    /* Async Tasks *********************************************************************/
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
        protected void onPreExecute() {
            progressDialog.setMessage("please wait....");
            progressDialog.show();
        }

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

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            progressDialog.dismiss();
        }
    }

    private class GetIssuedToSubscribers extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("please wait....");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            progressDialog.dismiss();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(getString(R.string.get_issued_to_subscribers_url));
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
                    return issuedToSubscriberNames;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject subscriber = root.getJSONObject(i);
                        Subscriber current = new Subscriber();
                        current.setId(subscriber.getString("subscriber_id"));
                        current.setName(subscriber.getString("subscriber_name"));

                        issuedToSubscriberNames.add(subscriber.getString("subscriber_name"));

                        issuedToSubscribers.add(current);

                    }

                    return issuedToSubscriberNames;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return issuedToSubscriberNames;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return issuedToSubscriberNames;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return issuedToSubscriberNames;
            }

        }

    }

    private class GetToys extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("please wait....");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            progressDialog.dismiss();
        }

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

    private class GetBooks extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("please wait....");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            progressDialog.dismiss();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(getString(R.string.get_books_url));
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
                    return bookIds;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject book = root.getJSONObject(i);
                        Book current = new Book();
                        current.setBookId(book.getString("book_id"));
                        current.setBookName(book.getString("book_name"));

                        bookIds.add(book.getString("book_id"));

                        books.add(current);

                    }

                    return bookIds;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return bookIds;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return bookIds;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return bookIds;
            }

        }

    }

    private class GetIssuedBooks extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("please wait....");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            progressDialog.dismiss();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(getString(R.string.get_issued_books_url));
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
                    return issuedBookIds;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject book = root.getJSONObject(i);
                        Book current = new Book();
                        current.setBookId(book.getString("book_id"));
                        current.setBookName(book.getString("book_name"));

                        issuedBookIds.add(book.getString("book_id"));

                        issuedBooks.add(current);

                    }

                    return issuedBookIds;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return issuedBookIds;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return issuedBookIds;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return issuedBookIds;
            }

        }

    }

    private class GetIssuedToys extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("please wait....");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            progressDialog.dismiss();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(getString(R.string.get_issued_toys_url));
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
                    return issuedToyIds;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject toy = root.getJSONObject(i);
                        Toy current = new Toy();
                        current.setToyId(toy.getString("toy_id"));
                        current.setToyName(toy.getString("toy_name"));

                        issuedToyIds.add(toy.getString("book_id"));

                        issuedToys.add(current);

                    }

                    return issuedToyIds;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return issuedToyIds;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return issuedToyIds;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return issuedToyIds;
            }

        }

    }

    private class Protocol extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String id = strings[1];
            String sub = strings[2];
            String protocol = strings[3];

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(getString(R.string.protocol_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));

                String data = URLEncoder.encode("type", "UTF-8") +"="+ URLEncoder.encode(type, "UTF-8") +"&"+
                        URLEncoder.encode("id", "UTF-8") +"="+ URLEncoder.encode(id, "UTF-8") +"&"+
                        URLEncoder.encode("protocol", "UTF-8") +"="+ URLEncoder.encode(protocol, "UTF-8") +"&"+
                        URLEncoder.encode("sub", "UTF-8") +"="+ URLEncoder.encode(sub, "UTF-8");

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
                        Log.v(LOG_TAG, e.toString());
                        return "IO: " + e.toString();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.contains("success")){

                Toast.makeText(HomeScreen.this, "book issued successfully", Toast.LENGTH_SHORT).show();

            } else if(s.contains("fail_issue")) {

                Toast.makeText(HomeScreen.this, "book issue failed.\nTry again later.\n: " + s, Toast.LENGTH_SHORT).show();

            } else if(s.contains("wrong type")){

                Toast.makeText(HomeScreen.this, "book issue failed.\nTry again later.\n: " + s, Toast.LENGTH_SHORT).show();

            }
        }

    }
    /* **********************************************************************************/
}
