package br.com.paiva.financial.stock.dashboard.stockposition;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import br.com.paiva.financial.stock.util.StockUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockPositionService {

  private final StockPositionRepository repository;

  public List<StockPosition> findAll() {
    return repository.findAll(Sort.by(Sort.Order.asc("date")));
  }

  public List<StockPosition> findByStockName(final String stockName) {
    return repository.findByStockNameOrderByDateDesc(stockName);
  }

  public StockPosition createOrUpdate(final Operation op, final LocalDate noteDate) {
    List<StockPosition> stocks = repository.findByStockNameOrderByDateDesc(op.getStockName());
    StockPosition stockDay = repository.findByDateAndStockName(noteDate, op.getStockName());
    StockPosition lastStockPosition = CollectionUtils.isEmpty(stocks) ? new StockPosition() : stocks.stream().findFirst().get();

      if (op.getType().equals(OperationType.BUY)) {
        return updateBuyStockPosition(
            op,
            lastStockPosition,
            ObjectUtils.isEmpty(stockDay) ? new StockPosition() : stockDay,
            noteDate);
      } else {
        return updateSellStockPosition(op, lastStockPosition, noteDate);
      }

  }

  private StockPosition updateSellStockPosition(Operation op, StockPosition lastStockPosition, final LocalDate noteDate) {
    StockPosition stockPosition = new StockPosition();
    stockPosition.setTotalPurchased(StockUtils.round(lastStockPosition.getTotalPurchased() - op.getPurchasePrice()));
    stockPosition.setQuantity(lastStockPosition.getQuantity() - op.getQuantity());
    stockPosition.setAveragePrice(stockPosition.getQuantity() == 0 ? 0 : StockUtils.round(stockPosition.getTotalPurchased() / stockPosition.getQuantity()));
    stockPosition.setStockName(op.getStockName());
    stockPosition.setLastModified(LocalDate.now());
    stockPosition.setDate(noteDate);
    return repository.save(stockPosition);
  }

  private StockPosition updateBuyStockPosition(final Operation op, final StockPosition lastStockPosition, StockPosition stockPosition, final LocalDate noteDate) {
    Double purchased = lastStockPosition.getTotalPurchased() < 0D ?  0D : lastStockPosition.getTotalPurchased() + stockPosition.getTotalPurchased();
    Integer quantity = 0;
    quantity = (lastStockPosition.getQuantity() < 0 ? 0: lastStockPosition.getQuantity()) + op.getQuantity();
    purchased = (op.getPurchasePrice() + purchased);
    stockPosition.setTotalPurchased(StockUtils.round(purchased));
    stockPosition.setQuantity(quantity);
    stockPosition.setAveragePrice(StockUtils.round(purchased / quantity));
    stockPosition.setStockName(op.getStockName());
    stockPosition.setLastModified(LocalDate.now());
    stockPosition.setDate(noteDate);
    return repository.save(stockPosition);
  }

}
