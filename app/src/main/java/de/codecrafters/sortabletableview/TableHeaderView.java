package de.codecrafters.sortabletableview;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This view represents the header of a table. The given {@link TableHeaderAdapter} is used to fill
 * this view with data.
 *
 * @author ISchwarz
 */
class TableHeaderView extends LinearLayout {

    protected TableHeaderAdapter adapter;
    protected List<View> headerViews = new ArrayList<>();

    public TableHeaderView(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    protected void renderHeaderViews() {
        removeAllViews();
        headerViews.clear();

        for(int columnIndex=0; columnIndex<adapter.getColumnCount(); columnIndex++) {
            View headerView = adapter.getHeaderView(columnIndex, this);
            headerViews.add(headerView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        removeAllViews();

        int widthUnit = (getWidth() / adapter.getColumnWeightSum());

        for(int columnIndex=0; columnIndex<headerViews.size(); columnIndex++) {
            View headerView = headerViews.get(columnIndex);
            if(headerView == null) {
                headerView = new TextView(getContext());
            }

            int width = widthUnit * adapter.getColumnWeight(columnIndex);
            LayoutParams headerLayoutParams = new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);

            headerView.setLayoutParams(headerLayoutParams);
            addView(headerView, columnIndex);
        }
    }

    public void setAdapter(TableHeaderAdapter adapter) {
        this.adapter = adapter;
        renderHeaderViews();
        invalidate();
    }

}
