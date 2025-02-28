package com.huiruan.calcite.catalog;

import java.util.HashMap;
import java.util.Map;

public class Catalog {

    public static final Catalog DEFAULT_CATALOG = new Catalog();

    private final Map<String, Database> dbMap = new HashMap<>();

    private Catalog() {}

    private static Catalog defaultCatalog() {
        return DEFAULT_CATALOG;
    }

    public Database getDatabase(String dbName) {
        return dbMap.get(dbName);
    }

    public void createDatabase(Database database) {
        if (dbMap.containsKey(database.getDbName())) {
            throw new IllegalArgumentException(
                    "Database with same name already exists: " + database.getDbName());
        } else {
            dbMap.put(database.getDbName(), database);
        }
    }

    public boolean dbExists(String dbName) {
        return dbMap.containsKey(dbName);
    }
}
