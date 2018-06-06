package example;

import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.*;

// Generate random prices
public class Generator
{
  static String[] instruments = {"GBP/AUD",
                                 "GBP/BGN",
                                 "GBP/CAD",
                                 "GBP/CHF",
                                 "GBP/CZK",
                                 "GBP/DKK",
                                 "GBP/HKD",
                                 "GBP/HUF",
                                 "GBP/JPY",
                                 "GBP/NOK"};


  static String[] sources = {"Reuters", "Bloomberg", "RTFXD"};

  static Random rand = new Random();
  static Iterator<Integer> srcs = rand.ints(0, 3).iterator();
  static Iterator<Integer> instrs  = rand.ints(0, 10).iterator();
  static Iterator<Double>  rates   = rand.doubles(1.0, 4.0).iterator();

  static Timer timer;
  static PriceTarget[] priceTargets;

  static public Price generatePrice() {
    Double d = rates.next();
    return new Price(instruments[instrs.next()],
                     sources[srcs.next()],
                     d,
                     d + 0.0005);
  }

  static void startTimer(PriceTarget... targets) {
    priceTargets = targets;

    timer = new Timer("price_generator", true);
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run()
      {
        Price p = generatePrice();
        for (PriceTarget t : priceTargets) {
          t.newPrice(p);
        }
      }
    }, 0, 500);
  }
}
