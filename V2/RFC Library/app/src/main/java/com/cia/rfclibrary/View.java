package com.cia.rfclibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class View extends AppCompatActivity {

    RecyclerView mRecyclerView;

    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mode = getIntent().getIntExtra("mode", 0);

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
                 break;

            case 150:
                break;

            case 200:
                break;

            case 300:
                break;

            case 350:
                break;

            default:
                break;

        }
    }

}
