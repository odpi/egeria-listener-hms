package org.apache.hive.metastore.listener;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.IHMSHandler;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.events.CreateTableEvent;
import org.apache.hadoop.hive.metastore.events.DropTableEvent;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ListenerTesterApp {
    private HMSListener hmsListener;
    private Map<String, Table> tableNameTableMap = new HashMap<>();
    private String catName;
    private String dbName;
    public ListenerTesterApp() {
        Configuration config = new Configuration();

        config.set(HMSListener.CONFIG_METADATA_COLLECTION_ID,"1");
        config.set(HMSListener.CONFIG_SERVER_NAME, "aaa");
        config.set(HMSListener.CONFIG_QUALIFIEDNAME_PREFIX, "todo");
        config.set(HMSListener.CONFIG_ORGANISATION_NAME  ,"Coco");
        config.set(HMSListener.CONFIG_KAFKA_TOPIC_NAME,"topic1");
        config.set(HMSListener.CONFIG_KAFKA_BOOTSTRAP_SERVER_URL,"localhost:9092");
        config.set(HMSListener.CONFIG_KAFKA_CLIENT_ID, "1");
        config.set(HMSListener.CONFIG_ACTIVE_FLAG,"ON");

        hmsListener = new HMSListener(config);

    }
    public static void main(String[] args) throws MetaException {
        ListenerTesterApp app = new ListenerTesterApp();
        app.run();
    }
    public void run() throws MetaException {
        Scanner sc = new Scanner(System.in); //System.in is a standard input stream


        catName = validateName(sc, "Catalog");
        dbName = validateName(sc, "Database");
        boolean processing = true;
        while (processing) {
            System.out.print("");
            System.out.print("Enter the table operation - a to add, d to delete u to update, q to quit:\n ");

            String op = sc.nextLine();              //reads string
            if (op.equalsIgnoreCase("a")) {
                String tableName = null;
                boolean tableNãmeInvalid = true;
                while(tableNãmeInvalid) {
                    System.out.print("Enter the table name");
                    tableName = sc.nextLine();
                    if (tableName == null || tableName.equals("")) {
                        System.err.println("table name must be entered");
                    } else if (tableName.contains(" ")) {
                        System.err.println("table name cannot contain blanks");
                    } else {
                        tableNãmeInvalid = false;
                    }
                }

                Table addedTable = addTable(tableName);
                tableNameTableMap.put(addedTable.getTableName(), addedTable);

            } else if (op.equalsIgnoreCase("d")) {
                if (tableNameTableMap.isEmpty()) {
                    System.err.println("Nothing to delete");
                } else {
                    System.err.println("Choose which table to delete (copy and paste from the below list)");
                    for (String tableName : tableNameTableMap.keySet()) {
                        System.err.println(tableName);
                    }
                    String enteredTableName = sc.nextLine();
                    if (tableNameTableMap.keySet().contains(enteredTableName)) {
                        System.err.println("Press Y to delete table " + enteredTableName);
                        String tabledeleteconfirm = sc.nextLine();
                        if (tabledeleteconfirm.equalsIgnoreCase("y")) {
                            System.err.println("Dropping table " + enteredTableName);
                            dropTable(enteredTableName);
                        } else {
                            System.err.println("Aborting delete");
                        }
                    } else {
                        System.err.println("Unable to find the table name to delete");
                    }
                }
            } else if (op.equalsIgnoreCase("u")) {
                //TODO
            } else if (op.equalsIgnoreCase("q")) {
                processing = false;
            } else {
                System.out.print("Invalid Operation :" + op);
            }
        }
    }

    private static String validateName(Scanner sc, String artifiactName) {
        String artifiactValue = null;
        boolean nameInvalid = true;
        while(nameInvalid) {
            System.out.print("Enter the " + artifiactName +" name :\n ");
            artifiactValue = sc.nextLine();
            if (artifiactValue == null || artifiactValue.equals("")) {
                System.err.println(artifiactName + " name must be entered");
            } else if (artifiactValue.contains(" ")) {
                System.err.println(artifiactName + " name cannot contain blanks");
            } else {
                nameInvalid = false;
            }
        }
        return artifiactValue;
    }

    private Table addTable(String tableName) throws MetaException {
        Table table = new Table();

        table.setDbName(dbName);
        table.setCatName(catName);
        table.setTableName(tableName);
        StorageDescriptor sd = new StorageDescriptor();
        List<FieldSchema> cols = new ArrayList<>();
        for (int i=0; i<5;i++) {
            FieldSchema fs = new FieldSchema();
            fs.setName(tableName + "-col" + i);
            fs.setType("string");
            cols.add(fs);
        }
        sd.setCols(cols);
        table.setSd(sd);
        CreateTableEvent tableEvent = getCreateTableEvent(table);
        System.err.println("Adding a Table using event:");
        System.err.println(tableEvent);
        hmsListener.onCreateTable(tableEvent);
        return table;

    }
    private void dropTable(String tableName) throws MetaException {
        Table table = new Table();
        table.setDbName(dbName);
        table.setCatName(catName);
        table.setTableName(tableName);
        DropTableEvent tableEvent = getDropTableEvent(table);
        System.err.println("Dropping a Table using event:");
        System.err.println(tableEvent);
        hmsListener.onDropTable(tableEvent);
    }

    private static DropTableEvent getDropTableEvent(Table table) {
        DropTableEvent tableEvent = null;
        try {
            Class<?> dropTableEventclass = Class.forName("org.apache.hadoop.hive.metastore.events.DropTableEvent");
            Constructor constructor = null;

            try {
                constructor = dropTableEventclass.getDeclaredConstructor(Table.class, boolean.class, boolean.class, IHMSHandler.class);
                tableEvent = (DropTableEvent) constructor.newInstance(table, true, false,  null);
            } catch (NoSuchMethodException e) {
                    // we expect this if running against hms v3.1.3
            }

            if (constructor == null) {
                constructor = dropTableEventclass.getDeclaredConstructor(Table.class, boolean.class, IHMSHandler.class);
                tableEvent = (DropTableEvent) constructor.newInstance(table, true, null);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return tableEvent;
    }

    /**
     * Unfortunately HMS 3.1.3 and HMS 4.0.0 alpha 2 have different constructor parameters for CreateTableEvent, this method uses reflection
     * to try each constructor shape.
     * @param table the table to put in the CreateTableEvent
     * @return CreateTableEvent
     */
    private static CreateTableEvent getCreateTableEvent(Table table) {
        CreateTableEvent tableEvent = null;
        try {
            Class<?> createTableEventclass = Class.forName("org.apache.hadoop.hive.metastore.events.CreateTableEvent");
            Constructor constructor = null;
            try {
                constructor = createTableEventclass.getDeclaredConstructor(Table.class, boolean.class, IHMSHandler.class, boolean.class);
                tableEvent = (CreateTableEvent) constructor.newInstance(table, true, null, false);
            } catch (NoSuchMethodException e) {
                // we expect this if running against hms v3.1.3
            }

            if (constructor == null) {
                constructor = createTableEventclass.getDeclaredConstructor(Table.class, boolean.class, IHMSHandler.class);
                tableEvent = (CreateTableEvent) constructor.newInstance(table, true, null);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return tableEvent;
    }
}
