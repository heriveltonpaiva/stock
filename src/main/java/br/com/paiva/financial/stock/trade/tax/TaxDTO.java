package br.com.paiva.financial.stock.trade.tax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDTO {

  private Double emoluments;
  private Double liquidation;
  private Double brokerage;
  private Double taxes;
  private Double incomingTax;
  private Double otherTaxes;

}
