package br.com.paiva.financial.stock.trade.operation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationSearchRequest {

  private String tradingNoteCode;
  private String typeOperation;
  private String stockName;
  private String referenceMonth;
  private String broker;

}
