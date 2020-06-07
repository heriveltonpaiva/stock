package br.com.paiva.financial.stock.trade.tax;

import br.com.paiva.financial.stock.trade.operation.OperationType;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import static br.com.paiva.financial.stock.util.StockUtils.getToY;
import static br.com.paiva.financial.stock.util.StockUtils.round;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tax {
  @Id
  private String id;
  private Double emoluments;
  private Double liquidation;
  private Double brokerage;
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
    return round(emoluments + liquidation + brokerage + taxes + otherTaxes + incomingTax);
  }

  public Double getOperationalCosts(){
    return round(brokerage + taxes + otherTaxes);
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
