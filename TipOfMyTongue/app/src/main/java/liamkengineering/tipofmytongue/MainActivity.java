package liamkengineering.tipofmytongue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.json.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public ArrayList<String> mostFreqKeywords(JSONObject googResults) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
         //each key in the jsonobject
        JSONArray arr = googResults.getJSONArray("keyword"); //insert key
        for (int i = 0; i < arr.length(); i++) {
            map.put(arr[i], map.get[arr[i]] + 1);
        }
        Object[] a = map.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });
        ArrayList<String> results = new ArrayList<String>();
        for (int i = 0; i < 10; ++i) {
            results.add(a[i].getValue());
        }
        return results;
    }

}
