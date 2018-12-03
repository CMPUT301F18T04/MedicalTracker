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

import ca.ualberta.t04.medicaltracker.Model.Language;
import ca.ualberta.t04.medicaltracker.R;

/**
 * This activity is a custom language list adapter for the application
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */


public class LanguageAdapter extends ArrayAdapter {

    private int recourseId;
    private List<Language> countryLanguages;

    public LanguageAdapter(@NonNull Context context, int resource, @NonNull List<Language> countryLanguages) {
        super(context, resource, countryLanguages);
        recourseId = resource;
        this.countryLanguages = countryLanguages;
    }

    /**
     * returns the custom view
     * @param position int
     * @param convertView View
     * @param parent ViewGroup
     * @return view View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(recourseId, null);
        TextView countryName = view.findViewById(R.id.tvCountry);
        ImageView countryPic = view.findViewById(R.id.ivCountry);

        Language language = countryLanguages.get(position);
        countryName.setText(language.countryName);
        countryPic.setImageResource(language.resId);

        return view;
    }

}
