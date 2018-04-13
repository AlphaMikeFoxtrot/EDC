package com.edc.rfc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edc.rfc.Classes.Book;
import com.edc.rfc.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class IssuedBookRVAdapter extends RecyclerView.Adapter<IssuedBookRVAdapter.IssuedBookRVViewHolder> {

    Context context;
    ArrayList<Book> books;

    public IssuedBookRVAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public IssuedBookRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(context).inflate(R.layout.view_stuff_card_view, parent, false);
        return new IssuedBookRVViewHolder(listItemView, context, books);
    }

    @Override
    public void onBindViewHolder(@NonNull IssuedBookRVViewHolder holder, int position) {

        Book currentBook = books.get(position);

        String idText = currentBook.getBookId() + " (" + currentBook.getBookName() + ")";
        holder.id.setText(idText);

        String subText = currentBook.getIssuedToId() + " (" + currentBook.getIssuedToName() + ")";
        holder.sub.setText(subText);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class IssuedBookRVViewHolder extends RecyclerView.ViewHolder{

        TextView sub, id;
        Context context;
        ArrayList<Book> books;

        public IssuedBookRVViewHolder(View itemView, Context context, ArrayList<Book> books) {
            super(itemView);

            this.context = context;
            this.books = books;

            this.sub = itemView.findViewById(R.id.view_stuff_sub);
            this.id = itemView.findViewById(R.id.view_stuff_id);
        }
    }

}
