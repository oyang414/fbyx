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

import java.text.DecimalFormat;

/**
 *
 * @author david
 * 
 * Some Earth Constants
 */
public final class CONSTANTS {
    
    public static final boolean debug = false;
    
    public static final double D2R = Math.PI / 180.0;
    public static final double R2D = 180.0 / Math.PI;    
    
    private final static DecimalFormat DF8 = new DecimalFormat("###0.00000000");
    private final static DecimalFormat DF5 = new DecimalFormat("###0.00000");
    private final static DecimalFormat DF3  = new DecimalFormat("###0.000");
    
    public static final double EARTH_RADIUS = 6371000.0;  //km
    public static final double EARTH_RADIUS_EQUITORIAL = 6378137.0;  //km
    public static final double EARTH_RADIUS_POLAR = 6356752.0;  //km
    
    public static final double EARTH_MASS = 5.9723E24;  //kg
}
