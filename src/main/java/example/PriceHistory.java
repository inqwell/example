package example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PriceHistory implements PriceTarget
{
  private HashMap<String, LinkedList<Price>> prcHistory = new HashMap<>();

  public void newPrice(Price p)
  {
    String key = p.getKey();

    LinkedList<Price> hist = prcHistory.get(key);

    if (hist == null)
    {
      hist = new LinkedList<>();
      hist.addFirst(p);
      prcHistory.put(key, hist);
    } else
    {
      hist.addFirst(p);
      if (hist.size() > 50)
        hist.removeLast();
    }
  }

  public ArrayList<Price> getHistory(Price p) {
    List<Price> hist = prcHistory.get(p.getKey());

    if (hist != null)
      return new ArrayList<Price> (hist);

    return null;
  }
}
