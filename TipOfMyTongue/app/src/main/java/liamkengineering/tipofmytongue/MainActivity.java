package liamkengineering.tipofmytongue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchEnter = (EditText) findViewById(R.id.searchPhrase);

        Button searchBut = (Button) findViewById(R.id.searchButton);
    }
}
