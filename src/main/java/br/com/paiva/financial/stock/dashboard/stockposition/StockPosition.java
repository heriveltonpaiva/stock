package br.com.paiva.financial.stock.dashboard.stockposition;

import br.com.paiva.financial.stock.trade.tradingnote.BrokerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StockPosition {
    @Id
    private String id;
    private LocalDate date;
    private String stockName;
    private BrokerType broker;
    private Integer quantity;
    private Double averagePrice;
    private Double totalPurchased;
    private LocalDateTime lastModified;

    public StockPosition(){
        this.quantity = 0;
        this.averagePrice = 0D;
        this.totalPurchased = 0D;
    }
}
