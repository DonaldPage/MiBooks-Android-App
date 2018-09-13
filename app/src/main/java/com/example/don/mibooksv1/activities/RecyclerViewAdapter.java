package com.example.don.mibooksv1.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.don.mibooksv1.activities.UpdateBookActivty;
import com.example.don.mibooksv1.R;
import com.example.don.mibooksv1.model.Book;
import com.example.don.mibooksv1.sql.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<Book> mBookList;
    private Context mContext;
    private RecyclerView mRecyclerV;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView bookTitleTxtV;
        public TextView bookCategoryTxtV;
        public TextView bookIsbnTxtV;
        public TextView bookDescriptionTxtV;
        public ImageView bookImageImgV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            bookTitleTxtV = (TextView) v.findViewById(R.id.title);
            bookCategoryTxtV = (TextView) v.findViewById(R.id.category);
            bookIsbnTxtV = (TextView) v.findViewById(R.id.isbn);
            bookDescriptionTxtV = (TextView) v.findViewById(R.id.description);
            bookImageImgV = (ImageView) v.findViewById(R.id.image);




        }
    }

    public void add(int position, Book book) {
        mBookList.add(position, book);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mBookList.remove(position);
        notifyItemRemoved(position);
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(List<Book> myDataset, Context context, RecyclerView recyclerView) {
        mBookList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
//        LayoutInflater inflater = LayoutInflater.from(
//               parent.getContext());
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Book book = mBookList.get(position);
        holder.bookTitleTxtV.setText("Title: " + book.getTitle());
        holder.bookCategoryTxtV.setText("Category: " + book.getCategory());
        holder.bookIsbnTxtV.setText("ISBN: " + book.getIsbn());
        holder.bookDescriptionTxtV.setText("Description: " + book.getDescription());
        Picasso.with(mContext).load(book.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.bookImageImgV);

        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose option");
                builder.setMessage("Update or delete book?");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


//                        Intent intent = new Intent(mContext, UpdateBookActivty.class);
//                        intent.putExtra("BOOK_ID", book.getId());
//                        mContext.startActivity(intent);
                        //go to update activity
                        goToUpdateActivity(book.getId());

                    }
                }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                        dbHelper.deleteBookRecord(book.getId(), mContext);

                        mBookList.remove(position);
                        mRecyclerV.removeViewAt(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mBookList.size());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });


    }

    private void goToUpdateActivity(long bookid){
        Intent goToUpdate = new Intent(mContext, UpdateBookActivty.class);
        goToUpdate.putExtra("BOOK_ID", bookid);
        mContext.startActivity(goToUpdate);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mBookList.size();
    }



}
