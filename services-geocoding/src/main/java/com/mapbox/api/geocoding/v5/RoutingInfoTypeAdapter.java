package com.mapbox.api.geocoding.v5;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.api.geocoding.v5.models.RoutingInfo;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import com.mapbox.geojson.utils.GeoJsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoutingInfoTypeAdapter extends TypeAdapter<RoutingInfo> {

  @Override
  public void write(JsonWriter out, RoutingInfo value) throws IOException {

    if (value == null) {
      out.nullValue();
      return;
    }

    out.beginArray();

    List<Point> pointList = value.routableDestinations();

    for (Point singlePoint: pointList) {
      List<Double> unshiftedCoordinates = CoordinateShifterManager.getCoordinateShifter().unshiftPoint(singlePoint);
      out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(0)));
      out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(1)));
      if (singlePoint.hasAltitude()) {
        out.value(unshiftedCoordinates.get(2));
      }
    }

    out.endArray();
  }

  @Override
  public RoutingInfo read(JsonReader in) throws IOException {

    List<Point> pointList = new ArrayList<>();
    List<Double> rawCoordinates = new ArrayList<Double>();
    in.beginArray();
    while (in.hasNext()) {
      pointList.add(Point.fromLngLat()in.nex());
    }
    in.endArray();
    if (pointList.size() == 1) {
      return RoutingInfo.fromPoint(pointList.get(0));
    }
    return RoutingInfo.fromPoints(pointList);
  }
}
