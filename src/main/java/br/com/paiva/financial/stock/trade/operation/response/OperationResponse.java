package br.com.paiva.financial.stock.trade.operation.response;

import br.com.paiva.financial.stock.dashboard.totaloperation.filtered.TotalOperationFiltered;
import br.com.paiva.financial.stock.trade.operation.Operation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationResponse {

  private List<Operation> operations;
  private TotalOperationFiltered totalOperation;
}
