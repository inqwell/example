package example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.LinkedList;

public class PriceRenderer extends DefaultTableCellRenderer implements PriceTarget
{
  // Remember the three most recent price updates
  private LinkedList<Price> recent = new LinkedList<Price>();

  private PriceTableModel model;

  public PriceRenderer(PriceTableModel model) {
    this.model = model;
  }

  public void newPrice(Price p) {
    Price rp = null;
    if (recent.size() == 3)
      rp = recent.removeLast();

    recent.add(0, p);

    // Force repaint of cells that are no longer highlighted
    if (rp != null)
      model.newPrice(rp);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected, boolean hasFocus, int row, int column) {
    // value is a Price
    Price p = (Price)value;

    JLabel c = (JLabel)super.getTableCellRendererComponent(table, (p == null) ? "" : p.getTwoWayPrice(), isSelected, hasFocus, row, column);

    if (recent.contains(p))
      c.setBackground(Color.PINK);
    else
      c.setBackground(table.getBackground());

    return c;
  }
}
