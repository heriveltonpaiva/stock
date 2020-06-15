package br.com.paiva.financial.stock.trade.operation;

import br.com.paiva.financial.stock.trade.operation.request.OperationSearchRequest;
import br.com.paiva.financial.stock.trade.tradingnote.TradingNote;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OperationRepositoryImpl {

  private final MongoTemplate mongoTemplate;

  public List<Operation> findByFilter(final OperationSearchRequest search) {

    Criteria criteria = new Criteria();
    criteria.where("1").is("1");

    if (!ObjectUtils.isEmpty(search.getStockName())) {
      criteria.and("stockName").is(search.getStockName());
    }
    if (!ObjectUtils.isEmpty(search.getTypeOperation())) {
      criteria.and("type").is(search.getTypeOperation());
    }
    if (!ObjectUtils.isEmpty(search.getBroker())
        || !ObjectUtils.isEmpty(search.getReferenceMonth())) {
      Collection<String> ids = getIdsTradingNote(search);
      if (!ObjectUtils.isEmpty(ids)) {
        criteria.and("tradingNoteCode").in(ids);
      }
    } else {
      if (!ObjectUtils.isEmpty(search.getTradingNoteCode())) {
        criteria.and("tradingNoteCode").is(search.getTradingNoteCode());
      }
    }

    Query query = new Query(criteria);
    return mongoTemplate.find(query, Operation.class);
  }

  private Collection<String> getIdsTradingNote(OperationSearchRequest search) {
    List<TradingNote> tradingNotes = new ArrayList<>();

    if (!ObjectUtils.isEmpty(search.getBroker())) {
      tradingNotes.addAll(
          mongoTemplate.find(
              new Query(Criteria.where("broker").is(search.getBroker())), TradingNote.class));
    } else {
      tradingNotes.addAll(mongoTemplate.findAll(TradingNote.class));
    }

    if (!ObjectUtils.isEmpty(search.getReferenceMonth())) {
      return tradingNotes.stream()
          .filter(
              tradingNote -> {
                LocalDate noteDate =
                    tradingNote.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return noteDate.getMonth().ordinal()
                    == Integer.parseInt(search.getReferenceMonth()) - 1;
              })
          .map(tradingNote -> tradingNote.getCode())
          .collect(Collectors.toList());
    }
    return tradingNotes.stream()
        .map(tradingNote -> tradingNote.getCode())
        .collect(Collectors.toList());
  }
}
