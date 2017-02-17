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


}
