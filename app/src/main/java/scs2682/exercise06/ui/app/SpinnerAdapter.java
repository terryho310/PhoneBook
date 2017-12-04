package scs2682.exercise06.ui.app;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import scs2682.exercise06.R;
import scs2682.exercise06.data.ContactInfo;

/**
 * Created by Terry on 2017-10-17.
 */

public class SpinnerAdapter extends ArrayAdapter<ContactInfo> {

    private final LayoutInflater layoutInflater;
    private int selectItemPosition;

    public SpinnerAdapter(Context context, List<ContactInfo> contacts){
        super(context, 0, contacts);
        layoutInflater = LayoutInflater.from(context);
    }

    public void setSelectItemPosition(int selectItemPosition) {
        this.selectItemPosition = selectItemPosition;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View cell, @NonNull ViewGroup spinner) {
        if (cell == null) {
            //create a new cell ONLY if there is no such already recycled and ready to be used
            cell = layoutInflater.inflate(R.layout.appactivity_cell, spinner, false);
        }

        ContactInfo contactInfo = getItem(position);

        if (contactInfo != null){
            String title = contactInfo.getFirstName() + " " + contactInfo.getLastName();
            TextView textView = (TextView) cell;
            textView.setText(title);
        }

        return cell;
    }

    @Override
    public View getDropDownView(int position, @Nullable View cell, @NonNull ViewGroup spinner) {

        if (cell == null){
            cell = layoutInflater.inflate(R.layout.appactivity_dropdown_cell, spinner, false);
        }

        ContactInfo contactInfo = getItem(position);

        if (contactInfo != null){
            String title = contactInfo.getFirstName() + " " + contactInfo.getLastName();
            TextView textView = (TextView) cell;
            textView.setText(title);
        }

        // set darker background for currently selected item or 0 (no color) for any other
        int backgroundColor = selectItemPosition == position ? Color.LTGRAY : 0;
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }
}
