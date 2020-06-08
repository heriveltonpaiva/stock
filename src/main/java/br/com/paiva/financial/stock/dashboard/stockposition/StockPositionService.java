package br.com.paiva.financial.stock.dashboard.stockposition;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import br.com.paiva.financial.stock.trade.tradingnote.BrokerType;
import br.com.paiva.financial.stock.util.StockUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockPositionService {

  private final StockPositionRepository repository;

  public List<StockPosition> findAll() {
    return repository.findAll(Sort.by(Sort.Order.asc("date")));
  }

  public List<StockPosition> findByStockName(final String stockName) {
    return repository.findByStockNameOrderByLastModifiedDesc(stockName);
  }

  public StockPosition createOrUpdate(final Operation op, final LocalDate noteDate, final BrokerType brokerType) {
      List<StockPosition> stocks =
          repository.findByStockNameOrderByLastModifiedDesc(op.getStockName());
      if(stocks != null){
        stocks = stocks.stream().filter(s -> s.getBroker().equals(brokerType)).collect(Collectors.toList());
      }
      StockPosition stockDay =
          repository.findByDateAndStockNameAndBrokerOrderByLastModifiedDesc(noteDate, op.getStockName(), brokerType);

      StockPosition lastStockPosition =
          stocks.stream()
              .filter(s -> s.getBroker().equals(brokerType))
              .findFirst()
              .orElse(new StockPosition());

      if (op.getType().equals(OperationType.BUY)) {
        return updateBuyStockPosition(
            op,
            lastStockPosition,
            ObjectUtils.isEmpty(stockDay) ? new StockPosition() : stockDay,
            noteDate,
            brokerType);
      } else {
        return updateSellStockPosition(op, lastStockPosition, noteDate, brokerType);
      }

  }

  private StockPosition updateSellStockPosition(Operation op, StockPosition lastStockPosition, final LocalDate noteDate, final BrokerType brokerType) {
    StockPosition stockPosition = new StockPosition();
    stockPosition.setTotalPurchased(StockUtils.round(lastStockPosition.getTotalPurchased() - op.getPurchasePrice()));
    stockPosition.setQuantity(lastStockPosition.getQuantity() - op.getQuantity());
    stockPosition.setAveragePrice(stockPosition.getQuantity() == 0 ? 0 : StockUtils.round(stockPosition.getTotalPurchased() / stockPosition.getQuantity()));
    stockPosition.setStockName(op.getStockName());
    stockPosition.setLastModified(LocalDateTime.now());
    stockPosition.setDate(noteDate);
    stockPosition.setBroker(brokerType);

    return repository.save(stockPosition);
  }

  private StockPosition updateBuyStockPosition(final Operation op, final StockPosition lastStockPosition, StockPosition stockPosition, final LocalDate noteDate, final BrokerType brokerType) {

    Integer quantity = 0;
    quantity = (lastStockPosition.getQuantity() < 0 ? 0: lastStockPosition.getQuantity()) + op.getQuantity();
    stockPosition.setTotalPurchased(calculatePurchasePrice(lastStockPosition, stockPosition, op));
    stockPosition.setQuantity(quantity);
    stockPosition.setAveragePrice(StockUtils.round(stockPosition.getTotalPurchased() / quantity));
    stockPosition.setStockName(op.getStockName());
    stockPosition.setLastModified(LocalDateTime.now());
    stockPosition.setDate(noteDate);
    stockPosition.setBroker(brokerType);

    return repository.save(stockPosition);
  }

  private Double calculatePurchasePrice(final StockPosition lastStockPosition, StockPosition stockPosition, final Operation op){

    if(lastStockPosition.getDate() != null && stockPosition.getDate() != null && lastStockPosition.getDate().equals(stockPosition.getDate())){
      Double purchased = op.getOperationPrice() + stockPosition.getTotalPurchased();
      return StockUtils.round(purchased);
    }
    Double purchased = lastStockPosition.getTotalPurchased() < 0D ?  0D : lastStockPosition.getTotalPurchased() + stockPosition.getTotalPurchased();
    purchased = (op.getPurchasePrice() + purchased);

    return StockUtils.round(purchased);
  }

}
