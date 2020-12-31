package fcul.cm.g20.ecopack.fragments.profile.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import fcul.cm.g20.ecopack.R;

public class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> {
    public interface OnLocationClickListener {
        void onLocationClick(int position);
    }

    private Context context;
    private OnLocationClickListener onLocationClickListener;
    private ArrayList<HashMap<String, String>> userVisits;

    public LocationAdapter(Context context, ArrayList<HashMap<String, String>> userVisits) {
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
        HashMap<String, String> currentItem = userVisits.get(position);

        holder.getLocationName().setText(currentItem.get("store_name"));
        holder.getLocationDate().setText(currentItem.get("visit_date"));

        String marker = currentItem.get("visit_marker");
        if (marker.equals("0")) holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_reusable_home_round));
        else if (marker.equals("1")) holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_bio_home_round));
        else if (marker.equals("2")) holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_paper_home_round));
        else if (marker.equals("3")) holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_plastic_home_round));
        else if (marker.equals("4")) holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_reusable_round));
        else if (marker.equals("5")) holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_bio_round));
        else if (marker.equals("6")) holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_paper_round));
        else if (marker.equals("7")) holder.getLocationMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_plastic_round));
    }

    @Override
    public int getItemCount() {
        return userVisits.size();
    }

    public void setOnLocationClickListener(OnLocationClickListener onLocationClickListener) {
        this.onLocationClickListener = onLocationClickListener;
    }
}