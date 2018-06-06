package example;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PriceHistoryTableModel extends AbstractTableModel
{
  private ArrayList<Price> rows;

  public PriceHistoryTableModel(ArrayList<Price> rows) {
    this.rows = rows;
  }

  @Override
  public int getRowCount()
  {
    return rows.size();
  }

  @Override
  public int getColumnCount()
  {
    return 2;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    return rows.get(rowIndex);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex)
  {
    return Price.class;
  }

  public String	getColumnName(int column) {
    String name;

    switch (column) {
      case 0: name = "Price";
      break;

      case 1: name = "Time";
      break;

      default: name = "";
    }
    return name;
  }

  public void sort(boolean ascending, Price.SortBy... sortBy) {
    Comparator c = new Price.Comparator(ascending, sortBy);

    Collections.sort(rows, c);

    fireTableDataChanged();
  }
}
