package br.com.paiva.financial.stock.dashboard.totaloperation;

import br.com.paiva.financial.stock.dashboard.totaloperation.month.TotalOperationMonth;
import br.com.paiva.financial.stock.dashboard.totaloperation.year.TotalOperationYear;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class TotalOperationController {

  private final TotalOperationService  service;

  @RequestMapping(value = "/total-operations", method = RequestMethod.GET)
  public List<TotalOperation> findAll() {
    return service.findAll();
  }

  @RequestMapping(value = "/total-operations/month", method = RequestMethod.GET)
  public List<TotalOperationMonth> findAllMonth() {
    return service.findAllMonth();
  }

  @RequestMapping(value = "/total-operations/year", method = RequestMethod.GET)
  public List<TotalOperationYear> findAllYear() {
    return service.findAllYear();
  }

}
