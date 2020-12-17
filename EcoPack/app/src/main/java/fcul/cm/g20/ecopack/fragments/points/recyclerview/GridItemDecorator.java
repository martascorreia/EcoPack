package fcul.cm.g20.ecopack.fragments.points.recyclerview;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridItemDecorator extends RecyclerView.ItemDecoration {

    private int mSizeGridSpacingPx;
    private int mGridSize;
    private boolean mNeedLeftSpacing;

    public GridItemDecorator(int gridSpacingPx, int gridSize) {
        this.mSizeGridSpacingPx = gridSpacingPx;
        this.mGridSize = gridSize;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        int frameWidth = (int)(((float)parent.getWidth() - (float)this.mSizeGridSpacingPx * (float)(this.mGridSize - 1)) / (float)this.mGridSize);
        int padding = parent.getWidth() / this.mGridSize - frameWidth;

        ViewGroup.LayoutParams var10000 = view.getLayoutParams();

        if (var10000 == null) {
            throw new NullPointerException("null cannot be cast to non-null type androidx.recyclerview.widget.RecyclerView.LayoutParams");
        } else {
            int itemPosition = ((androidx.recyclerview.widget.RecyclerView.LayoutParams)var10000).getViewAdapterPosition();
            if (itemPosition < this.mGridSize) {
                outRect.top = 0;
            } else {
                outRect.top = this.mSizeGridSpacingPx;
            }
            if (itemPosition % this.mGridSize == 0) {
                outRect.left = 0;
                outRect.right = padding;
                this.mNeedLeftSpacing = true;
            } else if ((itemPosition + 1) % this.mGridSize == 0) {
                this.mNeedLeftSpacing = false;
                outRect.right = 0;
                outRect.left = padding;
            } else if (this.mNeedLeftSpacing) {
                this.mNeedLeftSpacing = false;
                outRect.left = this.mSizeGridSpacingPx - padding;
                if ((itemPosition + 2) % this.mGridSize == 0) {
                    outRect.right = this.mSizeGridSpacingPx - padding;
                } else {
                    outRect.right = this.mSizeGridSpacingPx / 2;
                }
            } else if ((itemPosition + 2) % this.mGridSize == 0) {
                this.mNeedLeftSpacing = false;
                outRect.left = this.mSizeGridSpacingPx / 2;
                outRect.right = this.mSizeGridSpacingPx - padding;
            } else {
                this.mNeedLeftSpacing = false;
                outRect.left = this.mSizeGridSpacingPx / 2;
                outRect.right = this.mSizeGridSpacingPx / 2;
            }
            outRect.bottom = 0;
        }
    }
}
