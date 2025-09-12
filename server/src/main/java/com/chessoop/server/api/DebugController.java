package com.chessoop.server.api;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DebugController {

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @PostMapping("/echo")
    public Map<String, Object> echo(@RequestBody Map<String, Object> body){
        return body; // echoes back whatever you POST
    }
}
