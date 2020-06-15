package br.com.paiva.financial.stock.trade.operation;

import br.com.paiva.financial.stock.trade.operation.request.OperationSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class OperationController {

  private final OperationService service;

  @RequestMapping(value = "/operation", method = RequestMethod.POST)
  public Operation createOperation(@RequestBody OperationDTO dto) {
    return service.create(dto);
  }

  @RequestMapping(value = "/operations", method = RequestMethod.GET)
  public List<Operation> findByFilter(
      @RequestParam(name = "broker", required = false) final String broker,
      @RequestParam(name = "referenceMonth", required = false) final String referenceMonth,
      @RequestParam(name = "stockName", required = false) final String stockName,
      @RequestParam(name = "tradingNoteCode", required = false) final String tradingNoteCode,
      @RequestParam(name = "type", required = false) final String type) {

    OperationSearchRequest operationSearchRequest = new OperationSearchRequest();
    operationSearchRequest.setReferenceMonth(referenceMonth);
    operationSearchRequest.setBroker(broker);
    operationSearchRequest.setTypeOperation(type);
    operationSearchRequest.setStockName(stockName);
    operationSearchRequest.setTradingNoteCode(tradingNoteCode);

    return service.findByFilter(operationSearchRequest);
  }
}
