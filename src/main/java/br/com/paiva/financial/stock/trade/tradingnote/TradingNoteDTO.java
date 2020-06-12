package br.com.paiva.financial.stock.trade.tradingnote;

import br.com.paiva.financial.stock.trade.tax.TaxDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradingNoteDTO {

  private String code;
  private String broker;
  private String date;
  private Double value;
  private Double operationSell;
  private TaxDTO taxes;
}
