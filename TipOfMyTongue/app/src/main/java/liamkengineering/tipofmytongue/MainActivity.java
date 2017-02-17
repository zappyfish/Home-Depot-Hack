package liamkengineering.tipofmytongue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.json.*;
import java.util.*;
import android.widget.Button;
import android.widget.EditText;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    //list of most common words in english. Copied from https://gist.github.com/gravitymonkey/2406023
    public static final String[] IGNORED_WORDS = new String[] {"the","of","and","a","to","in","is",
            "you","that","it","he","was","for","on","are","as","with","his","they","I","at","be",
            "this","have","from","or","one","had","by","word","but","not","what","all","were","we",
            "when","your","can","said","there","use","an","each","which","she","do","how","their",
            "if","will","up","other","about","out","many","then","them","these","so","some","her",
            "would","make","like","him","into","has","look","two","more","write","go","see",
            "no","way","could","people","my","than","first","been","call","who",
            "its","now","find","down","day","did","get","come","made","may","part"};;
    public static final Set<String> MY_SET = new HashSet<String>(Arrays.asList(IGNORED_WORDS));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchEnter = (EditText) findViewById(R.id.searchPhrase);
        Button searchBut = (Button) findViewById(R.id.searchButton);


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
