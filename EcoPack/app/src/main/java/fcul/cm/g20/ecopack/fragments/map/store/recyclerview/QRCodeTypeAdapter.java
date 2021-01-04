package fcul.cm.g20.ecopack.fragments.map.store.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.objects.QR_Code_Type;

public class QRCodeTypeAdapter extends RecyclerView.Adapter<QRCodeTypeAdapter.QRCodeTypeViewHolder> {
    public interface OnTypeClickListener {
        void onTypeClickListener(int position);
    }

    private QRCodeTypeAdapter.OnTypeClickListener onTypeClickListener;

    private Context context;
    private LinkedList<QR_Code_Type> qr_code_type;

    public QRCodeTypeAdapter(Context context, LinkedList<QR_Code_Type> counters ) {
        this.qr_code_type = counters;
        this.context = context;
    }

    public void setOnQRCodeTypeClickListener(QRCodeTypeAdapter.OnTypeClickListener onTypeClickListener) {
        this.onTypeClickListener = onTypeClickListener;
    }

    public QR_Code_Type getQR_Code_Type(int position) {
        return this.qr_code_type.get(position);
    }


    @NonNull
    @Override
    public QRCodeTypeAdapter.QRCodeTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.item_store_qrcode, parent, false);
        return new QRCodeTypeViewHolder(row, onTypeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QRCodeTypeAdapter.QRCodeTypeViewHolder holder, int position) {
        QR_Code_Type currentItem = qr_code_type.get(position);
        holder.getTitle().setText(currentItem.getTitle());
        holder.getMarker().setImageDrawable(currentItem.getMarker());

    }

    @Override
    public int getItemCount() {
        return qr_code_type.size();
    }

    public class QRCodeTypeViewHolder extends  RecyclerView.ViewHolder{

        CardView cardView;
        ImageView marker;
        TextView title;

        public QRCodeTypeViewHolder(@NonNull View itemView, final QRCodeTypeAdapter.OnTypeClickListener onTypeClickListener) {
            super(itemView);
            marker = itemView.findViewById(R.id.store_qrcode_marker);
            title = itemView.findViewById(R.id.store_qrcode_title);
            cardView = itemView.findViewById(R.id.cardView);

            setItemClickListener(itemView, onTypeClickListener);

        }

        private void setItemClickListener(@NonNull View itemView, final QRCodeTypeAdapter.OnTypeClickListener onTypeClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTypeClickListener != null)
                        if (getAdapterPosition() != RecyclerView.NO_POSITION)
                            onTypeClickListener.onTypeClickListener(getAdapterPosition());
                }
            });
        }

        public ImageView getMarker() {
            return marker;
        }

        public void setMarker(CircleImageView marker) {
            this.marker = marker;
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }
    }
}
