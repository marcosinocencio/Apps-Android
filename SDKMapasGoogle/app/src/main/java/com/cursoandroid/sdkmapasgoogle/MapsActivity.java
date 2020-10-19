package com.cursoandroid.sdkmapasgoogle;

import androidx.fragment.app.FragmentActivity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        final LatLng igrejaMatriz = new LatLng(-21.482777, -51.5343053);
        //-21.482777,-51.5343053 Igreja Matriz

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;

                /*Toast.makeText(MapsActivity.this,
                        "Lat: "+ latitude + " Long: " + longitude,
                         Toast.LENGTH_SHORT).show();*/
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(igrejaMatriz);
                polylineOptions.add(latLng);
                polylineOptions.color(Color.RED);
                polylineOptions.width(20);

                mMap.addPolyline(polylineOptions);

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Local")
                        .snippet("Descrição")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;

                Toast.makeText(MapsActivity.this,
                        "Lat: "+ latitude + " Long: " + longitude,
                        Toast.LENGTH_SHORT).show();

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Local")
                        .snippet("Descrição")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            }
        });

        mMap.addMarker(new MarkerOptions()
                .position(igrejaMatriz)
                .title("Igreja Matriz Dracena")
                .icon(
                        BitmapDescriptorFactory.
                                defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
                )
                /*.icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.iconfinder)
                )*/

        );

        mMap.moveCamera(
                CameraUpdateFactory
                .newLatLngZoom(igrejaMatriz, 17)
        );

        /*CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(igrejaMatriz);
        circleOptions.radius(500); //medida em metros
        circleOptions.fillColor(Color.BLUE);
        circleOptions.strokeColor(Color.BLUE);
        mMap.addCircle(circleOptions);*/

        /*PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-21.481954,-51.5332797));
        polygonOptions.add(new LatLng(-21.48395,-51.5362457));
        polygonOptions.add(new LatLng(-21.481674,-51.5380057));

        mMap.addPolygon(polygonOptions);*/

    }
}