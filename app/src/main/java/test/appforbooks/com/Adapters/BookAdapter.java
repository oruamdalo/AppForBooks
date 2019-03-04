package test.appforbooks.com.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public static class BookViewHolder extends RecyclerView.ViewHolder {
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



        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), ((TextView)v.findViewById(R.id.book_title)).getText().toString(),Toast.LENGTH_SHORT).show();
                MyBottomSheet bottomSheet = new MyBottomSheet();
                bottomSheet.show(((FragmentActivity)viewGroup.getContext()).getSupportFragmentManager(), "bottomSheet");
            }
        });


        return new BookViewHolder(v);
    }

    public void onBindViewHolder(final BookViewHolder viewHolder, final int i) {
        data.get(i).getBookInfo(new VolleyResponse() {
            @Override
            public void onResponse() {
                TextView title = viewHolder.bookView.findViewById(R.id.book_title);
                TextView author = viewHolder.bookView.findViewById(R.id.book_author);
                TextView price = viewHolder.bookView.findViewById(R.id.book_price);
                title.setText(data.get(i).getTitle());
                author.setText(data.get(i).getAuthor());
                price.setText(data.get(i).getPrice());

                ImageView thumbnail = viewHolder.bookView.findViewById(R.id.book_thumbnail);
                Picasso.get().load(data.get(i).getImageURL()).into(thumbnail);
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


}