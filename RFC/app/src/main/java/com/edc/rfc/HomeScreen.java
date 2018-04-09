package com.edc.rfc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    android.support.v7.widget.Toolbar toolbar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    public static final String LOG_TAG = "home_screen_log";

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

    private void issueToyClicked() {

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
}
