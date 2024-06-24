package com.example.healthdouglas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FindHospitalsNearYourActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap gMap;
    LatLng center;
    public String GOOGLE_MAPS_API_KEY = "";

    public Location searchLocation;
    public ArrayList<Place> searchPlaces = new ArrayList<>();
    public ArrayList<Place> searchPlacesVicinity = new ArrayList<>();

    public ArrayList<String> SearchPlacesNames = new ArrayList<>();
    public ArrayList<String> SearchPlacesVicinity = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_hospitals_near_your);
        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {findNearbyHospitals(center.latitude,center.longitude);}
        });

        SupportMapFragment mapFragment = (SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

//        findCoordinates("Calgary");


        //Go Back Button:
        findViewById(R.id.goBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindHospitalsNearYourActivity.this,HomeActivity.class));
            }
        });
    }

    //default:
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(49.2827,-123.1207);
//        googleMap.addMarker(new MarkerOptions().position(location).title("VANCOUVER"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,2));
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                center = googleMap.getCameraPosition().target;
                Log.d("Map Center", "lat: " + center.latitude + ", lng: " + center.longitude);
            }
        });
    }


    public void findCoordinates(String addressQuery){
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + addressQuery + "&key=" + GOOGLE_MAPS_API_KEY;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET,url, response -> {
            Log.d("HospitalAddress",response);
            searchLocation = parseResponse(response);
            findNearbyHospitals(searchLocation.lat,searchLocation.lng);
        },error -> {
            Log.e("MainActivity", "Error finding coordinate: " + error.getLocalizedMessage());
            Toast.makeText(this, "Error finding coordinates.", Toast.LENGTH_SHORT).show();
        }
        );
        queue.add(request);
    }


    private void findNearbyHospitals(double latitude, double longitude){
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + latitude + "," + longitude +
                "&radius=5000" + // Adjust radius as needed (in meters)
                "&type=hospital" +
                "&key=" + GOOGLE_MAPS_API_KEY;

        // Use Volley or Retrofit or any other networking library to make the HTTP request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET,url,response -> {
            Toast.makeText(this,"Found " + response, Toast.LENGTH_SHORT).show();

            searchPlaces = parsePlacesResponse(response);
            Log.d("Print all responses ",response);
            for (Place place: searchPlaces){
                Log.d("HospitalsName Found",place.name);
                SearchPlacesNames.add(place.name);
            }

            searchPlacesVicinity = parseVicinity(response);
            for (Place place: searchPlacesVicinity){
                Log.d("Vicinity Found",place.vicinity);
                SearchPlacesVicinity.add(place.vicinity);
            }

            ArrayList<Hospital> hospitals = parseHospitalsResponse(response);

            Bundle bundle = new Bundle();
            bundle.putSerializable("ShowName",SearchPlacesNames);
            bundle.putSerializable("ShowVicinity",SearchPlacesVicinity);

            Intent intent = new Intent(FindHospitalsNearYourActivity.this,HospitalsResultsActivity.class);
            intent.putExtras(bundle);
            intent.putExtra("Hospitals", hospitals);
            startActivity(intent);

        }, error -> {
            //Handle API error when requested:
            Toast.makeText(this,"Error in finding hospitals.",Toast.LENGTH_SHORT).show();
        });
        queue.add(request);
    }


    //return an (Location) object with long and lat
    public Location parseResponse(String response){
        Gson gson = new Gson();
        Location L1;
        //Convert JSON string to a GeocodingResponse object
        GeocodingResponse geocodingResponse = gson.fromJson(response,GeocodingResponse.class);

        //Check if results are available and the status is OK
        if (geocodingResponse.results != null && geocodingResponse.status.equals("OK")){
            //Access the first result assuming the address is valid
            GeocodingResult firstResult = geocodingResponse.results.get(0);

            //Extract geometry data
            Geometry geometry = firstResult.geometry;

            //Extract latitude and longitude
            double latitude = geometry.location.lat;
            double longitude = geometry.location.lng;

            L1 = new Location();
            L1.lat = latitude;
            L1.lng = longitude;

            return L1;   //now we have an object type Location with longitude and latitude
        } else {
            Toast.makeText(this,"Geocoding failed: " + geocodingResponse.status, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public ArrayList<Hospital> parseHospitalsResponse(String response) {
        Gson gson = new Gson();
        ArrayList<Hospital> hospitals = new ArrayList<>();
        //convert JSON string to a PlaceResponse object (PlaceResponse = List<Place> results + String status)
        PlacesResponse placesResponse = gson.fromJson(response, PlacesResponse.class);
        Log.d("investigate1",response);
        //Check if results are available and status is OK
        if(placesResponse.results != null && placesResponse.status.equals("OK")){
            for(Place place : placesResponse.results){
                String placeName = place.name;
                String vicinity = place.vicinity;
                hospitals.add(new Hospital(placeName, vicinity));
            }
        } else {
            Toast.makeText(this,"Error finding places: " + placesResponse.status, Toast.LENGTH_SHORT).show();
        }
        return hospitals;
    }

    //Parse to names in an arraylist:
    public ArrayList<Place> parsePlacesResponse(String response) {
        Gson gson = new Gson();
        Place P1;
        ArrayList<Place> Places = new ArrayList<>();
        //convert JSON string to a PlaceResponse object (PlaceResponse = List<Place> results + String status)
        PlacesResponse placesResponse = gson.fromJson(response,PlacesResponse.class);
        Log.d("investigate1",response);
        //Check if results are available and status is OK
        if(placesResponse.results != null && placesResponse.status.equals("OK")){
            for(Place place : placesResponse.results){
                String placeName = place.name;
                P1 = new Place();
                P1.name = placeName;
                Places.add(P1);
            }
        } else {
            Toast.makeText(this,"Error finding places: " + placesResponse.status, Toast.LENGTH_SHORT).show();
        }
        return Places;
    }

    //Parse to vicinity in an arraylist:
    public ArrayList<Place> parseVicinity(String response) {
        Gson gson = new Gson();
        Place P1;
        ArrayList<Place> Places = new ArrayList<>();
        //convert JSON string to a PlaceResponse object (PlaceResponse = List<Place> results + String status)
        PlacesResponse placesResponse = gson.fromJson(response,PlacesResponse.class);
        Log.d("investigate1",response);
        //Check if results are available and status is OK
        if(placesResponse.results != null && placesResponse.status.equals("OK")){
            for(Place place : placesResponse.results){
                String placeVicinity = place.vicinity;
                P1 = new Place();
                P1.vicinity = placeVicinity;
                Places.add(P1);
            }
        } else {
            Toast.makeText(this,"Error finding places: " + placesResponse.status, Toast.LENGTH_SHORT).show();
        }
        return Places;
    }

    class GeocodingResponse{
        List<GeocodingResult> results;
        String status;
    }
    class GeocodingResult{
        Geometry geometry;
    }
    class Geometry{
        Location location;
    }
    class Location{
        double lat;
        double lng;
    }
    class PlacesResponse{
        List<Place> results;
        String status;
    }
    class Place{
        String name;
        String vicinity;
    }
}