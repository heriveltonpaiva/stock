package br.com.paiva.financial.stock.trade.operation;

import br.com.paiva.financial.stock.dashboard.stockposition.StockPositionService;
import br.com.paiva.financial.stock.trade.tax.Tax;
import br.com.paiva.financial.stock.trade.tax.TaxService;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTest {

  @InjectMocks private OperationService operationService;
  @Mock private TradingNoteService tradingNoteService;
  @Mock private TaxService taxService;
  @Mock private OperationRepository repository;
  @Mock private StockPositionService stockPositionService;

  @Test
  public void validateCreateOperation() {

    when(tradingNoteService.findByCode(any())).thenReturn(BuyOperationFactory.tradingNote());
    when(taxService.calculateTaxes(any(), any(), any())).thenReturn(BuyOperationFactory.tax());
    when(repository.save(any())).thenReturn(any());
    Operation op = operationService.create(BuyOperationFactory.operationDTO());

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
  public void validateCreateOperationWith2Stocks() {
    TradingNote tradingNote = Buy2OperationFactory.tradingNote();
    OperationDTO gol = Buy2OperationFactory.operationGOLL4DTO();
    OperationDTO petr = Buy2OperationFactory.operationPETR4DTO();

    when(tradingNoteService.findByCode(any())).thenReturn(tradingNote);
    when(taxService.calculateTaxes(tradingNote, gol.getOperationPrice(), OperationType.BUY)).thenReturn(Buy2OperationFactory.taxGol());
    when(repository.save(any())).thenReturn(any());
    Operation op = operationService.create(Buy2OperationFactory.operationGOLL4DTO());

    Double purchase = op.getTaxes().getTotalValue() + op.getOperationPrice();
    Double average = op.getPurchasePrice() / op.getQuantity();
    Double unitPrice = op.getOperationPrice() / op.getQuantity();
    assertEquals(op.getPurchasePrice(), purchase);
    assertEquals(op.getAveragePrice(), average);
    assertEquals(op.getUnitPrice(), unitPrice);
    assertEquals(op.getTaxes().getIncomingTax(), 0D);
    assertEquals(op.getDarf(), 0D);

    when(taxService.calculateTaxes(tradingNote, petr.getOperationPrice(), OperationType.BUY)).thenReturn(Buy2OperationFactory.taxPetr4());
    Operation op2 = operationService.create(Buy2OperationFactory.operationPETR4DTO());

    Double purchase2 = op2.getTaxes().getTotalValue() + op2.getOperationPrice();
    Double average2 = op2.getPurchasePrice() / op2.getQuantity();
    Double unitPrice2 = op2.getOperationPrice() / op2.getQuantity();
    assertEquals(op2.getPurchasePrice(), purchase2);
    assertEquals(op2.getAveragePrice(), average2);
    assertEquals(op2.getUnitPrice(), unitPrice2);
    assertEquals(op2.getTaxes().getIncomingTax(), 0D);
    assertEquals(op2.getDarf(), 0D);

    assertEquals(op2.getOperationPrice() + op.getOperationPrice(), tradingNote.getValue());
    assertEquals(op2.getTaxes().getEmoluments() + op.getTaxes().getEmoluments(), tradingNote.getTaxes().getEmoluments());
    assertEquals(op2.getTaxes().getLiquidation() + op.getTaxes().getLiquidation(), tradingNote.getTaxes().getLiquidation());
    assertEquals(op2.getTaxes().getTaxes() + op.getTaxes().getTaxes(), tradingNote.getTaxes().getTaxes());
    assertEquals(round(op2.getTaxes().getOtherTaxes() + op.getTaxes().getOtherTaxes()), tradingNote.getTaxes().getOtherTaxes());
    assertEquals(op2.getTaxes().getTotalValue() + op.getTaxes().getTotalValue(), Buy2OperationFactory.tradingNote().getTaxes().getTotalValue());
    
  }


  @Test
  public void validateSellOperation() {
    final Double GAIN_VALUE = 262.10;
    final Double DARF_VALUE = 39.32;
    when(tradingNoteService.findByCode(any())).thenReturn(SellOperationFactory.tradingNote());
    when(taxService.calculateTaxes(any(), any(), any())).thenReturn(SellOperationFactory.tax());
    when(stockPositionService.findByStockName(any())).thenReturn(Arrays.asList(SellOperationFactory.stockPosition()));
    when(repository.save(any())).thenReturn(any());
    Operation op = operationService.create(SellOperationFactory.operationDTO());

    Double purchase = SellOperationFactory.stockPosition().getTotalPurchased();
    Double average = SellOperationFactory.stockPosition().getAveragePrice();

    assertEquals(op.getPurchasePrice(), purchase);
    assertEquals(op.getAveragePrice(), average);
    assertEquals(op.getTaxes().getIncomingTax(), SellOperationFactory.tax().getIncomingTax());
    assertEquals(op.getTaxes().getTotalValue(), SellOperationFactory.tax().getTotalValue());
    assertEquals(op.getGainValue(), GAIN_VALUE);
    assertEquals(op.getDarf(), DARF_VALUE);
  }

  private Double round(final Double value){
    NumberFormat nf = new DecimalFormat("0.00");
    nf.setRoundingMode(RoundingMode.HALF_UP);
    return Double.parseDouble(nf.format(value).replace(",", "."));
  }

}
