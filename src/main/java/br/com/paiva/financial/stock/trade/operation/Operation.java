package br.com.paiva.financial.stock.trade.operation;

import br.com.paiva.financial.stock.trade.tax.Tax;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@Data
@NoArgsConstructor
public class Operation {

  private final static Double SWING_TRADE_TAX = 0.15;

  @Id
  private String id;
  private String tradingNoteCode;
  private String stockName;
  private Integer quantity;
  private Double unitPrice;
  private Double operationPrice;
  private Double averagePrice;
  private Double purchasePrice;
  private Double gainValue;
  private Tax taxes;
  private OperationType type;
  private Double darf;

  public Operation(
      final OperationType type,
      final String stockName,
      final Double operationPrice,
      final Integer quantity,
      final Double purchasePrice,
      final TradingNote tradingNote) {
    this.type = type;
    this.stockName = stockName;
    this.operationPrice = operationPrice;
    this.quantity = quantity;
    this.taxes = new Tax(tradingNote, operationPrice, type);
    this.unitPrice = operationPrice / quantity;
    this.purchasePrice = (type == OperationType.BUY) ?  operationPrice + taxes.getTotalValue() : purchasePrice;
    this.gainValue = 0D;
    this.averagePrice = type == OperationType.BUY ? this.purchasePrice/quantity : purchasePrice/quantity;
  }

  public Double getDarf(){
    if(type.equals(OperationType.SELL)){
      return getGainValue() * SWING_TRADE_TAX;
    }
    return 0D;
  }

  public Double getGainValue(){
    if(type.equals(OperationType.SELL)){
      return round(this.operationPrice - this.purchasePrice - this.taxes.getTotalValue() - this.getTaxes().getIncomingTax());
    }
    return BigDecimal.ZERO.doubleValue();
  }

  private Double round(final Double value){
    NumberFormat nf = new DecimalFormat("0.00");
    nf.setRoundingMode(RoundingMode.HALF_UP);
    return Double.parseDouble(nf.format(value).replace(",", "."));
  }

  @Override
  public String toString() {
    return "\nAtivo: "
        + stockName
        + " Quantidade:"
        + quantity
        + " Preço Unit.:"
        + unitPrice
        + " Preço Total:"
        + operationPrice
        + " Preço de Aquisição:"
        + purchasePrice
        + " Lucro:"
        + gainValue
        + "\nTaxas:"
        + taxes
        + " Tipo:"
        + type;
  }
}
