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

import org.json.JSONObject;

import java.util.Random;

/**
 *
 * Generated 
 * 
 * @author david
 */
public class Attributes {
    

    /**
     * Generates a JSONObject with random attributes or field values
     * 
     * @param i Index Number
     * @param lon Longitude 
     * @param lat Latitude
     * @param size Size (e.g. Radius of the circle)
     * @return 
     */
    public JSONObject generateAttributes(int i, double lon, double lat, double size) {
        JSONObject properties = new JSONObject();

        properties.put("fid", i);
        properties.put("longitude", lon);
        properties.put("latitude", lat);
        properties.put("size", size);
        properties.put("rndfield1", generateRandomWords(8));
        properties.put("rndfield2", generateRandomWords(8));
        properties.put("rndfield3", generateRandomWords(8));
        properties.put("rndfield4", generateRandomWords(8));

        return properties;
    }

    public String generateRandomWords(int numchars) {
        Random random = new Random();
        char[] word = new char[numchars];
        for (int j = 0; j < word.length; j++) {
            word[j] = (char) ('a' + random.nextInt(26));
        }
        return new String(word);
    }
    
    public static void main(String args[]) {
        
        Attributes t = new Attributes();
        String str = t.generateAttributes(0, 0, 0, 0).toString();
        System.out.println(str);
        
    }
    
    
}
