package mindbit.de.rubbellosapp.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import mindbit.de.rubbellosapp.R;
import mindbit.de.rubbellosapp.util.RubbellosWebClient;
import mindbit.de.rubbellosapp.util.WebViewUtil;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {

    private static final String ARG_HELP_URL = "helpUrl";

    private String helpUrl;

    public WebViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param helpUrl URL to display in the webView.
     * @return A new instance of fragment WebViewFragment.
     */
    public static WebViewFragment newInstance(String helpUrl) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HELP_URL, helpUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            helpUrl = getArguments().getString(ARG_HELP_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProgressBar progressBar = getView().findViewById(R.id.progressbar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        WebView helpWebView = getView().findViewById(R.id.wv_help);
        WebViewUtil.setUpWebViewDefaults(helpWebView);
        helpWebView.setWebViewClient(new RubbellosWebClient(progressBar));
        helpWebView.loadUrl(helpUrl);
    }



}
