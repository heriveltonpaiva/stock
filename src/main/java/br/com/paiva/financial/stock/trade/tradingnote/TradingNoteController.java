package br.com.paiva.financial.stock.trade.tradingnote;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class TradingNoteController {

    private final TradingNoteService tradingNoteService;

    @RequestMapping(value = "/note", method = RequestMethod.POST)
    public ResponseEntity<TradingNote> createTradingNote(@Valid @RequestBody TradingNoteDTO tradingNote) throws ParseException {
        return ResponseEntity.ok(tradingNoteService.createTradingNote(tradingNote));
    }

    @GetMapping(value = "/notes")
    public List<TradingNote> findAll() {
        return tradingNoteService.findAll();
    }

    @GetMapping(value = "/note/{code}")
    public TradingNote findByCode(@PathVariable final String code) {
        return tradingNoteService.findByCode(code);
    }

}
