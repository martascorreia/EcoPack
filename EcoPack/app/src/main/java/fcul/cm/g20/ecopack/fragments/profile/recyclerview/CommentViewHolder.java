package fcul.cm.g20.ecopack.fragments.profile.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fcul.cm.g20.ecopack.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    private ImageView commentMarker;
    private TextView commentDate;
    private TextView commentContent;

    public CommentViewHolder(@NonNull View itemView, final CommentAdapter.OnCommentClickListener onCommentClickListener) {
        super(itemView);

        commentMarker = itemView.findViewById(R.id.item_comment_marker);
        commentDate = itemView.findViewById(R.id.item_comment_date);
        commentContent = itemView.findViewById(R.id.item_comment_content);

        setCommentClickListener(itemView, onCommentClickListener);
    }

    private void setCommentClickListener(@NonNull View itemView, final CommentAdapter.OnCommentClickListener onCommentClickListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommentClickListener != null)
                    if (getAdapterPosition() != RecyclerView.NO_POSITION)
                        onCommentClickListener.onCommentClick(getAdapterPosition());
            }
        });
    }

    public ImageView getCommentMarker() {
        return commentMarker;
    }

    public TextView getCommentDate() {
        return commentDate;
    }

    public TextView getCommentContent() {
        return commentContent;
    }
}