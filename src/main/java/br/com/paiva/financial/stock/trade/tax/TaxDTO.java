package br.com.paiva.financial.stock.trade.tax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class TaxDTO {

  private Double emoluments;
  private Double liquidation;
  private Double brokerage;
  private Double taxes;
  private Double incomingTax;
  private Double otherTaxes;

  public TaxDTO(){
    this.liquidation = 0D;
    this.emoluments = 0D;
    this.brokerage = 0D;
    this.incomingTax = 0D;
    this.otherTaxes = 0D;
    this.taxes = 0D;
  }

}
