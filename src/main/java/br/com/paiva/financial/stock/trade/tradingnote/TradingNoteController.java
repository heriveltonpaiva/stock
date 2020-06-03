package br.com.paiva.financial.stock.trade.tradingnote;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class TradingNoteController {

    private final TradingNoteService tradingNoteService;

    @RequestMapping(value = "/note", method = RequestMethod.POST)
    public TradingNote createTradingNote(@RequestBody TradingNoteDTO tradingNote) throws ParseException {
        return tradingNoteService.createTradingNote(tradingNote);
    }

    @GetMapping(value = "/notes")
    public List<TradingNote> findAll() {
        return tradingNoteService.findAll();
    }

}
