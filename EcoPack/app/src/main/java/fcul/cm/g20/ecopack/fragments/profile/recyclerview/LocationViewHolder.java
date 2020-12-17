package fcul.cm.g20.ecopack.fragments.profile.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fcul.cm.g20.ecopack.R;

public class LocationViewHolder extends RecyclerView.ViewHolder {
    private TextView locationName;
    private TextView visitDate;

    public LocationViewHolder(@NonNull View itemView, final LocationAdapter.OnLocationClickListener onLocationClickListener) {
        super(itemView);

        // TODO: ADD THE MARKER SETTER BASED ON THE LOCATION INFO
        locationName = itemView.findViewById(R.id.item_location_name);
        visitDate = itemView.findViewById(R.id.item_location_date);

        setDrawingClickListener(itemView, onLocationClickListener);
    }

    private void setDrawingClickListener(@NonNull View itemView, final LocationAdapter.OnLocationClickListener onLocationClickListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLocationClickListener != null)
                    if (getAdapterPosition() != RecyclerView.NO_POSITION)
                        onLocationClickListener.onLocationClick(getAdapterPosition());
            }
        });
    }

    public TextView getLocationName() {
        return locationName;
    }

    public TextView getVisitDate() {
        return visitDate;
    }
}