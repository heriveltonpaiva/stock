package br.com.paiva.financial.stock.dashboard.totaloperation;

import br.com.paiva.financial.stock.trade.operation.OperationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static br.com.paiva.financial.stock.dashboard.totaloperation.TotalOperationFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TotalOperationServiceTest {

    @InjectMocks
    private TotalOperationService service;

    @Mock
    private TotalOperationRepository repository;

    @Test
    public void validateCreateTotalBuyOperation() {
        when(repository.findByReferenceMonthAndStockName(any(), anyString())).thenReturn(null);
        TotalOperation totalOperation = service.createOrUpdate("CVCB", OperationType.BUY, LocalDate.now(), Arrays.asList(operationBUY(100)));
        Assertions.assertEquals(totalOperation, totalOperationBUY(100));
    }
    @Test
    public void validateUpdateTotalBuyOperation() {
        when(repository.findByReferenceMonthAndStockName(any(), anyString())).thenReturn(totalOperationBUY(300));
        TotalOperation totalOperation = service.createOrUpdate("CVCB", OperationType.BUY, LocalDate.now(), Arrays.asList(operationBUY(100)));
        Assertions.assertEquals(totalOperation, totalOperationBUY(1100));
    }

    @Test
    public void validateCreateTotalSellOperation() {
        when(repository.findByReferenceMonthAndStockName(any(), anyString())).thenReturn(null);
        TotalOperation totalOperationSell = service.createOrUpdate("CVCB", OperationType.SELL, LocalDate.now(), Arrays.asList(operationSELL(100)));

        TotalOperation total = totalOperationSELL(100);
        Assertions.assertEquals(totalOperationSell, total);
    }

    @Test
    public void validateUpdateTotalSellOperation() {
        when(repository.findByReferenceMonthAndStockName(any(), anyString())).thenReturn(totalOperationSELL(100));
        TotalOperation totalOperationSell = service.createOrUpdate("CVCB", OperationType.BUY, LocalDate.now(), Arrays.asList(operationSELL(100)));

        TotalOperation total = totalOperationSELL(300);
        Assertions.assertEquals(total, totalOperationSell);
    }

}
