package br.com.paiva.financial.stock.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class StockUtils {

  public static Double round(final Double value) {
    NumberFormat nf = new DecimalFormat("0.00");
    nf.setRoundingMode(RoundingMode.HALF_UP);
    return Double.parseDouble(nf.format(value).replace(",", "."));
  }

  public static double getToY(final Double x, final Double toX, final Double y) {
    return (y * toX) / x;
  }
}
