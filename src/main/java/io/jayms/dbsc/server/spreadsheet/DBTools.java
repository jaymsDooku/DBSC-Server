package io.jayms.dbsc.server.spreadsheet;

import org.apache.poi.ss.usermodel.Cell;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DBTools {

    public static DatabaseColumn[] getDatabaseColumns(ResultSetMetaData meta) throws SQLException {
        int columnCount = meta.getColumnCount();
        DatabaseColumn[] columns = new DatabaseColumn[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            String colName = meta.getColumnName(i);
            String colLabel = meta.getColumnLabel(i);
            int colType = meta.getColumnType(i);
            String typeName = meta.getColumnTypeName(i);
            String tableName = meta.getTableName(i);
            DatabaseColumn column = new DatabaseColumn();
            column.setIndex(i);
            column.setName(colName);
            column.setLabel(colLabel);
            column.setType(colType);
            column.setTableName(tableName);
            columns[i-1] = column;
        }
        return columns;
    }

    public static void setCellValue(Cell cell, ResultSet resultSet, String label, int type) throws SQLException {
        switch (type) {
            case DatabaseColumnTypes.NVARCHAR:
                cell.setCellValue(resultSet.getNString(label));
                break;
            case DatabaseColumnTypes.CHAR:
            case DatabaseColumnTypes.VARCHAR:
            case DatabaseColumnTypes.INTERVAL_DS:
            case DatabaseColumnTypes.INTERVAL_YM:
                cell.setCellValue(resultSet.getString(label));
                break;
            case DatabaseColumnTypes.NUMBER:
                cell.setCellValue(resultSet.getDouble(label));
                break;
            case DatabaseColumnTypes.INT:
                cell.setCellValue(resultSet.getInt(label));
                break;
            case DatabaseColumnTypes.BOOL:
                cell.setCellValue(resultSet.getBoolean(label));
                break;
            case DatabaseColumnTypes.DATE:
            case DatabaseColumnTypes.TIMESTAMP:
                cell.setCellValue(resultSet.getDate(label));
                break;
            case DatabaseColumnTypes.CLOB:
                cell.setCellValue(resultSet.getClob(label).toString());
                break;
            case DatabaseColumnTypes.ROWID:
            case DatabaseColumnTypes.RAW:
                cell.setCellValue(new String(resultSet.getBytes(label)));
                break;
            default:
                break;
        }
    }

}
