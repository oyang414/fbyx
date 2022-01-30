/*
 * (C) Copyright 2017 David Jennings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     David Jennings
 */
package com.ouyang.fbyx.demo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class can be used to create Ellipses
 *
 *
 * @author david
 */
public class Ellipse {

    static final boolean debug = CONSTANTS.debug;
    
    /**
     *
     * Will not work for Ellipses where semi-major axis is greater than half
     * circumference of Earth
     *
     * Also may have issues when ellipses extend into polar regions
     *
     * Uses GreatCircle to calculate points on the ellipse.
     *
     * @param clon Center Longitude
     * @param clat Center Latitude
     * @param a Semi-Major Axis (km)
     * @param b Semi-Minor Axis (km)
     * @param rotationDeg Clockwise rotation in degrees. For 0 semi-major axis
     * is North-South
     * @param numPoints The actual number of points returned will be
     * approximately this amount
     * @param isClockwise For GeoJSON set false; for Esri Geometries set true
     * @return JSONArray with ellipse points
     */
    public JSONArray createEllipse(double clon, double clat, Double a, Double b, Double rotationDeg, Integer numPoints, boolean isClockwise) {

        GreatCircle gc = new GreatCircle();

        if (numPoints == null) {
            numPoints = 20;
        }

        if (a == null) {
            // default to 500 meters or 0.50 km
            a = 0.50;
        }

        if (b == null) {
            // default to 50 meters or 0.050 km
            b = 0.050;
        }

        if (rotationDeg == null) {
            rotationDeg = 0.0;
        }

        if (a < b) {
            // Switch a and b
            // Semimajor axis should be the larger of the two
            double t = a;
            a = b;
            b = t;
        }

        // If this was a circle then even distribuation would be d
        double d = 360.0 / (numPoints - 1);

        // The number of points returns will depend on eccentricity
        ArrayList<GeographicCoordinate> coords = new ArrayList<>();

        double v = 360.0;
        GeographicCoordinate coord1 = new GeographicCoordinate(clon, clat);

        GeographicCoordinate lastcoord = coord1;

        while (v > 0.0001) {

            double r = v * CONSTANTS.D2R;
            double sv = Math.sin(r);
            double cv = Math.cos(r);
            double radiusKM = a * b / Math.sqrt(a * a * sv * sv + b * b * cv * cv);

            double bearing = v;
            if (bearing > 180) {
                bearing = bearing - 360;
            }
            DistanceBearing distb = new DistanceBearing(radiusKM, bearing);

            if (rotationDeg != 0.0) {
                bearing = (distb.getBearing() + rotationDeg) % 360.0;
                if (bearing > 180) {
                    bearing = bearing - 360;
                }
                distb.setBearing(bearing);
            }

            GeographicCoordinate nc = gc.getNewCoordPair(coord1, distb);

            // Only add the nc if it is different from previous
            double lon = nc.getLon();
            double lat = nc.getLat();

            if (Math.abs(lon - lastcoord.getLon()) < 0.00001 && Math.abs(lat - lastcoord.getLat()) < 0.00001) {
                // skip this point too close to previous
            } else {
                coords.add(nc);
                if (debug) {
                    System.out.println(v + "," + bearing + "," + radiusKM + "," + lon + "," + lat);
                }
                lastcoord = nc;
            }

            // Densifying the points around 0 and 360 and 180; just 10 degrees out really improved shape of ellipse 
            if (v > 350 || v < 10 || (v > 170 && v < 190)) {
                v = v - d * b / a;
            } else {
                v = v - d;
            }

        }

        // Added code to prevent adding a last point that was too close to the end point.
        GeographicCoordinate firstPoint = coords.get(0);
        GeographicCoordinate lastPoint = coords.get(coords.size() - 1);

        if (Math.abs(firstPoint.getLon() - lastPoint.getLon()) < 0.00001 && Math.abs(firstPoint.getLat() - lastPoint.getLat()) < 0.00001) {
            // Replace last point with first
            coords.set(coords.size() - 1, firstPoint);
        } else {
            // Append firstpoint 
            coords.add(firstPoint);
            if (debug) {
                System.out.println(-1 + "," + -1 + "," + -1 + "," + firstPoint.getLon() + "," + firstPoint.getLat());
            }

        }

//        System.exit(0);
        GeographicCoordinate nc1;
        GeographicCoordinate nc2;

        JSONArray[] exteriorRing = new JSONArray[2];
        exteriorRing[0] = new JSONArray();
        exteriorRing[1] = new JSONArray();

        int ringNum = 0;

        nc1 = coords.get(0);
        int i = 0;
        double lon1 = nc1.getLon();
        double lat1 = nc1.getLat();
        if (debug) {
            System.out.println(i + ":" + lon1 + "," + lat1);
        }
        JSONArray coord = new JSONArray("[" + lon1 + ", " + lat1 + "]");
        exteriorRing[ringNum].put(coord);
        i++;

        boolean crossedDTEast = false;
        boolean crossedDTWest = false;
        boolean crossedZeroEast = false;
        boolean crossedZeroWest = false;

        while (i < coords.size()) {

            nc2 = coords.get(i);
            // Compare coordinates
            double lon2 = nc2.getLon();
            double lat2 = nc2.getLat();

            if (lon1 == 0.0 && lon2 < 0.0) {
                crossedZeroWest = true;

                // Switch ringnum
                if (ringNum == 0) {
                    ringNum = 1;
                } else if (ringNum == 1) {
                    ringNum = 0;
                }

                // Add nc1 to ring
                coord = new JSONArray("[" + lon1 + ", " + lat1 + "]");
                exteriorRing[ringNum].put(coord);

            } else if (lon2 == 0.0 && lon1 < 0.0) {

                crossedZeroEast = true;

                // Add nc2 to ring
                coord = new JSONArray("[" + lon2 + ", " + lat2 + "]");
                exteriorRing[ringNum].put(coord);

                if (crossedZeroWest) {
                    // close ring
                    exteriorRing[ringNum].put(exteriorRing[ringNum].get(0));
                }

                // Switch ringnum
                if (ringNum == 0) {
                    ringNum = 1;
                } else if (ringNum == 1) {
                    ringNum = 0;
                }

            } else if (lon2 > 0.0 && lon1 < 0.0) {

                if (Math.abs(lon2 - lon1) > 180.0) {
                    // Crossing DT heading west
                    if (debug) {
                        System.out.println("Crossing DT heading west");
                    }
                    crossedDTWest = true;

                    double x1 = (lon1 < 0) ? lon1 + 360.0 : lon1;
                    double x2 = (lon2 < 0) ? lon2 + 360.0 : lon2;
                    double crossing = gc.findCrossing(x1, lat1, x2, lat2, 180.0);
                    if (debug) {
                        System.out.println(crossing);
                    }

                    // Add point to current ring
                    coord = new JSONArray("[-180.0, " + crossing + "]");
                    exteriorRing[ringNum].put(coord);

                    if (crossedZeroWest) {
                        // South Pole in Footprint
                        coord = new JSONArray("[-180.0, -90.0]");
                        exteriorRing[ringNum].put(coord);
                        coord = new JSONArray("[0.0, -90.0]");
                        exteriorRing[ringNum].put(coord);
                    }

                    // Swith to other ring
                    if (ringNum == 0) {
                        ringNum = 1;
                    } else if (ringNum == 1) {
                        ringNum = 0;
                    }

                    if (crossedZeroWest) {
                        // South Pole in Footprint
                        coord = new JSONArray("[0.0, -90.0]");
                        exteriorRing[ringNum].put(coord);
                        coord = new JSONArray("[180, -90.0]");
                        exteriorRing[ringNum].put(coord);
                    }

                    // Add point to other ring
                    coord = new JSONArray("[180.0, " + crossing + "]");
                    exteriorRing[ringNum].put(coord);

                } else {
                    // or gone from -1.xx to 1.xx  (Crossing 0 heading east)
                    if (debug) {
                        System.out.println("Crossing 0 heading east");
                        System.out.println("lon1 = " + lon1);
                        System.out.println("lon2 = " + lon2);
                    }

                    crossedZeroEast = true;

                    // -1.xx to 1.00
                    double x1 = lon1;
                    double x2 = lon2;
                    double crossing = gc.findCrossing(x1, lat1, x2, lat2, 0.0);
                    if (debug) {
                        System.out.println(crossing);
                    }

                    // Add point to current ring
                    coord = new JSONArray("[0.0, " + crossing + "]");
                    exteriorRing[ringNum].put(coord);

                    if (crossedDTEast) {
                        // North Pole is in footprint add 180,90 and 0, 90 
                        coord = new JSONArray("[0.0,90.0]");
                        exteriorRing[ringNum].put(coord);
                        coord = new JSONArray("[-180.0,90.0]");
                        exteriorRing[ringNum].put(coord);
                    }

                    // Swith to other ring
                    if (ringNum == 0) {
                        ringNum = 1;
                    } else if (ringNum == 1) {
                        ringNum = 0;
                    }

                    if (crossedDTEast) {
                        // North pole is in foot print add point 0,90, -180,90  
                        coord = new JSONArray("[180.0,90.0]");
                        exteriorRing[ringNum].put(coord);
                        coord = new JSONArray("[0.0,90.0]");
                        exteriorRing[ringNum].put(coord);

                    }

                    // Add point to other ring
                    coord = new JSONArray("[0.0, " + crossing + "]");
                    exteriorRing[ringNum].put(coord);

                }

            } else if (lon2 < 0.0 && lon1 >= 0.0) {

                if (Math.abs(lon2 - lon1) > 180.0) {
                    // Either gone from 179.xx to -179.xx (Crossing DT heading west)                    
                    if (debug) {
                        System.out.println("Crossing DT heading east");
                    }
                    crossedDTEast = true;

                    double x1 = (lon1 < 0) ? lon1 + 360.0 : lon1;
                    double x2 = (lon2 < 0) ? lon2 + 360.0 : lon2;
                    double crossing = gc.findCrossing(x1, lat1, x2, lat2, 180.0);
                    if (debug) {
                        System.out.println(crossing);
                    }

                    coord = new JSONArray("[180.0, " + crossing + "]");
                    exteriorRing[ringNum].put(coord);

                    if (crossedZeroEast) {
                        // North Pole in Footprint
                        coord = new JSONArray("[180.0, 90.0]");
                        exteriorRing[ringNum].put(coord);
                        coord = new JSONArray("[0.0, 90.0]");
                        exteriorRing[ringNum].put(coord);
                    }

                    // Swith to other ring
                    if (ringNum == 0) {
                        ringNum = 1;
                    } else if (ringNum == 1) {
                        ringNum = 0;
                    }

                    if (crossedZeroEast) {
                        // North Pole in Footprint
                        coord = new JSONArray("[0.0, 90.0]");
                        exteriorRing[ringNum].put(coord);
                        coord = new JSONArray("[-180, 90.0]");
                        exteriorRing[ringNum].put(coord);
                    }

                    coord = new JSONArray("[-180.0, " + crossing + "]");
                    exteriorRing[ringNum].put(coord);

                } else {
                    if (debug) {
                        System.out.println("Crossing 0 heading west");
                    }
                    crossedZeroWest = true;

                    // or gone from 1.xx to -1.xx (Crossing 0 heading west 
                    double x1 = lon1;
                    double x2 = lon2;
                    double crossing = gc.findCrossing(x1, lat1, x2, lat2, 0.0);
                    if (debug) {
                        System.out.println(crossing);
                    }

                    // Add point to current ring
                    coord = new JSONArray("[0.0, " + crossing + "]");
                    exteriorRing[ringNum].put(coord);

                    if (crossedDTWest) {
                        // South Pole is in footprint 
                        coord = new JSONArray("[0.0,-90.0]");
                        exteriorRing[ringNum].put(coord);
                        coord = new JSONArray("[180.0,-90.0]");
                        exteriorRing[ringNum].put(coord);
                    }

                    // Swith to other ring
                    if (ringNum == 0) {
                        ringNum = 1;
                    } else if (ringNum == 1) {
                        ringNum = 0;
                    }

                    if (crossedDTWest) {
                        // North pole is in foot print add point 0,90, -180,90  
                        coord = new JSONArray("[-180.0,-90.0]");
                        exteriorRing[ringNum].put(coord);
                        coord = new JSONArray("[0.0,-90.0]");
                        exteriorRing[ringNum].put(coord);

                    }

                    // Add point to other ring
                    coord = new JSONArray("[0.0, " + crossing + "]");
                    exteriorRing[ringNum].put(coord);
                }
            } else if (lon1 == 0.0) {
                if (lon2 > 0.0) {

                } else {

                }
            }

            if (debug) {
                System.out.println(i + ":" + lon2 + "," + lat2);
            }

            if (i < coords.size() - 1) {
                // Inject last coord
                coord = new JSONArray("[" + lon2 + ", " + lat2 + "]");
                exteriorRing[ringNum].put(coord);
            }

            nc1 = nc2;
            lon1 = nc1.getLon();
            lat1 = nc1.getLat();
            i++;

        }

        JSONArray polys = new JSONArray();
        JSONArray poly;


        for (i = 0; i < 2; i++) {
            poly = new JSONArray();
            if (exteriorRing[i].length() > 0) {
                // Add last point same as first
                exteriorRing[i].put(exteriorRing[i].get(0));

                if (isClockwise) {
                    // Reverse order 
                    JSONArray reversedPoly = new JSONArray();
                    int j = exteriorRing[i].length() - 1;
                    while (j >= 0) {
                        reversedPoly.put(exteriorRing[i].get(j));
                        j--;
                    }
                    polys.put(reversedPoly);
                } else {
                    poly.put(exteriorRing[i]);
                    polys.put(poly);
                }
                
            }
        }

        if (debug) {
            int j = 0;
            while (j < 2) {
                System.out.println("exteriorRing[" + j + "]");
                i = 0;

                while (i < exteriorRing[j].length()) {
                    System.out.println(exteriorRing[j].get(i));
                    i++;
                }
                j++;
            }
        }
        // Create the Geom
        return polys;

    }

