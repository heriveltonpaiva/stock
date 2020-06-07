package br.com.paiva.financial.stock.trade.operation;

import br.com.paiva.financial.stock.trade.tax.Tax;
import br.com.paiva.financial.stock.trade.tradingnote.BrokerType;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;

import java.util.Date;

public class BuyOperationFactory {

  public static OperationDTO operationDTO() {
    final OperationDTO operation = new OperationDTO();

    operation.setTradingNoteCode("20569898");
    operation.setStockName("PETR4");
    operation.setQuantity(100);
    operation.setOperationPrice(1632.0);
    operation.setType(OperationType.BUY.name());
    operation.setPurchasePrice(null);

    return operation;
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
        .incomingTax(0D)
        .otherTaxes(0.73)
        .build();
  }
}
