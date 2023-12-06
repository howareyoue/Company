package com.example.company;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CarListAdapter extends ArrayAdapter<RegistrationInfo> {

    private Context context;

    public CarListAdapter(Context context, int resource, List<RegistrationInfo> registrationList) {
        super(context, resource, registrationList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final RegistrationInfo registrationInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.car_list_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.textView);
        String displayText = registrationInfo.getDepartureLocation() + " > " + registrationInfo.getDestination();
        textView.setText(displayText);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CarDetailActivity.class);
                intent.putExtra("driverName", registrationInfo.getDriverName());
                intent.putExtra("departureTime", registrationInfo.getDepartureTime());
                intent.putExtra("departureLocation", registrationInfo.getDepartureLocation());
                intent.putExtra("destination", registrationInfo.getDestination());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
