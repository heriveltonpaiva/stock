package br.com.paiva.financial.stock;

import br.com.paiva.financial.stock.dashboard.stockposition.StockPositionRepository;
import br.com.paiva.financial.stock.dashboard.stockposition.StockPositionService;
import br.com.paiva.financial.stock.dashboard.totaloperation.TotalOperationRepository;
import br.com.paiva.financial.stock.dashboard.totaloperation.month.TotalOperationMonthRepository;
import br.com.paiva.financial.stock.dashboard.totaloperation.year.TotalOperationYearRepository;
import br.com.paiva.financial.stock.trade.operation.OperationRepository;
import br.com.paiva.financial.stock.trade.operation.OperationRepositoryImpl;
import br.com.paiva.financial.stock.trade.operation.OperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class StockApplication implements CommandLineRunner {

  @Autowired private OperationRepository repository;

  @Autowired private OperationService operationService;
  @Autowired private TotalOperationRepository totalOperationRepository;
  @Autowired private TotalOperationMonthRepository totalOperationMonthRepository;
  @Autowired private TotalOperationYearRepository totalOperationYearRepository;
  @Autowired private StockPositionRepository stockPositionRepository;
  @Autowired private StockPositionService stockPositionService;
  @Autowired private OperationRepositoryImpl operationRepository;
  public static void main(String[] args) {

    SpringApplication.run(StockApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    totalOperationYearRepository.deleteAll();
    totalOperationMonthRepository.deleteAll();
    stockPositionRepository.deleteAll();
    operationService.reprocessStockPosition();
    totalOperationRepository.deleteAll();
    operationService.reprocessTotalOperation();

  }
}
