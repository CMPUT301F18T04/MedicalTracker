package ca.ualberta.t04.medicaltracker.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Locale;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
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
                if(position == 0){
                    Toast.makeText(getApplicationContext(),"You have changed the language to Chinese",Toast.LENGTH_SHORT).show();
                    setLocale("zh","CN");
                }
                if(position == 1){
                    Toast.makeText(getApplicationContext(),"You have changed the language to English",Toast.LENGTH_SHORT).show();
                    setLocale("en","CA");
                }
                if(position == 2){
                    Toast.makeText(getApplicationContext(),"You have changed the language to French",Toast.LENGTH_SHORT).show();
                    setLocale("fr","CA");
                }
                if(position == 3){
                    Toast.makeText(getApplicationContext(),"You have changed the language to Japanese",Toast.LENGTH_SHORT).show();
                    setLocale("ja","JP");
                }
                if(position == 4){
                    Toast.makeText(getApplicationContext(),"You have changed the language to Traditional Chinese",Toast.LENGTH_SHORT).show();
                    setLocale("zh","HK");
                }
                if(position == 5){
                    Toast.makeText(getApplicationContext(),"You have changed the language to Traditional Chinese",Toast.LENGTH_SHORT).show();
                    setLocale("zh","MO");
                }
            }
        });

    }

    public void setLocale(String lang,String district) {
        Locale myLocale = new Locale(lang,district);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LanguageActivity.class);
        startActivity(refresh);
        finish();
    }


}
