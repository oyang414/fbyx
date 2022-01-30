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

import java.math.BigDecimal;

/**
 *
 * @author david
 *
 * The Great Circle is the shortest path from two points on a sphere (e.g.
 * Earth)
 *
 * The calculations implement
 *
 */
public class GreatCircle {

    static final boolean debug = CONSTANTS.debug;

    /**
     *
     * @param coord1
     * @param coord2
     * @return
     */
    public DistanceBearing getDistanceBearing(GeographicCoordinate coord1, GeographicCoordinate coord2) {

        double lon1 = coord1.getLon();
        double lat1 = coord1.getLat();

        double lon2 = coord2.getLon();
        double lat2 = coord2.getLat();
        return getDistanceBearing(lon1, lat1, lon2, lat2);
    }

    /**
     *
     * Given two geographic points find the distance and bearing between the
     * points.
     *
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return
     */
    public DistanceBearing getDistanceBearing(double lon1, double lat1, double lon2, double lat2) {

        double gcDist;
        double bearing;

        double lon1R = lon1 * CONSTANTS.D2R;
        double lat1R = lat1 * CONSTANTS.D2R;
        double lon2R = lon2 * CONSTANTS.D2R;
        double lat2R = lat2 * CONSTANTS.D2R;

        /*
            Functions are a little whacky around the north and south pole.
            The only valid bearing from north pole is -180.
            I wouldn't trust the program for points near the poles.
         */
        if (Math.abs(lat1 - 90.0) < 0.00001) {
            // very close to north pole distance R * theta            
            double l = 90.0 - lat2;
            gcDist = CONSTANTS.EARTH_RADIUS * l * CONSTANTS.D2R / 1000.0;
            // let bearing in lon2
            bearing = lon2;
        } else if (Math.abs(lat1 + 90) < 0.00001) {
            // very close to south pole distance R * theta
            double l = lat2 + 90.0;
            gcDist = CONSTANTS.EARTH_RADIUS * l * CONSTANTS.D2R / 1000.0;
            bearing = lon2;

        } else {

            // law of Cosines
            double lambda = Math.abs(lon2R - lon1R);

            double x1 = Math.cos(lat2R) * Math.sin(lambda);

            double x2 = Math.cos(lat1R) * Math.sin(lat2R)
                    - Math.sin(lat1R) * Math.cos(lat2R) * Math.cos(lambda);

            double x3 = Math.sin(lat1R) * Math.sin(lat2R)
                    + Math.cos(lat1R) * Math.cos(lat2R) * Math.cos(lambda);

            double x4 = Math.sqrt(x1 * x1 + x2 * x2);

            double sigma = Math.atan2(x4, x3);

            gcDist = sigma * CONSTANTS.EARTH_RADIUS / 1000.0;

            double y1 = Math.sin(lon2R - lon1R) * Math.cos(lat2R);

            double y2 = Math.cos(lat1R) * Math.sin(lat2R)
                    - Math.sin(lat1R) * Math.cos(lat2R) * Math.cos(lon2R - lon1R);

            double y3 = Math.atan2(y1, y2);

            bearing = (y3 * CONSTANTS.R2D) % 360;

            // Convert to value from -180 to 180
            bearing = bearing > 180.0 ? bearing - 360.0 : bearing;

        }

        DistanceBearing distB = new DistanceBearing(gcDist, bearing);

        return distB;
    }

    /**
     *
     * Given a point and a distance and bearing find a new point.
     *
     * @param lon1
     * @param lat1
     * @param distance
     * @param bearing
     * @return
     */
    public GeographicCoordinate getNewCoordPair(double lon1, double lat1, double distance, double bearing) {
        DistanceBearing distB = new DistanceBearing(distance, bearing);
        GeographicCoordinate coord1 = new GeographicCoordinate(lon1, lat1);
        return getNewCoordPair(coord1, distB);
    }

