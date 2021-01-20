package fcul.cm.g20.ecopack.fragments.points.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fcul.cm.g20.ecopack.models.Prize;
import fcul.cm.g20.ecopack.R;

public class PrizesAdapter extends RecyclerView.Adapter<PrizeViewHolder> {
    public interface OnPrizeClickListener {
        void onPrizeClickListener(int position);
    }

    private OnPrizeClickListener onPrizeClickListener;
    private List<Prize> prizeModels;

    public PrizesAdapter(List<Prize> prizeModels) {
        this.prizeModels = prizeModels;
    }

    public void setOnPrizeClickListener(OnPrizeClickListener onPrizeClickListener) {
        this.onPrizeClickListener = onPrizeClickListener;
    }

    public Prize getPrize(int position) {
        return this.prizeModels.get(position);
    }

    @NonNull
    @Override
    public PrizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_points_prizes_cardviews, parent, false);
        return new PrizeViewHolder(view, onPrizeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PrizeViewHolder holder, int position) {
        Prize currentItem = prizeModels.get(position);
        holder.getTitle().setText(currentItem.getTitle());
        holder.getImage().setImageBitmap(currentItem.getImage());
        holder.getCost().setText(currentItem.getCost()+" Pontos");
        if(currentItem.isDisabled()){
            holder.disable();
        }
    }

    @Override
    public int getItemCount() {
        return prizeModels.size();
    }
}