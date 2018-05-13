package com.cia.rfclibrary;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cia.rfclibrary.Adapters.Book.BookRVAdapter;
import com.cia.rfclibrary.Adapters.Book.IssuedBookRVAdapter;
import com.cia.rfclibrary.Adapters.Subscriber.SubscriberRVAdapter;
import com.cia.rfclibrary.Adapters.Toy.IssuedToyRVAdapter;
import com.cia.rfclibrary.Adapters.Toy.ToyRVAdapter;
import com.cia.rfclibrary.Classes.Book;
import com.cia.rfclibrary.Classes.Subscriber;
import com.cia.rfclibrary.Classes.Toy;
import com.google.android.gms.stats.internal.G;

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

public class ViewActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    TextView error, title;

    ProgressBar progressBar;

    private final String LOG_TAG = "view_act";

    ProgressDialog progressDialog;

    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Book> issuedBooks = new ArrayList<>();
    ArrayList<Subscriber> subscribers = new ArrayList<>();;
    ArrayList<Toy> toys = new ArrayList<>();
    ArrayList<Toy> issuedToys = new ArrayList<>();

    BookRVAdapter bookAdapter;
    IssuedBookRVAdapter issuedBookAdapter;
    ToyRVAdapter toyAdapter;
    IssuedToyRVAdapter issuedToyAdapter;
    SubscriberRVAdapter subscriberAdapter;

    android.support.v7.widget.Toolbar toolbar;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        progressDialog = new ProgressDialog(this);
        mRecyclerView = findViewById(R.id.view_act_rv);
        progressBar = findViewById(R.id.view_stuff_bar);
        error = findViewById(R.id.view_act_error);
        title = findViewById(R.id.view_stuff_toolbar_title);
        toolbar = findViewById(R.id.view_stuff_toolbar);
        setSupportActionBar(toolbar);

        mode = getIntent().getIntExtra("mode", 0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        switch (mode){

            /*
             * MODE CODES:
             * 100 : books
             * 150 : issued books
             * 200 : subscribers
             * 300 : toys
             * 350 : issued toys
             */

            case 100:
                // books
                title.setText("View Books");
                GetBooksAST book_ast = new GetBooksAST();
                book_ast.execute();
                break;

            case 150:
                // issued books
                title.setText("View Issued Books");
                GetIssuedBooksAST issued_book_ast = new GetIssuedBooksAST();
                issued_book_ast.execute();
                break;

            case 200:
                // subscribers
                title.setText("View Subscribers");
                GetSubscribersAST getSubscribersAST = new GetSubscribersAST();
                getSubscribersAST.execute();
                break;

            case 300:
                // toys
                title.setText("View Toys");
                GetToysAST getToysAST = new GetToysAST();
                getToysAST.execute();
                break;

            case 350:
                // issued toys
                title.setText("View Issued Toys");
                GetIssuedToysAST getIssuedToysAST = new GetIssuedToysAST();
                getIssuedToysAST.execute();
                break;

            default:
                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.view_subscribers_refresh){

            new GetSubscribersAST().execute();

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        if(mode != 200) {

            getMenuInflater().inflate(R.menu.view_stuff_menu, menu);
            MenuItem menuItem = menu.findItem(R.id.view_stuff_search);
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

//                progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);

                    switch (mode) {

                        case 100:
                            // books
                            try {

                                String json = new SearchAST().execute("100", query).get();
                                if (json.isEmpty()) {
                                    setSearchError();
                                } else {

                                    ArrayList<Book> list = new ArrayList<>();
                                    JSONArray root = new JSONArray(json.toString());
                                    for (int i = 0; i < root.length(); i++) {

                                        JSONObject iBook = root.getJSONObject(i);
                                        Book book = new Book();
                                        book.setBookId(iBook.getString("book_id"));
                                        book.setBookName(iBook.getString("book_name"));
                                        book.setIsIssued(iBook.getString("is_issued"));
                                        book.setIssuedToName(iBook.getString("issued_to_name"));
                                        list.add(book);

                                    }

                                    books.clear();
                                    books = list;
                                    BookRVAdapter adapter = new BookRVAdapter(ViewActivity.this, list);
                                    mRecyclerView.setAdapter(adapter);
////                                progressBar.setVisibility(View.INVISIBLE);

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case 150:
                            // issued books
                            try {

                                String json = new SearchAST().execute("150", query).get();
                                if (json.isEmpty()) {
                                    setSearchError();
                                } else {

                                    ArrayList<Book> list = new ArrayList<>();
                                    JSONArray root = new JSONArray(json.toString());
                                    for (int i = 0; i < root.length(); i++) {

                                        JSONObject iBook = root.getJSONObject(i);
                                        Book book = new Book();
                                        book.setBookId(iBook.getString("book_id"));
                                        book.setBookName(iBook.getString("book_name"));
                                        book.setIsIssued(iBook.getString("is_issued"));
                                        book.setIssuedToName(iBook.getString("issued_to_name"));
                                        list.add(book);

                                    }

                                    issuedBooks.clear();
                                    issuedBooks = list;
                                    BookRVAdapter adapter = new BookRVAdapter(ViewActivity.this, issuedBooks);
                                    mRecyclerView.setAdapter(adapter);

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case 200:
                            // subscribers
                            try {

                                String json = new SearchAST().execute("200", query).get();
                                if (json.isEmpty()) {
                                    setSearchError();
                                } else {

                                    ArrayList<Subscriber> list = new ArrayList<>();
                                    JSONArray root = new JSONArray(json.toString());
                                    for (int i = 0; i < root.length(); i++) {

                                        JSONObject iSubscriber = root.getJSONObject(i);
                                        Subscriber subscriber = new Subscriber();
                                        subscriber.setId(iSubscriber.getString("subscriber_id"));
                                        subscriber.setName(iSubscriber.getString("subscriber_name"));
                                        subscriber.setBookIssued(iSubscriber.getString("book_issued"));
                                        subscriber.setToyIssued(iSubscriber.getString("toy_issued"));
                                        subscriber.setIsGen(iSubscriber.getString("is_gen"));
                                        subscriber.setIsToy(iSubscriber.getString("is_toy"));
                                        subscriber.setEnrolledFor(iSubscriber.getString("subscriber_enrolled_for"));
                                        subscriber.setEnrolledOn(iSubscriber.getString("subscriber_enrolled_on"));
                                        subscriber.setEnrollmentType(iSubscriber.getString("subscriber_enrollement_type"));
                                        subscriber.setLeb(iSubscriber.getString("subscriber_local_education_board"));
                                        subscriber.setReb(iSubscriber.getString("subscriber_regional_education_board"));
                                        subscriber.setCenter(iSubscriber.getString("subscriber_center"));
                                        subscriber.setPhone(iSubscriber.getString("subscriber_phone"));
                                        subscriber.setDob(iSubscriber.getString("subscriber_date_of_birth"));
                                        subscriber.setGender(iSubscriber.getString("subscriber_gender"));
                                        list.add(subscriber);

                                    }

                                    subscribers.clear();
                                    subscribers = list;
                                    SubscriberRVAdapter adapter = new SubscriberRVAdapter(ViewActivity.this, subscribers);
                                    mRecyclerView.setAdapter(adapter);

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case 300:
                            // toys
                            try {

                                String json = new SearchAST().execute("300", query).get();
                                if (json.isEmpty()) {
                                    setSearchError();
                                } else {

                                    ArrayList<Toy> list = new ArrayList<>();
                                    JSONArray root = new JSONArray(json.toString());
                                    for (int i = 0; i < root.length(); i++) {

                                        JSONObject iToy = root.getJSONObject(i);
                                        Toy toy = new Toy();
                                        toy.setToyId(iToy.getString("toy_id"));
                                        toy.setToyName(iToy.getString("toy_name"));
                                        toy.setIsIssued(iToy.getString("is_issued"));
                                        toy.setIssuedToName(iToy.getString("issued_to_name"));
                                        list.add(toy);

                                    }

                                    toys.clear();
                                    toys = list;
                                    ToyRVAdapter adapter = new ToyRVAdapter(ViewActivity.this, toys);
                                    mRecyclerView.setAdapter(adapter);
////                                progressBar.setVisibility(View.INVISIBLE);

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case 350:
                            // issued toys
                            break;

                        default:
                            break;

                    }

                    return true;

                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            return true;
        } else {

            getMenuInflater().inflate(R.menu.view_subscriber_menu, menu);
            MenuItem menuItem = menu.findItem(R.id.view_subscriber_search);
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

//                progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);

                    switch (mode) {

                        case 100:
                            // books
                            try {

                                String json = new SearchAST().execute("100", query).get();
                                if (json.isEmpty()) {
                                    setSearchError();
                                } else {

                                    ArrayList<Book> list = new ArrayList<>();
                                    JSONArray root = new JSONArray(json.toString());
                                    for (int i = 0; i < root.length(); i++) {

                                        JSONObject iBook = root.getJSONObject(i);
                                        Book book = new Book();
                                        book.setBookId(iBook.getString("book_id"));
                                        book.setBookName(iBook.getString("book_name"));
                                        book.setIsIssued(iBook.getString("is_issued"));
                                        book.setIssuedToName(iBook.getString("issued_to_name"));
                                        list.add(book);

                                    }

                                    books.clear();
                                    books = list;
                                    BookRVAdapter adapter = new BookRVAdapter(ViewActivity.this, list);
                                    mRecyclerView.setAdapter(adapter);
////                                progressBar.setVisibility(View.INVISIBLE);

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case 150:
                            // issued books
                            try {

                                String json = new SearchAST().execute("150", query).get();
                                if (json.isEmpty()) {
                                    setSearchError();
                                } else {

                                    ArrayList<Book> list = new ArrayList<>();
                                    JSONArray root = new JSONArray(json.toString());
                                    for (int i = 0; i < root.length(); i++) {

                                        JSONObject iBook = root.getJSONObject(i);
                                        Book book = new Book();
                                        book.setBookId(iBook.getString("book_id"));
                                        book.setBookName(iBook.getString("book_name"));
                                        book.setIsIssued(iBook.getString("is_issued"));
                                        book.setIssuedToName(iBook.getString("issued_to_name"));
                                        list.add(book);

                                    }

                                    issuedBooks.clear();
                                    issuedBooks = list;
                                    BookRVAdapter adapter = new BookRVAdapter(ViewActivity.this, issuedBooks);
                                    mRecyclerView.setAdapter(adapter);

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case 200:
                            // subscribers
                            try {

                                String json = new SearchAST().execute("200", query).get();
                                if (json.isEmpty()) {
                                    setSearchError();
                                } else {

                                    ArrayList<Subscriber> list = new ArrayList<>();
                                    JSONArray root = new JSONArray(json.toString());
                                    for (int i = 0; i < root.length(); i++) {

                                        JSONObject iSubscriber = root.getJSONObject(i);
                                        Subscriber subscriber = new Subscriber();
                                        subscriber.setId(iSubscriber.getString("subscriber_id"));
                                        subscriber.setName(iSubscriber.getString("subscriber_name"));
                                        subscriber.setBookIssued(iSubscriber.getString("book_issued"));
                                        subscriber.setToyIssued(iSubscriber.getString("toy_issued"));
                                        subscriber.setIsGen(iSubscriber.getString("is_gen"));
                                        subscriber.setIsToy(iSubscriber.getString("is_toy"));
                                        subscriber.setEnrolledFor(iSubscriber.getString("subscriber_enrolled_for"));
                                        subscriber.setEnrolledOn(iSubscriber.getString("subscriber_enrolled_on"));
                                        subscriber.setEnrollmentType(iSubscriber.getString("subscriber_enrollement_type"));
                                        subscriber.setLeb(iSubscriber.getString("subscriber_local_education_board"));
                                        subscriber.setReb(iSubscriber.getString("subscriber_regional_education_board"));
                                        subscriber.setCenter(iSubscriber.getString("subscriber_center"));
                                        subscriber.setPhone(iSubscriber.getString("subscriber_phone"));
                                        subscriber.setDob(iSubscriber.getString("subscriber_date_of_birth"));
                                        subscriber.setGender(iSubscriber.getString("subscriber_gender"));
                                        list.add(subscriber);

                                    }

                                    subscribers.clear();
                                    subscribers = list;
                                    SubscriberRVAdapter adapter = new SubscriberRVAdapter(ViewActivity.this, subscribers);
                                    mRecyclerView.setAdapter(adapter);

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case 300:
                            // toys
                            try {

                                String json = new SearchAST().execute("300", query).get();
                                if (json.isEmpty()) {
                                    setSearchError();
                                } else {

                                    ArrayList<Toy> list = new ArrayList<>();
                                    JSONArray root = new JSONArray(json.toString());
                                    for (int i = 0; i < root.length(); i++) {

                                        JSONObject iToy = root.getJSONObject(i);
                                        Toy toy = new Toy();
                                        toy.setToyId(iToy.getString("toy_id"));
                                        toy.setToyName(iToy.getString("toy_name"));
                                        toy.setIsIssued(iToy.getString("is_issued"));
                                        toy.setIssuedToName(iToy.getString("issued_to_name"));
                                        list.add(toy);

                                    }

                                    toys.clear();
                                    toys = list;
                                    ToyRVAdapter adapter = new ToyRVAdapter(ViewActivity.this, toys);
                                    mRecyclerView.setAdapter(adapter);
////                                progressBar.setVisibility(View.INVISIBLE);

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v(LOG_TAG, e.toString());
                                Toast.makeText(ViewActivity.this, "Sorry! Something went wrong when searching for results:\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case 350:
                            // issued toys
                            break;

                        default:
                            break;

                    }

                    return true;

                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            return true;

        }

    }

    public void setSearchError(){

        error.setVisibility(View.VISIBLE);
        error.setText("Sorry! Your search query returned no results!");

    }

    private class GetIssuedBooksAST extends AsyncTask<Void, Void, ArrayList<Book>> {

        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("please wait...");
            progressDialog.show();

        }

        @Override
        protected ArrayList<Book> doInBackground(Void... voids) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            ArrayList<Book> books = new ArrayList<Book>();

            try {

                URL url = new URL(getString(R.string.get_issued_books_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

                if(response.toString().length() < 0){

                    return books;

                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for(int i = 0; i < root.length(); i++){

                        JSONObject iBook = root.getJSONObject(i);
                        Book book = new Book();
                        book.setBookId(iBook.getString("book_id"));
                        book.setBookName(iBook.getString("book_name"));
                        book.setIsIssued(iBook.getString("is_issued"));
                        book.setIssuedToId(iBook.getString("issued_to_id"));
                        book.setIssuedToName(iBook.getString("issued_to_name"));
                        books.add(book);

                    }

                    return books;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return books;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return books;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return books;
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
        protected void onPostExecute(ArrayList<Book> books) {
            progressDialog.dismiss();
            if(books.size() > 0){
                issuedBookAdapter = new IssuedBookRVAdapter(ViewActivity.this, books);
                mRecyclerView.setAdapter(issuedBookAdapter);
            } else {
                error.setVisibility(View.VISIBLE);
                error.setText("Sorry there seems to be no books issued currently!");
            }
        }
    }

    private class GetIssuedToysAST extends AsyncTask<Void, Void, ArrayList<Toy>> {

        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("please wait...");
            progressDialog.show();

        }

        @Override
        protected ArrayList<Toy> doInBackground(Void... voids) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            ArrayList<Toy> toys = new ArrayList<Toy>();

            try {

                URL url = new URL(getString(R.string.get_issued_toys_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

                if(response.toString().length() < 0){

                    return toys;

                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for(int i = 0; i < root.length(); i++){

                        JSONObject iToy = root.getJSONObject(i);
                        Toy toy = new Toy();
                        toy.setToyId(iToy.getString("toy_id"));
                        toy.setToyName(iToy.getString("toy_name"));
                        toy.setIsIssued(iToy.getString("is_issued"));
                        toy.setIssuedToId(iToy.getString("issued_to_id"));
                        toy.setIssuedToName(iToy.getString("issued_to_name"));
                        toys.add(toy);

                    }

                    return toys;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return toys;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return toys;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return toys;
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
        protected void onPostExecute(ArrayList<Toy> toys) {
            progressDialog.dismiss();
            if(toys.size() > 0){
                issuedToyAdapter = new IssuedToyRVAdapter(ViewActivity.this, toys);
                mRecyclerView.setAdapter(issuedToyAdapter);
            } else {
                error.setVisibility(View.VISIBLE);
                error.setText("Sorry there seems to be no toys issued currently!");
            }
        }
    }

    private class GetBooksAST extends AsyncTask<Void, Void, ArrayList<Book>>{

        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("please wait...");
            progressDialog.show();

        }

        @Override
        protected ArrayList<Book> doInBackground(Void... voids) {

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

                while((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

                if(response.toString().length() < 0){

                    return books;

                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for(int i = 0; i < root.length(); i++){

                        JSONObject iBook = root.getJSONObject(i);
                        Book book = new Book();
                        book.setBookId(iBook.getString("book_id"));
                        book.setBookName(iBook.getString("book_name"));
                        book.setIsIssued(iBook.getString("is_issued"));
                        book.setIssuedToName(iBook.getString("issued_to_name"));
                        books.add(book);

                    }

                    return books;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return books;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return books;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return books;
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
        protected void onPostExecute(ArrayList<Book> books) {
            progressDialog.dismiss();
            if(books.size() > 0){
                bookAdapter = new BookRVAdapter(ViewActivity.this, books);
                mRecyclerView.setAdapter(bookAdapter);
            } else {
                error.setVisibility(View.VISIBLE);
                error.setText("Sorry there seems to be no books issued currently!");
            }
        }

    }

    private class GetToysAST extends AsyncTask<Void, Void, ArrayList<Toy>>{

        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("please wait...");
            progressDialog.show();

        }

        @Override
        protected ArrayList<Toy> doInBackground(Void... voids) {

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

                while((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

                if(response.toString().length() < 0){

                    return toys;

                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for(int i = 0; i < root.length(); i++){

                        JSONObject iToy = root.getJSONObject(i);
                        Toy toy = new Toy();
                        toy.setToyId(iToy.getString("toy_id"));
                        toy.setToyName(iToy.getString("toy_name"));
                        toy.setIsIssued(iToy.getString("is_issued"));
                        toy.setIssuedToName(iToy.getString("issued_to_name"));
                        toys.add(toy);

                    }

                    return toys;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return toys;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return toys;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return toys;
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
        protected void onPostExecute(ArrayList<Toy> toys) {
            progressDialog.dismiss();
            if(toys.size() > 0){
                toyAdapter = new ToyRVAdapter(ViewActivity.this, toys);
                mRecyclerView.setAdapter(toyAdapter);
            } else {
                error.setVisibility(View.VISIBLE);
                error.setText("Sorry there seems to be no toys issued currently!");
            }
        }

    }

    private class GetSubscribersAST extends AsyncTask<Void, Void, ArrayList<Subscriber>>{

        @Override
        protected void onPreExecute() {

            if(subscribers.size() > 0){
                subscribers.clear();
            }
            progressDialog.setMessage("please wait...");
            progressDialog.show();

        }

        @Override
        protected ArrayList<Subscriber> doInBackground(Void... voids) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(getString(R.string.get_subscribers_raw_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

                if(response.toString().length() < 0){

                    return subscribers;

                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for(int i = 0; i < root.length(); i++){

                        JSONObject iSubscriber = root.getJSONObject(i);
                        Subscriber subscriber = new Subscriber();
                        subscriber.setId(iSubscriber.getString("subscriber_id"));
                        subscriber.setName(iSubscriber.getString("subscriber_name"));
                        subscriber.setBookIssued(iSubscriber.getString("book_issued"));
                        subscriber.setToyIssued(iSubscriber.getString("toy_issued"));
                        subscriber.setIsGen(iSubscriber.getString("is_gen"));
                        subscriber.setIsToy(iSubscriber.getString("is_toy"));
                        subscriber.setEnrolledFor(iSubscriber.getString("subscriber_enrolled_for"));
                        subscriber.setEnrolledOn(iSubscriber.getString("subscriber_enrolled_on"));
                        subscriber.setEnrollmentType(iSubscriber.getString("subscriber_enrollement_type"));
                        subscriber.setLeb(iSubscriber.getString("subscriber_local_education_board"));
                        subscriber.setReb(iSubscriber.getString("subscriber_regional_education_board"));
                        subscriber.setCenter(iSubscriber.getString("subscriber_center"));
                        subscriber.setPhone(iSubscriber.getString("subscriber_phone"));
                        subscriber.setDob(iSubscriber.getString("subscriber_date_of_birth"));
                        subscriber.setGender(iSubscriber.getString("subscriber_gender"));
                        subscribers.add(subscriber);

                    }

                    return subscribers;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return subscribers;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return subscribers;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return subscribers;
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
        protected void onPostExecute(ArrayList<Subscriber> subscribers) {
            progressDialog.dismiss();
            if(subscribers.size() > 0){
                subscriberAdapter = new SubscriberRVAdapter(ViewActivity.this, subscribers);
                mRecyclerView.setAdapter(subscriberAdapter);
            } else {
                error.setVisibility(View.VISIBLE);
                error.setText("Sorry there seems to be no subscribers available right now!");
            }
        }

    }

    private class SearchAST extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String mode = strings[0];
            String query = strings[1];

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(getString(R.string.search_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));

                String data = URLEncoder.encode("mode", "UTF-8") +"="+ URLEncoder.encode(mode, "UTF-8") +"&"+
                        URLEncoder.encode("query", "UTF-8") +"="+ URLEncoder.encode(query, "UTF-8");

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
                return "";
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return "";
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
                        return "";
                    }
                }
            }
        }
    }

}
