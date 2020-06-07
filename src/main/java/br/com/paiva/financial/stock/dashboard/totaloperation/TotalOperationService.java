package br.com.paiva.financial.stock.dashboard.totaloperation;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TotalOperationService {

  private final TotalOperationRepository repository;

  public List<TotalOperation> findAll() {
    return repository.findAll(Sort.by(Sort.Order.asc("referenceMonth")));
  }

  public TotalOperation createOrUpdate(final LocalDate noteDate, final Operation operation) {
    TotalOperation totalOperationBD =
        repository.findByReferenceMonthAndStockName(
            noteDate.getMonth().getValue(), operation.getStockName());

      if (Objects.isNull(totalOperationBD)) {
        return createNewTotalOperation(noteDate, operation);
      } else {

        if (operation.getType() == OperationType.BUY) {
          totalOperationBD.setBuyQuantity(
              totalOperationBD.getBuyQuantity() + operation.getQuantity());
          totalOperationBD.setTotalPurchased(
              totalOperationBD.getTotalPurchased() + operation.getOperationPrice());

        } else {
          totalOperationBD.setSellQuantity(
              totalOperationBD.getSellQuantity() + operation.getQuantity());
          totalOperationBD.setTotalSold(
              totalOperationBD.getTotalSold() + operation.getOperationPrice());
        }

        totalOperationBD.setTotalEmoluments(
            totalOperationBD.getTotalEmoluments() + operation.getTaxes().getEmoluments());
        totalOperationBD.setTotalGainValue(
            totalOperationBD.getTotalGainValue() + operation.getGainValue());
        totalOperationBD.setTotalLiquidation(
            totalOperationBD.getTotalLiquidation() + operation.getTaxes().getLiquidation());

        totalOperationBD.setTotalIncomingTax(
            totalOperationBD.getTotalIncomingTax() + operation.getTaxes().getIncomingTax());
        Double totalDarf = totalOperationBD.getTotalDarf() + operation.getDarf();
        totalOperationBD.setTotalDarf(totalDarf < 0D ? 0D : totalDarf);
        totalOperationBD.setTotalOtherTaxes(
            totalOperationBD.getTotalOtherTaxes() + operation.getTaxes().getOtherTaxes());
        totalOperationBD.setTotalTaxes(
            totalOperationBD.getTotalTaxes() + operation.getTaxes().getTaxes());
        totalOperationBD.setTotalOperationalCosts(
            totalOperationBD.getTotalOperationalCosts()
                + operation.getTaxes().getOperationalCosts());
        totalOperationBD.setLastModified(LocalDate.now());
        return repository.save(totalOperationBD);
      }

  }

  private TotalOperation createNewTotalOperation(
      final LocalDate noteDate, final Operation operation) {
    TotalOperation totalOperation = new TotalOperation();
    if (operation.getType().equals(OperationType.BUY)) {
      totalOperation.setBuyQuantity(operation.getQuantity());
      totalOperation.setTotalPurchased(operation.getOperationPrice());
    }else{
      totalOperation.setTotalSold(operation.getOperationPrice());
      totalOperation.setSellQuantity(operation.getQuantity());
    }
    totalOperation.setReferenceMonth(noteDate.getMonth().getValue());
    totalOperation.setStockName(operation.getStockName());
    totalOperation.setTotalEmoluments(operation.getTaxes().getEmoluments());
    totalOperation.setTotalGainValue(operation.getGainValue());
    totalOperation.setTotalLiquidation(operation.getTaxes().getLiquidation());
    totalOperation.setTotalIncomingTax(operation.getTaxes().getIncomingTax());
    totalOperation.setTotalDarf(operation.getDarf());
    totalOperation.setTotalOtherTaxes(operation.getTaxes().getOtherTaxes());
    totalOperation.setTotalTaxes(operation.getTaxes().getTaxes());
    totalOperation.setTotalOperationalCosts(operation.getTaxes().getOperationalCosts());
    totalOperation.setLastModified(LocalDate.now());
    return repository.save(totalOperation);
  }
}
