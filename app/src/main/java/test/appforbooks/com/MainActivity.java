package test.appforbooks.com;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import test.appforbooks.com.Activities.IntroSlider;
import test.appforbooks.com.Activities.MyBottomSheet;
import test.appforbooks.com.Adapters.BookAdapter;
import test.appforbooks.com.BookUtils.Book;
import test.appforbooks.com.SchoolUtils.ResultManager;
import test.appforbooks.com.SchoolUtils.SchoolFinder;
import test.appforbooks.com.SchoolUtils.SchoolManager;
import test.appforbooks.com.SchoolUtils.SchoolManagerInterface;
import test.appforbooks.com.Utils.PrefManager;
import test.appforbooks.com.Utils.VolleyResponse;

public class MainActivity extends AppCompatActivity implements MyBottomSheet.BottomSheetListener {

    RecyclerView bList;
    RecyclerView.Adapter bAdapter;
    ArrayList<Book> bookList;

    PrefManager prefManager;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        int id = menuItem.getItemId();
                        if(id ==  R.id.nav_account){
                            drawerLayout.openDrawer(GravityCompat.START);
                        }else if(id == R.id.nav_logout){
//            Toast.makeText(MainActivity.this, "CIAO",Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogTheme)
                                    .setTitle("Eseguire il logout?")
                                    .setMessage("Sei sicuro di voler uscire?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            prefManager.clearPreferences();
                                            startActivity(new Intent(MainActivity.this, IntroSlider.class));
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton("No", null)
                                    .setIcon(R.drawable.ic_logout)
                                    .show();
                        }


                        return true;
                    }
                });


        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Menu clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });


        bookList = new ArrayList<>();

        prefManager = new PrefManager(this);

        bList = findViewById(R.id.books_list);
        bList.setHasFixedSize(true);
        bList.setLayoutManager(new LinearLayoutManager(this));

        getAllBooks(new VolleyResponse() {
            @Override
            public void onResponse() {
                bAdapter = new BookAdapter(bookList);
                bList.setAdapter(bAdapter);
            }

            @Override
            public void onError() {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    private void getAllBooks(final VolleyResponse callback){
        String query = "libri/"+prefManager.getClassroomCode()+"/"+prefManager.getSchoolCode();
        SchoolFinder.makeRequest(query, this, new ResultManager() {
            @Override
            public void managerResult(String result) {
                try{
                    JSONArray resArray = new JSONArray(result);
                    for(int i=0;i<resArray.length();i++){
                        try{
                            JSONObject book = resArray.getJSONObject(i);
                            String isbnCode = book.getString("ISBN");
                            Book bookObj = new Book(isbnCode, MainActivity.this);
                            bookObj.setAuthor(book.getString("AUTORI"));
                            bookObj.setTitle(book.getString("TITOLO"));
                            bookList.add(bookObj);
                        }catch(Exception e){
                            Log.e("ERRORE ALL BOOKS",e.getMessage());
                            Book b = new Book("9788804665793", MainActivity.this);
                            b.setTitle("ERRORE");
                            b.setAuthor("ERRORE");
                            bookList.add(b);
                        }
                    }
                    callback.onResponse();
                }catch(Exception e){
                    Log.e("ERRORE LIBRI: ",e.getMessage());

                }
            }
        });
    }

    @Override
    public void onButtonClicked(String text) {

    }
}
