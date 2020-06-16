package br.com.paiva.financial.stock.dashboard.totaloperation.filtered;

import br.com.paiva.financial.stock.dashboard.totaloperation.TotalOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class TotalOperationFiltered {

  private Integer buyQuantity;
  private Integer sellQuantity;
  private Double totalPurchased;
  private Double totalSold;
  private Double totalEmoluments;
  private Double totalLiquidation;
  private Double totalBrokerage;
  private Double totalTaxes;
  private Double totalIncomingTax;
  private Double totalOtherTaxes;
  private Double totalOperationalCosts;
  private Double totalGainValue;
  private Double totalDarf;

  public TotalOperationFiltered() {
    this.buyQuantity = 0;
    this.sellQuantity = 0;
    this.totalPurchased = 0D;
    this.totalSold = 0D;
    this.totalEmoluments = 0D;
    this.totalLiquidation = 0D;
    this.totalBrokerage = 0D;
    this.totalOtherTaxes = 0D;
    this.totalOperationalCosts = 0D;
    this.totalGainValue = 0D;
    this.totalDarf = 0D;
    this.totalTaxes = 0D;
    this.totalIncomingTax = 0D;
  }

  public TotalOperationFiltered convertAndUpdate(final TotalOperation totalOperation){
    this.setBuyQuantity(this.buyQuantity + totalOperation.getBuyQuantity());
    this.setSellQuantity(this.sellQuantity + totalOperation.getSellQuantity());
    this.setTotalBrokerage(this.totalBrokerage + totalOperation.getTotalBrokerage());
    this.setTotalDarf(this.totalDarf + totalOperation.getTotalDarf());
    this.setTotalPurchased(this.totalPurchased + totalOperation.getTotalPurchased());
    this.setTotalSold(this.totalSold + totalOperation.getTotalSold());
    this.setTotalEmoluments(this. totalEmoluments + totalOperation.getTotalEmoluments());
    this.setTotalLiquidation(this.totalLiquidation + totalOperation.getTotalLiquidation());
    this.setTotalTaxes(this.totalTaxes + totalOperation.getTotalTaxes());
    this.setTotalOtherTaxes(this.totalOtherTaxes + totalOperation.getTotalOtherTaxes());
    this.setTotalOperationalCosts(this.totalOperationalCosts + totalOperation.getTotalOperationalCosts());

    return this;
  }
}
