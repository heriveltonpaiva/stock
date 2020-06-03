package br.com.paiva.financial.stock.trade.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class OperationController {

  private final OperationService service;

  @RequestMapping(value = "/operation", method = RequestMethod.POST)
  public Operation createOperation(@RequestBody OperationDTO dto){
    return service.create(dto);
  }
  @RequestMapping(value = "/operations", method = RequestMethod.GET)
  public List<Operation> findAll() {
    return service.findAll();
  }
}
