/*
 * Copyright 2016 Gustavo Pacheco <ryctabo@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.edu.unicartagena.platf.model;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0
 */
public class PasswordGenerator {

    private PasswordGenerator() {}

    private static final String NUMBERS = "0123456789";
    
    private static final String MAYUS = "ABCDEFGHIJKLMNOPQRSTUWXYZ";
    
    private static final String MINUS = "abcdefghijklmnopqrstuwxyz";
    
    public static String getPassword() {
        return getKey(MAYUS + NUMBERS + MINUS ,(int) (3 * Math.random() + 8));
    }
    
    public static String getPassword(int length) {
        return getKey(MAYUS + NUMBERS + MINUS , length);
    }
    
    public static String getKey(String sample, int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++)
            builder.append(sample.charAt((int) (Math.random() * sample.length())));
        return builder.toString();
    }
    
}
