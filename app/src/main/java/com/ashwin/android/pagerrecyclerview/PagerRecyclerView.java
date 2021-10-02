package com.ashwin.android.pagerrecyclerview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple wrapped {@link RecyclerView}: integrated page browsing actions and item click actions
 * <p>
 * Please refactor it to decoupling the recyclerView and LayoutManager
 */
public class PagerRecyclerView extends RecyclerView {
    private final static String TAG = PagerRecyclerView.class.getSimpleName();

    private int mPageIndex = 1;       // Current page index
    private int mPageCount = 1;       // Total pages

    private int spanRow = 3;          // Rows in one page
    private int spanColumn = 2;       // Columns in one page
    private int onePageSize = 6;      // Max item number in one page
    private int itemCount = 0;        // Size of data in Adapter

    private int itemWidth = -1;       // the item width in Adapter
    private int itemHeight = -1;      // the item hight in Adapter
    private int scrollDistance = 0;   // Available scroll distance for each page switching

    private int mOrientation = 0;     // Layout orientation

    private Adapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    private onPageChangeListener pageChangeListener;

    public void setOnePageSize(int onePageSize) {
        this.onePageSize = onePageSize;
    }

    public int getOnePageSize() {
        return onePageSize;
    }

    public void setPageSize(int row, int column) {
        if (row > 0 && column > 0) {
            setPageRowAndColumn(row, column);
            this.onePageSize = row * column;
        } else {
            Log.w(TAG, "Invalidate value for row and column!");
        }
    }

    private void setPageRowAndColumn(int row, int column) {
        this.spanRow = row;
        this.spanColumn = column;
    }

    public PagerRecyclerView(@NonNull Context context) {
        super(context);
    }

    public PagerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (gridLayoutManager != null) {
            if (canScrollHorizontally()) {
                int availableWidth = getAvailableWidth();
                itemWidth = availableWidth / spanColumn;
                scrollDistance = availableWidth - availableWidth % spanColumn;
            } else {
                int availableHeight = getAvailableHeight();
                itemHeight = availableHeight / spanRow;
                scrollDistance = availableHeight - availableHeight % spanRow;
            }
            Log.d(TAG, "Available scroll distance: " + scrollDistance + " item width: " + itemWidth + " height: " + itemHeight);
        }
    }

    @Override
    public void setLayoutManager(@Nullable LayoutManager layout) {
        if (layout instanceof GridLayoutManager) {
            super.setLayoutManager(layout);
            this.gridLayoutManager = (GridLayoutManager) getLayoutManager();

            if (this.gridLayoutManager != null) {
                this.mOrientation = this.gridLayoutManager.getOrientation();
                int spanCount = this.gridLayoutManager.getSpanCount();
                if (canScrollHorizontally()) {
                    setPageRowAndColumn(spanCount, onePageSize / spanCount);
                } else {
                    setPageRowAndColumn(onePageSize / spanCount, spanCount);
                }
            }
        } else {
            throw new IllegalArgumentException("LayoutManager invalidate! Please set a GridlayoutManager!");
        }
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        mAdapter = getAdapter();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_IDLE) {
            pageUpdate();
        }
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        pageUpdate();
    }


    private static int calculatePageCount(int count, int onePageSize) {
        if (count <= 0) return 1;
        int pageCount = count / onePageSize;
        if (count % onePageSize > 0) {
            ++pageCount;
        }
        return pageCount;
    }

    private void pageUpdate() {
        // Update page count
        if (mAdapter != null) {
            if (itemCount != mAdapter.getItemCount()) {
                itemCount = mAdapter.getItemCount();
                mPageCount = calculatePageCount(mAdapter.getItemCount(), onePageSize);
            }
        }
        // Update page Index
        if (gridLayoutManager != null) {
            int lastCompletelyVisibleItemPosition = this.gridLayoutManager.findLastCompletelyVisibleItemPosition() + 1;
            // Log.d(TAG, "lastCompletelyVisibleItemPosition: " + lastCompletelyVisibleItemPosition);
            mPageIndex = calculatePageCount(lastCompletelyVisibleItemPosition, onePageSize);
        }
        // Expose to user
        if (pageChangeListener != null) {
            this.pageChangeListener.onPageChange(mPageIndex, mPageCount);
        }
    }

    public int getVisibleItemsCount() {
        if (mAdapter != null && mAdapter instanceof PagerAdapter) {
            if (((PagerAdapter) mAdapter).getTrueItemCount() > 0) {
                int remains = (((PagerAdapter) mAdapter).getTrueItemCount() - 1) % onePageSize + 1;
                return isLastPage() ? remains : onePageSize;
            }
        }
        return 0;
    }

    public boolean canScrollHorizontally() {
        return mOrientation == HORIZONTAL;
    }

    public boolean canScrollVertically() {
        return mOrientation == VERTICAL;
    }

    public int getAvailableWidth() {
        return getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
    }

    public int getAvailableHeight() {
        return getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    }

    public void nextPage() {
        smoothScrollBy(scrollDistance, scrollDistance);
    }

    public void previousPage() {
        smoothScrollBy(-scrollDistance, -scrollDistance);
    }

    public boolean isLastPage() {
        return mPageIndex == mPageCount;
    }

    public boolean isFirstPage() {
        return mPageIndex == 1;
    }

    public void setOnPageChangeListener(onPageChangeListener listener) {
        this.pageChangeListener = listener;
    }

    public interface onPageChangeListener {
        void onPageChange(int pageIndex, int pageCount);
    }

    @Override
    public void onChildAttachedToWindow(@NonNull View child) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child.getLayoutParams();
        if (gridLayoutManager.canScrollHorizontally()) {
            child.getLayoutParams().width = itemWidth - marginLayoutParams.getMarginStart() - marginLayoutParams.getMarginEnd();
        } else {
            child.getLayoutParams().height = itemHeight - marginLayoutParams.topMargin - marginLayoutParams.bottomMargin;
        }
    }

    public abstract static class PagerAdapter<VH extends RecyclerView.ViewHolder, DataType> extends RecyclerView.Adapter<VH> {
        protected int onePageSize = 1;
        protected List<DataType> mDataSet = new ArrayList<>();
        protected onItemClickListener<DataType> itemClickListener;

        public abstract void updateData(List<DataType> data);

        @Override
        public void onViewAttachedToWindow(@NonNull VH holder) {
            if (itemClickListener != null) {
                holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(v, mDataSet.get(holder.getAdapterPosition())));
            }
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull VH holder) {
            super.onViewDetachedFromWindow(holder);
            if (holder.itemView.hasOnClickListeners()) {
                holder.itemView.setOnClickListener(null);
            }
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            if (recyclerView instanceof PagerRecyclerView) {
                onePageSize = ((PagerRecyclerView) recyclerView).getOnePageSize();
            }
        }

        protected int getVirtualItemCount(int dataSize) {
            int remains = dataSize % onePageSize;
            return remains > 0 ? dataSize + (onePageSize - remains) : dataSize;
        }

        public abstract int getTrueItemCount();

        public void setOnItemClickListener(onItemClickListener<DataType> listener) {
            this.itemClickListener = listener;
        }

        public interface onItemClickListener<DataType> {
            void onItemClick(View view, DataType data);
        }
    }
}
