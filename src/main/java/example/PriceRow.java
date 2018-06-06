package example;

import java.util.HashMap;

public class PriceRow
{
  // Map source to Price
  private HashMap<String, Price> prices = new HashMap<>();
  private String instrument;

  public PriceRow(String instrument) {
    this.instrument = instrument;
  }

  public PriceRow(Price source1, Price source2, Price source3) {
    instrument = source1.getInstrument();

    prices.put(source1.getSource(), source1);
    prices.put(source2.getSource(), source2);
    prices.put(source3.getSource(), source3);
  }

  public Price getPrice(String source) {
    return prices.get(source);
  }

  public String getInstrument() {
    return instrument;
  }

  public void setPrice(Price p) {
    if (p.getInstrument().equals(this.instrument))
    {
      prices.put(p.getSource(), p);
    } else {
      throw new RuntimeException("Price for " + p.getInstrument() + " not for row " + instrument);
    }
  }
}
