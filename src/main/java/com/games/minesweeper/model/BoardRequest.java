package com.games.minesweeper.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardRequest {
    @NotEmpty
    private String name;

    @NotNull
    private int columns;

    @NotNull
    private int rows;

    @NotNull
    private int mines;
}
