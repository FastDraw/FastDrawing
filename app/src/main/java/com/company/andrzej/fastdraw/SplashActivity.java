package com.company.andrzej.fastdraw;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    @BindView(R.id.vp_splash)
    ViewPager mViewPager;
    @BindView(R.id.tab_splash)
    TabLayout tabLayout;
    @BindView(R.id.main_content)
    View mainContent;
    @BindView(R.id.cl_splash_content_container)
    View contentContainer;
    @BindView(R.id.pb_splash)
    ProgressBar pbInitialSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("me.fastdraw.app", MODE_PRIVATE);
        if (!Settings.isFirstSplashLunch()) {
            MainActivity.startMainActivity(this);
        } else {
            mainContent.setBackgroundColor(Color.WHITE);
            contentContainer.setVisibility(View.VISIBLE);
            pbInitialSplash.setVisibility(View.GONE);
        }
        String[] quotes = getResources().getStringArray(R.array.splash_quotes);
        String[] authors = getResources().getStringArray(R.array.splash_authors);
        int[] images = new int[]{
                R.drawable.splash_emoticon_1,
                R.drawable.splash_emoticon_2,
                R.drawable.splash_emoticon_3,
                R.drawable.splash_emoticon_4};
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), quotes, authors, images);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager, true);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class QuoteFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_QUOTE = "quote";
        private static final String ARG_SECTION_AUTHOR = "author";
        private static final String ARG_SECTION_IMAGE_RES_ID = "image";

        @BindView(R.id.iv_section_logo)
        ImageView imageView;
        @BindView(R.id.tv_section_quote)
        TextView tvQuote;
        @BindView(R.id.tv_section_author)
        TextView tvAuthor;

        public QuoteFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static QuoteFragment newInstance(String quote, String author, int imageResId) {
            QuoteFragment fragment = new QuoteFragment();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_QUOTE, quote);
            args.putString(ARG_SECTION_AUTHOR, author);
            args.putInt(ARG_SECTION_IMAGE_RES_ID, imageResId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_splash_screen, container, false);
            ButterKnife.bind(this, rootView);

            Bundle args = getArguments();

            tvQuote.setText(args.getString(ARG_SECTION_QUOTE));
            tvAuthor.setText(args.getString(ARG_SECTION_AUTHOR));
            imageView.setImageResource(args.getInt(ARG_SECTION_IMAGE_RES_ID));
            return rootView;
        }
    }

    public static class LastFragment extends Fragment {

        @BindView(R.id.btn_done)
        Button btnDone;

        public LastFragment() {
        }

        public static LastFragment newInstance() {
            return new LastFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_splash_last_fragment, container, false);
            ButterKnife.bind(this, rootView);
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.setFirstSplashLaunch(false);
                    MainActivity.startMainActivity(getActivity());
                }
            });
            return rootView;
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        final String[] quotes;
        final String[] authors;
        final int[] images;

        SectionsPagerAdapter(FragmentManager fm, String[] quotes, String[] authors, int[] images) {
            super(fm);
            this.quotes = quotes;
            this.authors = authors;
            this.images = images;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == getCount() - 1)
                return LastFragment.newInstance();
            else
                return QuoteFragment.newInstance(quotes[position], authors[position], images[position]);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
    }
}


