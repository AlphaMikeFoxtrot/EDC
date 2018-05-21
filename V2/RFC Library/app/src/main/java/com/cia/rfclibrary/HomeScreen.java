package com.cia.rfclibrary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    android.support.v7.widget.Toolbar toolbar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    public static final String LOG_TAG = "home_screen_log";

    Calendar myCalendar = Calendar.getInstance();

    AlertDialog alertDialogAdd;

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
        // returnBook = findViewById(R.id.return_book_card);
        // returnToy = findViewById(R.id.return_toy_card);
        viewIssuedBooks = findViewById(R.id.view_issued_books_card);
        viewIssuedToys = findViewById(R.id.view_issued_toys_card);
        viewSubscribers = findViewById(R.id.view_subscribers_card);
        viewBooks = findViewById(R.id.view_books_card);
        viewToys = findViewById(R.id.view_toys_card);
        viewSummary = findViewById(R.id.view_summary_card);

        issueBook.setOnClickListener(this);
        issueToy.setOnClickListener(this);
//        returnBook.setOnClickListener(this);
//        returnToy.setOnClickListener(this);
        viewIssuedBooks.setOnClickListener(this);
        viewIssuedToys.setOnClickListener(this);
        viewSubscribers.setOnClickListener(this);
        viewBooks.setOnClickListener(this);
        viewToys.setOnClickListener(this);
        viewSummary.setOnClickListener(this);

        LinearLayout bookCon = findViewById(R.id.issue_book_container);
        LinearLayout toyCon = findViewById(R.id.issue_toy_container);

        if(sharedPreferences.getString(getString(R.string.sp_clearance), "0").equals("0")){
            bookCon.setVisibility(View.GONE);
            toyCon.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.issue_book_card:
                issueBookClicked();
                break;

            case R.id.issue_toy_card:
                issueToyClicked();
                break;

//            case R.id.return_book_card:
//                returnBookClicked();
//                break;

//            case R.id.return_toy_card:
//                returnToyClicked();
//                break;

            case R.id.view_issued_books_card:
                viewIssuedBooksClicked();
                break;

            case R.id.view_issued_toys_card:
                viewIssuedToysClicked();
                break;

            case R.id.view_subscribers_card:
                viewSubscribersClicked();
                break;

            case R.id.view_books_card:
                viewBooksClicked();
                break;

            case R.id.view_toys_card:
                viewToysClicked();
                break;

            case R.id.view_summary_card:
                viewSummaryClicked();
                break;

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
                    String[] selectedBookIdList = dialogBookIds.get(position).split("  ");
                    selectedBookId = selectedBookIdList[0];
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
                    String[] selectedSubIdList = dialogSubscriberNames.get(position).split("  ");
                    selectedSubId = selectedSubIdList[0];
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
                .setTitle("Issue Book")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

//

        final ImageView icon = prompt.findViewById(R.id.prompt_icon);
        icon.setImageResource(R.drawable.return_arrow);

        builder
                .setTitle("Return Book")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
            dialogSubscriberNames = new GetSubscriberNamesAST().execute("200").get();
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
                        String[] selectedSubIdList = dialogSubscriberNames.get(position).split("  ");
                        selectedSubId = selectedSubIdList[0];
                    } else {
                        selectedSubId = " ";
                    }
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
                    String[] selectedSubIdList = dialogSubscriberNames.get(position).split("  ");
                    selectedSubId = selectedSubIdList[0];
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

        Intent toView = new Intent(this, ViewActivity.class);
        toView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toView.putExtra("mode", 350);
        startActivity(toView);

    }

    private void viewBooksClicked(){

        Intent toView = new Intent(this, ViewActivity.class);
        toView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toView.putExtra("mode", 100);
        startActivity(toView);

    }

    private void viewToysClicked(){

        Intent toView = new Intent(this, ViewActivity.class);
        toView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toView.putExtra("mode", 300);
        startActivity(toView);

    }

    private void viewSubscribersClicked(){

        Intent toView = new Intent(this, ViewActivity.class);
        toView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toView.putExtra("mode", 200);
        startActivity(toView);

    }

    private void viewSummaryClicked(){

        Intent toSum = new Intent(this, Summary.class);
        toSum.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toSum);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(sharedPreferences.getString(getString(R.string.sp_clearance), "0").equals("0")){
            getMenuInflater().inflate(R.menu.privilaged_home_screen_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.home_screen_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.hs_logout){
            logOut();
        } else if(item.getItemId() == R.id.hs_reset){
            reset();
        } else if(item.getItemId() == R.id.hs_add){
            add();
        }
        return true;
    }

    private void add() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.add_prompt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(prompt);

        final Button addBook = prompt.findViewById(R.id.add_prompt_book);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookClicked();
            }
        });

        final Button addToy = prompt.findViewById(R.id.add_prompt_toy);
        addToy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToyClicked();
            }
        });

        final Button addSubscriber = prompt.findViewById(R.id.add_prompt_subscriber);
        addSubscriber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubscriberClicked();
            }
        });

        builder
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);

        alertDialogAdd = builder.create();
        alertDialogAdd.show();

    }

    private void addSubscriberClicked() {

        LayoutInflater layoutInflater = LayoutInflater.from(HomeScreen.this);
        View prompt =  layoutInflater.inflate(R.layout.add_subscriber_prompt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
        builder.setView(prompt);

        final EditText name, reb, leb, center, dob, phone;
        name = prompt.findViewById(R.id.add_subscriber_name);
        reb = prompt.findViewById(R.id.add_subscriber_reb);
        leb = prompt.findViewById(R.id.add_subscriber_leb);
        center = prompt.findViewById(R.id.add_subscriber_center);
        dob = prompt.findViewById(R.id.add_subscriber_dob);
        phone = prompt.findViewById(R.id.add_subscriber_phone);

        final String e_type_sel, e_for_sel, gender_sel;

        String[] eTypes = {"new", "old"};
        String[] eFors = {"RFC", "ETL", "RFC + ETL"};
        String[] genders = {"male", "female"};

        final RadioGroup e_type, e_for, gender;
        e_type = prompt.findViewById(R.id.add_subscriber_e_type_rg);
        e_for = prompt.findViewById(R.id.add_subscriber_e_for_rg);
        gender = prompt.findViewById(R.id.add_subscriber_gender_rg);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar myCalendar = Calendar.getInstance();

                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {

                        String myFormat = "dd-MM-yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        dob.setText(sdf.format(myCalendar.getTime()));

                    }

                };

                dob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(HomeScreen.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

            }
        });

        builder
                .setCancelable(false)
                .setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do something
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        alertDialogAdd.dismiss();
                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(name.getText().toString().isEmpty()){
                            name.setError("Value cannot be empty");
                        } else if(phone.getText().toString().isEmpty()){
                            phone.setError("Value cannot be empty");
                        } else if(dob.getText().toString().isEmpty()){
                            dob.setError("Value cannot be empty");
                        } else if(reb.getText().toString().isEmpty()){
                            reb.setError("Value cannot be empty");
                        } else if(leb.getText().toString().isEmpty()){
                            leb.setError("Value cannot be empty");
                        } else if(center.getText().toString().isEmpty()){
                            center.setError("Value cannot be empty");
                        } else {

                        }

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void addToyClicked() {

        LayoutInflater inflater = LayoutInflater.from(HomeScreen.this);
        View prompt = inflater.inflate(R.layout.add_book_toy_prompt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
        builder.setView(prompt);

        final EditText name = prompt.findViewById(R.id.add_book_toy_prompt_et);

        builder
                .setCancelable(false)
                .setMessage("Name of the Toy: ")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AddAST().execute("200", name.getText().toString());
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void addBookClicked() {

        LayoutInflater inflater = LayoutInflater.from(HomeScreen.this);
        View prompt = inflater.inflate(R.layout.add_book_toy_prompt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
        builder.setView(prompt);

        final EditText name = prompt.findViewById(R.id.add_book_toy_prompt_et);

        builder
                .setCancelable(false)
                .setMessage("Name of the Book: ")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AddAST().execute("100", name.getText().toString());
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void reset() {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.reset_prompt , null);

        final EditText rootUsername = promptsView.findViewById(R.id.root_username);
        final EditText rootPassword = promptsView.findViewById(R.id.root_password);

        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);

        builder.setView(promptsView);

        builder
                .setCancelable(false)
                .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(rootUsername.getText().toString().contains("russia") && rootPassword.getText().toString().contains("moscow")) {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeScreen.this);
                            builder1.setMessage("Are you sure you want to perform soft reset on the data?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new Reset().execute();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                            AlertDialog alertDialog2 = builder1.create();
                            alertDialog2.show();

                        } else {
                            dialogInterface.dismiss();
                            Toast.makeText(HomeScreen.this, "username and password incorrect.", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void logOut(){

        new LogoutAST().execute();

    }

    private class LogoutAST extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(HomeScreen.this);
        }

        @Override
        protected String doInBackground(Void... voids) {
            editor.putBoolean(getString(R.string.sp_session), false);
            editor.putString(getString(R.string.sp_username), "");
            editor.putString(getString(R.string.sp_clearance), "");
            editor.commit();

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
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
                Log.v("name to id", "not id");
                try {
                    Log.v("name to id", "try start protocol");
                    sub_id = new GetIdFromName().execute(subscriber_id).get();
                    if(sub_id.length() < 0){
                        Log.v("name to id","sub_id.length() < 0");
                        return "";
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.v("name to id","interrupted");
                    return "";
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Log.v("name to id","execution");
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

                        StringBuilder listItem = new StringBuilder();
                        listItem.append(book.getString("book_id"));
                        listItem.append("  (");
                        listItem.append(book.getString("book_name"));
                        listItem.append(") ");

                        dialogBookIds.add(listItem.toString());

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
        }
    }

    private class GetToyIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
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

                        StringBuilder listItem = new StringBuilder();
                        listItem.append(subscriber.getString("subscriber_id"));
                        listItem.append("  (");
                        listItem.append(subscriber.getString("subscriber_name"));
                        listItem.append(")");
                        dialogSubscriberNames.add(listItem.toString());

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

    private class GetIssuedBookIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
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

                        StringBuilder listItem = new StringBuilder();
                        listItem.append(book.getString("book_id"));
                        listItem.append("  (");
                        listItem.append(book.getString("book_name"));
                        listItem.append(") ");

                        dialogBookIds.add(listItem.toString());

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
        }
    }

    private class GetIssuedToyIdsAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
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
        }
    }

    private class GetIssuedToSubscriberNamesAST extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
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

    private class Reset extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("resetting server data");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(getString(R.string.soft_reset_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

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
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();

            if(s.isEmpty()){
                Toast.makeText(HomeScreen.this, "something went wrong when resetting data", Toast.LENGTH_SHORT).show();
            } else if(s.contains("reset")){
                Toast.makeText(HomeScreen.this, "Data reset successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeScreen.this, "Something went wrong when resetting data: \n" + s, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class AddAST extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialogAdd = new ProgressDialog(HomeScreen.this);

        @Override
        protected void onPreExecute() {
            progressDialogAdd.setMessage("Please wait...");
            progressDialogAdd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String name = strings[1];

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(getString(R.string.add_url));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                String data = URLEncoder.encode("type", "UTF-8") +"="+ URLEncoder.encode(type, "UTF-8") +"&"+
                        URLEncoder.encode("name", "UTF-8") +"="+ URLEncoder.encode(name, "UTF-8");

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
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialogAdd.dismiss();
            if(s.isEmpty()){
                Toast.makeText(HomeScreen.this, "Sorry! Something went wrong when adding data to the server\nPlease try again after some time", Toast.LENGTH_SHORT).show();
            } else if (s.contains("success")){
                Toast.makeText(HomeScreen.this, "added successfully", Toast.LENGTH_SHORT).show();
                alertDialogAdd.dismiss();
            } else if(s.contains("error")){
                Toast.makeText(HomeScreen.this, "Sorry! Something went wrong when adding data to the server: \n" + s, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
