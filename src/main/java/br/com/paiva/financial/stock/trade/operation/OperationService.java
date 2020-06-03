package br.com.paiva.financial.stock.trade.operation;

import br.com.paiva.financial.stock.dashboard.stockposition.StockPositionService;
import br.com.paiva.financial.stock.dashboard.totaloperation.TotalOperationService;
import br.com.paiva.financial.stock.trade.tax.TaxService;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNoteRepository;
import br.com.paiva.financial.stock.util.StockUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationService {

  private static final Double SWING_TRADE_TAX = 0.15;

  private final OperationRepository repository;

  private final TaxService taxService;

  private final TradingNoteRepository tradingNoteRepository;

  private final StockPositionService stockPositionService;

  private final TotalOperationService totalOperationService;

  public List<Operation> findByStockName(final String stockName) {
    return repository.findByStockName(stockName);
  }

  public List<Operation> findAll() {
    return repository.findAll();
  }

  public Operation createOperation(final OperationDTO dto, final TradingNote note) {
    Operation op = new Operation();

    if (dto.getPurchasePrice() == null) {
      op.setPurchasePrice(0D);
    }

    op.setTradingNoteCode(note.getCode());
    op.setType(OperationType.valueOf(dto.getType()));
    op.setStockName(dto.getStockName());
    op.setOperationPrice(dto.getOperationPrice());
    op.setQuantity(dto.getQuantity());
    op.setUnitPrice(op.getOperationPrice() / op.getQuantity());

    op.setTaxes(taxService.createOperationTax(note, dto.getOperationPrice(), op.getType()));
    op.setPurchasePrice(
        OperationType.valueOf(dto.getType()) == OperationType.BUY
            ? op.getOperationPrice() + op.getTaxes().getTotalValue()
            : dto.getPurchasePrice());
    op.setAveragePrice(
        OperationType.valueOf(dto.getType()) == OperationType.BUY
            ? op.getOperationPrice() / op.getQuantity()
            : dto.getPurchasePrice() / op.getQuantity());
    op.setGainValue(getGainValue(op));
    op.setDarf(getDarf(op));
    save(op);

    LocalDate noteDate = note.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    stockPositionService.createOrUpdate(op, noteDate);
    List<Operation> operations = findByStockName(op.getStockName());
    totalOperationService.createOrUpdate(op, noteDate, operations);

    return op;
  }

  private Double getDarf(final Operation op) {
    if (op.getType().equals(OperationType.SELL)) {
      return op.getGainValue() * SWING_TRADE_TAX;
    }
    return 0D;
  }

  private Double getGainValue(final Operation op) {
    if (op.getType().equals(OperationType.SELL)) {
      return StockUtils.round(
          op.getOperationPrice()
              - op.getPurchasePrice()
              - op.getTaxes().getTotalValue()
              - op.getTaxes().getIncomingTax());
    }
    return BigDecimal.ZERO.doubleValue();
  }

  public Operation save(final Operation operation) {
    return repository.save(operation);
  }

  public Operation create(OperationDTO dto) {
    Operation op = new Operation();
    TradingNote note = tradingNoteRepository.findByCode(dto.getTradingNoteCode());

    if (Objects.nonNull(note)) {
      if (Objects.isNull(dto.getPurchasePrice())) {
        op.setPurchasePrice(0D);
      }

      op.setTradingNoteCode(note.getCode());
      op.setType(OperationType.valueOf(dto.getType()));
      op.setStockName(dto.getStockName());
      op.setOperationPrice(dto.getOperationPrice());
      op.setQuantity(dto.getQuantity());
      op.setUnitPrice(op.getOperationPrice() / op.getQuantity());

      op.setTaxes(taxService.createOperationTax(note, dto.getOperationPrice(), op.getType()));
      op.setPurchasePrice(
          OperationType.valueOf(dto.getType()) == OperationType.BUY
              ? op.getOperationPrice() + op.getTaxes().getTotalValue()
              : dto.getPurchasePrice());
      op.setAveragePrice(
          OperationType.valueOf(dto.getType()) == OperationType.BUY
              ? op.getOperationPrice() / op.getQuantity()
              : dto.getPurchasePrice() / op.getQuantity());
      op.setGainValue(getGainValue(op));
      op.setDarf(getDarf(op));
      save(op);
      LocalDate noteDate = note.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      stockPositionService.createOrUpdate(op, noteDate);
      List<Operation> operations = findByStockName(op.getStockName());
      totalOperationService.createOrUpdate(op, noteDate, operations);

      return op;
    }
    log.error("Trading note i'snt exist!");
    return null;
  }
}
