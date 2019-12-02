package com.example.munch.ui.map;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.munch.HttpRequests;
import com.example.munch.LocationCalculator;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.foodTruck.CustomInfoWindowAdaptor;
import com.example.munch.ui.foodTruck.FoodTruckFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    //region global variables
    static GoogleMap munMap;
    private String searchFormatted = "";

    static ArrayList<FoodTruck> listing;
    static ArrayList<FoodTruck> forWindow;

    private Marker lastClicked;

    EditText searchText;
    EditText locText;
    ArrayList<String> tagInputArray;
    ListView resultsList;

    CheckBox mAmercianCheck;
    ArrayList<String> amerTags;
    CheckBox mAsianCheck;
    ArrayList<String> asianTags;
    CheckBox mBarbequeCheck;
    ArrayList<String> barbTags;
    CheckBox mSouthernCheck;
    ArrayList<String> southTags;
    CheckBox mBreakfastCheck;
    ArrayList<String> breakTags;
    CheckBox mMexicanCheck;
    ArrayList<String> mexiTags;
    CheckBox mSeafoodCheck;
    ArrayList<String> seaTags;
    CheckBox mDessertCheck;
    ArrayList<String> dessTags;
    ArrayList<FoodTruck> searchListings;
    String locInput;
    String responseTruck;

    HttpRequests foodTruckRequest;

    TextView sortByText;
    Spinner sortBySpinner;
    TextView noResultsText;

    //endregion


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
//        populateClosestTrucksList(root);
        mapFragment.getMapAsync(this);

        searchText = root.findViewById(R.id.search_explore_pg1);
        locText = root.findViewById(R.id.location_explore_pg1);
        sortBySpinner = (Spinner) root.findViewById(R.id.spinner_sort);
        noResultsText = root.findViewById(R.id.no_results_text);

        final Search searchTool = Search.getInstance(root);
        searchTool.performSearch("", "", "");

        //region slidingPanel
        final SlidingUpPanelLayout slideUpPanel = (SlidingUpPanelLayout) root.findViewById(R.id.sliding_layout);
        final View searchBar = (View) root.findViewById(R.id.search_map);
        final View locationBar = (View) root.findViewById(R.id.location_map);
        final View options = (View) root.findViewById(R.id.options);
        final View background = (View) root.findViewById(R.id.search_bg);
        background.setVisibility(View.GONE);
        locationBar.setVisibility(View.GONE);
        options.setVisibility(View.GONE);
        final TextView logoText = (TextView) root.findViewById(R.id.logo_text);
        slideUpPanel.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                if(previousState == PanelState.COLLAPSED && newState == PanelState.DRAGGING){
                    ObjectAnimator animation = ObjectAnimator.ofFloat(searchBar, "translationY", -230f);
                    animation.setDuration(1000);
                    animation.start();
                    ObjectAnimator animation2 = ObjectAnimator.ofFloat(locationBar, "translationY", 0f);
                    animation2.setDuration(1000);
                    animation2.start();
                    AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
                    fadeOut.setDuration(1000);
                    logoText.setVisibility(View.INVISIBLE);
                    logoText.startAnimation(fadeOut);
                }
                if(previousState == PanelState.DRAGGING && newState == PanelState.COLLAPSED){
                    ObjectAnimator animation = ObjectAnimator.ofFloat(searchBar, "translationY", 0f);
                    animation.setDuration(1000);
                    animation.start();
                    logoText.setVisibility(View.VISIBLE);
                    AlphaAnimation fadeIn = new AlphaAnimation( 0.0f , 1.0f ) ;
                    fadeIn.setDuration(1000);
                    fadeIn.setFillAfter(true);
                    logoText.startAnimation(fadeIn);
                }
            }
        });
        searchText.setClickable(true);
        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                fadeOut.setDuration(1000);
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                fadeIn.setDuration(1000);
                fadeIn.setFillAfter(true);
                ObjectAnimator animation = ObjectAnimator.ofFloat(searchBar, "translationY", -170f);
                animation.setDuration(1000);
                animation.start();
                if(logoText.getVisibility() == View.VISIBLE) {
                    logoText.setVisibility(View.INVISIBLE);
                    logoText.startAnimation(fadeOut);
                }
                if (locationBar.getVisibility() == View.GONE) {
                    locationBar.setVisibility(View.VISIBLE);
                    locationBar.startAnimation(fadeIn);
                    background.setVisibility(View.VISIBLE);
                    int negHeight = background.getHeight() * -1;
                    TranslateAnimation animate = new TranslateAnimation(
                            0,
                            0,
                            negHeight,
                            0);
                    animate.setDuration(1000);
                    animate.setFillAfter(true);
                    background.startAnimation(animate);
                    options.setVisibility(View.VISIBLE);
                    options.startAnimation(fadeIn);
                }

            }
        });

        TextView close = (TextView)root.findViewById(R.id.close_button);
        final TextView search = (TextView)root.findViewById(R.id.search_button);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch(slideUpPanel,logoText,searchBar,locationBar,background,options);
            }
        });
        //endregion

        //region search and filter logic


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch(slideUpPanel, logoText, searchBar, locationBar, background, options);
                searchTool.makeSearch();
                populateMap();
                sortBySpinner.setSelection(0);
            }
        });
        //endregion

        //region sort by logic
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setOnItemSelectedListener(           //action triggered on button click
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0){     //distance
                            searchTool.sortByDistance();
                            populateMap();
                        }
                        if (position == 1){      //rating
                            searchTool.sortByRating();
                            populateMap();
                        }
                        if (position == 2){      //num reviews

                            searchTool.sortByNumReviews();
                            populateMap();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
//                        searchTool.replaceMethod();
                        searchTool.performSearch("", "", "");
                        populateMap();
                    }

                });
        //endregion


        return root;
    }


    @Override
    public void onMapReady(GoogleMap mMap) {

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.clear(); //clear old markers
        LocationCalculator locationCalculator = new LocationCalculator(getContext());
        double lat = locationCalculator.getLat();
        double lng = locationCalculator.getLng();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
        munMap = mMap;

        populateMap();
        munMap.setInfoWindowAdapter(new CustomInfoWindowAdaptor(getContext()));
        munMap.setOnMarkerClickListener(myMarkerClick);
        munMap.setOnInfoWindowClickListener(myWindowClick);

    }

    private GoogleMap.OnInfoWindowClickListener myWindowClick = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker markey) {
            FragmentActivity activity = (FragmentActivity) getContext();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FoodTruckFragment NAME = new FoodTruckFragment(getTruckFromMarker(markey), false);
            fragmentTransaction.replace(R.id.nav_host_fragment, NAME);
            fragmentTransaction.addToBackStack("map");
            fragmentTransaction.commit();
        }
    };


    private GoogleMap.OnMarkerClickListener myMarkerClick = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker markey) {
            if(lastClicked != null) {
                lastClicked.setIcon(BitmapDescriptorFromVector(getContext(), R.drawable.ft_dot));

            }
            markey.showInfoWindow();
            markey.setIcon(BitmapDescriptorFromVector(getContext(), R.drawable.ft_truck));;
            lastClicked = markey;
            LatLng center = markey.getPosition();
            munMap.animateCamera(CameraUpdateFactory.newLatLng(center));
            munMap.setPadding(0, 400, 0, 0);
            return true;
        }
    };


    private void closeSearch (SlidingUpPanelLayout slideUpPanel, TextView logoText,View searchBar,View locationBar, View background, View options){
        searchText.clearFocus();
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(1000);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);
        float move = 0f;
        if (slideUpPanel.getPanelState() == PanelState.EXPANDED){
            move = -230f;
        } else {
            logoText.setVisibility(View.VISIBLE);
            logoText.startAnimation(fadeIn);
        }
        ObjectAnimator animation = ObjectAnimator.ofFloat(searchBar, "translationY", move);
        animation.setDuration(1000);
        animation.start();
        if (locationBar.getVisibility() == View.VISIBLE) {
            locationBar.startAnimation(fadeOut);
            locationBar.setVisibility(View.GONE);
        }
        int negHeight = background.getHeight() * -1;
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                0,
                negHeight);
        animate.setDuration(1000);
        animate.setFillAfter(true);
        background.setVisibility(View.GONE);
        background.startAnimation(animate);
        options.startAnimation(fadeOut);
        options.setVisibility(View.GONE);
    }

    private void populateMap() {
        //initial Food Truck Population
        munMap.clear();
        lastClicked = null;
        if (listing != null) {
            for (int i = 0; i < listing.size(); i++) {
                populateFromFT(listing.get(i));

            }
        }
    }

    private FoodTruck getTruckFromMarker(Marker myMark) {
        for (int i = 0; i < listing.size(); i++) {
            if (listing.get(i).getMarker().equals(myMark)) {
                return listing.get(i);
            }
        }
        return listing.get(0);
    }

    private void populateFromFT(FoodTruck truck){
        //creates new marker for FoodTruck and displays them on map
        Marker marker = munMap.addMarker(truck.getMarkerOption());
        truck.setMarker(marker);
        truck.setVisibilityOn();
        marker.setIcon(BitmapDescriptorFromVector(getContext(), R.drawable.ft_dot));

        if(truck.getPhotos().size() != 0){
            marker.setSnippet(truck.getAvgRating() + " " + truck.getPhotos().get(0));
        }
        else if(truck.getPhotos().size() == 0){
            marker.setSnippet(truck.getAvgRating() + " " + "None");
        }
    }


    private BitmapDescriptor BitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable= ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}