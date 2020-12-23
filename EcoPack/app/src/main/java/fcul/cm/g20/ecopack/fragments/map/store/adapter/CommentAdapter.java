package fcul.cm.g20.ecopack.fragments.map.store.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;
import fcul.cm.g20.ecopack.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private LinkedList<Comment> comments;

    public CommentAdapter(Context context, LinkedList<Comment> rv_comments) {
        this.comments = rv_comments;
        this.context = context;
    }


    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.item_row_comment, parent, false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment currentItem = comments.get(position);
        holder.getComment().setText(currentItem.getComment());
        holder.getUsername().setText(currentItem.getName());
        holder.getMarker().setImageDrawable(currentItem.getMarker());

        //PICTURE
        String picture = currentItem.getAvatar();
        if (picture.equals("N/A")) holder.getAvatar().setImageResource(R.drawable.ic_user_empty);
        else {
            byte[] pictureArray = android.util.Base64.decode(picture, android.util.Base64.DEFAULT);
            Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureArray, 0, pictureArray.length);
            holder.getAvatar().setImageBitmap(pictureBitmap);
        }

        // DATE
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(currentItem.getDate()));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        holder.getDate().setText(day + "-" + month + "-" + year);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends  RecyclerView.ViewHolder{

        CircleImageView avatar;
        TextView username, comment;
        ImageView marker;
        TextView date;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            marker = itemView.findViewById(R.id.marker);
            date = itemView.findViewById(R.id.date);
        }

        public CircleImageView getAvatar(){
            return avatar;
        }

        public TextView getUsername(){
            return username;
        }

        public TextView getComment(){
            return comment;
        }

        public TextView getDate(){
            return date;
        }

        public ImageView getMarker(){
            return marker;
        }


    }
}
