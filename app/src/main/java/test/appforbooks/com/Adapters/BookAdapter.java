package test.appforbooks.com.Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import test.appforbooks.com.Activities.MyBottomSheet;
import test.appforbooks.com.BookUtils.Book;
import test.appforbooks.com.R;
import test.appforbooks.com.Utils.VolleyResponse;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> implements MyBottomSheet.BottomSheetListener{

    ArrayList<Book> data;

    public BookAdapter(ArrayList<Book> data){
        this.data = new ArrayList<>(data);
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public View bookView;
        public BookViewHolder(View v) {
            super(v);
            bookView = v;
        }
    }

    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {

        // create a new view
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_item_list, viewGroup, false);


        return new BookViewHolder(v);
    }
    //TODO: fix null value for lowset price and add isbn displayer
    public void onBindViewHolder(final BookViewHolder viewHolder, final int i) {
        viewHolder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(i).getBookPrices(new VolleyResponse() {
                    Bundle dataBook = new Bundle();
                    MyBottomSheet bottomSheet = new MyBottomSheet();
                    @Override
                    public void onResponse() {
                        try {
                            dataBook.putString("title", data.get(i).getTitle());
                            dataBook.putString("price1", data.get(i).getLowestPrice1());
                            dataBook.putString("price2", data.get(i).getLowestPrice2());
                            dataBook.putString("imageUrl", data.get(i).getImageURL());
                            dataBook.putString("isbn", data.get(i).getIsbn());
                        }catch(Exception e){
                            Log.d("ERROR DATA BOOK", e.getMessage());

                        }
                        bottomSheet.setArguments(dataBook);
                        Log.d("Price used: ",""+data.get(i).getLowestPrice1());
                        bottomSheet.show(((FragmentActivity)viewHolder.bookView.getContext()).getSupportFragmentManager(), "bottomSheet");
                    }

                    @Override
                    public void onError() {
                        Log.e("ERRORE BOOK ADAPTER","ERRORE");
                        dataBook.putString("title", data.get(i).getTitle());
                        dataBook.putString("price1", "Non disponibile");
                        dataBook.putString("price2", "Non disponibile");
                        dataBook.putString("imageUrl", "https://via.placeholder.com/150x200");
                        dataBook.putString("isbn", data.get(i).getIsbn());
                        bottomSheet.setArguments(dataBook);
                        Log.d("Price used: ",""+data.get(i).getLowestPrice1());
                        bottomSheet.show(((FragmentActivity)viewHolder.bookView.getContext()).getSupportFragmentManager(), "bottomSheet");
                    }
                });
            }
        });
        data.get(i).getBookInfo(new VolleyResponse() {
            @Override
            public void onResponse() {
                TextView title = viewHolder.bookView.findViewById(R.id.book_title);
                TextView author = viewHolder.bookView.findViewById(R.id.book_author);
                TextView price = viewHolder.bookView.findViewById(R.id.book_price);
                title.setText(data.get(i).getShortTitle());
                author.setText(data.get(i).getAuthor());
                price.setText(data.get(i).getPrice());

                ImageView thumbnail = viewHolder.bookView.findViewById(R.id.book_thumbnail);
                Picasso.get().load(data.get(i).getImageURL()).into(thumbnail);
            }

            @Override
            public void onError() {
                TextView title = viewHolder.bookView.findViewById(R.id.book_title);
                TextView author = viewHolder.bookView.findViewById(R.id.book_author);
                TextView price = viewHolder.bookView.findViewById(R.id.book_price);
                title.setText(data.get(i).getShortTitle());
                author.setText(data.get(i).getAuthor());
                price.setText("Non disponibile");

                ImageView thumbnail = viewHolder.bookView.findViewById(R.id.book_thumbnail);
                Picasso.get().load("https://via.placeholder.com/150x200").into(thumbnail);
            }

        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onButtonClicked(String text) {
    }


    class ItemListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }
}