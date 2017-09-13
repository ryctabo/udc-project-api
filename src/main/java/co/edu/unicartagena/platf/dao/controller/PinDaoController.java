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

package co.edu.unicartagena.platf.dao.controller;

import co.edu.unicartagena.platf.dao.EntityDao;
import co.edu.unicartagena.platf.dao.exception.NotCreatedEntityManagerException;
import co.edu.unicartagena.platf.entity.Pin;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public class PinDaoController extends EntityDao<Pin, Integer> implements PinDao {

    public PinDaoController() {
        super(Pin.class);
    }

    @Override
    public Pin findByCode(String code) throws NotCreatedEntityManagerException {
        List<Parameter> params = Arrays.asList(new Parameter("code", code));
        return executeNamedQuery("pin.findByCode", params);
    }

    @Override
    public Pin findByCodeAndEmail(String pinCode, String email)
            throws NotCreatedEntityManagerException {
        List<Parameter> params = Arrays.asList(new Parameter("code", pinCode),
                new Parameter("email", email));
        return executeNamedQuery("pin.findByCodeAndEmail", params);
    }

}
