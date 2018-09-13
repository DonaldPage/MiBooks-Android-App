package com.example.don.mibooksv1.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.don.mibooksv1.model.Book;
import com.example.don.mibooksv1.model.User;

import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    // Sets database name
    private static final String DATABASE_NAME = "UserManager.db";
    //----------------------------------------------------------------------------------------------
    // CREATING USER TABLE
    // Sets table name
    private static final String TABLE_USER = "user";

    // Sets the column names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // SQLite query to create User Table
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // SQLite query to drop User table
    private  String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // CREATING THE BOOKS
    // Sets table name
    private static final String TABLE_BOOK = "book";

    // Sets the column names
    private static final String COLUMN_BOOK_ID = "id";
    private static final String COLUMN_BOOK_ISBN = "isbn";
    private static final String COLUMN_BOOK_TITLE = "title";
    private static final String COLUMN_BOOK_CATEGORY = "category";
    private static final String COLUMN_BOOK_DESCRIPTION = "description";
    private static final String COLUMN_BOOK_IMAGE = "image";

    // SQLite query to create User Table
    private String CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_BOOK + "("
            + COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_BOOK_ISBN + " VARCHAR,"
            + COLUMN_BOOK_TITLE + " TEXT," + COLUMN_BOOK_CATEGORY + " TEXT,"
            + COLUMN_BOOK_DESCRIPTION + " TEXT," + COLUMN_BOOK_IMAGE + " TEXT" + ")";

    // SQLite query to drop User table
    private  String DROP_BOOK_TABLE = "DROP TABLE IF EXISTS " + TABLE_BOOK;

    //----------------------------------------------------------------------------------------------
    // DatabaseHelper CONSTRUCTOR
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);          // When created requires a NAME and a VERSION
    }

    //----------------------------------------------------------------------------------------------
    // DATABASE FUNCTIONS

    // Executes the CREATE_USER_TABLE and CREATE_BOOK_TABLE query
    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_BOOK_TABLE);

    }

    // Upgrading the database by dropping old and creating new
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drops old tables
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_BOOK_TABLE);

        onCreate(db);
    }

    //----------------------------------------------------------------------------------------------
    // USER TABLE FUNCTIONS

    // Adds a User to the USER table
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();                             // Creates a ContentValues object
        values.put(COLUMN_USER_NAME, user.getName());                           // Assigns user name to the right column
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER,  null, values);                   // Inserts user data into the User table
        db.close();                                                             // Closes access to the database
    }

    // Checks if the user already exists
    public boolean checkUser(String email) {
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount >0){
            return  true;
        }

        return false;
    }

    // Checks for user email and user password
    public boolean checkUser(String email, String password) {
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount >0){
            return  true;
        }

        return false;
    }

    //----------------------------------------------------------------------------------------------
    // BOOK TABLE FUNCTIONS

    /**create record**/
    public void saveNewBook(Book book) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_TITLE, book.getTitle());
        values.put(COLUMN_BOOK_CATEGORY, book.getCategory());
        values.put(COLUMN_BOOK_ISBN, book.getIsbn());
        values.put(COLUMN_BOOK_DESCRIPTION, book.getDescription());
        values.put(COLUMN_BOOK_IMAGE, book.getImage());

        // insert
        db.insert(TABLE_BOOK,null, values);
        db.close();
    }

    /**Query records, give options to filter results**/
    public List<Book> bookList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_BOOK;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_BOOK + " ORDER BY "+ filter;
        }

        List<Book> bookLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Book book;

        if (cursor.moveToFirst()) {
            do {
                book = new Book();

                book.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_BOOK_ID)));
                book.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_TITLE)));
                book.setIsbn(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_ISBN)));
                book.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_CATEGORY)));
                book.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_DESCRIPTION)));
                book.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_IMAGE)));
                bookLinkedList.add(book);
            } while (cursor.moveToNext());
        }


        return bookLinkedList;
    }


    /**Query only 1 record**/
    public Book getBookByID(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_BOOK + " WHERE id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        Book receivedBook = new Book();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedBook.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_TITLE)));
            receivedBook.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_CATEGORY)));
            receivedBook.setIsbn(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_ISBN)));
            receivedBook.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_DESCRIPTION)));
            receivedBook.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_IMAGE)));
        }


        return receivedBook;

    }

    /**delete record**/
    public void deleteBookRecord(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_BOOK+" WHERE id='"+id+"'");
        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }

    /**update record**/
    public void updateBookRecord(long bookId, Context context, Book updatedBook) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_BOOK+" SET title ='" + updatedBook.getTitle() + "',category ='"
        + updatedBook.getCategory() + "',isbn ='" + updatedBook.getIsbn() + "',description = '"
        + updatedBook.getDescription() + "',image ='"+ updatedBook.getImage() + "'  WHERE id='" + bookId + "'");
        // Provides a to the user informing them of the successful operation
        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();


    }

}
