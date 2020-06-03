package br.com.paiva.financial.stock.trade.tradingnote;

import br.com.paiva.financial.stock.trade.operation.Operation;
import br.com.paiva.financial.stock.trade.operation.OperationService;
import br.com.paiva.financial.stock.trade.tax.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TradingNoteService {

  private final TradingNoteRepository repository;
  private final OperationService operationService;
  private final TaxService taxService;

  public TradingNote createTradingNote(final TradingNoteDTO tradingNoteDTO) throws ParseException {
    //TradingNote noteBD = repository.findByCode(tradingNoteDTO.getCode());
    //if (Objects.isNull(noteBD)) {
      return createNewTradingNote(tradingNoteDTO);
    //}
   // addOperationTradingNote(tradingNoteDTO, noteBD);
    //return noteBD;
  }

  public List<TradingNote> findAll(){
    return repository.findAll();
  }

  private TradingNote createNewTradingNote(TradingNoteDTO tradingNoteDTO) throws ParseException {
    TradingNote note = new TradingNote();
    note.setCode(tradingNoteDTO.getCode());
    note.setBroker(BrokerType.valueOf(tradingNoteDTO.getBroker()));
    note.setTaxes(taxService.createTradingNoteTax(tradingNoteDTO.getTaxes()));
    note.setValue(tradingNoteDTO.getValue());
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    Date date = simpleDateFormat.parse(tradingNoteDTO.getDate());
    note.setDate(date);
    note.setValueSell(tradingNoteDTO.getOperationSell());
    TradingNote noteSaved = repository.save(note);
    addOperationTradingNote(tradingNoteDTO, noteSaved);
    return note;
  }

  private void addOperationTradingNote(TradingNoteDTO tradingNoteDTO, TradingNote noteSaved) {
    tradingNoteDTO
        .getStocks()
        .forEach(
            stock -> {
              Operation op = operationService.createOperation(stock, noteSaved);
              noteSaved.setOperationList(new ArrayList<>());
              noteSaved.getOperationList().add(op);
            });
  }
}
