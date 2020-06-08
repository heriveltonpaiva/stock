package br.com.paiva.financial.stock.trade.operation;

import br.com.paiva.financial.stock.dashboard.stockposition.StockPosition;
import br.com.paiva.financial.stock.dashboard.stockposition.StockPositionService;
import br.com.paiva.financial.stock.dashboard.totaloperation.TotalOperationService;
import br.com.paiva.financial.stock.trade.tax.TaxService;
import br.com.paiva.financial.stock.trade.tradingnote.BrokerType;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNoteService;
import br.com.paiva.financial.stock.util.StockUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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

  private final TradingNoteService tradingNoteService;

  private final StockPositionService stockPositionService;

  private final TotalOperationService totalOperationService;

  public List<Operation> findByStockName(final String stockName) {
    return repository.findByStockName(stockName);
  }

  public List<Operation> findAll() {
    return repository.findAll(Sort.by(Sort.Order.asc("tradingNoteCode")));
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

  public Operation create(OperationDTO dto){
    TradingNote note = tradingNoteService.findByCode(dto.getTradingNoteCode());

    if (Objects.nonNull(note)) {
      log.info("TradingNote code={} was found", note.getCode());
      Operation op = new Operation();
      op.setTradingNoteCode(note.getCode());
      op.setType(OperationType.valueOf(dto.getType()));
      op.setStockName(dto.getStockName());
      op.setOperationPrice(dto.getOperationPrice());
      op.setQuantity(dto.getQuantity());
      op.setUnitPrice(op.getOperationPrice() / op.getQuantity());
      op.setTaxes(taxService.calculateTaxes(note, dto.getOperationPrice(), op.getType()));

      log.info("Taxes created totalTaxes={}"+op.getTaxes().getTotalValue());
      op.getTaxes().setBrokerage(note.getBroker().equals(BrokerType.XP) ? 18.9 : 0D);

      calculateOperationValues(op, note.getBroker());

      save(op);
      log.info("Operation was created successfully. operation={}", op);

      updateTotalOperation(op);
      updateStockPosition(op);

      return op;
    }
    log.error("Trading note i'snt exist!");
    return null;
  }

  private void calculateOperationValues(final Operation op, final BrokerType brokerType){
    if(op.getType() == OperationType.SELL){
      List<StockPosition> stockPositions = stockPositionService.findByStockName(op.getStockName());
      StockPosition lastStockPosition = stockPositions.stream().filter(s -> s.getBroker().equals(brokerType)).findFirst().get();
      log.info("Stock position was found stockPosition={}",lastStockPosition);
      log.info("{} Stock position purchasePrice={}", op.getStockName(), lastStockPosition.getTotalPurchased());

      op.setPurchasePrice(StockUtils.getToY(lastStockPosition.getQuantity(), lastStockPosition.getTotalPurchased(), op.getQuantity()));
      op.setAveragePrice(op.getPurchasePrice() / op.getQuantity());
      op.setGainValue(getGainValue(op));
      op.setDarf(getDarf(op));
      log.info("SELL Operation gainValue={} DARF={}", op.getGainValue(), op.getDarf());
    } else {
      op.setPurchasePrice(op.getOperationPrice() + op.getTaxes().getTotalValue());
      op.setAveragePrice(op.getPurchasePrice() / op.getQuantity());
    }
    log.info("PurchasePrice calculated (operationPrice + totalTaxes)={}"+op.getPurchasePrice());
    log.info("AveragePrice calculated (purchasePrice / quantity)={}"+op.getAveragePrice());
  }

  public void reprocessTotalOperation(){
    List<Operation> operations = findAll();
    operations.forEach(op ->{
      updateTotalOperation(op);
    });
  }

  private void updateTotalOperation(Operation op) {
    TradingNote note = tradingNoteService.findByCode(op.getTradingNoteCode());
    LocalDate noteDate = note.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    totalOperationService.createOrUpdate(noteDate, op);
  }

  public void reprocessStockPosition(){
    List<Operation> operations = findAll();
    operations.forEach(op ->{
        updateStockPosition(op);
    });
  }

  public void updateStockPosition(final Operation op){
    TradingNote note = tradingNoteService.findByCode(op.getTradingNoteCode());
    LocalDate noteDate = note.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    stockPositionService.createOrUpdate(op, noteDate, note.getBroker());
  }
}
