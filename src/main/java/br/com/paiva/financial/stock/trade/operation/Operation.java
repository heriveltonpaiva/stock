package br.com.paiva.financial.stock.trade.operation;

import br.com.paiva.financial.stock.trade.tax.Tax;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

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

  public Double getDarf(){
    if(type.equals(OperationType.SELL)){
      return round(getGainValue() * SWING_TRADE_TAX);
    }
    return 0D;
  }

  public Double getPurchasePrice() {
    return Objects.isNull(purchasePrice) ? 0 : purchasePrice;
  }

  public Double getGainValue(){
    if(type.equals(OperationType.SELL)){
      return round(this.operationPrice - this.purchasePrice - this.taxes.getTotalValue());
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
