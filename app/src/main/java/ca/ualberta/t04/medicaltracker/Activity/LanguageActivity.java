package ca.ualberta.t04.medicaltracker.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.t04.medicaltracker.Adapter.LanguageSettingAdapter;
import ca.ualberta.t04.medicaltracker.Controller.CountryLanguage;
import ca.ualberta.t04.medicaltracker.R;

/*
  This activity displays the language choosing options

  Will be implemented in part 5
 */

public class LanguageActivity extends AppCompatActivity {
    List<CountryLanguage> lstdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        lstdata = new ArrayList<>();

        lstdata.add(new CountryLanguage(R.drawable.china,"Chinese"));
        lstdata.add(new CountryLanguage(R.drawable.canada,"Canadian English"));
        lstdata.add(new CountryLanguage(R.drawable.france,"French"));
        lstdata.add(new CountryLanguage(R.drawable.japan,"Japanese"));
        lstdata.add(new CountryLanguage(R.drawable.hk,"Traditional Chinese"));
        lstdata.add(new CountryLanguage(R.drawable.macao,"Traditional Chinese"));


        ListView listView = findViewById(R.id.CountryList);

        LanguageSettingAdapter adapter  = new LanguageSettingAdapter(this,R.layout.setting_language,lstdata);
        listView.setAdapter(adapter);


    }

}
