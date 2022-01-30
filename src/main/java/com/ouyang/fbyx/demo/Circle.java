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

/**
 * Uses DistanceBearing to create points around a center (A circle)
 *
 *
 * @author david
 */
public class Circle {

    final static boolean debug = CONSTANTS.debug;

    /**
     * Return geometry object rings for Esri or coordinates for GeoJSON
     *
     * @param clon Center Longitude
     * @param clat Center Latitude
     * @param radiusKM Radius in Kilometers
     * @param numPoints Number of Points
     * @param isClockwise Use true for Clockwise Points (Esri) or false for GeoJSON
     *
     * @return
     */
    public JSONArray createCircle(double clon, double clat, Double radiusKM, Integer numPoints, boolean isClockwise) {

        GreatCircle gc = new GreatCircle();

        if (numPoints == null) {
            numPoints = 20;
        }

        if (radiusKM == null) {
            // default to 50 meters or 0.050 km
            radiusKM = 0.050;
        }

        // d = Number of degrees between points 
        double d = 360.0 / (numPoints - 1);

        // Array of coordinates
        GeographicCoordinate coords[] = new GeographicCoordinate[numPoints];

        // Center Coordinate
        GeographicCoordinate coord1 = new GeographicCoordinate(clon, clat);
        
        int i = 0;
        while (i < numPoints - 1) {
            // Starts at 180 and finds points to -180 degrees (counterclockwise)
            
            double v;
            v = 180.0 - i * d;

            DistanceBearing distb = new DistanceBearing(radiusKM, v);

            GeographicCoordinate nc = gc.getNewCoordPair(coord1, distb);

            coords[i] = nc;
            i++;

        }

        // Last point same as first
        coords[numPoints - 1] = coords[0]; 

        GeographicCoordinate nc1;
        GeographicCoordinate nc2;

        // Creating Array of exterior rings and checking for 0 and/or 180 degree crossings
        
        JSONArray[] exteriorRing = new JSONArray[2];
        exteriorRing[0] = new JSONArray();
        exteriorRing[1] = new JSONArray();

        int ringNum = 0;

        // Add first point to exteriorRing
        nc1 = coords[0];
        i = 0;
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
        
        while (i < numPoints) {
            // Loop through remaining points
            
            nc2 = coords[i];
            // Compare coordinates
            double lon2 = nc2.getLon();
            double lat2 = nc2.getLat();

            if (lon2 >= 0.0 && lon1 <= 0.0) {
                // Crossed 180 or Crossed 0

                if (Math.abs(lon2 - lon1) > 180.0) {
                    // Crossing DT heading west 
                    if (debug) {
                        System.out.println("Crossing DT heading west");
                    }
                    crossedDTWest = true;

                    double x1 = (lon1 < 0) ? lon1 + 360.0 : lon1;
                    double x2 = (lon2 < 0) ? lon2 + 360.0 : lon2;
                    
                    // Find approximate latitude of the crossing
                    double crossing = gc.findCrossing(x1, lat1, x2, lat2, 180.0);
                    if (debug) {
                        System.out.println(crossing);
                    }
                    
                    // Add point to current ring
                    coord = new JSONArray("[-180.0, " + crossing + "]");
                    exteriorRing[ringNum].put(coord);

                    if (crossedZeroWest) {                        
                        // If already crossed Zero going West -> South Pole in Footprint
                        coord = new JSONArray("[-180.0, -90.0]");
                        exteriorRing[ringNum].put(coord);
                        coord = new JSONArray("[0.0, -90.0]");
                        exteriorRing[ringNum].put(coord);
                    }

                    // Swith ExteriorRing Pointer
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
                    // Crossing 0 heading east
                    if (debug) {
                        System.out.println("Crossing 0 heading east");
                    }

                    // Find the crossing latitude
                    double x1 = lon1;
                    double x2 = lon2;
                    double crossing = gc.findCrossing(x1, lat1, x2, lat2, 0.0);
                    if (debug) {
                        System.out.println(crossing);
                    }

                    // Add point to current ring
                    if (lon2 > 0.0) {
                        coord = new JSONArray("[0.0, " + crossing + "]");
                        exteriorRing[ringNum].put(coord);
                    }

                    if (crossedDTEast) {                        
                        // Already crossed DT East -> North Pole is in footprint add 180,90 and 0, 90 
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
                    if (lon2 > 0.0) {
                        coord = new JSONArray("[0.0, " + crossing + "]");
                        exteriorRing[ringNum].put(coord);
                    }

                    crossedZeroEast = true;
                }

            } else if (lon2 < 0.0 && lon1 > 0.0) {
                // Crossed 180 or 0

                if (Math.abs(lon2 - lon1) > 180.0) {
                    // Crossing DT heading east                    
                    if (debug) {
                        System.out.println("Crossing DT heading east");
                    }
                    crossedDTEast = true;

                    // Find the crossing
                    double x1 = (lon1 < 0) ? lon1 + 360.0 : lon1;
                    double x2 = (lon2 < 0) ? lon2 + 360.0 : lon2;
                    double crossing = gc.findCrossing(x1, lat1, x2, lat2, 180.0);
                    if (debug) {
                        System.out.println(crossing);
                    }

                    // Add point 
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
                    // Zero Crossing West
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
            }

            if (debug) {
                System.out.println(i + ":" + lon2 + "," + lat2);
            }

            if (i < numPoints - 1) {
                // Add point (skipping the last point)
                coord = new JSONArray("[" + lon2 + ", " + lat2 + "]");
                exteriorRing[ringNum].put(coord);
            }

            // Go on to next point
            nc1 = nc2;
            lon1 = nc1.getLon();
            lat1 = nc1.getLat();
            i++;

        }

        // Create the polygons (reversing coordinates if needed)
        JSONArray polys = new JSONArray();
        JSONArray poly;

        for (i = 0; i < 2; i++) {
            poly = new JSONArray();
            if (exteriorRing[i].length() > 0) {
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
            System.out.println("HERE1");
            i = 0;
            while (i < exteriorRing[0].length()) {
                System.out.println(exteriorRing[0].get(i));
                i++;
            }

            System.out.println("HERE2");
            i = 0;
            while (i < exteriorRing[1].length()) {
                System.out.println(exteriorRing[1].get(i));
                i++;
            }

        }
        //System.out.println(poly);
        // Create the Geom
        return polys;

    }

    public String createWktCircle(double lon, double lat, double radius, int numPoints) {
        
        
        JSONArray polys = createCircle(lon, lat, radius, numPoints, false);
        
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
        if (debug) System.out.println(numPolys);
                
        
        return buf.toString();
        
    }    
    


    public void createEsriTest(double lon, double lat, double size, int numPoints) {
//        GreatCircle gc = new GreatCircle();
        Attributes at = new Attributes();

        JSONArray features = new JSONArray();

        JSONObject feature = new JSONObject();

        // Add properties (At least one is required)
        JSONObject fields = at.generateAttributes(1, lon, lat, size);
        feature.put("feature", fields);

        // Get the coordinates
        JSONArray coords = createCircle(lon, lat, size, numPoints, true);

        JSONObject geom = new JSONObject();
        geom.put("coordinates", coords);

        feature.put("geometry", geom);

        features.put(feature);

        JSONObject json = new JSONObject();
        json.put("features", features);
        
        System.out.println(json.toString());

    }

    public void createGeojsonTest(double lon, double lat, double size, int numPoints) {
//        GreatCircle gc = new GreatCircle();
        Attributes at = new Attributes();

        JSONObject featureCollection = new JSONObject();
        featureCollection.put("type", "FeatureCollection");

        JSONArray features = new JSONArray();

        JSONObject feature = new JSONObject();
        feature.put("type", "Feature");

        // Add properties (At least one is required)
        JSONObject properties = at.generateAttributes(1, lon, lat, size);
        feature.put("properties", properties);

        // Get the coordinates
        JSONArray coords = createCircle(lon, lat, size, numPoints, false);

        JSONObject geom = new JSONObject();
        geom.put("type", "MultiPolygon");
        geom.put("coordinates", coords);

        feature.put("geometry", geom);

        features.put(feature);
        featureCollection.put("features", features);

        System.out.println(featureCollection.toString());

    }

    public String createCircle(double lon, double lat, double radius, int numPoints, GeomType geomType) {
        switch (geomType) {
            case EsriJson:
                return createCircle(lon, lat, radius, numPoints, true).toString();
            case GeoJson:      
                return createCircle(lon, lat, radius, numPoints, false).toString();
            case Wkt:      
                return createWktCircle(lon, lat, radius, numPoints);
        }
        return null;
        
    }    
    
    public static void main(String args[]) {
        Circle t = new Circle();
        System.out.println(t.createCircle(0, 0, 400, 50, GeomType.GeoJson));
        System.out.println(t.createCircle(0, 0, 400, 50, GeomType.EsriJson));
        System.out.println(t.createCircle(0, 0, 400, 50, GeomType.Wkt));
        
        System.out.println();
        System.out.println(t.createCircle(10, 10, 400, 50, GeomType.GeoJson));
        System.out.println(t.createCircle(10, 10, 400, 50, GeomType.EsriJson));
        System.out.println(t.createCircle(10, 10, 400, 50, GeomType.Wkt));      
        
        //t.createGeojsonTest(0, 0, 300, 100);
        //t.createEsriTest(0, 0, 300, 100);
    }
    
    
}
