package com.example.scibdisrat.myapplication2;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author SCIBBDISRAT
 */
public class RestaurantActivity extends FragmentActivity {
    private static final String TAG = "DemoApp";
    double lat,lng;
    Marker marker=null;
    public String restaurantURL=null;
    protected GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gMap))
                    .getMap();
            if (mMap != null) {

                mMap.clear();
                setUpMap();
            }
        }
    }
    private void showCurrentLocation(Location location) {

        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        lat = location.getLatitude();

        lng = location.getLongitude();
        restaurantURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=1000&sensor=true&types=restauranto&key=*use own google places api web service*";

        if(marker!=null) {
            marker.remove();
            marker= mMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map))
                    .flat(true)
                    .title("You!"));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 18));

        }
        if (marker==null) {
            marker= mMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map))
                    .flat(true)
                    .title("You!"));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 18));
        }

    }

    private void setUpMap() {


        // mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String provider = locationManager.getBestProvider(criteria, true);

        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                showCurrentLocation(location);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        locationManager.requestLocationUpdates(provider, 20000, 10, locationListener);

        // Getting initial Location
        Location location = locationManager.getLastKnownLocation(provider);
        // Show the initial location


        if (location != null) {
            showCurrentLocation(location);

        }

        new Thread(new Runnable() {
            public void run() {
                try {
                    AddRestaurants();
                } catch (IOException e) {
                    Log.e("abc", "Cannot retrive restaurant", e);
                    return;
                }
            }
        }).start();
    }

    protected void AddRestaurants() throws IOException {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(restaurantURL);

            conn = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read2;
            char[] buff = new char[1972432];
            while ((read2 = in.read(buff)) != -1) {
                json.append(buff, 0, read2);
            }
        } catch (IOException e) {
            Log.e("abc", "Error connecting to service", e);
            throw new IOException("Error connecting to service", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // Must run this on the UI thread since it's a UI operation.
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    Log.i("JSON object", "" + json);
                    JSONObject jsonObject = new JSONObject(json.toString());

                    createMarkersFromJson(jsonObject);
                } catch (JSONException e) {
                    Log.e("abc", "Error processing JSON", e);
                }
            }
        });
    }

    void createMarkersFromJson(JSONObject json) throws JSONException {

        JSONArray jsonArray = json.getJSONArray("results");
        Log.i("JSON ARRAY", "" + jsonArray);

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObj = jsonArray.getJSONObject(i);
            mMap.addMarker(new MarkerOptions()
                            .title(jsonObj.getString("name"))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant))
                            .snippet(jsonObj.getString("vicinity"))
                            .position(new LatLng(jsonObj.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                    jsonObj.getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                            ))
            );
        }
    }

}

