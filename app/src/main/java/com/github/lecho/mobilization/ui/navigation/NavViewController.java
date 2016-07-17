package com.github.lecho.mobilization.ui.navigation;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.fragment.AboutFragment;
import com.github.lecho.mobilization.ui.fragment.MyAgendaFragment;
import com.github.lecho.mobilization.ui.fragment.SpeakersFragment;
import com.github.lecho.mobilization.ui.fragment.SponsorsFragment;
import com.github.lecho.mobilization.ui.fragment.VenuesFragment;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.EventViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Leszek on 2015-10-10.
 */
public class NavViewController {

    /**
     * Int def with different types of navigation items, alternative to enum.
     * Note. IntDef causes problems when storing int the bundle.
     */
    @Retention(RetentionPolicy.CLASS)
    @IntDef({NAVIGATION_MY_AGENDA, NAVIGATION_VENUE, NAVIGATION_SPEAKERS, NAVIGATION_SPONSORS, NAVIGATION_ABOUT})
    public @interface NavItemType {
    }

    private static final int NAVIGATION_MY_AGENDA = 0;
    private static final int NAVIGATION_VENUE = 1;
    private static final int NAVIGATION_SPEAKERS = 2;
    private static final int NAVIGATION_SPONSORS = 3;
    private static final int NAVIGATION_ABOUT = 4;

    private final NavHeaderController navHeaderController;
    private final NavMenuController navMenuController;

    public NavViewController(@NonNull NavigationView navigationView, @NonNull MenuItemListener listener) {
        this.navHeaderController = new NavHeaderController(navigationView);
        this.navMenuController = new NavMenuController(navigationView, listener);
    }

    public void bindHeader(@NonNull Context context, @NonNull EventViewModel eventViewModel) {
        navHeaderController.bind(context, eventViewModel);
    }

    /**
     * Call this in onCreate to avoid waiting for loader just to load header image.
     *
     * @param context
     */
    public void bindHeaderImage(@NonNull Context context) {
        navHeaderController.bindHeaderImage(context);
    }

    public void bindMenu() {
        navMenuController.bind();
    }

    static class NavHeaderController {

        private static final String NAVIGATION_HEADER_IMAGE = "navigation_header.jpg";

        @BindView(R.id.navigation_header_image)
        ImageView headerView;

        @BindView(R.id.text_event_title)
        TextView eventTitleView;

        @BindView(R.id.text_event_date)
        TextView eventDateView;

        @BindView(R.id.text_event_place)
        TextView eventPlaceView;

        @BindView(R.id.map_button)
        ImageButton mapButton;

        public NavHeaderController(@NonNull NavigationView navigationView) {
            ButterKnife.bind(this, navigationView.getHeaderView(0));
        }

        public void bind(@NonNull Context context, @NonNull EventViewModel eventViewModel) {
            eventTitleView.setText(eventViewModel.title);
            String eventDateText = new StringBuilder()
                    .append(eventViewModel.date)
                    .append(", ")
                    .append(eventViewModel.time)
                    .toString();
            eventDateView.setText(eventDateText);
            String eventPlaceText = new StringBuilder()
                    .append(eventViewModel.place)
                    .append("\n")
                    .append(eventViewModel.street)
                    .append(" ")
                    .append(eventViewModel.city)
                    .toString();
            eventPlaceView.setText(eventPlaceText);
            mapButton.setOnClickListener(new MapButtonClickListener(context, eventViewModel));
        }

        public void bindHeaderImage(@NonNull Context context) {
            Utils.loadHeaderImage(context.getApplicationContext(), NAVIGATION_HEADER_IMAGE, headerView);
        }
    }

    private static class NavMenuController {

        private NavigationView navigationView;
        private MenuItemListener listener;

        public NavMenuController(@NonNull NavigationView navigationView, @NonNull MenuItemListener listener) {
            this.navigationView = navigationView;
            this.listener = listener;
        }

        public void bind() {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {

                    Fragment fragment;
                    switch (item.getItemId()) {
                        case R.id.nav_my_agenda:
                            fragment = MyAgendaFragment.newInstance();
                            break;
                        case R.id.nav_venues:
                            fragment = VenuesFragment.newInstance();
                            break;
                        case R.id.nav_speakers:
                            fragment = SpeakersFragment.newInstance();
                            break;
                        case R.id.nav_sponsors:
                            fragment = SponsorsFragment.newInstance();
                            break;
                        case R.id.nav_about:
                            fragment = AboutFragment.newInstance();
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid navigation item: " + item);
                    }
                    listener.onItemClick(item, fragment);
                    return true;
                }
            });
        }
    }

    private static class MapButtonClickListener implements View.OnClickListener {

        private final Context context;
        private final EventViewModel eventViewModel;

        public MapButtonClickListener(Context context, EventViewModel eventViewModel) {
            this.context = context;
            this.eventViewModel = eventViewModel;
        }

        @Override
        public void onClick(View v) {
            String address = eventViewModel.street + ", " + eventViewModel.city;
            Utils.launchGMaps(context, eventViewModel.latitude, eventViewModel.longitude, address);
        }
    }

    public interface MenuItemListener {
        void onItemClick(@NonNull MenuItem item, @NonNull Fragment fragment);
    }
}
