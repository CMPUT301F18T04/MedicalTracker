package ca.ualberta.t04.medicaltracker.Activity;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.ualberta.t04.medicaltracker.R;
import io.searchbox.core.Count;

public class LanguageAdapter extends ArrayAdapter<CountryLanguage> {

    Context context;
    int layoutResourceId;
    List<CountryLanguage> data=null;


    public LanguageAdapter(Context context, int resource, List<CountryLanguage> objects){
        super(context,resource,objects);

        this.layoutResourceId = resource;
        this.context = context;
        this.data=objects;
    }

    static class DataHolder
    {
        ImageView ivFlag;
        TextView tvCountryName;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataHolder holder = null;
        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId,parent);

            holder = new DataHolder();
            holder.ivFlag = (ImageView)convertView.findViewById(R.id.ivCountry);
            holder.tvCountryName = (TextView)convertView.findViewById(R.id.tvCountry);

            convertView.setTag(holder);
        }
        else{
            holder =(DataHolder)convertView.getTag();
        }

        CountryLanguage language = data.get(position);
        holder.tvCountryName.setText(language.countryName);
        holder.ivFlag.setImageResource(language.resId);

        return convertView;
    }
}
