package br.com.paiva.financial.stock.trade.tradingnote;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.tax.Tax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradingNote {

  @Id
  private String id;
  private String code;
  private BrokerType broker;
  private Date date;
  private Double value;
  private Double valueSell;
  private Tax taxes;
  @Transient
  private List<Operation> operationList;

  @Override
  public String toString() {
    return "Pregão:" + date + " Total Investido:" + value + " Taxas:" + taxes;
  }
}
