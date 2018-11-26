package ca.ualberta.t04.medicaltracker.Activity;

import android.os.Bundle;
import android.provider.ContactsContract;
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
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Language;
import ca.ualberta.t04.medicaltracker.R;


public class LanguageActivity extends AppCompatActivity {

    List<Language> languageData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        languageData = new ArrayList<>();

        languageData.add(new Language(R.drawable.china,getString(R.string.Chinese)));
        languageData.add(new Language(R.drawable.canada,getString(R.string.English)));
        languageData.add(new Language(R.drawable.france,getString(R.string.French)));
        languageData.add(new Language(R.drawable.japan,getString(R.string.Japanese)));
        languageData.add(new Language(R.drawable.hk,getString(R.string.HK)));
        languageData.add(new Language(R.drawable.macao,getString(R.string.Macao)));


        final ListView listView = findViewById(R.id.CountryList);

        LanguageAdapter adapter = new LanguageAdapter(this,R.layout.setting_language,languageData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 0){
                    setLocale("zh","CN");
                }
                if(position == 1){
                    setLocale("en","CA");
                }
                if(position == 2){
                    setLocale("fr","CA");
                }
                if(position == 3){
                    setLocale("ja","JP");
                }
                if(position == 4){
                    setLocale("zh","HK");
                }
                if(position == 5){
                    setLocale("zh","MO");
                }
            }
        });

    }

    public void setLocale(String lang,String district) {
        DataController.updateLanguage(lang,district);
        Locale myLocale = new Locale(lang,district);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);
        finish();
        Toast.makeText(getApplicationContext(),R.string.language_change_toast,Toast.LENGTH_LONG).show();
    }


}
