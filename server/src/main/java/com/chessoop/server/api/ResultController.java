package com.chessoop.server.api;

import com.chessoop.server.api.dto.*;
import com.chessoop.server.service.ResultService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ResultController {
    private final ResultService svc;

    public ResultController(ResultService svc){
        this.svc = svc;
    }

    @PostMapping("/results")
    public RecordResultResponse record(@RequestBody @Valid RecordResultRequest req){
        System.out.println("REQ: " + req);
        var saved = svc.record(req);

        return new RecordResultResponse(saved.getId());
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardRow> leaderboard(@RequestParam(defaultValue="10") int limit){
        return svc.leaderboard(limit);
    }

}