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
     * @param username
     * @return
     */
    MineSweeperBean getUserGame(String username);

    /**
     * Hits the playing logic of the game
     * @param username
     * @param request
     * @return
     */
    MineSweeperBean playGame(String username, PlayRequest request);

    /**
     * If the game status is BLOWUP, restes the game status to ACTIVE
     * so that player can continue the game
     * @param userName
     * @return
     */
    MineSweeperBean resetGameStatus(String userName);

}
