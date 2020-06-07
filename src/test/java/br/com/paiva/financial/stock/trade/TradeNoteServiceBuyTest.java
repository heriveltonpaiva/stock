package br.com.paiva.financial.stock.trade;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationDTO;
import br.com.paiva.financial.stock.trade.operation.OperationService;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import br.com.paiva.financial.stock.trade.tax.Tax;
import br.com.paiva.financial.stock.trade.tax.TaxDTO;
import br.com.paiva.financial.stock.trade.tax.TaxService;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNoteDTO;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNoteRepository;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TradeNoteServiceBuyTest {

  @InjectMocks private TradingNoteService service;
  @Mock private TaxService taxService;
  @Mock private TradingNoteRepository repository;

  @Mock private OperationService operationService;

  @Test
  public void validateTotalPriceList() throws ParseException {
    TradingNoteDTO input = getTradingNoteDTO();
    TradingNote note = service.createTradingNote(input);
    assertEquals(note.getValue(), input.getValue());
  }

  @Test
  public void validatePurchasePriceList() throws ParseException {
    TradingNoteDTO input = getTradingNoteDTO();
    TradingNote note = service.createTradingNote(input);

    Operation op = note.getOperationList().get(0);
    TradingNote td = new TradingNote();
    when(taxService.calculateTaxes(td, op.getOperationPrice(), OperationType.BUY))
        .thenReturn(tax());
    Double purchase = op.getTaxes().getTotalValue() + op.getOperationPrice();
    Double average = op.getPurchasePrice() / op.getQuantity();
    Double unitPrice = op.getOperationPrice() / op.getQuantity();
    assertEquals(op.getPurchasePrice(), purchase);
    assertEquals(op.getAveragePrice(), average);
    assertEquals(op.getUnitPrice(), unitPrice);
    assertEquals(op.getTaxes().getIncomingTax(), 0D);
    assertEquals(op.getDarf(), 0D);
  }

  @Test
  public void validateTotalTaxPriceList() throws ParseException {
    TradingNoteDTO input = getTradingNoteDTO();
    TradingNote note = service.createTradingNote(input);

    AtomicReference<Double> totalEmoluments = new AtomicReference<>(new Double(0));
    AtomicReference<Double> totalLiquidation = new AtomicReference<>(new Double(0));
    AtomicReference<Double> totalTaxes = new AtomicReference<>(new Double(0));
    AtomicReference<Double> totalBrokerage = new AtomicReference<>(new Double(0));
    AtomicReference<Double> totalOtherTaxes = new AtomicReference<>(new Double(0));

    note.getOperationList()
        .forEach(
            it -> {
              totalEmoluments.updateAndGet(v -> v + it.getTaxes().getEmoluments());
              totalLiquidation.updateAndGet(v -> v + it.getTaxes().getLiquidation());
              totalTaxes.updateAndGet(v -> v + it.getTaxes().getTaxes());
              totalBrokerage.updateAndGet(v -> v + it.getTaxes().getBrokerage());
              totalOtherTaxes.updateAndGet(v -> v + it.getTaxes().getOtherTaxes());
            });

    assertEquals(note.getTaxes().getEmoluments(), round(totalEmoluments.get()));
    assertEquals(note.getTaxes().getLiquidation().doubleValue(), round(totalLiquidation.get()));
    assertEquals(note.getTaxes().getBrokerage().doubleValue(), round(totalBrokerage.get()));
    assertEquals(note.getTaxes().getTaxes().doubleValue(), round(totalTaxes.get()));
    assertEquals(note.getTaxes().getOtherTaxes().doubleValue(), round(totalOtherTaxes.get()));

    Double totalTax =
        totalEmoluments.get()
            + totalLiquidation.get()
            + totalBrokerage.get()
            + totalOtherTaxes.get()
            + totalTaxes.get();
    Double operationalCosts = totalBrokerage.get() + totalOtherTaxes.get() + totalTaxes.get();
    assertEquals(note.getTaxes().getTotalValue(), round(totalTax));
    assertEquals(note.getTaxes().getOperationalCosts(), operationalCosts);
  }

  private TaxDTO taxDTO() {
    return TaxDTO.builder()
        .liquidation(1.49)
        .brokerage(56.70)
        .emoluments(0.16)
        .taxes(6.05)
        .incomingTax(0D)
        .otherTaxes(2.21)
        .build();
  }

  private Tax tax() {
    return Tax.builder()
        .liquidation(1.49)
        .brokerage(56.70)
        .emoluments(0.16)
        .taxes(6.05)
        .incomingTax(0D)
        .otherTaxes(2.21)
        .build();
  }

  private TradingNoteDTO getTradingNoteDTO() {

    final OperationDTO op1 = new OperationDTO();
    op1.setStockName("COGN3");
    op1.setQuantity(300);
    op1.setOperationPrice(1446.0);
    op1.setType(OperationType.BUY.name());

    final OperationDTO op2 = new OperationDTO();
    op2.setStockName("GUAR3");
    op2.setQuantity(200);
    op2.setOperationPrice(2376.0);
    op2.setType(OperationType.BUY.name());

    final OperationDTO op3 = new OperationDTO();
    op3.setStockName("VVAR3");
    op3.setQuantity(300);
    op3.setOperationPrice(1617.0);
    op3.setType(OperationType.BUY.name());
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    return TradingNoteDTO.builder()
        .broker("XP")
        .date(simpleDateFormat.format(new Date()))
        .stocks(Arrays.asList(op1, op2, op3))
        .taxes(taxDTO())
        .value(5439.0)
        .build();
  }

  private Double round(final Double value) {
    NumberFormat nf = new DecimalFormat("0.00");
    nf.setRoundingMode(RoundingMode.HALF_UP);
    return Double.parseDouble(nf.format(value).replace(",", "."));
  }
}
