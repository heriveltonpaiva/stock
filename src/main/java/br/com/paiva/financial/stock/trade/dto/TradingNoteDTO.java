package br.com.paiva.financial.stock.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradingNoteDTO {

    private Date date;
    private Double value;
    private Double operationSell;
    private TaxDTO taxes;
    private List<OperationDTO> stocks;

}
