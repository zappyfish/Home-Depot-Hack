package liamkengineering.tipofmytongue;

import android.content.Intent;
import android.net.Uri;
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

    TextView text;
    EditText searchEnter;

    private void setUp() {
        apiKey = getString(R.string.apikey);
        engineKey = getString(R.string.enginekey);
        searchURL = "https://www.googleapis.com/customsearch/v1?";
    }

    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    public static final String[] IGNORED_WORDS = new String[] { "a", "an", "the", "thing" };
    public static final Set<String> MY_SET = new HashSet<String>(Arrays.asList(IGNORED_WORDS));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setUp();
        searchEnter = (EditText) findViewById(R.id.searchPhrase);
        Button searchBut = (Button) findViewById(R.id.searchButton);
        text = (TextView)findViewById(R.id.test);

        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchURL = "http://www.google.com/#q=";
                searchURL += searchEnter.getText().toString();
                Toast t = Toast.makeText(MainActivity.this, searchURL, Toast.LENGTH_SHORT);
                t.show();
                Uri uri = Uri.parse(searchURL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public static Set<String> searchKeywords (String search){
        Set<String> keyWords = new HashSet<String>();
        StringTokenizer token = new StringTokenizer(search);

        while (token.hasMoreTokens()){
            String keywordCandidate = token.nextToken().toLowerCase();
            if (!MY_SET.contains(keywordCandidate)){
                keyWords.add(keywordCandidate);
            }

        }

        return keyWords;
    }

    public static void getResults() throws Exception {
        String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
        String search = "stackoverflow";
        String charset = "UTF-8";

        URL url = new URL(google + URLEncoder.encode(search, charset));
        Reader reader = new InputStreamReader(url.openStream(), charset);
        GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

        // Show title and URL of 1st result.
        System.out.println(results.getResponseData().getResults().get(0).getTitle());
        System.out.println(results.getResponseData().getResults().get(0).getUrl());
    }


    public static ArrayList<String> mostFreqKeywords(JSONObject googResults) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        JSONArray arr = googResults.getJSONArray("keyword"); // Insert key
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
}
