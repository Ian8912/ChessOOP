package com.chessoop.server.api.dto;

import com.chessoop.server.domain.Winner;
import jakarta.validation.constraints.*;

import java.time.Instant;

public record RecordResultRequest(
        @NotBlank @Size(max=64) String whiteName,
        @NotBlank @Size(max=64) String blackName,
        @NotNull Winner winner,
        String pgn,
        Instant finishedAt
) {}
