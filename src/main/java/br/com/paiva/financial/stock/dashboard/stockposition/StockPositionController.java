package br.com.paiva.financial.stock.dashboard.stockposition;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class StockPositionController {

  private final StockPositionService service;

  @RequestMapping(value = "/stock-position", method = RequestMethod.GET)
  public List<StockPosition> findAll() {
    return service.findAll();
  }
}
