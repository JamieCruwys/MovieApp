package com.fitc.movieapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.fitc.movieapp.api.Client;
import com.fitc.movieapp.api.Movie;
import com.fitc.movieapp.api.MovieApi;
import com.fitc.movieapp.api.MovieResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private final static String API_KEY_V3 = "e298be227d1a1adf67a07ac343ef8fa9";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
	            Intent emailIntent = new Intent(Intent.ACTION_SEND);
	            emailIntent.setType("text/html");
	            emailIntent.putExtra(Intent.EXTRA_EMAIL, "a@a.com");
	            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this awesome movie");
	            emailIntent.putExtra(Intent.EXTRA_TEXT, "The Shawshank Redemption");

				startActivity(Intent.createChooser(emailIntent, "Send email"));
            }
        });

        if (API_KEY_V3.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain an API KEY", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class Page1 extends Fragment {

        Adapter adapter ;

        public Page1() {
        }

        public static Page1 newInstance() {
            Page1 fragment = new Page1();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            RecyclerView recyclerView = rootView.findViewById(R.id.my_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

            adapter = new Adapter(R.layout.movie_item, this.getActivity().getApplicationContext());
            recyclerView.setAdapter(adapter);

            MovieApi movieApi =
                    Client.getClient().create(MovieApi.class);

            Observable<Response<MovieResponse>> movieRequest = movieApi.getTopRated(API_KEY_V3);
            movieRequest.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(httpResponse -> {
                        if (httpResponse.isSuccessful()) {
                            List<Movie> movies = httpResponse.body().results;
                            adapter.setData(movies);
                        }
                    }, error -> {
                        Toast.makeText(this.getActivity(), "Failed to load data", Toast.LENGTH_LONG);
                    });

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return Page1.newInstance();
                default:
                    return Page1.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
