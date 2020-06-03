package br.com.paiva.financial.stock.dashboard.totaloperation;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TotalOperation {
  @Id private String id;
  private Integer referenceMonth;
  private String stockName;
  private Integer buyQuantity;
  private Integer sellQuantity;
  private Double totalPurchased;
  private Double totalSold;
  private Double totalEmoluments;
  private Double totalLiquidation;
  private Double totalTaxes;
  private Double totalIncomingTax;
  private Double totalOtherTaxes;
  private Double totalOperationalCosts;
  private Double totalGainValue;
  private Double totalDarf;
  private LocalDate lastModified;

}
