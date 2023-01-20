package org.apache.hive.metastore.listener.beans;

public class OriginatorHelper {


    public static Originator getOriginator() {

        Originator originator = new Originator.Builder()
                .setMetadataCollectionId("d81b0efd-b2b9-45de-a755-cfd9ce482c65")
                .setServerName("danielIBM")
                .setServerType("Repository Proxy")
                .createOriginator();

        return originator;

    }
}