    /**
     *
     * @param coord1
     * @param distB
     * @return
     */
    public GeographicCoordinate getNewCoordPair(GeographicCoordinate coord1, DistanceBearing distB) {

        double lat1 = coord1.getLat();
        double lon1 = coord1.getLon();
        double lat2 = 0.0;
        double lon2 = 0.0;

        boolean bln360 = false;

        // Allow for lon values 180 to 360 (adjust them to -180 to 0)
        double lonDD = lon1;
        if (lonDD > 180.0 && lonDD <= 360) {
            lonDD = lonDD - 360;
            lon1 = lonDD;
            bln360 = true;
        }

        double alpha;
        double l;
        double k;
        double gamma;
        double phi;
        //double theta;
        //double hdng2;

        double hdng = distB.getBearing();

        if (hdng < 0) {
            hdng = hdng + 360;
        }

        // Round the input            
        BigDecimal bd = new BigDecimal(hdng);
        bd = bd.setScale(6, BigDecimal.ROUND_HALF_UP);
        hdng = bd.doubleValue();

        double dist = distB.getDistance() * 1000;

        if (lat1 == 90 || lat1 == -90) {
            // hdng doesn't make a lot of since at the poles assume this is just the lon
            lon2 = hdng;
            alpha = dist / CONSTANTS.EARTH_RADIUS;
            if (lat1 == 90) {
                lat2 = 90 - alpha * CONSTANTS.R2D;
            } else {
                lat2 = -90 + alpha * CONSTANTS.R2D;
            }

        } else if (hdng == 0 || hdng == 360) {
            // going due north within some rounded number
            alpha = dist / CONSTANTS.EARTH_RADIUS;
            lat2 = lat1 + alpha * CONSTANTS.R2D;
            lon2 = lon1;
        } else if (hdng == 180) {
            // going due south witin some rounded number
            alpha = dist / CONSTANTS.EARTH_RADIUS;
            lat2 = lat1 - alpha * CONSTANTS.R2D;
            lon2 = lon1;
        } else if (hdng == 90) {
            lat2 = lat1;
            l = 90 - lat1;
            alpha = dist / CONSTANTS.EARTH_RADIUS / Math.sin(l * CONSTANTS.D2R);
            //phi = Math.asin(Math.sin(alpha)/ Math.sin(l*D2R));                 
            lon2 = lon1 + alpha * CONSTANTS.R2D;
        } else if (hdng == 270) {
            lat2 = lat1;
            l = 90 - lat1;
            alpha = dist / CONSTANTS.EARTH_RADIUS / Math.sin(l * CONSTANTS.D2R);
            //phi = Math.asin(Math.sin(alpha)/ Math.sin(l*D2R));                       
            lon2 = lon1 - alpha * CONSTANTS.R2D;
        } else if (hdng > 0 && hdng < 180) {
            l = 90 - lat1;
            alpha = dist / CONSTANTS.EARTH_RADIUS;
            k = Math.acos(Math.cos(alpha) * Math.cos(l * CONSTANTS.D2R)
                    + Math.sin(alpha) * Math.sin(l * CONSTANTS.D2R) * Math.cos(hdng * CONSTANTS.D2R));
            lat2 = 90 - k * CONSTANTS.R2D;
            double n = (Math.cos(alpha) - Math.cos(k) * Math.cos(l * CONSTANTS.D2R))
                    / (Math.sin(k) * Math.sin(l * CONSTANTS.D2R));
            if (n > 1) {
                n = 1;
            }
            if (n < -1) {
                n = -1;
            }
            phi = Math.acos(n);
            lon2 = lon1 + phi * CONSTANTS.R2D;
            //theta = Math.sin(phi) * Math.sin(l * CONSTANTS.D2R) / Math.sin(alpha);
            //hdng2 = 180 - theta * CONSTANTS.R2D;
        } else if (hdng > 180 && hdng < 360) {
            gamma = 360 - hdng;
            l = 90 - lat1;
            alpha = dist / CONSTANTS.EARTH_RADIUS;
            k = Math.acos(Math.cos(alpha) * Math.cos(l * CONSTANTS.D2R)
                    + Math.sin(alpha) * Math.sin(l * CONSTANTS.D2R) * Math.cos(gamma * CONSTANTS.D2R));
            lat2 = 90 - k * CONSTANTS.R2D;
            double n = (Math.cos(alpha) - Math.cos(k) * Math.cos(l * CONSTANTS.D2R))
                    / (Math.sin(k) * Math.sin(l * CONSTANTS.D2R));
            if (n > 1) {
                n = 1;
            }
            if (n < -1) {
                n = -1;
            }
            phi = Math.acos(n);
            lon2 = lon1 - phi * CONSTANTS.R2D;
            //theta = Math.sin(phi) * Math.sin(l * CONSTANTS.D2R) / Math.sin(alpha);
            //hdng2 = 180 - theta * CONSTANTS.R2D;
        }

        int decimalPlaces = 12;
        bd = new BigDecimal(lat2);
        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        lat2 = bd.doubleValue();

        bd = new BigDecimal(lon2);
        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        lon2 = bd.doubleValue();

        if (lat2 > 90) {
            lat2 = 180 - lat2;
            lon2 = (lon2 + 180) % 360;
        }

        if (lon2 > 180) {
            lon2 = lon2 - 360;
        }

        if (lat2 < -90) {
            lat2 = -180 - lat2;
            lon2 = (lon2 - 180) % 360;
        }
        if (lon2 < -180) {
            lon2 = lon2 + 360;
        }

        // adjust the lon back to 360 scale if input was like that
        if (bln360) {
            if (lon2 < 0) {
                lon2 = lon2 + 360;
            }
        }

        if (lon2 < -180.0) {
            System.out.println(lon2);
            System.out.println(distB.getBearing());

        }

        GeographicCoordinate nc = new GeographicCoordinate(lon2, lat2);

        return nc;
    }