    public String createEsrijsonEllipse(int id, double lon, double lat, double a, double b, double rot, int numPoints) {

        Ellipse el = new Ellipse();

        // Clockwise Poly
        JSONArray polys;
        polys = el.createEllipse(lon, lat, a, b, rot, numPoints, true);

        JSONArray rngs = new JSONArray();
        rngs.put(polys.getJSONArray(0).getJSONArray(0));
        try {
            rngs.put(polys.getJSONArray(1).getJSONArray(0));
        } catch (Exception e) {
            // ok to ignore
        }
        JSONObject geom = new JSONObject();
        geom.put("rings", rngs);

        JSONObject esriJson = new JSONObject();
        esriJson.put("a", a);
        esriJson.put("b", b);
        esriJson.put("rot", rot);
        esriJson.put("clon", lon);
        esriJson.put("clat", lat);
        esriJson.put("geometry", geom);

        return esriJson.toString();
    }

    public String createWktEllipse(double lon, double lat, double a, double b, double rot, int numPoints) {
        
        
        JSONArray polys = createEllipse(lon, lat, a, b, rot, numPoints, false);
        
        int numPolys = polys.length();
        
        StringBuffer buf = new StringBuffer();
        
        if (numPolys == 1) {
            // Polygon
            buf.append("POLYGON ((");
            JSONArray ring = polys.getJSONArray(0).getJSONArray(0);
            int numPts = ring.length();
            //System.out.println("numPts:" + numPts);
            int cnt = 0;
            while (cnt < numPts) {
                JSONArray pt = ring.getJSONArray(cnt);
                //System.out.println(pt);
                buf.append(String.valueOf(pt.getDouble(0)) + " " + String.valueOf(pt.getDouble(1)));
                cnt++;
                if (cnt < numPts) {
                    buf.append(",");
                }                
            }            
            buf.append("))");
        } else {
            // Multipolygon
            buf.append("MULTIPOLYGON (");
            
            int cntPoly = 0;
            while (cntPoly < numPolys) {
                JSONArray ring = polys.getJSONArray(cntPoly).getJSONArray(0);
                int numPts = ring.length();                
                int cntPt = 0;
                buf.append("((");
                while (cntPt < numPts) {
                    JSONArray pt = ring.getJSONArray(cntPt);
                    //System.out.println(pt);
                    buf.append(String.valueOf(pt.getDouble(0)) + " " + String.valueOf(pt.getDouble(1)));
                    cntPt++;
                    if (cntPt < numPts) {
                        buf.append(",");
                    }                
                }
                buf.append("))");
                cntPoly++;
                if (cntPoly < numPolys) {
                    buf.append(",");
                }                               
            }
            
            buf.append(")");
        }
        System.out.println(numPolys);
        
        
        
        
        return buf.toString();
        
    }
    
    
    public String createGeoJsonEllipse(int id, double lon, double lat, double a, double b, double rot, int numPoints) {


        JSONObject featureCollection = new JSONObject();
        featureCollection.put("type", "FeatureCollection");

        JSONArray features = new JSONArray();

        JSONObject feature = new JSONObject();
        feature.put("type", "Feature");

        // Add properties (At least one is required)
        JSONObject properties = new JSONObject();
        properties.put("a", a);
        properties.put("b", b);
        properties.put("rot", rot);
        properties.put("clon", lon);
        properties.put("clat", lat);
        feature.put("properties", properties);

        // Get the coordinates
        JSONArray coords = createEllipse(lon, lat, a, b, rot, numPoints, false);

        JSONObject geom = new JSONObject();
        geom.put("type", "MultiPolygon");
        geom.put("coordinates", coords);

        feature.put("geometry", geom);

        features.put(feature);
        featureCollection.put("features", features);

        return featureCollection.toString();
    }
    


