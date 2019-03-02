package test.appforbooks.com.BookUtils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

import test.appforbooks.com.SchoolUtils.ResultManager;
import test.appforbooks.com.SchoolUtils.SchoolFinder;
import test.appforbooks.com.SchoolUtils.SchoolManagerInterface;
import test.appforbooks.com.Utils.VolleyResponse;

public class Book {
    private String title, author, isbn;

    private String imageURL, amazonPage, price;
    private Context c;

    public Book(String isbn, Context c){
        this.isbn = isbn;
        this.c = c;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAmazonPage() {
        return amazonPage;
    }

    public void setAmazonPage(String amazonPage) {
        this.amazonPage = amazonPage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void getBookInfo(final VolleyResponse callback){
        String query = "lookup/" + isbn;
        SchoolFinder.makeRequest(query, c, new ResultManager() {
            @Override
            public void managerResult(String result) {
                HashMap<String, String> bookInfo = new HashMap<>();
                try{
                    Object json = new JSONTokener(result).nextValue();
                    JSONObject book;
                    if (json instanceof JSONObject) {
                        //you have an object
                        Log.d("OUTPUT: ","OBJECT");
                        book = new JSONObject(result);
                    }else{
                        Log.d("OUTPUT: ","ARRAY");
                        JSONArray array = new JSONArray(result);
                        book = array.getJSONObject(array.length()-1);
                    }



                    setAmazonPage(book.getString("DetailPageURL"));
                    setImageURL(book.getJSONObject("LargeImage").getString("URL"));

                    JSONObject itemAttr = book.getJSONObject("ItemAttributes");

                    setPrice(itemAttr.getJSONObject("ListPrice").getString("FormattedPrice"));

                    String authors = "";
                    Object authorJson = new JSONTokener(itemAttr.get("Author").toString()).nextValue();
                    Log.d("AUTHOR JSON 1", ""+authorJson);
                    Log.d("AUTHOR JSON 2", ""+ authorJson.getClass());
//                    if(authorJson instanceof String){
//                        Log.d("AUTHOR JSON: ","STRING");
//                        authors = authorJson.toString();
//                    }else if(authorJson instanceof JSONArray){
//                        Log.d("AUTHOR JSON: ","ARRAY");
//                        JSONArray array = new JSONArray(authorJson);
//                        for(int i=0;i<array.length();i++){
//                            authors += array.get(i).toString();
//                            if(i < array.length()-1){
//                                authors += ", ";
//                            }
//                        }
//                    }

                    setAuthor(authors);
//                    setAuthor("");
                    setTitle(itemAttr.getString("Title").substring(0, 25));



                    callback.onResponse();
                }catch(Exception e){
                    Log.e("ERROR MANAGE RESULT 1", e.getMessage());
                    Log.e("ERROR MANAGE RESULT 2", ""+e.getCause());
                }

            }
        });
    }
}

