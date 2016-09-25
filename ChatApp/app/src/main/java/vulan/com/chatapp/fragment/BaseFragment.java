package vulan.com.chatapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import vulan.com.chatapp.R;
import vulan.com.chatapp.activity.BaseActivity;

public abstract class BaseFragment extends Fragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getFragmentLayoutId(), container, false);
        onCreateContentView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getBaseActivity().getSupportActionBar() != null) {

        }
        getBaseActivity().getSupportActionBar().setTitle(getTitle());
        if (enableBackButton()) {
            getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getBaseActivity().getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        } else {
            getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        getBaseActivity().popFragment();
    }

    private String getTitle() {
        return getString(R.string.app_name);
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected boolean enableBackButton() {
        return true;
    }

    protected abstract int getFragmentLayoutId();

    protected abstract void onCreateContentView(View rootView);
}