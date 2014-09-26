package bdi.glue.jdbc.common;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Rows {
    private int nbCols;
    private Column[] columns;
    private List<Object[]> rows = new ArrayList<>();
    private int nbRows;

    public void defineColumns(ResultSetMetaData rsmd) throws SQLException {
        nbCols = rsmd.getColumnCount();
        columns = new Column[nbCols];
        for (int i = 1; i <= nbCols; i++) {
            columns[i - 1] = new Column(
                    i,
                    rsmd.getColumnType(i),
                    rsmd.getColumnName(i));
        }
    }

    public int getNbRows() {
        return nbRows;
    }

    public void appendRow(Object... values) {
        rows.add(values);
    }

    public void defineNumberOfRows(int nbRows) {
        this.nbRows = nbRows;
    }

    public static class Column {

        private final int index;
        private final int columnType;
        private final String columnName;

        public Column(int index, int columnType, String columnName) {

            this.index = index;
            this.columnType = columnType;
            this.columnName = columnName;
        }

        public int index() {
            return index;
        }

        public int columnType() {
            return columnType;
        }

        public String columnName() {
            return columnName;
        }
    }
}
