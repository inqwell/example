package example;

import org.junit.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

public class TestPrice {
  private Instant i = Instant.now();
  private Instant i0 = i.minus(1, ChronoUnit.MINUTES);
  private Instant i1 = i.plus(1, ChronoUnit.MINUTES);

  @Test
  public void testOrderPriceByPrice() {
    assertEquals(0, new Price.Comparator(true, Price.SortBy.PRICE).compare(new Price(1.36, 1.3605), new Price (1.36, 1.3605)));
    assertEquals(1, new Price.Comparator(true, Price.SortBy.PRICE).compare(new Price(1.36, 1.3606), new Price (1.36, 1.3605)));
    assertEquals(-1, new Price.Comparator(false, Price.SortBy.PRICE).compare(new Price(1.36, 1.3606), new Price (1.36, 1.3605)));
    assertEquals(-1, new Price.Comparator(true, Price.SortBy.PRICE).compare(new Price(1.36, 1.3605), new Price (1.36, 1.3606)));
    assertEquals(1, new Price.Comparator(false, Price.SortBy.PRICE).compare(new Price(1.36, 1.3605), new Price (1.36, 1.3606)));
    assertEquals(1, new Price.Comparator(true, Price.SortBy.PRICE).compare(new Price(1.37, 1.3706), new Price (1.36, 1.3605)));
    assertEquals(-1, new Price.Comparator(true, Price.SortBy.PRICE).compare(new Price(1.36, 1.3605), new Price (1.37, 1.3706)));
  }

  @Test
  public void testOrderPriceByTimestamp() {
    assertEquals(0, new Price.Comparator(true, Price.SortBy.TIMESTAMP).compare(new Price(1.36, 1.3605, i),
                                                                                 new Price(1.36, 1.3605, i)));

    assertEquals(1, new Price.Comparator(true, Price.SortBy.TIMESTAMP).compare(new Price(1.36, 1.3606, i1),
                                                                                 new Price(1.36, 1.3605, i)));

    assertEquals(-1, new Price.Comparator(true, Price.SortBy.TIMESTAMP).compare(new Price(1.36, 1.3605, i),
                                                                                  new Price(1.36, 1.3606, i1)));

    assertEquals(1, new Price.Comparator(true, Price.SortBy.TIMESTAMP).compare(new Price(1.37, 1.3706, i),
                                                                                 new Price(1.36, 1.3605, i0)));

    assertEquals(-1, new Price.Comparator(true, Price.SortBy.TIMESTAMP).compare(new Price(1.36, 1.3605, i0),
                                                                                  new Price(1.37, 1.3706, i)));
  }

  @Test
  public void testOrderPriceByPriceAndTimestamp() {
    assertEquals(0, new Price.Comparator(true, Price.SortBy.PRICE, Price.SortBy.TIMESTAMP).compare(new Price(1.36, 1.3605, i),
                                                                                                     new Price(1.36, 1.3605, i)));

    assertEquals(1, new Price.Comparator(true, Price.SortBy.PRICE, Price.SortBy.TIMESTAMP).compare(new Price(1.36, 1.3605, i),
                                                                                                     new Price(1.36, 1.3605, i0)));

    assertEquals(-1, new Price.Comparator(true, Price.SortBy.PRICE, Price.SortBy.TIMESTAMP).compare(new Price(1.36, 1.3605, i0),
                                                                                                      new Price(1.36, 1.3605, i)));

    assertEquals(1, new Price.Comparator(true, Price.SortBy.PRICE, Price.SortBy.TIMESTAMP).compare(new Price(1.36, 1.3606, i0),
                                                                                                     new Price(1.36, 1.3605, i)));

    assertEquals(-1, new Price.Comparator(true, Price.SortBy.PRICE, Price.SortBy.TIMESTAMP).compare(new Price(1.36, 1.3605, i),
                                                                                                      new Price(1.36, 1.3606, i1)));

    assertEquals(1, new Price.Comparator(true, Price.SortBy.PRICE, Price.SortBy.TIMESTAMP).compare(new Price(1.37, 1.3706, i),
                                                                                                     new Price (1.36, 1.3605, i1)));

    assertEquals(-1, new Price.Comparator(true, Price.SortBy.PRICE, Price.SortBy.TIMESTAMP).compare(new Price(1.36, 1.3605, i0),
                                                                                                      new Price(1.37, 1.3706, i)));
  }

  @Test
  public void testOrderPriceByTimestampAndPrice() {
    assertEquals(0, new Price.Comparator(true, Price.SortBy.TIMESTAMP, Price.SortBy.PRICE).compare(new Price(1.36, 1.3605, i),
                                                                                                     new Price(1.36, 1.3605, i)));

    assertEquals(1, new Price.Comparator(true, Price.SortBy.TIMESTAMP, Price.SortBy.PRICE).compare(new Price(1.36, 1.3605, i),
                                                                                                     new Price(1.36, 1.3605, i0)));

    assertEquals(-1, new Price.Comparator(true, Price.SortBy.TIMESTAMP, Price.SortBy.PRICE).compare(new Price(1.36, 1.3605, i0),
                                                                                                      new Price(1.36, 1.3605, i)));

    assertEquals(1, new Price.Comparator(true, Price.SortBy.TIMESTAMP, Price.SortBy.PRICE).compare(new Price(1.36, 1.3606, i1),
                                                                                                     new Price(1.36, 1.3605, i)));

    assertEquals(-1, new Price.Comparator(true, Price.SortBy.TIMESTAMP, Price.SortBy.PRICE).compare(new Price(1.36, 1.3605, i),
                                                                                                      new Price(1.36, 1.3606, i)));

    assertEquals(1, new Price.Comparator(true, Price.SortBy.TIMESTAMP, Price.SortBy.PRICE).compare(new Price(1.37, 1.3706, i),
                                                                                                     new Price(1.36, 1.3605, i)));

    assertEquals(-1, new Price.Comparator(true, Price.SortBy.TIMESTAMP, Price.SortBy.PRICE).compare(new Price(1.36, 1.3605, i0),
                                                                                                      new Price(1.37, 1.3706, i0)));
  }

  @Test
  public void testFormatPrice() {
    assertEquals("1.3600/05", new Price(1.36, 1.3605).getTwoWayPrice());
  }
}
