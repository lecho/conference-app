package com.github.lecho.mobilization.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Leszek on 2015-07-08.
 */
public class AboutFragment extends Fragment {

    public static final String TAG = AboutFragment.class.getSimpleName();
    public static final String TWITTER_USER = "leszekwach";
    public static final String GITHUB_URL = "https://github.com/lecho";
    private Unbinder unbinder;

    @BindView(R.id.text_version)
    TextView versionView;

    @BindView(R.id.button_twitter)
    Button twitterButton;

    @BindView(R.id.button_github)
    Button githubButton;

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_activity_about);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Pair<String, Integer> version = Utils.getAppVersionAndBuild(getContext());
        versionView.setText(version.first);
        setUpTwitterButton();
        setUpGithubButton();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setUpTwitterButton() {
        twitterButton.setOnClickListener(v -> Utils.openTwitter(getContext(), TWITTER_USER));
    }

    private void setUpGithubButton() {
        githubButton.setOnClickListener(v -> Utils.openWebBrowser(getContext(), GITHUB_URL));
    }
}
