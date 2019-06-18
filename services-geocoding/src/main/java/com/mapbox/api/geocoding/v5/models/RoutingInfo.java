package com.mapbox.api.geocoding.v5.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.geocoding.v5.RoutingInfoTypeAdapter;
import com.mapbox.geojson.Point;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class RoutingInfo implements Serializable {

  private List<Point> listOfRoutablePoints;

  RoutingInfo(List<Point> listOfRoutablePoints) {
    this.listOfRoutablePoints = listOfRoutablePoints;
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a list of routable points.
   * @return a new instance of this class defined by the values passed inside this static factory
   * method
   * @since 4.9.0
   */
  public static RoutingInfo fromJson(String json) {
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(RoutingInfo.class, new RoutingInfoTypeAdapter())
      .create();
    return gson.fromJson(json, RoutingInfo.class);
  }

  /**
   * Create a {@link RoutingInfo} object with a list of {@link Point}s.
   *
   * @param listOfRoutablePoints
   */
  public static RoutingInfo fromPointList(List<Point> listOfRoutablePoints) {
    return new RoutingInfo(listOfRoutablePoints);
  }

  /**
   * Create a {@link RoutingInfo} object with a single {@link Point}.
   *
   * @param routablePoint the single {@link Point} that's best for routing
   *                      to the {@link com.mapbox.geojson.Feature}.
   */
  public static RoutingInfo fromPoint(Point routablePoint) {
    return new RoutingInfo(Arrays.asList(routablePoint));
  }

  /**
   * Convenience method for getting the list of {@link Point}s to use for routing to the specific
   * Feature.
   *
   * @return a list of {@link Point} objects.
   * @since 4.9.0
   */
  public List<Point> routablePoints() {
    return listOfRoutablePoints;
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
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\"routable_points\":{\"points\":[");
    for (Point singlePoint : listOfRoutablePoints) {
      stringBuilder.append("{\"coordinates\":" + singlePoint.longitude() + "," + singlePoint.latitude() + "}");
    }
    stringBuilder.append("]}");
    return stringBuilder.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof RoutingInfo) {
      RoutingInfo that = (RoutingInfo) obj;
      return (this.southwest.equals(that.southwest()))
        && (this.northeast.equals(that.northeast()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode *= 1000003;
    hashCode ^= southwest.hashCode();
    hashCode *= 1000003;
    hashCode ^= northeast.hashCode();
    return hashCode;
  }
}
