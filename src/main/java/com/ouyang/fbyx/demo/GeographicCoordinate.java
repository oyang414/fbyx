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

/**
 *
 * @author david
 * 
 * Java Bean for Geographic Coordinate (lon from -180 to 180; lat from -90 to 90)
 * 
 */

public class GeographicCoordinate {
    
    /** Creates a new instance of GeographicCoordinate */
    public GeographicCoordinate() {
        this.lon = 0.0;
        this.lat = 0.0;
    }

    public GeographicCoordinate(double lon, double lat) {
        
        if (lon > 180.0) {
            throw new NumberFormatException("lon must be less than 180.0");
        }
        
        if (lon < -180.0) {
            throw new NumberFormatException("lon must be greater than -180.0");
        }
        
        if (lat > 90.0) {
            throw new NumberFormatException("lat must be less than 90.0");
        }

        if (lat < -90.0) {
            throw new NumberFormatException("lat must be greater than -90.0");
        }
        
        this.lon = lon;
        this.lat = lat;
    }

    

    /**
     * Holds value of property lon.
     */
    private double lon;

    /**
     * Getter for property lon.
     * @return Value of property lon.
     */
    public double getLon() {
        return this.lon;
    }

    /**
     * Setter for property lon.
     * @param lon New value of property lon.
     */
    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * Holds value of property lat.
     */
    private double lat;

    /**
     * Getter for property lat.
     * @return Value of property lat.
     */
    public double getLat() {
        return this.lat;
    }

    /**
     * Setter for property lat.
     * @param lat New value of property lat.
     */
    public void setLat(double lat) {
        this.lat = lat;
    }
    
    public String toString() { 
        return "[" + this.lon + "," + this.lat + "]";
    }
    
}
