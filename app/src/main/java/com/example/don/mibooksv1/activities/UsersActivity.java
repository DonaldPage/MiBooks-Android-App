package com.example.don.mibooksv1.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.don.mibooksv1.R;
import com.example.don.mibooksv1.sql.DatabaseHelper;


public class UsersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHelper dbHelper;
    private RecyclerViewAdapter adapter;
    private String filter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        //initialize the variables
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //populate recyclerview
        populateRecyclerView(filter);


    }

    private void populateRecyclerView(String filter){
        dbHelper = new DatabaseHelper(this);
        adapter = new RecyclerViewAdapter(dbHelper.bookList(filter), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        MenuItem item = menu.findItem(R.id.filterSpinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filterOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = parent.getSelectedItem().toString();
                populateRecyclerView(filter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                populateRecyclerView(filter);
            }
        });


        spinner.setAdapter(adapter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addMenu:
                goToAddBookActivity();
                return true;
            case R.id.logout:
                logoutToStartActivity(getApplicationContext());

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    // Directs the user to the Add
    private void goToAddBookActivity(){
        Intent intent = new Intent(UsersActivity.this, AddNewRecordActivity.class);
        startActivity(intent);
    }

    // Takes the user back to the login screen and displays a toast to tell them they have been logged out.
    private void logoutToStartActivity(Context context){
        Intent intent = new Intent(UsersActivity.this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(context, "You have been logged out.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
