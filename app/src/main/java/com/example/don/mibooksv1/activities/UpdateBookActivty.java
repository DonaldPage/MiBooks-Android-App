package com.example.don.mibooksv1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.don.mibooksv1.model.Book;
import com.example.don.mibooksv1.sql.DatabaseHelper;
import com.example.don.mibooksv1.R;

public class UpdateBookActivty extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mCategoryEditText;
    private EditText mIsbnEditText;
    private EditText mDescriptionEditText;
    private EditText mImageEditText;
    private Button mUpdateBtn;

    private DatabaseHelper db;
    private long receivedBookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        //init
        mTitleEditText = (EditText)findViewById(R.id.bookTitleUpdate);
        mCategoryEditText = (EditText)findViewById(R.id.bookCategoryUpdate);
        mIsbnEditText = (EditText)findViewById(R.id.bookIsbnUpdate);
        mImageEditText = (EditText)findViewById(R.id.userProfileImageLinkUpdate);
        mDescriptionEditText = (EditText)findViewById(R.id.bookDescriptionUpdate);
        mUpdateBtn = (Button)findViewById(R.id.updateBookButton);

        db = new DatabaseHelper(this);

        try {
            //get intent to get person id
            receivedBookId = getIntent().getLongExtra("BOOK_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /***populate user data before update***/
        Book queriedBook = db.getBookByID(receivedBookId);
        //set field to this user data
        mTitleEditText.setText(queriedBook.getTitle());
        mCategoryEditText.setText(queriedBook.getCategory());
        mIsbnEditText.setText(queriedBook.getIsbn());
        mDescriptionEditText.setText(queriedBook.getDescription());
        mImageEditText.setText(queriedBook.getImage());



        //listen to add button click to update
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save person method
                updateBook();
            }
        });


    }

    private void updateBook(){
        String title = mTitleEditText.getText().toString().trim();
        String category = mCategoryEditText.getText().toString().trim();
        String isbn = mIsbnEditText.getText().toString().trim();
        String description = mDescriptionEditText.getText().toString().trim();
        String image = mImageEditText.getText().toString().trim();


        if(title.isEmpty()){
            //error title is empty
            Toast.makeText(this, "You must enter a title", Toast.LENGTH_SHORT).show();
        }

        if(category.isEmpty()){
            //error category is empty
            Toast.makeText(this, "You must enter an category", Toast.LENGTH_SHORT).show();
        }

        if(isbn.isEmpty()){
            //error ISBN is empty
            Toast.makeText(this, "You must enter an ISBN", Toast.LENGTH_SHORT).show();
        }

        if(description.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an description", Toast.LENGTH_SHORT).show();
        }

        if(image.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an image link", Toast.LENGTH_SHORT).show();
        }

        //create updated person
        Book updatedBook = new Book(title, category, isbn, description,image);

        //call db helper update
        db.updateBookRecord(receivedBookId, this, updatedBook);

        //finally redirect back home
        // NOTE you can implement an SQLite callback then redirect on success delete
        goBackHome();

    }

    private void goBackHome(){
        startActivity(new Intent(this, UsersActivity.class));
    }

}
