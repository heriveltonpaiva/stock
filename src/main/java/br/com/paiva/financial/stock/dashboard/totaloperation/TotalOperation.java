package br.com.paiva.financial.stock.dashboard.totaloperation;

import br.com.paiva.financial.stock.trade.operation.Operation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@EqualsAndHashCode
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
  private List<Operation> operationList;

  public Double getTotalPurchased(){
      return Objects.isNull(totalPurchased) ? 0 : totalPurchased;
  }

  public Integer getBuyQuantity() {
    return Objects.isNull(buyQuantity) ? 0 : buyQuantity;
  }

  public Integer getSellQuantity() {
    return Objects.isNull(sellQuantity) ? 0 : sellQuantity;
  }

  public Double getTotalSold() {
    return Objects.isNull(totalSold) ? 0D : totalSold;
  }

  public Double getTotalGainValue() {
    return  Objects.isNull(totalGainValue) ? 0D : totalGainValue;
  }

  public Double getTotalDarf() {
    return Objects.isNull(totalDarf) ? 0D : totalDarf;
  }

}
