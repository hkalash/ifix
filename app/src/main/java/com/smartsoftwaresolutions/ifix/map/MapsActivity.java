package com.smartsoftwaresolutions.ifix.map;

import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartsoftwaresolutions.ifix.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //private GoogleMap mMap;

    private GoogleMap googleMap;
    ArrayList<LatLngBean> arrayList;
    private ArrayList<LatLng> listLatLng;
    private RelativeLayout rlMapLayout;
    HashMap<Marker,LatLngBean> hashMapMarker = new HashMap<Marker,LatLngBean>();
    LatLng object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        rlMapLayout=(RelativeLayout) findViewById(R.id.rlMapLayout);

        setUpMapIfNeeded();
        setData();

//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);








    }
    private void setData()
    {
        arrayList=new ArrayList<LatLngBean>();
        LatLngBean bean=new LatLngBean();
        bean.setTitle("abou dabi");
        bean.setSnippet("mohamed abed khalik "); // titel
        bean.setLatitude("25.0657005");
        bean.setLongitude("55.1712799");
        arrayList.add(bean);

//        LatLngBean bean1=new LatLngBean();
//        bean1.setTitle("dubai");
//        bean1.setSnippet("hussein kalash");
//        bean1.setLatitude("25.0657115");
//        bean1.setLongitude("55.1712699");
//        arrayList.add(bean1); // added to the array
//
//        LatLngBean bean2=new LatLngBean();
//        bean2.setTitle("sharga");
//        bean2.setSnippet("mahmoud kalash");
//        bean2.setLatitude("25.0657225");
//        bean2.setLongitude("55.1712899");
//        arrayList.add(bean2);


    }
    private void setUpMapIfNeeded()
    {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Google Play Services are not available
        if(status!= ConnectionResult.SUCCESS)
        {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }
        else
        {
            if (googleMap == null)
            {
                //googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map).getMapAsync(this));
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
                if (googleMap != null)
                {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                   // googleMap.setMyLocationEnabled(true);// need a permision
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                }
            }
        }
    }

    void LoadingGoogleMap(ArrayList<LatLngBean> arrayList)
    {
        if (googleMap != null)
        {
            googleMap.clear();
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
           // googleMap.setMyLocationEnabled(true);// need a permision
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            if(arrayList.size()>0)
            {
                try
                {
                    listLatLng=new ArrayList<LatLng>();
                    for (int i = 0; i < arrayList.size(); i++)
                    {
                        // fill the array in an object
                        LatLngBean bean=arrayList.get(i);
                        if(bean.getLatitude().length()>0 && bean.getLongitude().length()>0)
                        {
                            double lat=Double.parseDouble(bean.getLatitude());
                            double lon=Double.parseDouble(bean.getLongitude());

                            Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat,lon))
                                    .title(bean.getTitle())
                                    .snippet(bean.getSnippet())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            //.icon(BitmapDescriptorFactory.fromResource(iconResID)

                            //Add Marker to Hashmap
                            hashMapMarker.put(marker,bean);

                            //Set Zoom Level of Map pin
                            object=new LatLng(lat, lon);
                            listLatLng.add(object);
                        }
                    }
                  //  SetZoomlevel(listLatLng);
                    float zoomLevel=(float)  12.0;
                   googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(object, zoomLevel));
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker position)
                    {
                        LatLngBean bean=hashMapMarker.get(position);
                        Toast.makeText(getApplicationContext(), bean.getTitle(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * @author Hasmukh Bhadani
     * Set Zoom level all pin withing screen on GoogleMap
     */
    public void  SetZoomlevel(ArrayList<LatLng> listLatLng)
    {
        if (listLatLng != null && listLatLng.size() == 1)
        {
            // here we set the zoom level
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(0), 2));
        }
        else if (listLatLng != null && listLatLng.size() > 1)
        {
            final LatLngBounds.Builder builder = LatLngBounds.builder();
            for (int i = 0; i < listLatLng.size(); i++)
            {
                builder.include(listLatLng.get(i));
            }

            final ViewTreeObserver treeObserver = rlMapLayout.getViewTreeObserver();
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout()
                {
                    if(googleMap != null){
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), findViewById(R.id.map)
                                .getWidth(), findViewById(R.id.map).getHeight(), 80));
                        rlMapLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });

        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap1) {
   googleMap=googleMap1;
// // the foucus of the first location is the user location
//        // Add a marker in Sydney and move the camera
//        LatLng Dubai = new LatLng(25.0657005, 55.1712799);
//        mMap.addMarker(new MarkerOptions().position(Dubai).title("Dubai, United Arab Emirates"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Dubai));
        LoadingGoogleMap(arrayList);
    }

    // Action Bar actions
    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
//            case R.id.menu_share:
//
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//
//                shared_Menu sharedMenu=new shared_Menu();
//                String flaver =BuildConfig.FLAVOR;
//                String shareBodyText =sharedMenu.flavor(flaver);
//
////                if(BuildConfig.FLAVOR.equals("Free")) {
////                    shareBodyText = getString(R.string.check_it) + getString(R.string.sharepath);
////                }else if (BuildConfig.FLAVOR.equals("NM")){
////                     // nouhad moukahal
////                //    shareBodyText = getString(R.string.check_it) + getString(R.string.sharepath_NM);
////                }
//
//
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
//                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
//                return true;
//            case R.id.action_Arabic:
//                setLocale("ar");
//
//                Toast.makeText(this, R.string.la, Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.action_English:
//                setLocale("en_US");

//                Toast.makeText(this, R.string.le, Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.action_Help:
//                Intent intent2=new Intent(MainActivity.this, Help_Activity.class);
//                startActivity(intent2);
//                return true;
//            case R.id.action_reminder:
//                Intent intent3=new Intent(MainActivity.this, Notefication_Activity.class);
//                startActivity(intent3);
//                return true;
//            case R.id.menu_setting:
//                Intent intent=new Intent(MainActivity.this, Setting.class);
//                startActivity(intent);
//
//                return true;
            case R.id.action_back:


                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
