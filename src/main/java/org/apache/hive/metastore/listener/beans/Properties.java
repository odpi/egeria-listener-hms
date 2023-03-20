/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.apache.hive.metastore.listener.beans;

import java.util.List;

public class Properties extends BaseEgeriaType {

    private Long deaderVersion;
    private InstanceProperties instanceProperties;
    private Long propertyCount;
    private List<String> propertyNames;

}
