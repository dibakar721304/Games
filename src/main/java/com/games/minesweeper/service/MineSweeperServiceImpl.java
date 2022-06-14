package com.games.minesweeper.service;

import com.games.minesweeper.dto.MineSweeper;
import com.games.minesweeper.exception.GameException;
import com.games.minesweeper.model.*;
import com.games.minesweeper.repository.GameRepository;
import com.games.minesweeper.util.GameHelper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MineSweeperServiceImpl implements MineSweeperService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GameHelper gameHelper;

    private GridBox gridBox[][];

    @Override
    public MineSweeperBean createNewGame(BoardRequest boardRequest) {
        try {
            if (gameRepository.findByUserNameAndState(boardRequest.getName(), GameStates.ACTIVE).isPresent()) {
                throw new GameException(String.format("Already an ACTIVE game is present for username=%s", boardRequest.getName()));
            }
            gridBox = gameHelper.setGameWizard(boardRequest);
            gameHelper.placeMinesRandomly(boardRequest, gridBox);
            gameHelper.setNumberOfMinesArround(boardRequest, gridBox);
            MineSweeper mineSweeper = new MineSweeper(gridBox, boardRequest.getName());
            mineSweeper = gameRepository.save(mineSweeper);
            log.info("MineSweeper Game has been successfully created for username={}", boardRequest.getName());

            return MineSweeperBean.builder()
                    .name(mineSweeper.getUserName())
                    .state(mineSweeper.getState())
                    .gridBox(mineSweeper.getGridBox())
                    .build();
        } catch(Exception exception) {
            throw new GameException(String.format("Error trying to create a new game for username=%s",
                    boardRequest.getName()), exception);
        }
    }


    @Override
    public MineSweeperBean getUserGame(String username) {
        return gameRepository.findByUserNameAndState(username, GameStates.ACTIVE)
                .map(game -> modelMapper.map(game, MineSweeperBean.class))
                .orElseThrow(() -> new GameException(String.format("There is no ACTIVE game for username=%s", username)));
    }

    @Override
    public MineSweeperBean playGame(String username, PlayRequest request) {
        int row = request.getRow();
        int column = request.getColumn();
        Optional<MineSweeper> game = gameRepository.findByUserNameAndState(username, GameStates.ACTIVE);

        if (!game.isPresent()) {
            throw new GameException(String.format("There's no ACTIVE game for username=%s", username));
        }
        gridBox = game.get().getGridBox();
        log.info("Executing the play of username={} in row={} and column={}", username, row, column);
       if(row>game.get().getGridBox().length - 1 || column>game.get().getGridBox()[0].length - 1)
       {
           throw new GameException(String.format("Row or column value exceeds grid size", gridBox.length));
       }
        if (gameHelper.mineFound(gridBox, row, column)) {
            log.debug("Blows up in row={} and column={}", row, column);
            game.get().setState(GameStates.BLOWUP);
        } else {
            gameHelper.clearEmptySpots(gridBox, request.getRow(), request.getColumn(), game.get().getGridBox().length - 1, game.get().getGridBox()[0].length - 1);

            if (!gameHelper.mineFound(gridBox, row, column)) {
                log.debug("Mine found in row={} and column={}", row, column);
                gridBox[row][column].setBoxShown(true);
            }

            game.get().setGridBox(gridBox);

            if (gameHelper.alreadyWon(gridBox)) {
                log.info("Game Won");
                game.get().setState(GameStates.WON);
            }
        }

        return modelMapper.map(gameRepository.save(game.get()), MineSweeperBean.class);
    }

    @Override
    public MineSweeperBean resetGameStatus(String userName) {
               Optional<MineSweeper> game  =
                Optional.ofNullable(gameRepository.findByUserNameAndState(userName, GameStates.BLOWUP)
                        .orElseThrow(() -> new GameException(String.format("Game not found with BLOWUP status for userName %s", userName))));
        if(!game.isPresent())
        throw new GameException(String.format("Could not set ACTIVE status for userName %s", userName) );
        game.get().setState(GameStates.ACTIVE);
        return modelMapper.map(gameRepository.save(game.get()), MineSweeperBean.class);
    }

}
