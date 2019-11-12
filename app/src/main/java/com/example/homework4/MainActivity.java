package com.example.homework4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextBirdName, editTextZipCode, editTextPersonName;
    Button buttonSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextBirdName = findViewById(R.id.editTextBirdName);
        editTextZipCode = findViewById(R.id.editTextZipCode);
        editTextPersonName = findViewById(R.id.editTextPersonName);

        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.itemHome) {
            Toast.makeText(this, "You are already on the home page!", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.itemSearch) {

            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Birds");

        String birdName = editTextBirdName.getText().toString();
        int zipCode = Integer.parseInt(editTextZipCode.getText().toString());
        String personName = editTextPersonName.getText().toString();

        Bird myBird = new Bird(birdName, zipCode, personName);

        Task databaseTask = myRef.push().setValue(myBird);

        databaseTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this, "Bird added successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error adding bird to database.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
