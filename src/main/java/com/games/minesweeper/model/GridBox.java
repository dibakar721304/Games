package com.games.minesweeper.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GridBox implements Serializable {

    private int horizontalPosition;
    private int verticalPosition;
    private boolean isBoxShown;
    private int numberOfMinesAround;
    private boolean isBoxAMine;
    public GridBox(boolean isBoxShown) {
        this.isBoxShown = isBoxShown;
    }

 }