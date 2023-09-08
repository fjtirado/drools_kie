/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.drools.commands;

import org.kie.api.command.ExecutableCommand;
import org.kie.api.runtime.Context;
import org.kie.api.runtime.RequestContext;


public class OutCommand<T> implements ExecutableCommand<T> {
    private String name;

    public OutCommand() {
    }

    public OutCommand(String name) {
        this.name = name;
    }

    @Override
    public T execute(Context context) {
        T returned = (T) ((RequestContext)context).getResult();

        String actualName;
        if ( this.name != null ) {
            actualName = this.name;
        } else {
            actualName = ((RequestContextImpl)context).getLastSet();
            if ( actualName == null ) {
                throw new RuntimeException("Name was null and there was no last set name either");
            }
        }

        ((RequestContext)context).setOutput(actualName, returned);

        return returned;
    }
}
