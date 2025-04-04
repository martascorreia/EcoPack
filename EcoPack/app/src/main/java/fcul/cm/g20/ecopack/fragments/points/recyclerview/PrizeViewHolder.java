package fcul.cm.g20.ecopack.fragments.points.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import fcul.cm.g20.ecopack.R;

public class PrizeViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private ImageView image;
    private TextView cost;
    private CardView itemCardView;
    private LinearLayout itemLayout;

    public PrizeViewHolder(@NonNull View itemView, final PrizesAdapter.OnPrizeClickListener onPrizeClickListener) {
        super(itemView);

        title = itemView.findViewById(R.id.item_prize_title);
        image = itemView.findViewById(R.id.item_prize_image);
        cost = itemView.findViewById(R.id.item_prize_cost);
        itemCardView = (CardView) itemView;
        itemLayout = itemView.findViewById(R.id.item_prize_item);
        setItemClickListener(itemView, onPrizeClickListener);
    }

    private void setItemClickListener(@NonNull View itemView, final PrizesAdapter.OnPrizeClickListener onPrizeClickListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPrizeClickListener != null)
                    if (getAdapterPosition() != RecyclerView.NO_POSITION)
                        onPrizeClickListener.onPrizeClickListener(getAdapterPosition());
            }
        });
    }

    public void disable(){
        itemCardView.setOnClickListener(null);
        image.setColorFilter(0xCCFFFFFF);
    }

    public TextView getTitle() {
        return title;
    }

    public ImageView getImage() {
        return image;
    }

    public TextView getCost() {
        return cost;
    }
}