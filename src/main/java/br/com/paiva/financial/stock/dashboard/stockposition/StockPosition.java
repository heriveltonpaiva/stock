package br.com.paiva.financial.stock.dashboard.stockposition;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class StockPosition {
    @Id
    private String id;
    private LocalDate date;
    private String stockName;
    private Integer quantity;
    private Double averagePrice;
    private Double totalPurchased;
    private LocalDate lastModified;
}
