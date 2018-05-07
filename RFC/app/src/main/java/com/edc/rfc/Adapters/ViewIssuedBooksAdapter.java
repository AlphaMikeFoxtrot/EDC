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

import java.util.ArrayList;

public class ViewIssuedBooksAdapter extends RecyclerView.Adapter<ViewIssuedBooksAdapter.ViewIsssuedBooksViewHolder> {

    Context context;
    ArrayList<Book> books;

    public ViewIssuedBooksAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public ViewIsssuedBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_stuff_card_view, parent, false);
        return new ViewIsssuedBooksViewHolder(listItemView, context, books);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewIsssuedBooksViewHolder holder, int position) {

        holder.issuedToId.setText(books.get(position).getIssuedToId());
        holder.issuedId.setText(books.get(position).getBookId());

    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }

    public class ViewIsssuedBooksViewHolder extends RecyclerView.ViewHolder{

        TextView issuedToId, issuedId;
        Context context;
        ArrayList<Book> books;

        public ViewIsssuedBooksViewHolder(View itemView, Context context, ArrayList<Book> books) {
            super(itemView);

            this.context = context;
            this.books = books;
            this.issuedId = itemView.findViewById(R.id.view_stuff_obj_id);
            this.issuedToId = itemView.findViewById(R.id.view_stuff_sub_id);
        }
    }

}
