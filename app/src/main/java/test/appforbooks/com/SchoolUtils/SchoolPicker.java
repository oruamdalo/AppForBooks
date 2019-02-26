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
import java.util.HashMap;

public class SchoolPicker {

    private String provinceId;
    private HashMap<String, String> municipalities;
    private HashMap<String, String> provinces;
    private ArrayList<School> schools;

    public SchoolPicker(String provinceId){
        municipalities = new HashMap<>();
        provinces = new HashMap<>();
        schools = new ArrayList<>();
        this.provinceId = provinceId;
    }

    public void getProvinces(Context context, final SchoolManagerInterface callback){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://www.adozionilibriscolastici.it/v1/regioni/"+provinceId;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray resArray = new JSONArray(response);
                            for(int i=0;i<resArray.length();i++){
                                JSONObject book = resArray.getJSONObject(i);
                                String id = book.getString("ID");
                                String name = book.getString("VALUE");
                                provinces.put(id, name);
                            }
                        }catch (Exception e){
                            Log.e("ERRORE CLASS RESPONSE", "ERRORE");
                            Log.e("ERRORE CLASS RESPONSE", e.getMessage());
                        }
                        callback.getResults(provinces);
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

    public void getMunicipality(String provinceShort, Context context, final SchoolManagerInterface callback){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://www.adozionilibriscolastici.it/v1/province/"+provinceShort;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray resArray = new JSONArray(response);
                            for(int i=0;i<resArray.length();i++){
                                JSONObject book = resArray.getJSONObject(i);
                                String id = book.getString("ID");
                                String name = book.getString("VALUE");
                                municipalities.put(id, name);
                            }
                        }catch (Exception e){
                            Log.e("ERRORE CLASS RESPONSE", "ERRORE");
                            Log.e("ERRORE CLASS RESPONSE", e.getMessage());
                        }
                        callback.getResults(municipalities);
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


    public void getSchool(String munName, final String schoolType, Context context, final SchoolManagerInterface callback){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://www.adozionilibriscolastici.it/v1/scuole?locId="+munName+"&grado="+schoolType;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray resArray = new JSONArray(response);
                            for(int i=0;i<resArray.length();i++){
                                JSONObject school = resArray.getJSONObject(i);
                                String type = school.getString("TIPO_SCUOLA");
                                String name = school.getString("NOMSCU");
                                String address = school.getString("INDSCU");
                                String code = school.getString("COSCUO");
                                schools.add(new School(code, name, address, type));
                            }
                        }catch (Exception e){
                            Log.e("ERRORE CLASS RESPONSE", "ERRORE");
                            Log.e("ERRORE CLASS RESPONSE", e.getMessage());
                        }
                        Log.d("SCHOOL VALUES: ", Integer.toString(schools.size()));
                        callback.getResults(schools);
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

    public String getProvinceId() {
        return provinceId;
    }
}
