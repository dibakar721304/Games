package com.games.minesweeper.service;

import com.games.minesweeper.model.BoardRequest;
import com.games.minesweeper.model.MineSweeperBean;
import com.games.minesweeper.model.PlayRequest;

public interface MineSweeperService {

    /**
     * Create new game for user.
     * @param boardRequest
     * @return
     */
    MineSweeperBean createNewGame(BoardRequest boardRequest);

    /**
     * Gets the current user game.
     * @param userName
     * @return
     */
    MineSweeperBean getUserGame(String userName);

    /**
     * Hits the playing logic of the game
     * @param userName
     * @param request
     * @return
     */
    MineSweeperBean playGame(String userName, PlayRequest request);

    /**
     * If the game status is BLOWUP, restes the game status to ACTIVE
     * so that player can continue the game
     * @param userName
     * @return
     */
    MineSweeperBean resetGameStatus(String userName);

}
