package com.games.minesweeper.repository;

import java.util.Optional;

import com.games.minesweeper.dto.MineSweeper;
import com.games.minesweeper.model.GameStates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<MineSweeper, Long> {

    Optional<MineSweeper> findByUserNameAndState(String userName, GameStates State);

}
