package br.com.paiva.financial.stock.trade;

import br.com.paiva.financial.stock.trade.operation.OperationDTO;
import br.com.paiva.financial.stock.trade.tax.TaxDTO;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNoteDTO;
import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TradeNoteServiceSellTest {

    @InjectMocks
    private TradingNoteService service;

    @Test
    public void validateTotalPriceList() throws ParseException {
        TradingNoteDTO input = getTradingNoteDTO();
        TradingNote note = service.createTradingNote(input);
        assertEquals(note.getValue(), input.getValue());
    }

    @Test
    public void validateCalculateDarf() throws ParseException {
        TradingNoteDTO input = getTradingNoteDTO();
        TradingNote note = service.createTradingNote(input);

        Operation op1 = note.getOperationList().get(0);
        assertEquals(op1.getDarf(), 0);
        assertEquals(op1.getTaxes().getIncomingTax(), 0);



        Operation op2 = note.getOperationList().get(1);

        assertEquals(op2.getGainValue(), 117.27);
        assertEquals(op2.getDarf(), 17.5905);
        assertEquals(op2.getTaxes().getIncomingTax(), input.getTaxes().getIncomingTax());
    }

    @Test
    public void validatePurchasePriceList() throws ParseException {
        TradingNoteDTO input = getTradingNoteDTO();
        TradingNote note = service.createTradingNote(input);

        Operation op = note.getOperationList().get(0);
        Double purchase = op.getTaxes().getTotalValue() + op.getOperationPrice();
        Double average = op.getPurchasePrice() / op.getQuantity();
        Double unitPrice = op.getOperationPrice() / op.getQuantity();
        assertEquals(op.getPurchasePrice(), purchase);
        assertEquals(op.getAveragePrice(), average);
        assertEquals(op.getUnitPrice(), unitPrice);
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

        note.getOperationList().forEach(it ->{
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

        Double totalTax = totalEmoluments.get() + totalLiquidation.get() + totalBrokerage.get() + totalOtherTaxes.get() + totalTaxes.get();
        Double operationalCosts = totalBrokerage.get() + totalOtherTaxes.get() + totalTaxes.get();
        assertEquals(note.getTaxes().getTotalValue(), round(totalTax));
        assertEquals(note.getTaxes().getOperationalCosts(), operationalCosts);
    }

    private TradingNoteDTO getTradingNoteDTO(){

    final TaxDTO tax = TaxDTO.builder()
            .liquidation(2.73)
            .brokerage(56.70)
            .emoluments(0.29)
            .taxes(6.05)
            .incomingTax(0.24)
            .otherTaxes(2.21)
            .build();

    final OperationDTO op1 = new OperationDTO();
    op1.setStockName("RENT3");
    op1.setQuantity(100);
    op1.setOperationPrice(3670.0);
    op1.setType(OperationType.BUY.name());

    final OperationDTO op2 = new OperationDTO();
    op2.setStockName("PETR4");
    op2.setQuantity(300);
    op2.setOperationPrice(4875.0);
    op2.setPurchasePrice(4733.07);
    op2.setType(OperationType.SELL.name());

    final OperationDTO op3 = new OperationDTO();
    op3.setStockName("VVAR3");
    op3.setQuantity(200);
    op3.setOperationPrice(1416.0);
    op3.setType(OperationType.BUY.name());
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    return TradingNoteDTO.builder()
                .date(simpleDateFormat.format(new Date()))
                .stocks(Arrays.asList(op1, op2, op3))
                .taxes(tax)
                .operationSell(4875.0)
                .value(9961.0).build();
    }


    private Double round(final Double value){
        NumberFormat nf = new DecimalFormat("0.00");
        nf.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(nf.format(value).replace(",", "."));
    }



}
