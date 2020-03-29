package io.jayms.dbsc.server.spreadsheet;

import java.util.Arrays;
import java.util.List;

public class DatabaseColumnTypes {

    public static final int NUMBER = 2;
    public static final int INT = 4;
    public static final int BOOL = -7;
    public static final int NVARCHAR = -9;
    public static final int VARCHAR = 12;
    public static final int UUID = 1;
    public static final int DATE = 91;
    public static final int TIMESTAMP = 93;
    public static final int INTERVAL_YM = -103;
    public static final int INTERVAL_DS = -104;
    public static final int ROWID = -8;
    public static final int CLOB = 2005;
    public static final int XMLTYPE = 2007;
    public static final int RAW = -3;
    public static final int CHAR = 1;

    public static final List<Integer> NUMBER_TYPES = Arrays.asList(NUMBER, INT);

}
