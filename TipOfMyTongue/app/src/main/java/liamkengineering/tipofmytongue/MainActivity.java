package liamkengineering.tipofmytongue;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import org.json.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.*;
import android.widget.Button;
import android.widget.EditText;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {
    //GoogleResults results;
    String apiKey;
    String engineKey;
    String searchURL;
    String result;
    BufferedReader br;
    URL url;
    String status = "";
    List<String> links = new ArrayList<>();
    List<String> wordList = new ArrayList<>();
    final String html = "<>p <span> foo </span> <em> bar <a> foobar </a> baz </em> </p>";
    String qry="";

    TextView text;
    EditText searchEnter;

    private void setUp() {
        apiKey = getString(R.string.apikey);
        engineKey = getString(R.string.enginekey);
        searchURL = "https://www.googleapis.com/customsearch/v1?";
    }

    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    public static final String[] IGNORED_WORDS = new String[]{"a", "an", "the", "thing"};
    public static final Set<String> MY_SET = new HashSet<String>(Arrays.asList(IGNORED_WORDS));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        searchEnter = (EditText) findViewById(R.id.searchPhrase);
        Button searchBut = (Button) findViewById(R.id.searchButton);
        text = (TextView) findViewById(R.id.test);

        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qry = searchEnter.getText().toString();
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            String key = apiKey;
                            URL url = null;
                            url = new URL("https://www.googleapis.com/customsearch/v1?q=" + qry + "&cx=012019511911701620333:ojaa7h2wxnc&key=" + key + "&alt=json");

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setRequestProperty("Accept", "application/json");
                            BufferedReader br = new BufferedReader(new InputStreamReader(
                                    (conn.getInputStream())));

                            String output;
                            Log.v("out", "here");
                            while ((output = br.readLine()) != null) {

                                if (output.contains("\"link\": \"")) {
                                    String link = output.substring(output.indexOf("\"link\": \"") + ("\"link\": \"").length(), output.indexOf("\","));
                                    Log.v("links:", link);       //Will print the google search links
                                    links.add(link);
                                }
                            }
                            conn.disconnect();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for(int i = 0; i < links.size(); ++i) {
                            Log.v("itearting: ", Integer.toString(i));
                            try {
                                Document doc = Jsoup.connect(links.get(i)).get();
                                Elements paragraphs = doc.select("p");
                                for (Element p : paragraphs) {
                                    wordList.add(p.text());
                                    Log.v("text: ", p.text());

                                }
                            }
                            catch(Exception e) {

                            }
                        }
                        return null;
                    }

                }.execute();



            }
        });


    }

    public static Set<String> searchKeywords(String search) {
        Set<String> keyWords = new HashSet<String>();
        StringTokenizer token = new StringTokenizer(search);

        while (token.hasMoreTokens()) {
            String keywordCandidate = token.nextToken().toLowerCase();
            if (!MY_SET.contains(keywordCandidate)) {
                keyWords.add(keywordCandidate);
            }

        }

        return keyWords;
    }
}



    public static ArrayList<String> mostFreqKeywords(JSONObject googResults) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        JSONArray arr = googleResults.getJSONArray("keyword"); // Insert key
        for (int i = 0; i < arr.length(); i++) { // Map of word and number of occurrences
            map.put(arr[i], map.get[arr[i]] + 1);
        }
        Object[] a = map.entrySet().toArray();
        Arrays.sort(a, new Comparator() { // Sort array by value
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });
        ArrayList<String> results = new ArrayList<String>();
        for (int i = 0; i < 10; ++i) { // Add top ten most freq words
            results.add(a[i].getValue());
        }
        return results;
    }