    /**
     *
     * Given two points on a line find the slope; then given a value x return y
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x
     * @return
     */
    public double findCrossing(double x1, double y1, double x2, double y2, double x) {
        double m = (y2 - y1) / (x2 - x1);

        return m * (x - x1) + y1;
    }

    public static void main(String[] args) {

        // Example Command Line args: localhost 5565 faa-stream.csv 1000 10000
        int numargs = args.length;
        if (numargs != 5) {
            // append append time option was added to support end-to-end latency; I used it for Trinity testing
            System.err.println("Usage: GreatCircle newpt <lon> <lat> <dist> <bearing>");
            System.err.println("Usage: GreatCircle distb <lon1> <lat1> <lon2> <lat2>");
        } else {

            GreatCircle gc = new GreatCircle();
            String req = args[0];

            if (req.equalsIgnoreCase("newpt")) {
                double lon = Double.parseDouble(args[1]);
                double lat = Double.parseDouble(args[2]);
                double dist = Double.parseDouble(args[3]);
                double bear = Double.parseDouble(args[4]);

                GeographicCoordinate pt = gc.getNewCoordPair(lon, lat, dist, bear);
                System.out.println(String.format("%.5f", pt.getLon()) + "," + String.format("%.5f", pt.getLat()));

            } else if (req.equalsIgnoreCase("distb")) {
                double lon1 = Double.parseDouble(args[1]);
                double lat1 = Double.parseDouble(args[2]);
                double lon2 = Double.parseDouble(args[3]);
                double lat2 = Double.parseDouble(args[4]);

                GeographicCoordinate pt1 = new GeographicCoordinate(lon1, lat1);
                GeographicCoordinate pt2 = new GeographicCoordinate(lon2, lat2);

                DistanceBearing db = gc.getDistanceBearing(pt1, pt2);
                System.out.println(String.format("%.0f", db.getDistance()) + "," + String.format("%.2f", db.getBearing()));

            } else {
                System.out.println("First parameter must be newpt or distb");
            }

        }
    }

}
