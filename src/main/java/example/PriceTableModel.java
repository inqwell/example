package example;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class PriceTableModel extends AbstractTableModel implements PriceTarget
{
  private List<String> sourceNames;
  private ArrayList<PriceRow> rows = new ArrayList<>();

  // Map instruments to row numbers
  private HashMap<String, Integer> instrMap = new HashMap<>();

  public PriceTableModel(String[] sourceNames) {
    this.sourceNames = new ArrayList<>(Arrays.asList(sourceNames));
  }

  public int getRowCount() {
    return rows.size();
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    PriceRow pr = rows.get(rowIndex);
    if (columnIndex == 0)
      return pr.getInstrument();
    else {
      String source = sourceNames.get(columnIndex-1);
      Price p = pr.getPrice(source);
      if (p != null)
        return p;
      else
        return null;
    }
  }

  public int getColumnCount() {
    return 4;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex)
  {
    if (columnIndex == 0)
      return String.class;

    return Price.class;
  }

  public String	getColumnName(int column) {
    if (column == 0)
      return "Instrument";

    return sourceNames.get(column-1);
  }

  public void newPrice(Price p)
  {
    if (SwingUtilities.isEventDispatchThread())
      newPriceInner(p);
    else
    {
      SwingUtilities.invokeLater(new Runnable()
      {
        @Override
        public void run()
        {
          newPriceInner(p);
        }
      });
    }
  }

  private void newPriceInner(Price p) {
    int colNum = sourceNames.indexOf(p.getSource()) + 1;
    if (colNum == 0)
      throw new RuntimeException("Unknown source " + p.getSource());

    int rowNum;

    Integer rowNumber = instrMap.get(p.getInstrument());
    if (rowNumber == null)
    {
      PriceRow pr = new PriceRow(p.getInstrument());
      pr.setPrice(p);
      rowNum = rows.size();
      pr.setPrice(p);
      rows.add(pr);
      instrMap.put(p.getInstrument(), rowNum);
      fireTableRowsInserted(rowNum, rowNum);
    } else
    {
      PriceRow pr = rows.get(rowNumber);
      pr.setPrice(p);
      rowNum = rowNumber;
      fireTableCellUpdated(rowNum, colNum);
    }
  }
}
