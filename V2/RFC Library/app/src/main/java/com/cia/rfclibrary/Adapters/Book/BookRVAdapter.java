package com.cia.rfclibrary.Adapters.Book;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cia.rfclibrary.Classes.Book;
import com.cia.rfclibrary.R;

import java.util.ArrayList;

public class BookRVAdapter extends RecyclerView.Adapter<BookRVAdapter.IssuedBookViewHolder> {

    Context context;
    private ArrayList<Book> books = new ArrayList<>();

    public BookRVAdapter(Context context, ArrayList<Book> books) {
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
        holder.bookName.setText(this.books.get(position).getBookName());
        String isIssued = this.books.get(position).getIsIssued();

        if(isIssued == "1"){
            holder.isIssued.setText("TRUE");
            holder.issuedTo.setVisibility(View.VISIBLE);
            holder.issuedTo.setText(this.books.get(position).getIssuedToName());
        } else {
            holder.isIssued.setText("FALSE");
        }

    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }

    public class IssuedBookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView bookId, bookName, isIssued, issuedTo;
        ImageButton overflow;
        Context context;
        ArrayList<Book> books;

        public IssuedBookViewHolder(View itemView, Context context, ArrayList<Book> books) {
            super(itemView);

            this.bookId = itemView.findViewById(R.id.view_stuff_obj_id);
            this.bookName = itemView.findViewById(R.id.view_stuff_obj_name);
            this.isIssued = itemView.findViewById(R.id.view_stuff_is_issued);
            this.issuedTo = itemView.findViewById(R.id.view_stuff_issued_to);
            this.overflow = itemView.findViewById(R.id.view_stuff_overflow_menu);

            this.overflow.setOnClickListener(this);

            this.context = context;
            this.books = books;

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "" + this.books.get(getAdapterPosition()).getBookName(), Toast.LENGTH_SHORT).show();
        }
    }

}
