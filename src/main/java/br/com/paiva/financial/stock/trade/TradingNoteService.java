package br.com.paiva.financial.stock.trade;

import br.com.paiva.financial.stock.trade.dto.OperationDTO;
import br.com.paiva.financial.stock.trade.dto.TradingNoteDTO;
import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TradingNoteService {

  public TradingNote createTradingNote(final TradingNoteDTO tradingNoteDTO) {

    Tax tax = new Tax();
    tax.setBrokerage(tradingNoteDTO.getTaxes().getBrokerage());
    tax.setEmoluments(tradingNoteDTO.getTaxes().getEmoluments());
    tax.setLiquidation(tradingNoteDTO.getTaxes().getLiquidation());
    tax.setOtherTaxes(tradingNoteDTO.getTaxes().getOtherTaxes());
    tax.setTaxes(tradingNoteDTO.getTaxes().getTaxes());
    tax.setIncomingTax(tradingNoteDTO.getTaxes().getIncomingTax());

    TradingNote note = new TradingNote();
    note.setTaxes(tax);
    note.setValue(tradingNoteDTO.getValue());
    note.setDate(tradingNoteDTO.getDate());
    note.setValueSell(tradingNoteDTO.getOperationSell());
    note.setOperationList(new ArrayList<>());

    tradingNoteDTO.getStocks().forEach(stock ->{
      Operation op = createOperation(stock, note);
      note.getOperationList().add(op);
    });

    return note;
  }

  public Operation createOperation(final OperationDTO operationDTO, final TradingNote note) {
    return new Operation(
        OperationType.valueOf(operationDTO.getType()),
        operationDTO.getStockName(),
        operationDTO.getOperationPrice(),
        operationDTO.getQuantity(),
        operationDTO.getPurchasePrice() == null ? 0D : operationDTO.getPurchasePrice(),
        note);
  }
}
