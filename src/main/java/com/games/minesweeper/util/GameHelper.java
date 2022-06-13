package com.games.minesweeper.util;

import java.util.Random;

import com.games.minesweeper.model.BoardRequest;
import com.games.minesweeper.model.GridBox;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;


@Slf4j
@Component
public class GameHelper {

    public GridBox[][] setGameWizard(BoardRequest boardRequest) {
        GridBox gridBox[][];
        gridBox = new GridBox[boardRequest.getRows()][];
        for (int i=0 ; i < boardRequest.getRows(); i++) {
            gridBox[i] = new GridBox[boardRequest.getColumns()];
            for (int j = 0; j < boardRequest.getColumns(); j++) {
                gridBox[i][j] = new GridBox();
                gridBox[i][j].setHorizontalPosition(i);
                gridBox[i][j].setVerticalPosition(j);
            }
        }
        log.info("A new game was initialized with rows={}, columns={}, mines={} for usaername={}",
                boardRequest.getRows(), boardRequest.getColumns(), boardRequest.getMines(), boardRequest.getName());
        return gridBox;
    }


    public void placeMinesRandomly(BoardRequest boardRequest, GridBox[][] gridBox) {
        int minesPlaced = 0;
        Random random = new Random();
        while(minesPlaced < boardRequest.getMines()) {
            int x = random.nextInt(boardRequest.getRows());
            int y = random.nextInt(boardRequest.getColumns());
            if(!gridBox[y][x].isBoxAMine()) {
                gridBox[y][x].setBoxAMine(true);
                gridBox[y][x].getHorizontalPosition();
                gridBox[y][x].getHorizontalPosition();
                minesPlaced ++;
            }
        }
        log.info("Mines have been set for user ={}", boardRequest.getName());
    }


    public void setNumberOfMinesArround(BoardRequest boardRequest, GridBox[][] gridBox) {
        for (int x=0; x < boardRequest.getRows(); x ++) {
            for (int y= 0; y < boardRequest.getColumns(); y++) {
                gridBox[x][y].setNumberOfMinesAround(minesNear(gridBox, x, y));
            }
        }
    }

    private int minesNear(GridBox[][] gridBox, int x, int y) {
        int mines = 0;
        mines += mineAt(gridBox, y - 1, x - 1);
        mines += mineAt(gridBox, y - 1, x);
        mines += mineAt(gridBox,y - 1, x + 1);
        mines += mineAt(gridBox, y,x - 1);
        mines += mineAt(gridBox, y, x + 1);
        mines += mineAt(gridBox, y + 1, x - 1);
        mines += mineAt(gridBox, y + 1, x);
        mines += mineAt(gridBox,y + 1, x + 1);
        return mines;
    }

    private int mineAt(GridBox[][] gridBox, int y, int x) {
        if(y >= 0 && y < gridBox[0].length && x >= 0 && x < gridBox.length && gridBox[y][x].isBoxAMine()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean mineFound(GridBox[][] gridBox, int row, int column) {
        return gridBox[row][column].isBoxAMine();
    }

    public boolean alreadyWon(GridBox[][] gridBox) {
        for (int i=0 ; i < gridBox.length ; i++) {
            for (int j = 0; j < gridBox[0].length; j++) {
                if (!gridBox[i][j].isBoxAMine() && !gridBox[i][j].isBoxShown()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clearEmptySpots(GridBox[][] gridBox, int x, int y, int xMax, int yMax) {
        // Base Case
        if (x < 0 || x > xMax || y < 0 || y > yMax){
            return;
        }

        if ( gridBox[x][y].getNumberOfMinesAround()== 0 && !gridBox[x][y].isBoxShown()) {
            gridBox[x][y].setBoxShown(true);
            clearEmptySpots(gridBox, x+1, y , xMax, yMax);
            clearEmptySpots(gridBox, x+1, y+1 , xMax, yMax);
            clearEmptySpots(gridBox, x+1, y-1 , xMax, yMax);
            clearEmptySpots(gridBox, x-1, y , xMax, yMax);
            clearEmptySpots(gridBox, x-1, y-1 , xMax, yMax);
            clearEmptySpots(gridBox, x-1, y+1 , xMax, yMax);
            clearEmptySpots(gridBox, x, y-1 , xMax, yMax);
            clearEmptySpots(gridBox, x, y+1 , xMax, yMax);
        } else {
            return;
        }
    }
}