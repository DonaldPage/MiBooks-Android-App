package com.example.don.mibooksv1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.don.mibooksv1.R;
import com.example.don.mibooksv1.model.Book;
import com.example.don.mibooksv1.sql.DatabaseHelper;
;

public class AddNewRecordActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mCategoryEditText;
    private EditText mIsbnEditText;
    private EditText mDescription;
    private EditText mImageEditText;
    private Button mAddBtn;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //init
        mTitleEditText = (EditText) findViewById(R.id.bookTitle);
        mCategoryEditText = (EditText)findViewById(R.id.bookCategory);
        mIsbnEditText = (EditText)findViewById(R.id.bookIsbn);
        mDescription = (EditText)findViewById(R.id.bookDescription);
        mImageEditText = (EditText)findViewById(R.id.userProfileImageLink);
        mAddBtn = (Button)findViewById(R.id.addNewUserButton);

        //listen to add button click
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save person method
                saveBook();
            }
        });

    }

    private void saveBook(){

        String title = mTitleEditText.getText().toString().trim();
        String category = mCategoryEditText.getText().toString().trim();
        String isbn = mIsbnEditText.getText().toString().trim();
        String description  = mDescription.getText().toString().trim();
        String image = mImageEditText.getText().toString().trim();
        db = new DatabaseHelper(this);

        if(title.isEmpty()){
            //error title is empty
            Toast.makeText(this, "You must enter a title", Toast.LENGTH_SHORT).show();
        }

        if(category.isEmpty()){
            //error category is empty
            Toast.makeText(this, "You must enter a category", Toast.LENGTH_SHORT).show();
        }

        if(isbn.isEmpty()){
            //error ISBN is empty
            Toast.makeText(this, "You must enter an ISBN", Toast.LENGTH_SHORT).show();
        }

        if(description.isEmpty()){
            //error description is empty
            Toast.makeText(this, "You must enter a description", Toast.LENGTH_SHORT).show();
        }

        if(image.isEmpty()){
            //error image is empty
            Toast.makeText(this, "You must enter an image link", Toast.LENGTH_SHORT).show();
        }

        //create new book
        Book book = new Book(title, category, isbn, description, image);
        db.saveNewBook(book);

        //finally redirect back home
        // NOTE you can implement an sqlite callback then redirect on success delete
        goBackHome();

    }

    private void goBackHome(){
        startActivity(new Intent(AddNewRecordActivity.this, UsersActivity.class));
    }
}
