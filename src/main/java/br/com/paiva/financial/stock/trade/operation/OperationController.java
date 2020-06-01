package br.com.paiva.financial.stock.trade.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class OperationController {

  private final OperationService service;

  @RequestMapping(value = "/operations", method = RequestMethod.GET)
  public List<Operation> findAll() {
    return service.findAll();
  }
}
