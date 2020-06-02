package br.com.paiva.financial.stock.trade.tradingnote;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TradingNoteRepository extends MongoRepository<TradingNote, String> {

    TradingNote findByCode(final String code);

}
