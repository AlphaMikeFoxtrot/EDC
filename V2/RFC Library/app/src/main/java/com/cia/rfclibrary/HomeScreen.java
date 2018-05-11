package com.cia.rfclibrary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cia.rfclibrary.Classes.Book;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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

    String selectedSubId, selectedBookId, selectedToyId;

    ArrayList<String> dialogSubscriberNames = new ArrayList<>();
    ArrayList<String> dialogBookIds = new ArrayList<>();
    ArrayList<String> dialogToyIds = new ArrayList<>();

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
                viewIssuedBooksClicked();
                break;

            case R.id.view_issued_toys_card:
                Toast.makeText(this, "view_issued_toys_card", Toast.LENGTH_SHORT).show();
                break;

            case R.id.view_subscribers_card:
                Toast.makeText(this, "view_subscribers_card", Toast.LENGTH_SHORT).show();
                break;

            case R.id.view_books_card:
                // Toast.makeText(this, "view_books_card", Toast.LENGTH_SHORT).show();
                viewBooksClicked();
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

        final SearchableSpinner bookId = prompt.findViewById(R.id.prompt_id);
        bookId.setTitle("issued book id.....");
        try {
            dialogBookIds = new GetBookIdsAST().execute().get();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    dialogBookIds);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            bookId.setAdapter(adapter);
            bookId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedBookId = dialogBookIds.get(position);
                    Toast.makeText(HomeScreen.this, selectedBookId, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final SearchableSpinner subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        try {
            dialogSubscriberNames = new GetSubscriberNamesAST().execute("100").get();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    dialogSubscriberNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subscriberId.setAdapter(adapter);
            subscriberId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedSubId = dialogSubscriberNames.get(position);
                    Toast.makeText(HomeScreen.this, selectedSubId, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
//        subscriberId.setHint("Subscriber Name or ID...");
//        try {
//            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                    this,
//                    android.R.layout.simple_dropdown_item_1line,
//                    new GetSubscriberNamesAST().execute().get());
//            subscriberId.setAdapter(adapter);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.issue);

        builder
                .setTitle("Issue Book")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "id: " + selectedBookId + "\nsub id: " + selectedSubId, Toast.LENGTH_SHORT).show();
                        new ProtocolAST().execute("issue", "book", selectedBookId, selectedSubId);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

        final SearchableSpinner bookId = prompt.findViewById(R.id.prompt_id);
        bookId.setTitle("returned book id.....");
        try {
            dialogBookIds = new GetIssuedBookIdsAST().execute().get();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    dialogBookIds);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            bookId.setAdapter(adapter);
            bookId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedBookId = dialogBookIds.get(position);
                    Toast.makeText(HomeScreen.this, selectedBookId, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final SearchableSpinner subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        try {
            dialogSubscriberNames = new GetIssuedToSubscriberNamesAST().execute().get();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    dialogSubscriberNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subscriberId.setAdapter(adapter);
            subscriberId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedSubId = dialogSubscriberNames.get(position);
                    Toast.makeText(HomeScreen.this, selectedSubId, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        final AutoCompleteTextView bookId = prompt.findViewById(R.id.prompt_id);
//        bookId.setHint("returned book id...");
//        try {
//            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                    this,
//                    android.R.layout.simple_dropdown_item_1line,
//                    new GetIssuedBookIdsAST().execute().get());
//            bookId.setAdapter(adapter);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        final AutoCompleteTextView subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
//        subscriberId.setHint("Subscriber Name...");
//        try {
//            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                    this,
//                    android.R.layout.simple_dropdown_item_1line,
//                    new GetIssuedToSubscriberNamesAST().execute().get());
//            subscriberId.setAdapter(adapter);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.return_arrow);

        builder
                .setTitle("Return Book")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "id: " + selectedBookId + "\nsub id: " + selectedSubId, Toast.LENGTH_SHORT).show();
                        new ProtocolAST().execute("return", "book", selectedBookId, selectedSubId);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

        final SearchableSpinner toyId = prompt.findViewById(R.id.prompt_id);
        toyId.setTitle("issued toy id.....");
        try {
            dialogToyIds = new GetToyIdsAST().execute().get();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    dialogToyIds);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            toyId.setAdapter(adapter);
            toyId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedToyId = dialogToyIds.get(position);
                    Toast.makeText(HomeScreen.this, selectedToyId, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final SearchableSpinner subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        try {
            dialogSubscriberNames = new GetSubscriberNamesAST().execute("300").get();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    dialogSubscriberNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subscriberId.setAdapter(adapter);
            subscriberId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(dialogSubscriberNames.size() > 0) {
                        selectedSubId = dialogSubscriberNames.get(position);
                    } else {
                        selectedSubId = " ";
                    }
                    Toast.makeText(HomeScreen.this, selectedSubId, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.issue);

        builder
                .setTitle("Issue Toy")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "id: " + selectedToyId + "\nsub id: " + selectedSubId, Toast.LENGTH_SHORT).show();
                        new ProtocolAST().execute("issue", "toy", selectedToyId, selectedSubId);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

        final SearchableSpinner toyId = prompt.findViewById(R.id.prompt_id);
        toyId.setTitle("returned toy id.....");
        try {
            dialogToyIds = new GetIssuedToyIdsAST().execute().get();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    dialogToyIds);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            toyId.setAdapter(adapter);
            toyId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(dialogToyIds.size() > 0) {
                        selectedToyId = dialogToyIds.get(position);
                    } else {
                        selectedToyId = " ";
                    }
                    // Toast.makeText(HomeScreen.this, selectedBookId, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final SearchableSpinner subscriberId = prompt.findViewById(R.id.prompt_subscriber_id);
        try {
            dialogSubscriberNames = new GetIssuedToSubscriberNamesAST().execute().get();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    dialogSubscriberNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subscriberId.setAdapter(adapter);
            subscriberId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedSubId = dialogSubscriberNames.get(position);
                    // Toast.makeText(HomeScreen.this, selectedSubId, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.return_arrow);

        builder
                .setTitle("Return Toy")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeScreen.this, "id: " + selectedToyId + "\nsub id: " + selectedSubId, Toast.LENGTH_SHORT).show();
                        new ProtocolAST().execute("return", "toy", selectedToyId, selectedSubId);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void viewIssuedBooksClicked(){

        Intent toView = new Intent(this, ViewActivity.class);
        toView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toView.putExtra("mode", 150);
        startActivity(toView);

    }

    private void viewIssuedToysClicked(){

    }

    private void viewBooksClicked(){

        Intent toView = new Intent(this, ViewActivity.class);
        toView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toView.putExtra("mode", 100);
        startActivity(toView);

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
            // progressDialog.setMessage("logging you out...");
            // progressDialog.show();
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
            // progressDialog.dismiss();
            Intent toLogin = new Intent(HomeScreen.this, LoginActivity.class);
            toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toLogin);
        }
    }

    private class ProtocolAST extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("running protocol...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String protocol = strings[0];
            String type = strings[1];
            String obj_id = strings[2];
            String subscriber_id = strings[3];
            String sub_id = "";

            if(!subscriber_id.contains("/")){
                // Toast.makeText(HomeScreen.this, "subscriber_id not id but name", Toast.LENGTH_SHORT).show();
                Log.v("name to id", "not id");
                try {
                    Log.v("name to id", "try start protocol");
                    sub_id = new GetIdFromName().execute(subscriber_id).get();
                    if(sub_id.length() < 0){
                        Log.v("name to id","sub_id.length() < 0");
                        // Toast.makeText(HomeScreen.this, "Sorry something went wrong when running protocol", Toast.LENGTH_SHORT).show();
                        return "";
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.v("name to id","interrupted");
                    // Toast.makeText(HomeScreen.this, "Sorry something went wrong when running protocol", Toast.LENGTH_SHORT).show();
                    return "";
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Log.v("name to id","execution");
                    // Toast.makeText(HomeScreen.this, "Sorry something went wrong when running protocol", Toast.LENGTH_SHORT).show();
                    return "";
                }
            } else {
                sub_id = subscriber_id;
            }

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
            } else {
                Toast.makeText(HomeScreen.this, "Something went wrong when running protocol:\n" + s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetBookIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {

            // progressDialog.setMessage("please wait");
            // progressDialog.show();
            if(dialogBookIds.size() > 0){
                dialogBookIds.clear();
            }

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
                    return dialogBookIds;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject book = root.getJSONObject(i);
                        dialogBookIds.add(book.getString("book_id"));
                        // dialogBookIds.add(book.getString("book_name"));

                    }

                    return dialogBookIds;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogBookIds;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogBookIds;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogBookIds;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            // progressDialog.dismiss();
        }
    }

    private class GetToyIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            // progressDialog.setMessage("please wait");
            // progressDialog.show();
            if(dialogToyIds.size() > 0){
                dialogToyIds.clear();
            }
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
                    return dialogToyIds;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject toy = root.getJSONObject(i);
                        dialogToyIds.add(toy.getString("toy_id"));
//                        dialogToyIds.add(toy.getString("toy_name"));

                    }

                    return dialogToyIds;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogToyIds;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogToyIds;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogToyIds;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    private class GetSubscriberNamesAST extends AsyncTask<String, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            if(dialogSubscriberNames.size() > 0){
                dialogSubscriberNames.clear();
            }
        }

        @Override
        protected ArrayList<String> doInBackground(String... voids) {

            String code = voids[0];

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(getString(R.string.get_subscribers_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));

                String data = URLEncoder.encode("code", "UTF-8") +"="+ URLEncoder.encode(code, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

                if (response.toString().isEmpty()){
                    return dialogSubscriberNames;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject subscriber = root.getJSONObject(i);
                        dialogSubscriberNames.add(subscriber.getString("subscriber_id"));
//                        dialogSubscriberNames.add(subscriber.getString("subscriber_name"));

                    }

                    return dialogSubscriberNames;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogSubscriberNames;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogSubscriberNames;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogSubscriberNames;
            }

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

    private class GetIssuedBooksAST extends AsyncTask<Void, Void, ArrayList<Book>>{

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
        }
    }

    private class GetIssuedBookIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            // progressDialog.setMessage("please wait...");
            // progressDialog.show();
            if(dialogBookIds.size() > 0){
                dialogBookIds.clear();
            }
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
                    return dialogBookIds;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject book = root.getJSONObject(i);
                        dialogBookIds.add(book.getString("book_id"));
                        // dialogBookIds.add(book.getString("book_name"));

                    }

                    return dialogBookIds;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogBookIds;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogBookIds;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogBookIds;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            // progressDialog.dismiss();
        }
    }

    private class GetIssuedToyIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            // progressDialog.setMessage("please wait...");
            // progressDialog.show();
            if(dialogToyIds.size() > 0){
                dialogToyIds.clear();
            }
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
                    return dialogToyIds;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject toy = root.getJSONObject(i);
                        dialogToyIds.add(toy.getString("toy_id"));

                    }

                    return dialogToyIds;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogToyIds;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogToyIds;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogToyIds;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            // progressDialog.dismiss();
        }
    }

    private class GetIssuedToSubscriberNamesAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            // progressDialog.setMessage("please wait...");
            // progressDialog.show();
            if(dialogSubscriberNames.size() > 0){
                dialogSubscriberNames.clear();
            }
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
                    return dialogSubscriberNames;
                } else {

                    JSONArray root = new JSONArray(response.toString());
                    for (int i = 0; i < root.length(); i++){

                        JSONObject subscriber = root.getJSONObject(i);
                        dialogSubscriberNames.add(subscriber.getString("subscriber_id"));
//                        dialogSubscriberNames.add(subscriber.getString("subscriber_name"));

                    }

                    return dialogSubscriberNames;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogSubscriberNames;
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogSubscriberNames;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, e.toString());
                return dialogSubscriberNames;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            // progressDialog.dismiss();
        }
    }

    private class GetIdFromName extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.v("name to id", "do in background start." + strings[0]);
            String name = strings[0];

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {

                Log.v("name to id", "try start");

                URL url = new URL(getString(R.string.get_name_from_id));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));

                String data = URLEncoder.encode("name", "UTF-8") +"="+ URLEncoder.encode(name, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                Log.v("name to id", "write complete");

                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                Log.v("name to id", "read complete.result: " + response.toString());

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
                    }
                }
            }
        }

    }

}
