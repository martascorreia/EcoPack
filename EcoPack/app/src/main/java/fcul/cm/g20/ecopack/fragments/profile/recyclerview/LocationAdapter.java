package fcul.cm.g20.ecopack.fragments.profile.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fcul.cm.g20.ecopack.models.MarkerTypes;
import fcul.cm.g20.ecopack.models.StoreVisit;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.utils.Utils;

public class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> {
    public interface OnLocationClickListener {
        void onLocationClick(int position);
    }

    private Context context;
    private OnLocationClickListener onLocationClickListener;
    private ArrayList<StoreVisit> userVisits;

    public LocationAdapter(Context context, ArrayList<StoreVisit> userVisits) {
        this.context = context;
        this.userVisits = userVisits;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_location, parent, false);
        return new LocationViewHolder(view, onLocationClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        StoreVisit currentItem = userVisits.get(position);

        holder.getLocationName().setText(currentItem.getStoreName());
        holder.getLocationDate().setText(Utils.getDateFromMilliseconds(currentItem.getDate()));

        MarkerTypes marker = currentItem.getMarkerTag();
        if(marker!=null) {
            switch (marker) {
                case marker_reusable_home:
                    holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_reusable_home_round));
                    break;
                case marker_bio_home:
                    holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_bio_home_round));
                    break;
                case marker_paper_home:
                    holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_paper_home_round));
                    break;
                case marker_plastic_home:
                    holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_plastic_home_round));
                    break;
                case marker_reusable:
                    holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_reusable_round));
                    break;
                case marker_bio:
                    holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_bio_round));
                    break;
                case marker_paper:
                    holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_paper_round));
                    break;
                case marker_plastic:
                    holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_plastic_round));
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return userVisits.size();
    }

    public void setOnLocationClickListener(OnLocationClickListener onLocationClickListener) {
        this.onLocationClickListener = onLocationClickListener;
    }
}