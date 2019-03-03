package test.appforbooks.com;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import test.appforbooks.com.Adapters.BookAdapter;
import test.appforbooks.com.BookUtils.Book;
import test.appforbooks.com.SchoolUtils.ResultManager;
import test.appforbooks.com.SchoolUtils.SchoolFinder;
import test.appforbooks.com.SchoolUtils.SchoolManager;
import test.appforbooks.com.SchoolUtils.SchoolManagerInterface;
import test.appforbooks.com.Utils.PrefManager;
import test.appforbooks.com.Utils.VolleyResponse;

public class MainActivity extends AppCompatActivity {

    RecyclerView bList;
    RecyclerView.Adapter bAdapter;
    ArrayList<Book> bookList;

    PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList = new ArrayList<>();

        prefManager = new PrefManager(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        bList = findViewById(R.id.books_list);
        bList.setHasFixedSize(true);
        bList.setLayoutManager(new LinearLayoutManager(this));

        getAllBooks(new VolleyResponse() {
            @Override
            public void onResponse() {
                bAdapter = new BookAdapter(bookList);
                bList.setAdapter(bAdapter);

                bList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "hey", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void getAllBooks(final VolleyResponse callback){
        String query = "libri/"+prefManager.getClassroomCode()+"/"+prefManager.getSchoolCode();
        SchoolFinder.makeRequest(query, this, new ResultManager() {
            @Override
            public void managerResult(String result) {
                try{
                    JSONArray resArray = new JSONArray(result);
                    for(int i=0;i<resArray.length();i++){
                        JSONObject book = resArray.getJSONObject(i);
                        String isbnCode = book.getString("ISBN");
                        String authors = book.getString("AUTORI");
                        Book bookObj = new Book(isbnCode, MainActivity.this);
                        bookObj.setAuthor(authors);
                        bookList.add(bookObj);
                    }
                    callback.onResponse();
                }catch(Exception e){
                    Log.e("ERRORE LIBRI: ",e.getMessage());
                }
            }
        });
    }


}
