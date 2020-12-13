package fcul.cm.g20.ecopack.ui.fragments.points.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fcul.cm.g20.ecopack.R;

public class PrizeViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private ImageView image;
    private TextView cost;

    public PrizeViewHolder(@NonNull View itemView, final PrizesAdapter.OnPrizeClickListener onLocationClickListener) {
        super(itemView);

        title = itemView.findViewById(R.id.item_prize_title);
        image = itemView.findViewById(R.id.item_prize_image);
        cost = itemView.findViewById(R.id.item_prize_cost);

        setItemClickListener(itemView, onLocationClickListener);
    }

    private void setItemClickListener(@NonNull View itemView, final PrizesAdapter.OnPrizeClickListener onLocationClickListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLocationClickListener != null)
                    if (getAdapterPosition() != RecyclerView.NO_POSITION)
                        onLocationClickListener.onPrizeClickListener(getAdapterPosition());
            }
        });
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