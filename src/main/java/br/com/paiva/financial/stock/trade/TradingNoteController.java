package br.com.paiva.financial.stock.trade;

import br.com.paiva.financial.stock.trade.dto.TradingNoteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class TradingNoteController {

    private final TradingNoteService tradingNoteService;

    @RequestMapping(value = "/note", method = RequestMethod.POST)
    public TradingNote createTradingNote(@RequestBody TradingNoteDTO tradingNote) {
        return tradingNoteService.createTradingNote(tradingNote);
    }

}
