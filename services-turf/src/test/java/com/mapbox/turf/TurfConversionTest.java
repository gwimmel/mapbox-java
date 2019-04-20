package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TurfConversionTest extends TestUtils {

  private static final String TURF_EXPLODE_MULTI_POINT = "turf-explode/multipoint.geojson";
  private static final String TURF_EXPLODE_LINESTRING = "turf-explode/linestring.geojson";
  private static final String TURF_EXPLODE_MULTILINESTRING = "turf-explode/multilinestring.geojson";
  private static final String TURF_EXPLODE_MULTIPOLYGON = "turf-explode/multipolygon.geojson";
  private static final String TURF_EXPLODE_GEOMETRY_COLLECTION = "turf-explode/geometrycollection.geojson";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void radiansToDistance() throws Exception {
    assertEquals(
      1, TurfConversion.radiansToLength(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
      6373, TurfConversion.radiansToLength(1, TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
      3960, TurfConversion.radiansToLength(1, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToRadians() throws Exception {
    assertEquals(
      1, TurfConversion.lengthToRadians(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
      1, TurfConversion.lengthToRadians(6373, TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
      1, TurfConversion.lengthToRadians(3960, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void distanceToDegrees() throws Exception {
    assertEquals(
      57.29577951308232, TurfConversion.lengthToDegrees(1, TurfConstants.UNIT_RADIANS), DELTA);
    assertEquals(
      0.8990393772647469, TurfConversion.lengthToDegrees(100,
        TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(
      0.14468631190172304, TurfConversion.lengthToDegrees(10, TurfConstants.UNIT_MILES), DELTA);
  }

  @Test
  public void convertDistance() throws TurfException {
    assertEquals(1,
      TurfConversion.convertLength(1000, TurfConstants.UNIT_METERS), DELTA);
    assertEquals(0.6213714106386318,
      TurfConversion.convertLength(1, TurfConstants.UNIT_KILOMETERS,
        TurfConstants.UNIT_MILES), DELTA);
    assertEquals(1.6093434343434343,
      TurfConversion.convertLength(1, TurfConstants.UNIT_MILES,
        TurfConstants.UNIT_KILOMETERS), DELTA);
    assertEquals(1.851999843075488,
      TurfConversion.convertLength(1, TurfConstants.UNIT_NAUTICAL_MILES), DELTA);
    assertEquals(100,
      TurfConversion.convertLength(1, TurfConstants.UNIT_METERS,
        TurfConstants.UNIT_CENTIMETERS), DELTA);
  }

  @Test
  public void combinePointsToMultiPoint() throws Exception {
    FeatureCollection pointFeatureCollection =
      FeatureCollection.fromFeatures(
        new Feature[]{
          Feature.fromGeometry(Point.fromLngLat(-2.46,
            27.6835)),
          Feature.fromGeometry(Point.fromLngLat(41.83,
            7.3624)),
        });

    FeatureCollection featureCollectionWithNewMultiPointObject = TurfConversion.combine(pointFeatureCollection);
    assertNotNull(featureCollectionWithNewMultiPointObject);

    MultiPoint multiPoint = (MultiPoint) featureCollectionWithNewMultiPointObject.features().get(0).geometry();
    assertNotNull(multiPoint);

    assertEquals(-2.46, multiPoint.coordinates().get(0).longitude(), DELTA);
    assertEquals(27.6835, multiPoint.coordinates().get(0).latitude(), DELTA);
    assertEquals(41.83, multiPoint.coordinates().get(1).longitude(), DELTA);
    assertEquals(7.3624, multiPoint.coordinates().get(1).latitude(), DELTA);
  }

  @Test
  public void combineLineStringToMultiLineString() throws Exception {
    FeatureCollection lineStringFeatureCollection =
      FeatureCollection.fromFeatures(
        new Feature[]{
          Feature.fromGeometry(LineString.fromLngLats(
            Arrays.asList(Point.fromLngLat(-11.25, 55.7765),
              Point.fromLngLat(41.1328, 22.91792)))),
          Feature.fromGeometry(LineString.fromLngLats(
            Arrays.asList(Point.fromLngLat(3.8671, 19.3111),
              Point.fromLngLat(20.742, -20.3034))))
        });

    FeatureCollection featureCollectionWithNewMultiLineStringObject = TurfConversion.combine(lineStringFeatureCollection);
    assertNotNull(featureCollectionWithNewMultiLineStringObject);

    MultiLineString multiLineString = (MultiLineString) featureCollectionWithNewMultiLineStringObject.features().get(0).geometry();
    assertNotNull(multiLineString);

    // Checking the first LineString in the MultiLineString
    assertEquals(-11.25, multiLineString.coordinates().get(0).get(0).longitude(), DELTA);
    assertEquals(55.7765, multiLineString.coordinates().get(0).get(0).latitude(), DELTA);

    // Checking the second LineString in the MultiLineString
    assertEquals(41.1328, multiLineString.coordinates().get(0).get(1).longitude(), DELTA);
    assertEquals(22.91792, multiLineString.coordinates().get(0).get(1).latitude(), DELTA);
  }

  @Test
  public void combinePolygonToMultiPolygon() throws Exception {
    FeatureCollection polygonFeatureCollection =
      FeatureCollection.fromFeatures(
        new Feature[]{
          Feature.fromGeometry(Polygon.fromLngLats(Arrays.asList(
            Arrays.asList(
              Point.fromLngLat(61.938950426660604, 5.9765625),
              Point.fromLngLat(52.696361078274485, 33.046875),
              Point.fromLngLat(69.90011762668541, 28.828124999999996),
              Point.fromLngLat(61.938950426660604, 5.9765625))))),
          Feature.fromGeometry(Polygon.fromLngLats(Arrays.asList(
            Arrays.asList(
              Point.fromLngLat(11.42578125, 16.636191878397664),
              Point.fromLngLat(7.91015625, -9.102096738726443),
              Point.fromLngLat(31.113281249999996, 17.644022027872726),
              Point.fromLngLat(11.42578125, 16.636191878397664)
            ))))
        });

    FeatureCollection featureCollectionWithNewMultiPolygonObject = TurfConversion.combine(polygonFeatureCollection);
    assertNotNull(featureCollectionWithNewMultiPolygonObject);

    MultiPolygon multiPolygon = (MultiPolygon) featureCollectionWithNewMultiPolygonObject.features().get(0).geometry();
    assertNotNull(multiPolygon);

    // Checking the first Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(61.938950426660604, multiPolygon.coordinates().get(0).get(0).get(0).longitude(), DELTA);
    assertEquals(5.9765625, multiPolygon.coordinates().get(0).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(52.696361078274485, multiPolygon.coordinates().get(0).get(0).get(1).longitude(), DELTA);
    assertEquals(33.046875, multiPolygon.coordinates().get(0).get(0).get(1).latitude(), DELTA);

    // Checking the second Polygon in the MultiPolygon

    // Checking the first Point
    assertEquals(11.42578125, multiPolygon.coordinates().get(1).get(0).get(0).longitude(), DELTA);
    assertEquals(16.636191878397664, multiPolygon.coordinates().get(1).get(0).get(0).latitude(), DELTA);

    // Checking the second Point
    assertEquals(7.91015625, multiPolygon.coordinates().get(1).get(0).get(1).longitude(), DELTA);
    assertEquals(-9.102096738726443, multiPolygon.coordinates().get(1).get(0).get(1).latitude(), DELTA);
  }

  // TODO: Add test that checks Feature amount
  @Test
  public void geometryTypeMixThrowsException() throws TurfException {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Your FeatureCollection must be of all of the same geometry type."));

    // Create a FeatureCollection with a Point Feature and a Polygon Feature
    FeatureCollection pointAndPolygonFeatureCollection =
      FeatureCollection.fromFeatures(
        new Feature[]{
          Feature.fromGeometry(Point.fromLngLat(-2.46,
            27.6835)),
          Feature.fromGeometry(Polygon.fromLngLats(Arrays.asList(
            Arrays.asList(
              Point.fromLngLat(11.42578125, 16.636191878397664),
              Point.fromLngLat(7.91015625, -9.102096738726443),
              Point.fromLngLat(31.113281249999996, 17.644022027872726),
              Point.fromLngLat(11.42578125, 16.636191878397664)
            ))))
        });

    // Building a geometry with this FeatureCollection should through an error
    TurfConversion.combine(pointAndPolygonFeatureCollection);
  }

  @Test
  public void explodePointSingleFeature() throws IOException, NullPointerException {
    Point point = Point.fromLngLat(102, 0.5);
    assertEquals(1, TurfConversion.explode(Feature.fromGeometry(point)).features().size());
  }

  @Test
  public void explodeMultiPointSingleFeature() throws IOException, NullPointerException {
    MultiPoint multiPoint = MultiPoint.fromJson(loadJsonFixture(TURF_EXPLODE_MULTI_POINT));
    assertEquals(4, TurfConversion.explode(Feature.fromGeometry(multiPoint)).features().size());
  }

  @Test
  public void explodeLineStringSingleFeature() throws IOException, NullPointerException {
    LineString lineString = LineString.fromJson(loadJsonFixture(TURF_EXPLODE_LINESTRING));
    assertEquals(4, TurfConversion.explode(Feature.fromGeometry(lineString)).features().size());
  }

  @Test
  public void explodePolygonSingleFeature() throws IOException, NullPointerException {
    Polygon polygon = Polygon.fromLngLats(Arrays.asList(
      Arrays.asList(
        Point.fromLngLat(0, 101),
        Point.fromLngLat(1, 101),
        Point.fromLngLat(1, 100),
        Point.fromLngLat(0, 100))));
    assertEquals(3, TurfConversion.explode(Feature.fromGeometry(polygon)).features().size());
  }

  @Test
  public void explodeMultiLineStringSingleFeature() throws IOException, NullPointerException {
    MultiLineString multiLineString = MultiLineString.fromJson(loadJsonFixture(TURF_EXPLODE_MULTILINESTRING));
    assertEquals(4, TurfConversion.explode(Feature.fromGeometry(multiLineString)).features().size());
  }

  @Test
  public void explodeMultiPolygonSingleFeature() throws IOException, NullPointerException {
    MultiPolygon multiPolygon = MultiPolygon.fromJson(loadJsonFixture(TURF_EXPLODE_MULTIPOLYGON));
    assertEquals(12, TurfConversion.explode(Feature.fromGeometry(multiPolygon)).features().size());
  }

  @Test
  public void explodeGeometryCollectionSingleFeature() throws IOException, NullPointerException {
    GeometryCollection geometryCollection = GeometryCollection.fromJson(loadJsonFixture(TURF_EXPLODE_GEOMETRY_COLLECTION));
    assertEquals(3, TurfConversion.explode(Feature.fromGeometry(geometryCollection)).features().size());
  }

  @Test
  public void explodeFeatureCollection() throws IOException, NullPointerException {
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(new Feature[] {
      Feature.fromGeometry(MultiLineString.fromJson(loadJsonFixture(TURF_EXPLODE_MULTILINESTRING))),
      Feature.fromGeometry(MultiPolygon.fromJson(loadJsonFixture(TURF_EXPLODE_MULTIPOLYGON)))
    });
    assertEquals(16, TurfConversion.explode(featureCollection).features().size());
  }
}
