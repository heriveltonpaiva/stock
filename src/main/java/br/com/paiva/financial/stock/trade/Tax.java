package br.com.paiva.financial.stock.trade;

import br.com.paiva.financial.stock.trade.operation.OperationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@Data
@NoArgsConstructor
public class Tax {
  @Id
  private String id;
  private Double emoluments;
  private Double liquidation;
  private Double brokerage = 18.90;
  private Double taxes;
  private Double incomingTax;
  private Double otherTaxes;
  private Double totalValue;
  private Double operationalCosts;

  public Tax(final TradingNote note, final Double operationValue, final OperationType type) {
    this.emoluments = round(getToY(note.getValue(), note.getTaxes().getEmoluments(), operationValue));
    this.liquidation = round(getToY(note.getValue(), note.getTaxes().getLiquidation(), operationValue));
    this.taxes = round(getToY(note.getValue(), note.getTaxes().getTaxes(), operationValue));
    this.incomingTax = type == OperationType.SELL ? round(getToY(note.getValueSell(), note.getTaxes().getIncomingTax(), operationValue)) : 0D;
    this.otherTaxes = round(getToY(note.getValue(), note.getTaxes().getOtherTaxes(), operationValue));
  }

  public Double getTotalValue(){
    return round(emoluments + liquidation + brokerage + taxes + otherTaxes);
  }

  public Double getOperationalCosts(){
    return round (brokerage + taxes + otherTaxes);
  }

  private double getToY(final Double x, final Double toX, final Double y) {
    return (y * toX) / x;
  }

  private Double round(final Double value){
    NumberFormat nf = new DecimalFormat("0.000");
    nf.setRoundingMode(RoundingMode.HALF_UP);
    return Double.parseDouble(nf.format(value).replace(",", "."));
  }

  @Override
  public String toString() {
    return "(Emolumentos:"
        + emoluments
        + " Taxa de Liquidação:"
        + liquidation
        + " Corretagem:"
        + brokerage
        + " Impostos:"
        + taxes
        + " IRRF:"
        + incomingTax
        + " Outros:"
        + otherTaxes
        + " Total: "
        + totalValue
        + ")";
  }
}
