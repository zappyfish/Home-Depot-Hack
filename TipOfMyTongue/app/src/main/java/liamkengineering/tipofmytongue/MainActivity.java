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
    String qry = "";
    List<String> resultWords = new ArrayList<>();

    TextView text;
    EditText searchEnter;



    private void setUp() {
        apiKey = getString(R.string.apikey);
        engineKey = getString(R.string.enginekey);
        searchURL = "https://www.googleapis.com/customsearch/v1?";
    }

    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";


    //list of most common words in english. Copied from https://gist.github.com/gravitymonkey/2406023
    public static final String[] IGNORED_WORDS = new String[]{"the", "of", "and", "a", "to", "in", "is",
            "you", "that", "it", "he", "was", "for", "on", "are", "as", "with", "his", "they", "I", "at", "be",
            "this", "have", "from", "or", "one", "had", "by", "word", "but", "not", "what", "all", "were", "we",
            "when", "your", "can", "said", "there", "use", "an", "each", "which", "she", "do", "how", "their",
            "if", "will", "up", "other", "about", "out", "many", "then", "them", "these", "so", "some", "her",
            "would", "make", "like", "him", "into", "has", "look", "two", "more", "write", "go", "see",
            "no", "way", "could", "people", "my", "than", "first", "been", "call", "who",
            "its", "now", "find", "down", "day", "did", "get", "come", "made", "may", "part"};

    public static final Set<String> MY_SET = new HashSet<String>(Arrays.asList(IGNORED_WORDS));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        searchEnter = (EditText) findViewById(R.id.searchPhrase);
        Button searchBut = (Button) findViewById(R.id.searchButton);
        Button displayBut = (Button) findViewById(R.id.display);
        displayBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<resultWords.size() && i<10; ++i) {
                    Toast t = Toast.makeText(MainActivity.this, resultWords.get(i), Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
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
                        for (int i = 0; i < links.size(); ++i) {
                            Log.v("itearting: ", Integer.toString(i));
                            try {
                                Document doc = Jsoup.connect(links.get(i)).get();
                                Elements paragraphs = doc.select("p");
                                for (Element p : paragraphs) {
                                    List<String> temp = new ArrayList<>();
                                    temp = searchKeywords(p.text());
                                    for(int j = 0; j<temp.size(); ++j) {
                                        wordList.add(temp.get(j));
                                    }
                                    Log.v("vals", p.text());

                                }
                            } catch (Exception e) {

                            }
                        }resultWords = mostFreqKeywords(wordList);

                        return null;
                    }

                }.execute();




            }
        });


    }

    public static List<String> searchKeywords(String search) {
            String[] array = {"the", "of", "and", "a", "to", "in", "is",
                    "you", "that", "it", "he", "was", "for", "on", "are", "as", "with", "his", "they", "I", "at", "be",
                    "this", "have", "from", "or", "one", "had", "by", "word", "but", "not", "what", "all", "were", "we",
                    "when", "your", "can", "said", "there", "use", "an", "each", "which", "she", "do", "how", "their",
                    "if", "will", "up", "other", "about", "out", "many", "then", "them", "these", "so", "some", "her",
                    "would", "make", "like", "him", "into", "has", "look", "two", "more", "write", "go", "see",
                    "no", "way", "could", "people", "my", "than", "first", "been", "call", "who", "lets", "thing",
                    "its", "now", "find", "down", "day", "did", "get", "come", "made", "may", "part"};
            List<String> list = Arrays.asList(array);
            List<String> keywords = new ArrayList<String>();

            StringTokenizer tokenizer = new StringTokenizer(search);

            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                if (!list.contains(word)) {
                    keywords.add(word);

                }

            }
            return keywords;
        }

    /*public static void getResults() throws Exception {
        String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
        String search = "stackoverflow";
        String charset = "UTF-8";

        URL url = new URL(google + URLEncoder.encode(search, charset));
        Reader reader = new InputStreamReader(url.openStream(), charset);
        GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

        // Show title and URL of 1st result.
        System.out.println(results.getResponseData().getResults().get(0).getTitle());
        System.out.println(results.getResponseData().getResults().get(0).getUrl());
    }*/


    public static ArrayList<String> mostFreqKeywords(List<String> googResults) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        if(googResults!=null) {
            for (int i = 0; i < (int)googResults.size(); ++i) {
                if(googResults.get(i)!=null) {
                    if(!map.containsKey(googResults.get(i)))
                    {
                        map.put(googResults.get(i), 1);
                    }
                    else
                    map.put(googResults.get(i), (int) map.get(googResults.get(i)) + 1);
                }
            }
        }

        /*
        try {
            JSONArray arr = googResults.getJSONArray("hi"); // Insert key
            for (int i = 0; i < arr.length(); i++) { // Map of word and number of occurrences
                map.put(arr.get(i).toString(), map.get(arr.get(i).toString()) + 1);
            }
        } catch (JSONException e) {
            Log.e("OOPS", "unexpected JSON exception", e);
        }
        */

            Object[] a = map.entrySet().toArray();
            Arrays.sort(a, new Comparator() { // Sort array by value
                public int compare(Object o1, Object o2) {
                    return ((Map.Entry<String, Integer>) o2).getValue()
                            .compareTo(((Map.Entry<String, Integer>) o1).getValue());
                }
            });
            ArrayList<String> results = new ArrayList<String>();
            for (int i = 0; i<a.length; ++i) { // Add top ten most freq words
                results.add(a[i].toString());
            }

        return results;

    }
}
