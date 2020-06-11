package br.com.paiva.financial.stock.dashboard.totaloperation;

import br.com.paiva.financial.stock.dashboard.totaloperation.month.TotalOperationMonth;
import br.com.paiva.financial.stock.dashboard.totaloperation.month.TotalOperationMonthRepository;
import br.com.paiva.financial.stock.dashboard.totaloperation.year.TotalOperationYear;
import br.com.paiva.financial.stock.dashboard.totaloperation.year.TotalOperationYearRepository;
import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TotalOperationService {

  private final TotalOperationRepository repository;
  private final TotalOperationMonthRepository monthRepository;
  private final TotalOperationYearRepository yearRepository;

  public List<TotalOperation> findAll() {
    return repository.findAll(Sort.by(Sort.Order.asc("referenceMonth")));
  }

  public List<TotalOperationMonth> findAllMonth() {
    return monthRepository.findAll(Sort.by(Sort.Order.asc("referenceMonth")));
  }

  public List<TotalOperationYear> findAllYear() {
    return yearRepository.findAll(Sort.by(Sort.Order.asc("referenceMonth")));
  }

  public Set<TotalOperationMonth> reprocessTotalOperationMonth(
      final List<TotalOperation> totalOperations) {
    Map<Integer, List<TotalOperation>> map = new HashMap<>();
    totalOperations.forEach(
        totalOperation -> {
          if (!map.containsKey(totalOperation.getReferenceMonth())) {
            map.put(totalOperation.getReferenceMonth(), new ArrayList<>());
          }
          map.get(totalOperation.getReferenceMonth()).add(totalOperation);
        });

    Set<TotalOperationMonth> totalOperationMonths =
        map.entrySet().stream()
            .map(obj -> calculateTotalOperationMonth(obj.getKey(), obj.getValue()))
            .collect(Collectors.toSet());

    return totalOperationMonths;
  }

  private TotalOperationMonth calculateTotalOperationMonth(
      Integer month, List<TotalOperation> totalOperations) {
    TotalOperationMonth total = new TotalOperationMonth();
    total.setReferenceMonth(month);
    totalOperations.stream()
        .forEach(
            it -> {
              total.setBuyQuantity(total.getBuyQuantity() + it.getBuyQuantity());
              total.setSellQuantity(total.getSellQuantity() + it.getSellQuantity());
              total.setTotalDarf(total.getTotalDarf() + it.getTotalDarf());
              total.setTotalGainValue(total.getTotalGainValue() + it.getTotalGainValue());
              total.setTotalEmoluments(total.getTotalEmoluments() + it.getTotalEmoluments());
              total.setTotalLiquidation(total.getTotalLiquidation() + it.getTotalLiquidation());
              total.setTotalIncomingTax(total.getTotalIncomingTax() + it.getTotalIncomingTax());
              total.setTotalOtherTaxes(total.getTotalOtherTaxes() + it.getTotalOtherTaxes());
              total.setTotalSold(total.getTotalSold() + it.getTotalSold());
              total.setTotalPurchased(total.getTotalPurchased() + it.getTotalPurchased());
              total.setTotalBrokerage(total.getTotalBrokerage() + it.getTotalBrokerage());
            });

    total.setTotalOperationalCosts(
        total.getTotalTaxes()
            + total.getTotalBrokerage()
            + total.getTotalOtherTaxes()
            + total.getTotalDarf()
            + total.getTotalIncomingTax());
    total.setTotalTaxes(
        total.getTotalLiquidation()
            + total.getTotalEmoluments()
            + total.getTotalOperationalCosts());

    return monthRepository.save(total);
  }

  public TotalOperationYear reprocessTotalOperationYear(
      final Integer year, final List<TotalOperationMonth> totalOperations) {
    TotalOperationYear total = new TotalOperationYear();
    total.setReferenceYear(year);
    totalOperations.stream()
        .forEach(
            it -> {
              total.setBuyQuantity(total.getBuyQuantity() + it.getBuyQuantity());
              total.setSellQuantity(total.getSellQuantity() + it.getSellQuantity());
              total.setTotalDarf(total.getTotalDarf() + it.getTotalDarf());
              total.setTotalGainValue(total.getTotalGainValue() + it.getTotalGainValue());
              total.setTotalEmoluments(total.getTotalEmoluments() + it.getTotalEmoluments());
              total.setTotalLiquidation(total.getTotalLiquidation() + it.getTotalLiquidation());
              total.setTotalIncomingTax(total.getTotalIncomingTax() + it.getTotalIncomingTax());
              total.setTotalOtherTaxes(total.getTotalOtherTaxes() + it.getTotalOtherTaxes());
              total.setTotalSold(total.getTotalSold() + it.getTotalSold());
              total.setTotalBrokerage(total.getTotalBrokerage() + it.getTotalBrokerage());
              total.setTotalPurchased(total.getTotalPurchased() + it.getTotalPurchased());
            });
    total.setTotalOperationalCosts(
        total.getTotalTaxes()
            + total.getTotalBrokerage()
            + total.getTotalOtherTaxes()
            + total.getTotalDarf()
            + total.getTotalIncomingTax());
    total.setTotalTaxes(
        total.getTotalLiquidation()
            + total.getTotalEmoluments()
            + total.getTotalOperationalCosts());
    return yearRepository.save(total);
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
      totalOperationBD.setTotalBrokerage(totalOperationBD.getTotalBrokerage() + operation.getTaxes().getBrokerage());
      totalOperationBD.setTotalOtherTaxes(
          totalOperationBD.getTotalOtherTaxes() + operation.getTaxes().getOtherTaxes());
      totalOperationBD.setTotalTaxes(
          totalOperationBD.getTotalTaxes() + operation.getTaxes().getTaxes());
      totalOperationBD.setTotalOperationalCosts(
          totalOperationBD.getTotalOperationalCosts() + operation.getTaxes().getOperationalCosts());
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
    } else {
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
    totalOperation.setTotalBrokerage(operation.getTaxes().getBrokerage());
    totalOperation.setTotalOtherTaxes(operation.getTaxes().getOtherTaxes());
    totalOperation.setTotalTaxes(operation.getTaxes().getTaxes());
    totalOperation.setTotalOperationalCosts(operation.getTaxes().getOperationalCosts());
    totalOperation.setLastModified(LocalDate.now());
    return repository.save(totalOperation);
  }
}
