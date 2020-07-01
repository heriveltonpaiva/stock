package br.com.paiva.financial.stock.trade.tradingnote;

import br.com.paiva.financial.stock.trade.tax.TaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradingNoteService {

  private final TradingNoteRepository repository;
  private final TaxService taxService;

  public TradingNote createTradingNote(final TradingNoteDTO tradingNoteDTO) throws ParseException {
    return createNewTradingNote(tradingNoteDTO);
  }

  public List<TradingNote> findAll() {
    return repository.findAll(Sort.by(Sort.Order.desc("date")));
  }

  public TradingNote findByCode(final String code) {
    return repository.findByCode(code);
  }

  private TradingNote createNewTradingNote(TradingNoteDTO tradingNoteDTO) throws ParseException {

    //validate(tradingNoteDTO);

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
    return repository.save(note);
  }

  private void validate(TradingNoteDTO tradingNoteDTO) {
    List<String> errors = new ArrayList<>();

    if (Objects.isNull(tradingNoteDTO.getCode())) {
      errors.add("Error: Code couldn't is not empty.");
    }

    if (Objects.nonNull(repository.findByCode(tradingNoteDTO.getCode()))) {
      errors.add("Error: Code couldn't use because code is duplicated.");
    }

    if (Objects.isNull(tradingNoteDTO.getDate())) {
      errors.add("Error: Date couldn't is not empty.");
    }

    if (Objects.nonNull(repository.findByDate(tradingNoteDTO.getDate()))) {
      errors.add("Error: Code couldn't use because date is duplicated.");
    }

    if (Objects.isNull(tradingNoteDTO.getTaxes().getEmoluments())) {
      errors.add("Error: Emoluments couldn't R$ 0,00.");
    }
    if (Objects.isNull(tradingNoteDTO.getTaxes().getLiquidation())) {
      errors.add("Error: Liquidation couldn't R$ 0,00.");
    }

    if (tradingNoteDTO.getBroker().equals(BrokerType.XP)) {
      if (Objects.isNull(tradingNoteDTO.getTaxes().getTaxes())) {
        errors.add("Error: Taxes couldn't R$ 0,00 for XP broker.");
      }
      if (Objects.isNull(tradingNoteDTO.getTaxes().getOtherTaxes())) {
        errors.add("Error: Other Taxes couldn't R$ 0,00 for XP broker.");
      }
    }

    if (Objects.nonNull(tradingNoteDTO.getOperationSell())
        && Objects.isNull(tradingNoteDTO.getTaxes().getIncomingTax())) {
      errors.add("Error: Incoming Tax couldn't R$ 0,00 for sell operation.");
    }

    if (tradingNoteDTO.getOperationSell() > tradingNoteDTO.getValue()) {
      errors.add("Error: Sell value couldn't more than value operation.");
    }

    if (Objects.nonNull(errors)) {
      log.error(errors.toString());
      throw new RuntimeException("Happened error when try validate trading note creation.");
    }
  }
}
