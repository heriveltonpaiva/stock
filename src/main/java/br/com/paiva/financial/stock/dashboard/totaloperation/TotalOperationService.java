package br.com.paiva.financial.stock.dashboard.totaloperation;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationService;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    return repository.findAll();
  }

  public TotalOperation createOrUpdate(final Operation op, final LocalDate noteDate, final List<Operation> operations) {
    TotalOperation totalOperationBD =
        repository.findByReferenceMonthAndStockName(
            noteDate.getMonth().getValue(), op.getStockName());

    if (Objects.isNull(totalOperationBD)) {
      TotalOperation totalOperation = new TotalOperation();

      operations.forEach(
          x -> {
            if (op.getType() == OperationType.BUY) {
              totalOperation.setBuyQuantity(x.getQuantity());
              totalOperation.setTotalPurchased(x.getOperationPrice());

            } else {
              totalOperation.setSellQuantity(x.getQuantity());
              totalOperation.setTotalSold(x.getOperationPrice());
            }
            totalOperation.setReferenceMonth(noteDate.getMonth().getValue());
            totalOperation.setStockName(x.getStockName());
            totalOperation.setTotalEmoluments(x.getTaxes().getEmoluments());
            totalOperation.setTotalGainValue(x.getGainValue());
            totalOperation.setTotalLiquidation(x.getTaxes().getLiquidation());
            totalOperation.setTotalIncomingTax(x.getTaxes().getIncomingTax());
            totalOperation.setTotalDarf(x.getDarf());
            totalOperation.setTotalOtherTaxes(x.getTaxes().getOtherTaxes());
            totalOperation.setTotalTaxes(x.getTaxes().getTaxes());
            totalOperation.setTotalOperationalCosts(x.getTaxes().getOperationalCosts());
            totalOperation.setLastModified(LocalDate.now());
          });
      repository.save(totalOperation);
      return totalOperation;
    } else {
      operations.forEach(
          x -> {
            if (op.getType() == OperationType.BUY) {
              totalOperationBD.setBuyQuantity(totalOperationBD.getBuyQuantity() + x.getQuantity());
              totalOperationBD.setTotalPurchased(
                  totalOperationBD.getTotalPurchased() + x.getOperationPrice());

            } else {
              totalOperationBD.setSellQuantity(
                  totalOperationBD.getSellQuantity() + x.getQuantity());
              totalOperationBD.setTotalSold(
                  totalOperationBD.getTotalSold() + x.getOperationPrice());
            }

            totalOperationBD.setTotalEmoluments(
                totalOperationBD.getTotalEmoluments() + x.getTaxes().getEmoluments());
            totalOperationBD.setTotalGainValue(
                totalOperationBD.getTotalGainValue() + x.getGainValue());
            totalOperationBD.setTotalLiquidation(
                totalOperationBD.getTotalLiquidation() + x.getTaxes().getLiquidation());
            totalOperationBD.setTotalIncomingTax(
                totalOperationBD.getTotalIncomingTax() + x.getTaxes().getIncomingTax());
            totalOperationBD.setTotalDarf(totalOperationBD.getTotalDarf() + x.getDarf());
            totalOperationBD.setTotalOtherTaxes(
                totalOperationBD.getTotalOtherTaxes() + x.getTaxes().getOtherTaxes());
            totalOperationBD.setTotalTaxes(
                totalOperationBD.getTotalTaxes() + x.getTaxes().getTaxes());
            totalOperationBD.setTotalOperationalCosts(
                totalOperationBD.getTotalOperationalCosts() + x.getTaxes().getOperationalCosts());
            totalOperationBD.setLastModified(LocalDate.now());
          });
      repository.save(totalOperationBD);
      return totalOperationBD;
    }
  }
}
