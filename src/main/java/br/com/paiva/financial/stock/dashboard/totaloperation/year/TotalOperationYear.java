package br.com.paiva.financial.stock.dashboard.totaloperation.year;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Objects;

@Data
@EqualsAndHashCode
public class TotalOperationYear {

    @Id private String id;
    private Integer buyQuantity;
    private Integer sellQuantity;
    private Integer referenceYear;
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

    public TotalOperationYear(){
        this.buyQuantity = 0;
        this.sellQuantity = 0;
        this.totalPurchased = 0D;
        this.totalSold = 0D;
        this.totalEmoluments = 0D;
        this.totalLiquidation = 0D;
        this.totalOtherTaxes = 0D;
        this.totalOperationalCosts = 0D;
        this.totalGainValue = 0D;
        this.totalDarf = 0D;
        this.totalTaxes = 0D;
        this.totalIncomingTax = 0D;
        this.lastModified = LocalDate.now();
    }

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
