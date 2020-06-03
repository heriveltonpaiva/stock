package br.com.paiva.financial.stock.dashboard.stockposition;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockPositionService {

  private final StockPositionRepository repository;

  public List<StockPosition> findAll() {
    return repository.findAll();
  }

  public List<StockPosition> createOrUpdate(final Operation op, final LocalDate noteDate) {
      List<StockPosition> stocks = repository.findByStockName(op.getStockName());
      StockPosition stockDay = repository.findByDateAndStockName(noteDate, op.getStockName());

    if (ObjectUtils.isEmpty(stockDay)) {
      StockPosition stock = new StockPosition();
      updateAverageQuantity(op, stocks, stock);
      stock.setStockName(op.getStockName());
      stock.setDate(noteDate);
      stock.setLastModified(LocalDate.now());
      repository.save(stock);
    } else {
      StockPosition stock = stockDay;
      updateAverageQuantity(op, stocks, stock);
      stock.setStockName(op.getStockName());
      stock.setDate(noteDate);
      stock.setLastModified(LocalDate.now());
      repository.save(stock);
    }

    return stocks;
  }

  private void updateAverageQuantity(Operation op, List<StockPosition> stocks, StockPosition stockPosition) {
    AtomicReference<Double> averagePrice = new AtomicReference<>(0D);
    AtomicReference<Integer> quantity = new AtomicReference<>(0);
    AtomicReference<Double> totalPurchased = new AtomicReference<>(0D);
    stocks.forEach(
        stock -> {
          averagePrice.updateAndGet(v -> v + stock.getAveragePrice());
          quantity.updateAndGet(v -> v + stock.getQuantity());
          totalPurchased.updateAndGet(v -> v + stock.getTotalPurchased());
        });

    Double average = 0D;
    Double purchased = 0D;
    if (op.getType() == OperationType.BUY) {
      quantity.updateAndGet(v -> v + op.getQuantity());
      purchased = (op.getPurchasePrice() + totalPurchased.get());
    } else {
      quantity.updateAndGet(v -> v - op.getQuantity());
      purchased = (totalPurchased.get() - op.getPurchasePrice());
    }

    average = purchased / quantity.get();
    stockPosition.setTotalPurchased(purchased);
    stockPosition.setQuantity(quantity.get());
    stockPosition.setAveragePrice(average);
  }
}
