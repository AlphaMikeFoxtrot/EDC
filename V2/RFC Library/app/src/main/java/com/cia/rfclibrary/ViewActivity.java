package com.cia.rfclibrary;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cia.rfclibrary.Adapters.Book.BookRVAdapter;
import com.cia.rfclibrary.Adapters.Book.IssuedBookRVAdapter;
import com.cia.rfclibrary.Classes.Book;
import com.cia.rfclibrary.Classes.Subscriber;
import com.cia.rfclibrary.Classes.Toy;
import com.google.android.gms.stats.internal.G;

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

public class ViewActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    TextView error, title;

    private final String LOG_TAG = "view_act";

    ProgressDialog progressDialog;

    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Book> issuedBooks = new ArrayList<>();
    ArrayList<Subscriber> subscribers = new ArrayList<>();;
    ArrayList<Toy> toys = new ArrayList<>();
    ArrayList<Toy> issuedToys = new ArrayList<>();

    BookRVAdapter bookAdapter;
    IssuedBookRVAdapter issuedBookAdapter;

    android.support.v7.widget.Toolbar toolbar;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        progressDialog = new ProgressDialog(this);
        mRecyclerView = findViewById(R.id.view_act_rv);
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
                break;

            case 300:
                // toys
                title.setText("View Toys");
                break;

            case 350:
                // issued toys
                title.setText("View Issued Toys");
                break;

            default:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_stuff_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.view_stuff_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast.makeText(ViewActivity.this, "" + query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

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
                        ArrayList<Book> newList = new ArrayList<>();
                        for (int i = 0; i < books.size(); i++){
                            if(books.get(i).getBookName().toLowerCase().contains(newText)){
                                newList.add(books.get(i));
                            }
                        }
                        bookAdapter.setFilter(newList);
                        break;

                    case 150:
                        // issued books
                        ArrayList<Book> newIssuedBookList = new ArrayList<>();
                        for (int i = 0; i < books.size(); i++){
                            if(books.get(i).getBookName().toLowerCase().contains(newText)){
                                newIssuedBookList.add(books.get(i));
                            }
                        }
                        issuedBookAdapter.setFilter(newIssuedBookList);
                        break;

                    case 200:
                        // subscribers
                        title.setText("View Subscribers");
                        break;

                    case 300:
                        // toys
                        title.setText("View Toys");
                        break;

                    case 350:
                        // issued toys
                        title.setText("View Issued Toys");
                        break;

                    default:
                        break;

                }

                return false;

            }
        });
        return true;
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

}
