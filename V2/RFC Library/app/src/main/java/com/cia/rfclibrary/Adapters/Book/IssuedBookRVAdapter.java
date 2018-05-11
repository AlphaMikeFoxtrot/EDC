package com.cia.rfclibrary.Adapters.Book;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cia.rfclibrary.Classes.Book;
import com.cia.rfclibrary.R;

import java.util.ArrayList;

public class IssuedBookRVAdapter extends RecyclerView.Adapter<IssuedBookRVAdapter.IssuedBookViewHolder> {

    Context context;
    ArrayList<Book> books;

    public IssuedBookRVAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public IssuedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_stuff_card, parent, false);
        return new IssuedBookViewHolder(listItemView, this.context, this.books);

    }

    @Override
    public void onBindViewHolder(@NonNull IssuedBookViewHolder holder, int position) {

        holder.bookId.setText(this.books.get(position).getBookId());
        holder.subId.setText(this.books.get(position).getIssuedToName());

    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }

    public class IssuedBookViewHolder extends RecyclerView.ViewHolder{

        TextView bookId, subId;
        Context context;
        ArrayList<Book> books;

        public IssuedBookViewHolder(View itemView, Context context, ArrayList<Book> books) {
            super(itemView);

            this.bookId = itemView.findViewById(R.id.view_stuff_obj_id);
            this.subId = itemView.findViewById(R.id.view_stuff_sub_id);

            this.context = context;
            this.books = books;

        }
    }

}
