package br.com.paiva.financial.stock.trade.operation;

import br.com.paiva.financial.stock.dashboard.stockposition.StockPosition;
import br.com.paiva.financial.stock.trade.tax.Tax;
import br.com.paiva.financial.stock.trade.tradingnote.BrokerType;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;

import java.time.LocalDate;
import java.util.Date;

public class SellOperationFactory {

  public static OperationDTO operationDTO() {
    final OperationDTO operation = new OperationDTO();

    operation.setTradingNoteCode("20569898");
    operation.setStockName("PETR4");
    operation.setQuantity(100);
    operation.setOperationPrice(1632.0);
    operation.setType(OperationType.SELL.name());
    operation.setPurchasePrice(null);

    return operation;
  }

  public static StockPosition stockPosition() {
    StockPosition stock = new StockPosition();
    stock.setStockName("PETR4");
    stock.setDate(LocalDate.now());
    stock.setLastModified(LocalDate.now());
    stock.setTotalPurchased(1347.70);
    stock.setQuantity(100);
    stock.setAveragePrice(13.48);
    return stock;
  }

  public static TradingNote tradingNote() {
    TradingNote tradingNote = new TradingNote();
    tradingNote.setCode("20569898");
    tradingNote.setBroker(BrokerType.XP);
    tradingNote.setDate(new Date());
    tradingNote.setValue(1632.0);
    tradingNote.setValueSell(null);
    tradingNote.setTaxes(tax());
    return tradingNote;
  }

  public static Tax tax() {
    return Tax.builder()
        .liquidation(0.44)
        .brokerage(18.90)
        .emoluments(0.04)
        .taxes(2.01)
        .incomingTax(0.08)
        .otherTaxes(0.73)
        .build();
  }
}
