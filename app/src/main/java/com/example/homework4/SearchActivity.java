package com.example.homework4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextSearch;
    Button buttonSearch;
    TextView textViewBirdResult, textViewPersonResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);

        textViewBirdResult = findViewById(R.id.textViewBirdResult);
        textViewPersonResult = findViewById(R.id.textViewPersonResult);

        buttonSearch.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.itemSearch) {
            Toast.makeText(this, "You are already on the search page!", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.itemHome) {

            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Birds");


        int searchZipCode = Integer.parseInt(editTextSearch.getText().toString());

        Query searchQuery = myRef.orderByChild("zipcode").equalTo(searchZipCode);

        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Toast.makeText(SearchActivity.this, "Bird record found!", Toast.LENGTH_SHORT).show();

                }  else {
                    Toast.makeText(SearchActivity.this, "No bird records exist in this zip code.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.orderByChild("zipcode").equalTo(searchZipCode).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //String editKey = dataSnapshot.getKey();
                Bird searchBird = dataSnapshot.getValue(Bird.class);
                String foundBirdName = searchBird.birdname;
                String foundPersonName = searchBird.personname;

                textViewBirdResult.setText(foundBirdName);
                textViewPersonResult.setText(foundPersonName);

                editTextSearch.setText("Search Birds");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
}
