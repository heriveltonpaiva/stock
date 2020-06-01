package br.com.paiva.financial.stock.trade.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OperationService {

  private final OperationRepository repository;

  public List<Operation> findByStockName(final String stockName) {
    return repository.findByStockName(stockName);
  }

  public List<Operation> findAll() {
    return repository.findAll();
  }
}
