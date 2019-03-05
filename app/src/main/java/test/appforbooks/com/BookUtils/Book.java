package test.appforbooks.com.BookUtils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import test.appforbooks.com.SchoolUtils.ResultManager;
import test.appforbooks.com.SchoolUtils.SchoolFinder;
import test.appforbooks.com.SchoolUtils.SchoolManagerInterface;
import test.appforbooks.com.Utils.VolleyResponse;

public class Book {
    private String title, author, isbn, subject;
    private String imageURL, amazonPage, price;
    private String lowestPrice1, lowestPrice2;

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

    public String getShortTitle() {
        int endIndex = (title.length() < 25) ? title.length() : 25;
        return capitalizeString(title).substring(0, endIndex) + ((endIndex == 25)?"...":"");
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
//        int endIndex = (title.length() < 25) ? title.length() : 25;
//        this.title = capitalizeString(title).substring(0, endIndex) + ((endIndex == 25)?"...":"");
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = "di "+capitalizeString(author).replaceAll("-",",");
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public String getSubject(){
        return subject;
    }

    public String getLowestPrice1() {
        return lowestPrice1;
    }

    public void setLowestPrice1(String lowestPrice1) {
        this.lowestPrice1 = lowestPrice1;
    }

    public String getLowestPrice2() {
        return lowestPrice2;
    }

    public void setLowestPrice2(String lowestPrice2) {
        this.lowestPrice2 = lowestPrice2;
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


                    callback.onResponse();
                }catch(Exception e){
                    Log.e("ERROR MANAGE RESULT 1", e.getMessage());
                    Log.e("ERROR MANAGE RESULT 2", ""+e.getCause());
                }

            }
        });
    }

    public void getBookPrices(final VolleyResponse callback){
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


                    JSONObject offerSummary = new JSONObject(book.getString("OfferSummary"));
                    JSONObject price1 = offerSummary.getJSONObject("LowestNewPrice");
                    JSONObject price2 = offerSummary.getJSONObject("LowestUsedPrice");
                    setLowestPrice1(price1.getString("FormattedPrice"));
                    setLowestPrice2(price2.getString("FormattedPrice"));
                    callback.onResponse();
                }catch(Exception e){
                    Log.e("ERROR MANAGE RESULT 1", e.getMessage());
                    Log.e("ERROR MANAGE RESULT 2", ""+e.getCause());
                }

            }
        });
    }



    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}

