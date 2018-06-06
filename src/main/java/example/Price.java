package example;

import java.time.Instant;

public class Price
{
  private String instrument;
  private String source;
  private double bid;
  private double ask;
  private Instant timestamp;

  // Cache the px
  private String twoWayPrice;

  private String key;

  public Price (String instrument, String source, double bid, double ask) {
    this(instrument, source, bid, ask, Instant.now());
  }

  // For testing only
  Price (double bid, double ask) {
    this("", "", bid, ask, Instant.now());
  }

  Price (double bid, double ask, Instant timestamp) {
    this("", "", bid, ask, timestamp);
  }

  private Price (String instrument, String source, double bid, double ask, Instant timestamp) {
    this.instrument = instrument;
    this.source = source;
    this.bid = bid;
    this.ask = ask;
    this.timestamp = timestamp;
  }

  String getTwoWayPrice() {
    if (twoWayPrice == null)
      twoWayPrice = String.format("%.4f/%02d", bid, (int)(ask * 10000 % 100));

    return twoWayPrice;
  }

  String getInstrument() {
    return instrument;
  }

  String getSource() {
    return source;
  }

  Instant getTimestamp() {
    return timestamp;
  }

  public boolean equals(Object other) {
    if (other.getClass() != Price.class)
      return false;

    Price op = (Price)other;
    return instrument.equals(op.instrument) && source.equals(op.source);
  }

  public int hashCode() {
    return instrument.hashCode() + source.hashCode();
  }

  public String getKey() {
    if (key == null)
      key = instrument + source;

    return key;
  }

  public enum SortBy { PRICE, TIMESTAMP };

  static public class Comparator implements java.util.Comparator<Price> {
    private SortBy[] sortBy;
    private boolean ascending = true;

    public Comparator(boolean ascending, SortBy... sortBy) {
      this.ascending = ascending;
      this.sortBy = sortBy;
    }

    public int compare(Price p1, Price p2) {
      for (SortBy s : sortBy) {
        int c = compareField(s, p1, p2);
        if (c != 0)
          return c;
      }
      return 0;
    }

    private int compareField(SortBy s, Price p1, Price p2) {
      int ret;
      switch (s) {
        case PRICE:
          ret = (p1.bid < p2.bid) ? -1
                                  : (p1.bid > p2.bid) ? 1
                                                      : (p1.ask < p2.ask) ? -1
                                                                          : (p1.ask > p2.ask) ? 1
                                                                                              : 0;
          break;
        case TIMESTAMP:
          ret = p1.timestamp.compareTo(p2.timestamp);
          break;

        default:
          throw new RuntimeException("No such field " + s);
      }
      if (!ascending)
        ret = -ret;

      return ret;
    }
  }
}
