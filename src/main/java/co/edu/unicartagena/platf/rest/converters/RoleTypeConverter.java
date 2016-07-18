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

package co.edu.unicartagena.platf.rest.converters;

import co.edu.unicartagena.platf.entity.RoleType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
@Provider
public class RoleTypeConverter implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType,
            Type genericType, Annotation[] annotations) {
        if (rawType.getName().equals(RoleType.class.getName())) {
            return new ParamConverter<T>() {
                @Override
                public T fromString(String string) {
                    switch (string) {
                        case "ADMINISTRADOR":
                            return rawType.cast(RoleType.ADMINISTRATOR);
                        case "DECANATURA":
                            return rawType.cast(RoleType.DEANCHIP);
                        case "FACULTAD":
                            return rawType.cast(RoleType.FACULTY);
                        case "PROGRAMA":
                            return rawType.cast(RoleType.PROGRAM);
                        default:
                            throw new AssertionError();
                    }
                }

                @Override
                public String toString(T t) {
                    return t == null ? null : t.toString();
                }
            };
        }
        return null;
    }

}
