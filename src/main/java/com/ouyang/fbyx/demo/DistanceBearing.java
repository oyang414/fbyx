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
 * Java Bean for holding the distance (meters; must be less than half Earths circumference) 
 * and bearing (degrees from North; must be from -180 to 180)
 * 
 * 
 * 
 */
public class DistanceBearing {
    
    /** Creates a new instance of DistanceBearing */
    public DistanceBearing() {
    }

    public DistanceBearing(double distance, double bearing) {
        
        if (distance * 1000 > CONSTANTS.EARTH_RADIUS * Math.PI) {
            throw new NumberFormatException("distance must be less than half radius of Earth");
        }
        
        if (bearing > 180.0) {
            throw new NumberFormatException("bearing must be less than 180.0");
        }

        if (bearing < -180.0) {
            throw new NumberFormatException("bearing must be greater than -180.0");
        }
        
        this.distance = distance;
        this.bearing = bearing;
        this.bearingReverse = bearing > 0 ? bearing - 180.0 : bearing + 180;
    }

    
    
    /**
     * Holds value of property distance.
     */
    private double distance;

    /**
     * Getter for property distance.
     * @return Value of property distance.
     */
    public double getDistance() {
        return this.distance;
    }

    /**
     * Setter for property distance.
     * @param distance New value of property distance.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Holds value of property bearing.
     */
    private double bearing;

    /**
     * Getter for property bearing.
     * @return Value of property bearing.
     */
    public double getBearing() {
        return this.bearing;
    }

    /**
     * Setter for property bearing.
     * @param bearing New value of property bearing.
     */
    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    /**
     * Holds value of property bearing2to1.
     */
    private double bearingReverse;

    /**
     * Getter for property bearing2to1.
     * @return Value of property bearing2to1.
     */
    public double bearingReverse() {
        return this.bearingReverse;
    }
}
