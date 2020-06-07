package br.com.paiva.financial.stock.trade.operation;

import br.com.paiva.financial.stock.trade.tax.Tax;
import br.com.paiva.financial.stock.trade.tradingnote.BrokerType;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;

import java.util.Date;

public class Buy2OperationFactory {

  public static OperationDTO operationPETR4DTO() {
    final OperationDTO operation = new OperationDTO();

    operation.setTradingNoteCode("20569898");
    operation.setStockName("PETR4");
    operation.setQuantity(100);
    operation.setOperationPrice(1325.0);
    operation.setType(OperationType.BUY.name());
    operation.setPurchasePrice(null);

    return operation;
  }

  public static OperationDTO operationGOLL4DTO() {
    final OperationDTO operation = new OperationDTO();

    operation.setTradingNoteCode("20569898");
    operation.setStockName("GOLL4");
    operation.setQuantity(100);
    operation.setOperationPrice(818.0);
    operation.setType(OperationType.BUY.name());
    operation.setPurchasePrice(null);

    return operation;
  }

  /**
   * Compra da GOLL4 e PETR4
   */
  public static TradingNote tradingNote() {

    TradingNote tradingNote = new TradingNote();

    tradingNote.setCode("20040289");
    tradingNote.setBroker(BrokerType.XP);
    tradingNote.setDate(new Date());
    tradingNote.setValue(2143.0);
    tradingNote.setValueSell(null);
    tradingNote.setTaxes(tax());

    return tradingNote;
  }

  public static Tax taxPetr4() {
    return Tax.builder()
            .liquidation(0.36)
            .brokerage(18.90)
            .emoluments(0.04)
            .taxes(2.49)
            .incomingTax(0D)
            .otherTaxes(0.91)
            .build();
  }

  public static Tax taxGol() {
    return Tax.builder()
            .liquidation(0.22)
            .brokerage(18.09)
            .emoluments(0.02)
            .taxes(1.54)
            .incomingTax(0D)
            .otherTaxes(0.56)
            .build();
  }

  public static Tax tax() {
    return Tax.builder()
        .liquidation(0.58)
        .brokerage(37.80)
        .emoluments(0.06)
        .taxes(4.03)
        .incomingTax(0D)
        .otherTaxes(1.47)
        .build();
  }
}
