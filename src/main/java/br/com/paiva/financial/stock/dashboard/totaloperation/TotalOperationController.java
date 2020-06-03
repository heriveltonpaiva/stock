package br.com.paiva.financial.stock.dashboard.totaloperation;

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
}
