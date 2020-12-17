package fcul.cm.g20.ecopack.fragments.profile.recyclerview;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import fcul.cm.g20.ecopack.R;

public class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> {
    public interface OnLocationClickListener {
        void onLocationClick(int position);
    }

    private OnLocationClickListener onLocationClickListener;
    private LinkedList<Pair<String, String>> locationsList;

    public LocationAdapter(LinkedList<Pair<String, String>> locationsList) {
        this.locationsList = locationsList;
    }

    public void setOnLocationClickListener(OnLocationClickListener onLocationClickListener) {
        this.onLocationClickListener = onLocationClickListener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_location, parent, false);
        return new LocationViewHolder(view, onLocationClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Pair<String, String> currentItem = locationsList.get(position);
        holder.getLocationName().setText(currentItem.first);
        holder.getVisitDate().setText(currentItem.second);
    }

    @Override
    public int getItemCount() {
        return locationsList.size();
    }
}