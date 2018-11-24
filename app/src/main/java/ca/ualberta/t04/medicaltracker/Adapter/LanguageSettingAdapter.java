package ca.ualberta.t04.medicaltracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.ualberta.t04.medicaltracker.Controller.CountryLanguage;
import ca.ualberta.t04.medicaltracker.R;


public class LanguageSettingAdapter extends ArrayAdapter {

    private int recourseId;
    private List<CountryLanguage> countryLanguages;

    public LanguageSettingAdapter(@NonNull Context context, int resource, @NonNull List<CountryLanguage> countryLanguages) {
        super(context, resource, countryLanguages);
        recourseId = resource;
        this.countryLanguages = countryLanguages;
    }

    // returns the custom view
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(recourseId, null);
        TextView countryName = view.findViewById(R.id.tvCountry);
        ImageView countryPic = view.findViewById(R.id.ivCountry);

        CountryLanguage country = countryLanguages.get(position);
        countryName.setText(country.countryName);
        countryPic.setImageResource(country.resId);

        return view;
    }

    }