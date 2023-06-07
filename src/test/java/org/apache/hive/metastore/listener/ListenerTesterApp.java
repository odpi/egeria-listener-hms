package org.apache.hive.metastore.listener;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.IHMSHandler;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.events.AlterTableEvent;
import org.apache.hadoop.hive.metastore.events.CreateTableEvent;
import org.apache.hadoop.hive.metastore.events.DropTableEvent;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ListenerTesterApp {
    private HMSListener hmsListener;
    private Map<String, Table> tableNameTableMap = new HashMap<>();
    private String catName;
    private String dbName;
    public ListenerTesterApp() {
        Configuration config = new Configuration();
        config.set(HMSListener.CONFIG_METADATA_COLLECTION_ID,"TODO");
        config.set(HMSListener.CONFIG_SERVER_NAME, "TODO");
        config.set(HMSListener.CONFIG_QUALIFIEDNAME_PREFIX, "TODO");
        
        // config.set(HMSListener.CONFIG_ORGANISATION_NAME  ,"Coco");
        config.set(HMSListener.CONFIG_KAFKA_TOPIC_NAME,"egeriaTopics.openmetadata.repositoryservices.cohort.myCohort2.OMRSTopic.instances");
        config.set(HMSListener.CONFIG_KAFKA_BOOTSTRAP_SERVER_URL,"localhost:9092");
        config.set(HMSListener.CONFIG_KAFKA_CLIENT_ID, "4");
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
            System.out.println("");
            System.out.println("Enter the table operation - a to add, d to delete u to update, q to quit:\n ");

            String op = sc.nextLine();              //reads string
            if (op.equalsIgnoreCase("a")) {
                String tableName = null;
                boolean tableNãmeInvalid = true;
                while(tableNãmeInvalid) {
                    System.out.println("Enter the table name");
                    tableName = sc.nextLine();
                    if (tableName == null || tableName.equals("")) {
                        System.out.println("table name must be entered");
                    } else if (tableName.contains(" ")) {
                        System.out.println("table name cannot contain blanks");
                    } else {
                        tableNãmeInvalid = false;
                    }
                }

                Table addedTable = addTable(tableName);
                tableNameTableMap.put(addedTable.getTableName(), addedTable);

            } else if (op.equalsIgnoreCase("d")) {
                if (tableNameTableMap.isEmpty()) {
                    System.out.println("Nothing to delete");
                } else {
                    System.out.println("Choose which table to delete - below are the tables we have created)");
                    for (String tableName : tableNameTableMap.keySet()) {
                        System.out.println(tableName);
                    }


                        String enteredTableName = null;
                        boolean tableNameInvalid = true;
                        while(tableNameInvalid) {
                            System.out.println("Enter the table name to delete");
                            enteredTableName = sc.nextLine();
                            if (enteredTableName == null || enteredTableName.length() == 0) {
                                System.out.println("Table name must be entered");
                            } else if (enteredTableName.contains(" ")) {
                                System.out.println("Table name cannot contain blanks");
                            } else {
                                tableNameInvalid = false;
                            }
                        }
                    System.out.println("Press Y to delete table " + enteredTableName);
                    String tableDeleteConfirm = sc.nextLine();
                    if (tableDeleteConfirm.equalsIgnoreCase("y")) {
                        dropTable(enteredTableName);
                    } else {
                        System.out.println("Aborting delete");
                    }
                }
            } else if (op.equalsIgnoreCase("u")) {
                System.out.println("Choose which table to update - below are the tables we have created)");
                for (String tableName : tableNameTableMap.keySet()) {
                    System.out.println(tableName);
                }
                String enteredTableName = sc.nextLine();

                System.out.println("");
                System.out.println("Existing table has columns:");
                Table oldTable = tableNameTableMap.get(enteredTableName);
                Iterator<FieldSchema>  colsIterator = oldTable.getSd().getColsIterator();
                while(colsIterator.hasNext()) {
                    FieldSchema fieldSchema =colsIterator.next();
                    System.out.println("Name:"+fieldSchema.getName()+",type:"+ fieldSchema.getType() );
                }

                System.out.println("Enter a column operation - a to add, d to delete u to update, q to quit: ");

                String colOp = sc.nextLine();              //reads string
                if (colOp.equalsIgnoreCase("a")) {
                    String colName = null;
                    boolean colNameInvalid = true;
                    while(colNameInvalid) {
                        System.out.println("Enter the new column name");
                        colName = sc.nextLine();
                        if (colName == null || colName.length() == 0) {
                            System.out.println("Column name must be entered");
                        } else if (colName.contains(" ")) {
                            System.out.println("Column name cannot contain blanks");
                        } else {
                            colNameInvalid = false;
                        }
                    }

                    // TODO check if already a column name
                    Table newTable = oldTable.deepCopy();
                    newTable.getSd().addToCols(new FieldSchema(colName, "String", null));

                    AlterTableEvent alterTableEvent = getAlterTableEvent(oldTable, newTable);
                    tableNameTableMap.put(enteredTableName, newTable);
                    hmsListener.onAlterTable(alterTableEvent);


                } else if (colOp.equalsIgnoreCase("d")) {
                    String colNameToDelete = null;
                    boolean colNameInvalid = true;
                    while(colNameInvalid) {
                        System.out.println("Enter the column name to delete");
                        colNameToDelete = sc.nextLine();
                        if (colNameToDelete == null || colNameToDelete.length() == 0) {
                            System.out.println("Column name to delete must be entered");
                        } else if (colNameToDelete.contains(" ")) {
                            System.out.println("Column name to delete cannot contain blanks");
                        } else {
                            colNameInvalid = false;
                        }
                    }
                    Table newTable = oldTable.deepCopy();
                    List<FieldSchema> newCols = newTable.getSd().getCols();
                    final String colName = colNameToDelete;
                    // the col name may not be in the new table - tht case nothing will be removed.
                    newCols = newCols.stream()
                            .filter(x -> !x.getName().equals(colName))
                            .collect(Collectors.toList());

                    newTable.getSd().setCols(newCols);
                    if (oldTable.getSd().getCols().size() !=newCols.size()) {
                        AlterTableEvent alterTableEvent = getAlterTableEvent(oldTable, newTable);
                        tableNameTableMap.put(enteredTableName, newTable);
                        hmsListener.onAlterTable(alterTableEvent);
                    }
                } else if (colOp.equalsIgnoreCase("u")) {
                    String colNameToUpdate = null;
                    boolean colNameInvalid = true;
                    while(colNameInvalid) {
                        System.out.println("Enter the column name to update");
                        colNameToUpdate = sc.nextLine();
                        if (colNameToUpdate == null || colNameToUpdate.length() == 0) {
                            System.out.println("Column name to update must be entered");
                        } else if (colNameToUpdate.contains(" ")) {
                            System.out.println("Column name to update cannot contain blanks");
                        } else {
                            colNameInvalid = false;
                        }
                    }
                    String newTypeName = null;
                    boolean typeNameInvalid = true;
                    while(typeNameInvalid) {
                        System.out.println("Enter the type name to update");
                        newTypeName = sc.nextLine();
                        if (newTypeName == null || newTypeName.length() == 0) {
                            System.out.println("Type name to update must be entered");
                        } else if (newTypeName.contains(" ")) {
                            System.out.println("Type name to update cannot contain blanks");
                        } else {
                            typeNameInvalid = false;
                        }
                    }
                    
                    Table newTable = oldTable.deepCopy();
                    List<FieldSchema> oldCols = newTable.getSd().getCols();
                    List<FieldSchema> newCols = new ArrayList<>();
                    Iterator<FieldSchema> iterator = oldCols.listIterator();
                    while (iterator.hasNext()) {
                        FieldSchema fieldSchema = iterator.next();
                        String name = fieldSchema.getName();
                        if (name.equals(newTypeName)) {
                            fieldSchema.setType(newTypeName);
                        }
                        newCols.add(fieldSchema);
                    }
                    newTable.getSd().setCols(newCols);
                    AlterTableEvent alterTableEvent = getAlterTableEvent(oldTable, newTable);
                    tableNameTableMap.put(enteredTableName, newTable);
                    hmsListener.onAlterTable(alterTableEvent);


                } else if (op.equalsIgnoreCase("q")) {
                    processing = false;
                } else {
                    System.out.println("Invalid Operation :" + op);
                }

            } else if (op.equalsIgnoreCase("q")) {
                processing = false;
            } else {
                System.out.println("Invalid Operation :" + op);
            }
        }
    }

    private static String validateName(Scanner sc, String artifiactName) {
        String artifiactValue = null;
        boolean nameInvalid = true;
        while(nameInvalid) {
            System.out.println("Enter the " + artifiactName +" name :\n ");
            artifiactValue = sc.nextLine();
            if (artifiactValue == null || artifiactValue.equals("")) {
                System.out.println(artifiactName + " name must be entered");
            } else if (artifiactValue.contains(" ")) {
                System.out.println(artifiactName + " name cannot contain blanks");
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
        int createTime = (int) new Date().getTime();
        table.setCreateTime(createTime);
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
        System.out.println("Adding a Table using event:");
        System.out.println(tableEvent);
        hmsListener.onCreateTable(tableEvent);
        return table;

    }
    private void dropTable(String tableName) throws MetaException {
        Table table = tableNameTableMap.get(tableName);
        DropTableEvent tableEvent = getDropTableEvent(table);
        System.out.println("Dropping a Table using event:");
        System.out.println(tableEvent);
        hmsListener.onDropTable(tableEvent);
        tableNameTableMap.remove(tableName);
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
    /**
     * Unfortunately HMS 3.1.3 and HMS 4.0.0 alpha 2 have different constructor parameters for AlterTableEvent, this method uses reflection
     * to try each constructor shape.
     * @param oldTable the table to put in the CreateTableEvent
     * @param newTable
     * @return alterTableEvent
     */
    private static AlterTableEvent getAlterTableEvent(Table oldTable, Table newTable) {
        AlterTableEvent tableEvent = null;
        try {
            Class<?> alterTableEventclass = Class.forName("org.apache.hadoop.hive.metastore.events.AlterTableEvent");
            Constructor constructor = null;
            try {
                constructor = alterTableEventclass.getDeclaredConstructor(Table.class, Table.class, boolean.class, boolean.class, IHMSHandler.class);
                tableEvent = (AlterTableEvent) constructor.newInstance(oldTable, newTable, true, true, null);
            } catch (NoSuchMethodException e) {
                // we expect this if running against hms v3.1.3
            }

            if (constructor == null) {
                constructor = alterTableEventclass.getDeclaredConstructor(Table.class, Table.class, boolean.class, boolean.class, IHMSHandler.class, boolean.class);
                tableEvent = (AlterTableEvent) constructor.newInstance(oldTable, newTable, true, true, null, true);
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
