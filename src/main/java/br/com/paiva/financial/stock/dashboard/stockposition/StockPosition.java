package br.com.paiva.financial.stock.dashboard.stockposition;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class StockPosition {
    @Id
    private String id;
    private LocalDate date;
    private String stockName;
    private Integer quantity;
    private Double averagePrice;
    private Double totalPurchased;
    private LocalDate lastModified;

    public StockPosition(){
        this.quantity = 0;
        this.averagePrice = 0D;
        this.totalPurchased = 0D;
    }
}
