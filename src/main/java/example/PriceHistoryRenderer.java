package example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class PriceHistoryRenderer extends DefaultTableCellRenderer
{
  DateTimeFormatter formatter =
          DateTimeFormatter.ofLocalizedTime( FormatStyle.MEDIUM)
                  .withLocale(Locale.UK)
                  .withZone(ZoneId.systemDefault());

  public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected, boolean hasFocus, int row, int column) {
    // value is a Price
    Price p = (Price)value;

    String s = (table.convertColumnIndexToModel(column) == 0) ? p.getTwoWayPrice() : formatter.format(p.getTimestamp());

    return super.getTableCellRendererComponent(table, s, isSelected, hasFocus, row, column);
  }
}
