package ca.cmput301t05.placeholder.utils;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CenterItemDecoration extends RecyclerView.ItemDecoration {
    private int itemWidth = -1;

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (itemWidth == -1) {
            view.measure(0, 0);
            itemWidth = view.getMeasuredWidth();
        }

        int totalWidth = parent.getWidth();
        int padding = (totalWidth - itemWidth) / 3;

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = padding;
        } else if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            outRect.right = padding;
        }
    }
}
