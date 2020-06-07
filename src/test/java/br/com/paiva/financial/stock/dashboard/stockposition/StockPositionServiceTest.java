package br.com.paiva.financial.stock.dashboard.stockposition;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static br.com.paiva.financial.stock.dashboard.stockposition.StockFactory.*;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockPositionServiceTest {

  @InjectMocks private StockPositionService service;

  @Mock private StockPositionRepository repository;

  @Test
  public void validateCreateStockPosition() {
    when(repository.findByDateAndStockName(any(), anyString())).thenReturn(null);
    when(repository.findByStockNameOrderByDateDesc(anyString())).thenReturn(emptyList());
    StockPosition stock = service.createOrUpdate(operation(), LocalDate.now());
    Assertions.assertEquals(stockPositionCreate(), stock);
  }

  @Test
  public void validateUpdateStockPosition() {
    when(repository.findByDateAndStockName(any(), anyString()))
        .thenReturn(StockFactory.stockPositionCreate());
    when(repository.findByStockNameOrderByDateDesc(anyString()))
        .thenReturn(Arrays.asList(StockFactory.stockPositionCreate()));
    StockPosition stock = service.createOrUpdate(operation(), LocalDate.now());
    Assertions.assertEquals(stockPositionUpdate(), stock);
  }
}
