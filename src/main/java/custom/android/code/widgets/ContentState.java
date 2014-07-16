package custom.android.code.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * ContentState:
 */
public class ContentState extends LinearLayout implements
        View.OnClickListener {

    public static enum VIEW {
        EMPTY,
        RELATED, // ExpandableListView
        SIMPLE, // Listview
        LOADING,
        RETRY
    }

    public interface OnClickRetryListener {
        public void onRetry();
    }

    OnClickRetryListener mOnClickRetry;

    public ContentState(Context context) {
        super(context);
        initialize(context);
    }

    public ContentState(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ContentState(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    protected void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.contentstate, this, true);
    }

    public void setOnClickRetryListener(OnClickRetryListener listener) {
        mOnClickRetry = listener;
    }

    public ExpandableListView getRelatedView() {
        return (ExpandableListView) findViewById(R.id.expandable_view);
    }

    public ListView getSimpleView() {
        return (ListView) findViewById(R.id.simple_view);
    }

    public void showLoading() {
        show(VIEW.LOADING);
    }

    public void showEmpty() {
        show(VIEW.EMPTY);
    }

    public void showRelatedView() {
        show(VIEW.RELATED);
    }

    public void showSimpleView() {
        show(VIEW.SIMPLE);
    }

    public void showRetry() {
        show(VIEW.RETRY);
    }

    public void show(VIEW view) {
        findViewById(R.id.layout_empty).setVisibility(view == VIEW.EMPTY ? View.VISIBLE : View.GONE);

        View contentLayout = findViewById(R.id.layout_content);
        contentLayout.setVisibility(view == VIEW.RELATED || view == VIEW.SIMPLE ?
                                    View.VISIBLE : View.GONE);
        switch (contentLayout.getVisibility()) {
            case View.VISIBLE:
                contentLayout.findViewById(R.id.expandable_view)
                        .setVisibility(view == VIEW.RELATED ? View.VISIBLE : View.GONE);
                contentLayout.findViewById(R.id.simple_view)
                        .setVisibility(view == VIEW.SIMPLE ? View.VISIBLE : View.GONE);
                break;
            default:
                contentLayout.findViewById(R.id.expandable_view)
                        .setVisibility(View.GONE);
                contentLayout.findViewById(R.id.simple_view)
                        .setVisibility(View.GONE);
        }

        findViewById(R.id.layout_loading).setVisibility(view == VIEW.LOADING ? View.VISIBLE : View.GONE);

        View retryLayout = findViewById(R.id.layout_retry);
        retryLayout.setVisibility(view == VIEW.RETRY ? View.VISIBLE : View.GONE);

        switch (retryLayout.getVisibility()) {
            case View.VISIBLE:
                Button button = (Button)
                        retryLayout.findViewById(R.id.retry_button);
                button.setOnClickListener(this);
                break;
        }
        requestLayout();
        invalidate();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.retry_button) {
            mOnClickRetry.onRetry();
        }
    }
}
