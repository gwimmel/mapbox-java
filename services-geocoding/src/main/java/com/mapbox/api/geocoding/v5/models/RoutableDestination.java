package com.mapbox.api.geocoding.v5.models;

import android.support.annotation.NonNull;

import com.mapbox.geojson.Point;

import java.util.List;

public class RoutableDestination {

  @NonNull
  private final List<Double> coordinates;

  RoutableDestination(List<Double> coordinates) {
    if (coordinates == null || coordinates.size() == 0) {
      throw new NullPointerException("Null coordinates for RoutableDestination");
    }
    this.coordinates = coordinates;
  }

  /**
   * Provides a single double array containing the longitude and latitude.
   * {@link #longitude()} and {@link #latitude()} are all available which make getting specific coordinates more direct.
   *
   * @return a double array which holds this Point's coordinates
   * @since 4.9.0
   */
  @NonNull
  public List<Double> coordinates()  {
    return coordinates;
  }

  /**
   * This returns a double value ranging from -180 to 180 representing the x or easting position of
   * this point. ideally, this value would be restricted to 6 decimal places to correctly follow the
   * GeoJson spec.
   *
   * @return a double value ranging from -180 to 180 representing the x or easting position of this
   *   point
   * @since 4.9.0
   */
  public double longitude() {
    return coordinates().get(0);
  }

  /**
   * This returns a double value ranging from -90 to 90 representing the y or northing position of
   * this point. ideally, this value would be restricted to 6 decimal places to correctly follow the
   * GeoJson spec.
   *
   * @return a double value ranging from -90 to 90 representing the y or northing position of this
   *   point
   * @since 4.9.0
   */
  public double latitude() {
    return coordinates().get(1);
  }

  /**
   * Convenience method to convert this {@link RoutableDestination} object's coordinates into a {@link Point} for
   * easier usage with the rest of the Maps SDK for Android.
   *
   * @return a {@link Point} object that has the same coordinates as the {@link RoutableDestination}.
   */
  public Point toPoint() {
    return Point.fromLngLat(longitude(), latitude());
  }

  @Override
  public String toString() {
    return "RoutableDestination [coordinates = " + coordinates + "]";
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode *= 1000003;
    hashCode ^= coordinates.hashCode();
    return hashCode;
  }
}
	