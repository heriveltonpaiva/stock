package br.com.paiva.financial.stock.trade.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationDTO {

    private String type;
    private String stockName;
    private Integer quantity;
    private Double operationPrice;
    private Double purchasePrice;

}
