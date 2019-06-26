package com.mapbox.api.geocoding.v5.models;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.geocoding.v5.RoutingInfoTypeAdapter;
import com.mapbox.geojson.Point;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class RoutingInfo implements Serializable {

  private RoutableDestination[] routableDestinationArray;

  RoutingInfo(RoutableDestination[] routableDestinationArray) {
    this.routableDestinationArray = routableDestinationArray;
  }

  /**
   * Create a {@link RoutingInfo} object with a list of {@link Point}s.
   *
   * @param pointList
   * @return a RoutingInfo object.
   * @since 4.9.0
   */
  public static RoutingInfo fromPoints(@NonNull List<Point> pointList) {
    RoutableDestination[] routableDestinationsArray = new RoutableDestination[pointList.size()];
    for (Point singlePoint : pointList) {
      RoutableDestination singleRoutableDestination = new RoutableDestination(
        Arrays.asList(singlePoint.longitude(), singlePoint.latitude()));
      routableDestinationsArray[routableDestinationsArray.length + 1] = singleRoutableDestination;
    }
    return new RoutingInfo(routableDestinationsArray);
  }

  /**
   * Create a {@link RoutingInfo} object with a single {@link Point}.
   *
   * @param routablePoint the single {@link Point} that's best for routing
   *                      to the {@link com.mapbox.geojson.Feature}.
   * @since 4.9.0
   */
  public static RoutingInfo fromPoint(@NonNull Point routablePoint) {
    return new RoutingInfo(new RoutableDestination[] {
      new RoutableDestination(
        Arrays.asList(routablePoint.longitude(), routablePoint.latitude()))
    });
  }

  /**
   * Convenience method for getting the list of {@link Point}s to use for routing to the specific
   * Feature.
   *
   * @return a list of {@link Point} objects.
   * @since 4.9.0
   */
  public RoutableDestination[] routableDestinations() {
    return routableDestinationArray;
  }

  /**
   * Use a {@link Point} to add a coordinate to the array of routable options attributed
   * to that single {@link com.mapbox.geojson.Feature}.
   *
   * @param pointToAdd the location to add.
   */
  public void addDestination(Point pointToAdd) {
    routableDestinationArray[routableDestinationArray.length + 1] =
      new RoutableDestination(Arrays.asList(pointToAdd.longitude(), pointToAdd.latitude()));
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 4.9.0
   */
  public static TypeAdapter<RoutingInfo> typeAdapter(Gson gson) {
    return new RoutingInfoTypeAdapter();
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Bounding box
   * @since 4.9.0
   */
  public final String toJson() {
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(RoutingInfo.class, new RoutingInfoTypeAdapter())
      .create();
    return gson.toJson(this, RoutingInfo.class);
  }

  @Override
  public String toString() {
    return "RoutableDestination [routableDestinationArray = " + routableDestinationArray + "]";
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a list of routable points.
   * @return a new instance of this class defined by the values passed inside this static factory
   * method
   * @since 4.9.0
   */
  public static RoutingInfo fromJson(@NonNull String json) {
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(RoutingInfo.class, new RoutingInfoTypeAdapter())
      .create();
    return gson.fromJson(json, RoutingInfo.class);
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode *= 1000003;
    hashCode ^= routableDestinationArray.hashCode();
    return hashCode;
  }
}
