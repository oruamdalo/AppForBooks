package test.appforbooks.com.SchoolUtils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import test.appforbooks.com.BookUtils.Book;

public class SchoolManager {

    private String schoolCode;
    private ArrayList<ClassRoom> classRooms;

    public SchoolManager(String schoolCode, Context context){
        this.schoolCode = schoolCode;
        classRooms = new ArrayList<>();
    }

    public void getClasses(Context context, final SchoolManagerInterface callback){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://www.adozionilibriscolastici.it/v1/classi/"+schoolCode;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
//                            Log.d("PARSE VALUES: ","PARSING");
//                            Log.d("VALUES: ", response);
                            JSONArray resArray = new JSONArray(response);
                            for(int i=0;i<resArray.length();i++){
                                JSONObject room = resArray.getJSONObject(i);
                                String number = room.getString("CLASSE");
                                String letter = room.getString("SEZION");
                                String desc = room.getString("DESCOMB");
                                String id = room.getString("CLID");
                                classRooms.add(new ClassRoom(id, number, letter, desc));
                            }
                        }catch (Exception e){
                            Log.e("ERRORE CLASS RESPONSE", "ERRORE");
                            Log.e("ERRORE CLASS RESPONSE", e.getMessage());
                        }
                        callback.getResults(classRooms);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERRORE VOLLEY", error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getClassBooks(String classId, Context context, final SchoolManagerInterface callback){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://www.adozionilibriscolastici.it/v1/libri/"+classId+"/"+schoolCode;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Book> books = new ArrayList<Book>();
                        try{
//                            Log.d("PARSE VALUES: ","PARSING");
//                            Log.d("VALUES: ", response);
                            JSONArray resArray = new JSONArray(response);
                            for(int i=0;i<resArray.length();i++){
                                JSONObject book = resArray.getJSONObject(i);
                                String title = book.getString("TITOLO");
                                String author = book.getString("AUTORI");
                                String isbn = book.getString("ISBN");
                                books.add(new Book(title, author, isbn));
                            }
                        }catch (Exception e){
                            Log.e("ERRORE CLASS RESPONSE", "ERRORE");
                            Log.e("ERRORE CLASS RESPONSE", e.getMessage());
                        }
                        callback.getResults(books);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERRORE VOLLEY", error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public String getSchoolCode(){
        return schoolCode;
    }
}