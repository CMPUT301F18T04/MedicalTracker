package ca.ualberta.t04.medicaltracker.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import ca.ualberta.t04.medicaltracker.Adapter.LanguageAdapter;
import ca.ualberta.t04.medicaltracker.Model.Language;
import ca.ualberta.t04.medicaltracker.R;

/*
  This activity displays the language choosing options

  Will be implemented in part 5
 */

public class LanguageActivity extends AppCompatActivity {

    List<Language> languageData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        languageData = new ArrayList<>();

        languageData.add(new Language(R.drawable.china,"Chinese"));
        languageData.add(new Language(R.drawable.canada,"Canadian English"));
        languageData.add(new Language(R.drawable.france,"French"));
        languageData.add(new Language(R.drawable.japan,"Japanese"));
        languageData.add(new Language(R.drawable.hk,"Traditional Chinese(HK)"));
        languageData.add(new Language(R.drawable.macao,"Traditional Chinese(Macao)"));


        final ListView listView = findViewById(R.id.CountryList);

        LanguageAdapter adapter = new LanguageAdapter(this,R.layout.setting_language,languageData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),R.string.language_change_toast,Toast.LENGTH_SHORT).show();
            }
        });

    }


}
