package example;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HistoryHeaderRenderer  implements TableCellRenderer {

  // Support one sort column at a time only!
  static int     sortCol = -1;
  static boolean ascending = true;

  TableCellRenderer delegate;

  public HistoryHeaderRenderer(TableCellRenderer r) {
    delegate = r;
  }

  public Component getTableCellRendererComponent(javax.swing.JTable table,
                                                 Object  value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int     row,
                                                 int     column) {
    Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    column = table.convertColumnIndexToModel(column);

    JLabel l = (JLabel)c;

    if (column == sortCol && l != null)
    {
      if (ascending)
      {
        l.setIcon(new Arrow(false, l.getFontMetrics(l.getFont()).getMaxAscent(), 0));
      }
      else
      {
        l.setIcon(new Arrow(true, l.getFontMetrics(l.getFont()).getMaxAscent(), 0));
      }
    }
    else if (l != null)
      l.setIcon(null);


    return c;
  }

  static void setSortColumn(int modelCol) {
    if (modelCol == sortCol)
      ascending = !ascending;
    else
    {
      ascending = true;
      sortCol = modelCol;
    }
  }

  static int getSortColumn() {
    return sortCol;
  }

  static boolean isAscending() {
    return ascending;
  }

  private static class Arrow implements Icon
  {
    private boolean descending_;
    private int     size_;
    private int     priority_;

    public Arrow(boolean descending, int size, int priority)
    {
      this.descending_ = descending;
      this.size_       = size;
      this.priority_   = priority;
    }

    public void paintIcon(Component c, Graphics g, int x, int y)
    {
      Color color = c == null ? Color.gray : c.getBackground();
      // In a compound sort, make each succesive triangle 20%
      // smaller than the previous one.
      int dx = (int)(size_ / 2 * Math.pow(0.8, priority_));
      int dy = descending_ ? dx : -dx;
      // Align icon (roughly) with font baseline.
      y = y + 5 * size_ / 6 + (descending_ ? -dy : 0);
      int shift = descending_ ? 1 : -1;
      g.translate(x, y);

      // Right diagonal.
      g.setColor(color.darker());
      g.drawLine(dx / 2, dy, 0, 0);
      g.drawLine(dx / 2, dy + shift, 0, shift);

      // Left diagonal.
      g.setColor(color.brighter());
      g.drawLine(dx / 2, dy, dx, 0);
      g.drawLine(dx / 2, dy + shift, dx, shift);

      // Horizontal line.
      if (descending_)
      {
        g.setColor(color.darker().darker());
      } else
      {
        g.setColor(color.brighter().brighter());
      }
      g.drawLine(dx, 0, 0, 0);

      g.setColor(color);
      g.translate(-x, -y);
    }

    public int getIconWidth()
    {
      return size_;
    }

    public int getIconHeight()
    {
      return size_;
    }
  }
}
