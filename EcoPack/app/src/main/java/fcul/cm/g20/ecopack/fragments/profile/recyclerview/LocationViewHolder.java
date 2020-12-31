package fcul.cm.g20.ecopack.fragments.profile.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fcul.cm.g20.ecopack.R;

public class LocationViewHolder extends RecyclerView.ViewHolder {
    private ImageView locationMarker;
    private TextView locationName;
    private TextView locationDate;

    public LocationViewHolder(@NonNull View itemView, final LocationAdapter.OnLocationClickListener onLocationClickListener) {
        super(itemView);

        locationMarker = itemView.findViewById(R.id.item_location_marker);
        locationName = itemView.findViewById(R.id.item_location_name);
        locationDate = itemView.findViewById(R.id.item_location_date);

        setLocationClickListener(itemView, onLocationClickListener);
    }

    private void setLocationClickListener(@NonNull View itemView, final LocationAdapter.OnLocationClickListener onLocationClickListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLocationClickListener != null)
                    if (getAdapterPosition() != RecyclerView.NO_POSITION)
                        onLocationClickListener.onLocationClick(getAdapterPosition());
            }
        });
    }

    public ImageView getLocationMarker() {
        return locationMarker;
    }

    public TextView getLocationName() {
        return locationName;
    }

    public TextView getLocationDate() {
        return locationDate;
    }
}