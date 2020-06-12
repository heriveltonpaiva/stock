package br.com.paiva.financial.stock.trade.tax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDTO {

  @NotNull(message = "emoluments is null")
  private Double emoluments;
  @NotNull(message = "liquidation is null")
  private Double liquidation;
  private Double brokerage;
  private Double taxes;
  private Double incomingTax;
  private Double otherTaxes;

}
