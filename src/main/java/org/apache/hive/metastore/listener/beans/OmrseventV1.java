/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.apache.hive.metastore.listener.beans;

public class OmrseventV1 extends BaseEgeriaType{

    private String eventCategory;
    private InstanceEventSection instanceEventSection;
    private Originator originator;
    private String protocolVersionId;
    private Long timestamp;
}
