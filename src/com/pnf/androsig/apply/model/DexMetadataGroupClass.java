/*
 * JEB Copyright PNF Software, Inc.
 * 
 *     https://www.pnfsoftware.com
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
 */

package com.pnf.androsig.apply.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.pnfsoftware.jeb.core.output.AddressConversionPrecision;
import com.pnfsoftware.jeb.core.units.MetadataGroup;
import com.pnfsoftware.jeb.core.units.MetadataGroupType;

/**
 * Metadata group with loose matching on Java FQ-name.
 * 
 * @author Ruoxiao Wang
 *
 */
public class DexMetadataGroupClass extends MetadataGroup {

    private Map<String, Object> cmap = new HashMap<>();
    private StructureInfo struInfo = new StructureInfo();

    public DexMetadataGroupClass(String name, MetadataGroupType type, StructureInfo struInfo) {
        super(name, type);
        this.struInfo = struInfo;
    }

    /**
     * Get a read-only map of all the key-value pairs of metadata items contained in this group. Not
     * all groups may be able to provide this functionality. If so, this method should return null.
     * 
     * @return a map of key-value pairs, possibly empty; if the operation is not supported, null is
     *         returned
     */
    @Override
    public Map<String, Object> getAllData() {
        return Collections.unmodifiableMap(cmap);
    }

    /**
     * Get the piece of metadata associated with the provided address.
     * 
     * @param address address
     * @param precision
     * @return the data at address, null if none
     */
    @Override
    public Object getData(String address, AddressConversionPrecision precision) {
        if(address == null) {
            return null;
        }
        int pos = address.indexOf("->");
        if(pos < 0) {
            return null;
        }
        String classPath = address.substring(0, pos);
        String newClassPath = struInfo.getMatchedClasses_new_orgPath().get(classPath);
        if(newClassPath == null) {
            return null;
        }
        return cmap.get(newClassPath);
    }

    /**
     * Set the piece of metadata associated with the given address.
     * 
     * @param classAddress class address
     * @param data metadata
     * @return true if the operation succeeded; false otherwise (eg, the operation is not supported)
     */
    @Override
    public boolean setData(String classAddress, Object data) {
        cmap.put(classAddress, data);
        return true;
    }
}