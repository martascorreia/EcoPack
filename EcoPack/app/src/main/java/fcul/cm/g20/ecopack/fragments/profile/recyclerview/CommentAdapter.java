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
import fcul.cm.g20.ecopack.utils.Utils;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    public interface OnCommentClickListener {
        void onCommentClick(int position);
    }

    private Context context;
    private OnCommentClickListener onCommentClickListener;
    private ArrayList<HashMap<String, Object>> userComments;

    public CommentAdapter(Context context, ArrayList<HashMap<String, Object>> userComments) {
        this.context = context;
        this.userComments = userComments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_comment, parent, false);
        return new CommentViewHolder(view, onCommentClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        HashMap<String, Object> currentItem = userComments.get(position);

        holder.getCommentDate().setText(Utils.getDateFromMilliseconds(Long.parseLong(currentItem.get("date").toString())));
        holder.getCommentContent().setText((String) currentItem.get("comment"));

        String marker = (String) currentItem.get("marker");
        if (marker.equals("ic_marker_reusable_home_round")) holder.getCommentMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_reusable_home_round));
        else if (marker.equals("ic_marker_bio_home_round")) holder.getCommentMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_bio_home_round));
        else if (marker.equals("ic_marker_paper_home_round")) holder.getCommentMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_paper_home_round));
        else if (marker.equals("ic_marker_plastic_home_round")) holder.getCommentMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_plastic_home_round));
        else if (marker.equals("ic_marker_reusable_round")) holder.getCommentMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_reusable_round));
        else if (marker.equals("ic_marker_bio_round")) holder.getCommentMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_bio_round));
        else if (marker.equals("ic_marker_paper_round")) holder.getCommentMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_paper_round));
        else if (marker.equals("ic_marker_plastic_round")) holder.getCommentMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_plastic_round));
        else if (marker.equals("ic_marker_home_round")) holder.getCommentMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker_home_round));
        else holder.getCommentMarker().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_comment));
    }

    @Override
    public int getItemCount() {
        return userComments.size();
    }

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }
}