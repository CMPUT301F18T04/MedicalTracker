package ca.ualberta.t04.medicaltracker.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ca.ualberta.t04.medicaltracker.R;

/*
  This activity displays the information on the development of the application
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String[] developers = {getString(R.string.developer_AnnabelleZhang), getString(R.string.developer_DevinDai), getString(R.string.developer_HumphreyLu),
                            getString(R.string.developer_LexiLi), getString(R.string.developer_PeterMa), getString(R.string.developer_YuanWang)};

        ListView listView = findViewById(R.id.about_list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, developers);

        listView.setAdapter(adapter);
    }
}