    public String createEllipse(int id, double lon, double lat, double a, double b, double rot, int numPoints, GeomType geomType) {
        switch (geomType) {
            case EsriJson:
                return createEllipse(lon, lat, a, b, rot, numPoints, true).toString();
            case GeoJson:      
                return createEllipse(lon, lat, a, b, rot, numPoints, false).toString();
            case Wkt:      
                return createWktEllipse(lon, lat, a, b, rot, numPoints);
        }
        return null;
        
    }
    
    public static void main(String args[]) {
        // GeoJSON Lint:  http://geojsonlint.com/
        // Wkt Lint: http://arthur-e.github.io/Wicket/sandbox-gmaps3.html
        
        // Need to fix so that EsriJson returns Geom correctly
        
        
        // Random polys in grids (e.g. landgrids.csv)
        // Polys in fixed pattern 1 meter circle every 1km 
        
        Ellipse t = new Ellipse();
        System.out.println(t.createEllipse(1, 0, 0, 400, 100, 45, 50, GeomType.GeoJson));
        System.out.println(t.createEllipse(1, 0, 0, 400, 100, 45, 50, GeomType.EsriJson));
        System.out.println(t.createEllipse(1, 0, 0, 400, 100, 45, 50, GeomType.Wkt));
        System.out.println();
        System.out.println(t.createEllipse(1, 10, 10, 400, 100, 45, 50, GeomType.GeoJson));
        System.out.println(t.createEllipse(1, 10, 10, 400, 100, 45, 50, GeomType.EsriJson));   
        System.out.println(t.createEllipse(1, 10, 10, 400, 100, 45, 50, GeomType.Wkt));
        
    }

}
