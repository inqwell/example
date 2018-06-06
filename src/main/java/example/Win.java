package example;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Win extends JFrame
{
  public Win() {
    PriceTableModel m = new PriceTableModel(Generator.sources);
    PriceRenderer pr = new PriceRenderer(m);
    PriceHistory ph = new PriceHistory();

    JTable table = new JTable(m);
    table.addMouseListener(new TableClickListener(table, ph));

    //add the table to the frame
    this.add(new JScrollPane(table));

    table.setDefaultRenderer(Price.class, pr);

    this.setTitle("BBVA Example");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setVisible(true);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    //this.setLocation(dim.width/2 - this.getSize().width/2, dim.height/2 - this.getSize().height/2);
    this.setLocationRelativeTo(null);

    Generator.startTimer(ph, pr, m);

  }

  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new Win();
      }
    });
  }

  private class TableClickListener extends MouseAdapter {
    private JTable t;
    private PriceHistory ph;

    private TableClickListener(JTable t, PriceHistory ph) {
      this.t = t;
      this.ph = ph;
    }

    public void mouseClicked(MouseEvent e) {
      Point p = e.getPoint();

      int col = t.convertColumnIndexToModel(t.columnAtPoint(p));
      if (col > 0) {
        int row = t.rowAtPoint(p);

        Price pr = (Price)t.getModel().getValueAt(row, col);
        if (pr != null && e.getClickCount() >= 2) {
          showHistory(pr);
        }
      }
    }

    private void showHistory(Price p) {
      ArrayList<Price> hist = ph.getHistory(p);
      if (hist != null)
      {
        JDialog dialog = new JDialog(Win.this, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(Win.this);
        String title = "Price History for " + p.getInstrument() + " (" + p.getSource() + ")";
        dialog.setTitle(title);
        dialog.setSize(600, 400);

        PriceHistoryTableModel hm = new PriceHistoryTableModel(hist);

        JTable table = new JTable(hm);

        for (int i = 0; i < table.getModel().getColumnCount(); i++)
        {
          TableColumn tc = table.getColumnModel().getColumn(i);
          TableCellRenderer headerRenderer = new HistoryHeaderRenderer(table.getTableHeader().getDefaultRenderer());
          tc.setHeaderRenderer(headerRenderer);
        }

        sort(HistoryHeaderRenderer.getSortColumn(), HistoryHeaderRenderer.isAscending(), hm);

        table.getTableHeader().addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent mouseEvent) {
            int index = table.convertColumnIndexToModel(table.columnAtPoint(mouseEvent.getPoint()));
            if (index >= 0) {
              HistoryHeaderRenderer.setSortColumn(index);
              table.getTableHeader().repaint();
              sort(index, HistoryHeaderRenderer.isAscending(), hm);
            }
          };
        });


        dialog.add(new JScrollPane(table));

        table.setDefaultRenderer(Price.class, new PriceHistoryRenderer());

        dialog.setVisible(true);
      }
    }

    private void sort(int column, boolean ascending, PriceHistoryTableModel hm) {
       Price.SortBy sortBy = (column == 0) ? Price.SortBy.PRICE : Price.SortBy.TIMESTAMP;
       hm.sort(ascending, sortBy);
    }
  }
}
