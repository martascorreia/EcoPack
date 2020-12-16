package fcul.cm.g20.ecopack.ui.fragments.points.recyclerview;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.ui.fragments.points.model.Prize;

public class PrizesAdapter extends RecyclerView.Adapter<PrizeViewHolder> {
    public interface OnPrizeClickListener {
        void onPrizeClickListener(int position);
    }

    private OnPrizeClickListener onPrizeClickListener;
    private LinkedList<Prize> prizes;

    public PrizesAdapter(LinkedList<Prize> Prizes) {
        this.prizes = Prizes;
    }

    public void setOnPrizeClickListener(OnPrizeClickListener onPrizeClickListener) {
        this.onPrizeClickListener = onPrizeClickListener;
    }

    public Prize getPrize(int position) {
        return this.prizes.get(position);
    }

    @NonNull
    @Override
    public PrizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_points_prizes_cardviews, parent, false);
        return new PrizeViewHolder(view, onPrizeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PrizeViewHolder holder, int position) {
        Prize currentItem = prizes.get(position);
        holder.getTitle().setText(currentItem.getTitle());
        holder.getImage().setImageBitmap(currentItem.getImage());
        holder.getCost().setText(currentItem.getCost()+" Pontos");
    }

    @Override
    public int getItemCount() {
        return prizes.size();
    }
}