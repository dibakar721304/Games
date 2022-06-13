package com.games.minesweeper.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MineSweeperBean {
    private String name;
    private GameStates state;
    private GridBox[][] gridBox;
   }