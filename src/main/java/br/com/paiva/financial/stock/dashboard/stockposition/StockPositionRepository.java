package br.com.paiva.financial.stock.dashboard.stockposition;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockPositionRepository extends MongoRepository<StockPosition, String> {

    StockPosition findByDateAndStockName(final LocalDate date, final String stockName);

    List<StockPosition> findByStockName(final String stockName);
}
