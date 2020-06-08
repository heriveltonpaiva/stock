package br.com.paiva.financial.stock.dashboard.stockposition;

import br.com.paiva.financial.stock.trade.tradingnote.BrokerType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockPositionRepository extends MongoRepository<StockPosition, String> {

    StockPosition findByDateAndStockNameAndBrokerOrderByLastModifiedDesc(final LocalDate date, final String stockName, final BrokerType brokerType);

    List<StockPosition> findByStockNameOrderByLastModifiedDesc(final String stockName);
}
