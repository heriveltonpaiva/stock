package br.com.paiva.financial.stock.dashboard.stockposition;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import br.com.paiva.financial.stock.trade.tax.Tax;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class StockFactory {

  public static StockPosition stockPositionCreate() {
    StockPosition stock = new StockPosition();
    stock.setStockName("RENT3");
    stock.setDate(LocalDate.now());
    stock.setLastModified(LocalDate.now());
    stock.setTotalPurchased(2000D);
    stock.setQuantity(200);
    stock.setAveragePrice(10D);
    return stock;
  }

  public static StockPosition stockPositionUpdate() {
    StockPosition stock = new StockPosition();
    stock.setStockName("RENT3");
    stock.setDate(LocalDate.now());
    stock.setLastModified(LocalDate.now());
    stock.setTotalPurchased(4000D);
    stock.setQuantity(400);
    stock.setAveragePrice(10D);
    return stock;
  }

  public static Tax tax() {
  final Tax tax = Tax.builder()
          .liquidation(2.73)
          .brokerage(56.70)
          .emoluments(0.29)
          .taxes(6.05)
          .incomingTax(0.24)
          .otherTaxes(2.21)
          .build();
    return tax;
  }

  public static Operation operation() {
    final Operation op2 = new Operation();
    op2.setStockName("RENT3");
    op2.setQuantity(200);
    op2.setOperationPrice(2000D);
    op2.setPurchasePrice(2000D);
    op2.setType(OperationType.BUY);
    op2.setTaxes(tax());

    return op2;
  }

  public static List<Operation> operationList() {
    final Operation op1 = new Operation();
    op1.setStockName("RENT3");
    op1.setQuantity(100);
    op1.setOperationPrice(1000D);
    op1.setType(OperationType.BUY);
    op1.setTaxes(tax());
    op1.setPurchasePrice(1000D);
    return Arrays.asList(op1, operation());
  }
}
