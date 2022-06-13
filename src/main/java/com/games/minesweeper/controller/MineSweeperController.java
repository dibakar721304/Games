package com.games.minesweeper.controller;

import com.games.minesweeper.model.BoardRequest;
import com.games.minesweeper.exception.GameException;
import com.games.minesweeper.model.PlayRequest;
import com.games.minesweeper.service.MineSweeperService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/minesweeper")
@Validated
@Slf4j
public class MineSweeperController {

    @Autowired
    private MineSweeperService mineSweeperService;

    @PostMapping(value="/createNewGame", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createNewGame(@Valid @RequestBody BoardRequest request) {
        try {
            if (request.getMines() > request.getColumns() * request.getRows()) {
                return ResponseEntity.badRequest().body("Number of mines are greater than the cells");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(mineSweeperService.createNewGame(request));
        } catch (GameException gameException) {
            log.error("Failed to create a new game exception={}", gameException);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(gameException.getMessage());
        }
    }

    @PostMapping(value = "/playGame/{userName}",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity playGame(@Valid @RequestBody PlayRequest request, @PathVariable String userName) {
        try {
           return ResponseEntity.ok(mineSweeperService.playGame(userName, request));
        } catch (GameException gameException) {
            log.error("Failed to create moves for username={}, exception={}", userName, gameException);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gameException.getMessage());
        }
    }

    @PostMapping(value="/resetGameStatus/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity resetGameStatus(@Valid @PathVariable String userName ) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(mineSweeperService.resetGameStatus(userName));
        } catch (GameException gameException) {
            log.error("Falied to update status for userName={}", userName);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(gameException.getMessage());
        }
    }

    @GetMapping(value="/getUserGame/{userName}")
    public ResponseEntity getUserGame(@PathVariable String userName) {
        try {
            return ResponseEntity.ok(mineSweeperService.getUserGame(userName));
        } catch (GameException gameException) {
            log.error("Failed to get existing game for username={}, exception={}", userName, gameException);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gameException.getMessage());
        }
    }

}