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

public class SchoolFinder {
    private String regionId;
    private static final String API_URL = "https://www.adozionilibriscolastici.it/v1/";

    public static final String API_CITY = "regioni/";
    public static final String API_PROVINCE = "province/";
    public static final String API_SCHOOL = "scuole/";

    public SchoolFinder(String regionId){
        this.regionId = regionId;
    }

    public static void makeRequest(String REST_URL, Context context, final ResultManager resultManager){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = API_URL+REST_URL;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resultManager.managerResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERRORE VOLLEY", error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    public String getRegionId(){
        return regionId;
    }

    //Pre-Made request
    //REGION
    public void regionRequest(String query, Context context, final DataGetter callback){
        final HashMap<String, String> data = new HashMap<String, String>();
        makeRequest(query, context, new ResultManager() {
            @Override
            public void managerResult(String result) {
                try{
                    JSONArray resArray = new JSONArray(result);
                    for(int i=0;i<resArray.length();i++){
                        JSONObject book = resArray.getJSONObject(i);
                        String id = book.getString("ID");
                        String name = book.getString("VALUE");
                        data.put(id, name);
                    }
                }catch (Exception e){
                    Log.e("ERRORE CLASS RESPONSE", "ERRORE");
                    Log.e("ERRORE CLASS RESPONSE", e.getMessage());
                }
                callback.sendResult(data);
            }
        });
    }

    //PROVINCE
    public void provinceRequest(String query, Context context, final DataGetter callback){
        final ArrayList<School> data = new ArrayList<School>();
        makeRequest(query, context, new ResultManager() {
            @Override
            public void managerResult(String result) {
                try{
                    JSONArray resArray = new JSONArray(result);
                    for(int i=0;i<resArray.length();i++){
                        JSONObject school = resArray.getJSONObject(i);
                        String type = school.getString("TIPO_SCUOLA");
                        String name = school.getString("NOMSCU");
                        String address = school.getString("INDSCU");
                        String code = school.getString("COSCUO");
                        data.add(new School(code, name, address, type));
                    }
                }catch (Exception e){
                    Log.e("ERRORE CLASS RESPONSE", "ERRORE");
                    Log.e("ERRORE CLASS RESPONSE", e.getMessage());
                }
                callback.sendResult(data);
            }
        });
    }

    //SCHOOL
    public void schoolRequest(String query, Context context, final DataGetter callback){
        final ArrayList<School> data = new ArrayList<School>();
        makeRequest(query, context, new ResultManager() {
            @Override
            public void managerResult(String result) {
                try{
                    JSONArray resArray = new JSONArray(result);
                    for(int i=0;i<resArray.length();i++){
                        JSONObject school = resArray.getJSONObject(i);
                        String type = school.getString("TIPO_SCUOLA");
                        String name = school.getString("NOMSCU");
                        String address = school.getString("INDSCU");
                        String code = school.getString("COSCUO");
                        data.add(new School(code, name, address, type));
                    }
                }catch (Exception e){
                    Log.e("ERRORE CLASS RESPONSE", "ERRORE");
                    Log.e("ERRORE CLASS RESPONSE", e.getMessage());
                }
                callback.sendResult(data);
            }
        });
    }

}