package com.huiruan.calcite.catalog;

import com.huiruan.calcite.expression.Identifier;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Database {

    private final String dbName;

    private final Map<Identifier.TableName, Table> tableMap;

    public Database(String dbName) {
        this.dbName = dbName;
        this.tableMap = new HashMap<>();
    }

    public void createTable(Table table) {
        if (tableMap.containsKey(table.getTableName())) {
            throw new IllegalArgumentException(
                    String.format("Table with same name already exists: db=%s, table=%s",
                            dbName, table.getTableName()));
        } else {
            tableMap.put(table.getTableName(), table);
        }
    }

    public boolean tableExists(Identifier.TableName tableName) {
        return tableMap.containsKey(tableName);
    }

    public Table getTable(Identifier.TableName tableName) {
        return tableMap.get(tableName);
    }
}
