package com.github.lecho.mobilization.ui.navigation;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.fragment.AboutFragment;
import com.github.lecho.mobilization.ui.fragment.MyAgendaFragment;
import com.github.lecho.mobilization.ui.fragment.SpeakersFragment;
import com.github.lecho.mobilization.ui.fragment.SponsorsFragment;
import com.github.lecho.mobilization.ui.fragment.VenueAgendaFragment;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.EventViewModel;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

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

    public void bindMenu(@NonNull List<VenueViewModel> venueViewModels) {
        navMenuController.bind(venueViewModels);
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

        private Menu navigationMenu;
        private SubMenu venuesSubMenu;
        private SubMenu moreSubMenu;
        private MenuItemListener listener;
        private final int groupId = 0;
        private int itemId;
        private int order;

        public NavMenuController(@NonNull NavigationView navigationView, @NonNull MenuItemListener listener) {
            navigationMenu = navigationView.getMenu();
            this.listener = listener;
        }

        public void bind(@NonNull List<VenueViewModel> venueViewModels) {
            itemId = 0;
            order = 0;
            navigationMenu.clear();
            //MyAgenda
            navigationMenu.setGroupCheckable(groupId, true, true);
            navigationMenu.add(groupId, itemId++, order++, R.string.navigation_my_agenda)
                    .setIcon(R.drawable.ic_nav_my_agenda)
                    .setCheckable(true)
                    .setOnMenuItemClickListener(new NavItemClickListener(NAVIGATION_MY_AGENDA, listener));
            //Tracks
            venuesSubMenu = navigationMenu.addSubMenu(groupId, itemId++, order++, R.string.navigation_venues);
            bindVenuesSubmenu(venueViewModels);
            //More
            moreSubMenu = navigationMenu.addSubMenu(groupId, itemId++, order++, R.string.navigation_more);
            bindMoreSubmenu();
        }

        private void bindVenuesSubmenu(@NonNull List<VenueViewModel> venueViewModels) {
            for (VenueViewModel venueViewModel : venueViewModels) {
                venuesSubMenu.add(groupId, itemId++, order++, venueViewModel.title)
                        .setIcon(R.drawable.ic_nav_agenda)
                        .setCheckable(true)
                        .setOnMenuItemClickListener(new NavItemClickListener(NAVIGATION_VENUE, venueViewModel,
                                listener));
            }
        }

        private void bindMoreSubmenu() {
            moreSubMenu.add(groupId, itemId++, order++, R.string.navigation_speakers)
                    .setIcon(R.drawable.ic_nav_speakers)
                    .setCheckable(true)
                    .setOnMenuItemClickListener(new NavItemClickListener(NAVIGATION_SPEAKERS, listener));
            moreSubMenu.add(groupId, itemId++, order++, R.string.navigation_sponsors)
                    .setIcon(R.drawable.ic_nav_sponsors)
                    .setCheckable(true)
                    .setOnMenuItemClickListener(new NavItemClickListener(NAVIGATION_SPONSORS, listener));
            moreSubMenu.add(groupId, itemId, order, R.string.navigation_about)
                    .setIcon(R.drawable.ic_nav_about)
                    .setCheckable(true)
                    .setOnMenuItemClickListener(new NavItemClickListener(NAVIGATION_ABOUT, listener));
        }

        /**
         * Returns fragment for given navigation item type
         *
         * @param navItemType
         * @param venueKey    have to be not null if navItemType==NAVIGATION_VENUE
         * @param venueTitle  have to be not null if navItemType==NAVIGATION_VENUE
         * @return
         */
        private static Fragment getFragmentForItemType(@NavItemType int navItemType, String venueKey,
                                                       String venueTitle) {
            Fragment fragment = null;
            switch (navItemType) {
                case NAVIGATION_MY_AGENDA:
                    fragment = MyAgendaFragment.newInstance();
                    break;
                case NAVIGATION_VENUE:
                    fragment = VenueAgendaFragment.newInstance(venueKey, venueTitle);
                    break;
                case NAVIGATION_SPEAKERS:
                    fragment = SpeakersFragment.newInstance();
                    break;
                case NAVIGATION_SPONSORS:
                    fragment = SponsorsFragment.newInstance();
                    break;
                case NAVIGATION_ABOUT:
                    fragment = AboutFragment.newInstance();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid navigation item type: " + navItemType);
            }
            return fragment;
        }
    }

    /**
     * As and alternative NavigationView.onNavigationItemSelected()
     */
    private static class NavItemClickListener implements MenuItem.OnMenuItemClickListener {

        @NavItemType
        private final int navItemType;
        private Optional<VenueViewModel> venueViewDto;
        private final MenuItemListener listener;

        public NavItemClickListener(@NavItemType int navItemType,
                                    @NonNull MenuItemListener listener) {
            if (navItemType == NAVIGATION_VENUE) {
                throw new UnsupportedOperationException("For venue items use constructor with 3 parameters!");
            }
            this.navItemType = navItemType;
            this.venueViewDto = Optional.empty();
            this.listener = listener;
        }

        public NavItemClickListener(@NavItemType int navItemType, @NonNull VenueViewModel venueViewModel,
                                    @NonNull MenuItemListener listener) {
            this.navItemType = navItemType;
            this.venueViewDto = Optional.of(venueViewModel);
            this.listener = listener;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String venueKey = null;
            String venueTitle = null;
            if (venueViewDto.isPresent()) {
                venueKey = venueViewDto.get().key;
                venueTitle = venueViewDto.get().title;
            }
            Fragment fragment = NavMenuController.getFragmentForItemType(navItemType, venueKey, venueTitle);
            listener.onItemClick(item, fragment);
            return true;
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
