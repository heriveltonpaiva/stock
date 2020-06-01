package br.com.paiva.financial.stock.trade;

import br.com.paiva.financial.stock.trade.operation.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradingNote {

  @Id
  private String id;
  private Date date;
  private Double value;
  private Double valueSell;
  private Tax taxes;
  private List<Operation> operationList;

  @Override
  public String toString() {
    return "Pregão:" + date + " Total Investido:" + value + " Taxas:" + taxes;
  }
}
