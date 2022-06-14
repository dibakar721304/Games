package com.games.minesweeper;


import java.util.Optional;

import com.games.minesweeper.exception.GameException;
import com.games.minesweeper.model.*;
import com.games.minesweeper.repository.GameRepository;
import com.games.minesweeper.service.MineSweeperServiceImpl;
import com.games.minesweeper.dto.MineSweeper;
import com.games.minesweeper.util.GameHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MineSweeperServiceImpl.class})
public class MineSweeperServiceImplTest {

    @Autowired
    private MineSweeperServiceImpl mineSweeperServiceImpl;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private ModelMapper modelMapper;

    private MineSweeper mineSweeper;
    private BoardRequest request;
    @MockBean
    private GameHelper gameHelper;


    @Before
    public void setUp() {
        GridBox[][] gridBox = new GridBox[2][2];
        mineSweeper = MineSweeper.builder().id(20L).userName("MineSweeperTest").gridBox(gridBox).build();

        request = BoardRequest.builder().columns(5).rows(5).mines(5).name("MinesweeperTest").build();
    }

    @Test
    public void testCreateNewGameSuccessful() {
        when(gameRepository.findByUserNameAndState(request.getName(), GameStates.ACTIVE)).thenReturn(Optional.empty());
        when(gameRepository.save(any(MineSweeper.class))).thenReturn(mineSweeper);
        mineSweeperServiceImpl.createNewGame(request);

        verify(gameRepository, times(1)).save(any(MineSweeper.class));
    }

    @Test(expected = GameException.class)
    public void testCreateNewGameAlreadyExists() {
        when(gameRepository.findByUserNameAndState(request.getName(), GameStates.ACTIVE))
                .thenReturn(Optional.of(new MineSweeper()));

        mineSweeperServiceImpl.createNewGame(request);
    }

    @Test
    public void testGetUserGame() {
        when(gameRepository.findByUserNameAndState(request.getName(), GameStates.ACTIVE)).thenReturn(Optional.of(mineSweeper));
        when(modelMapper.map(mineSweeper, MineSweeperBean.class)).thenReturn(MineSweeperBean.builder().build());
        assertThat(mineSweeperServiceImpl.getUserGame(request.getName())).isNotNull();
    }

    @Test(expected = GameException.class)
    public void testPlayGameNotFound() {
        when(gameRepository.findByUserNameAndState(request.getName(), GameStates.ACTIVE)).thenReturn(Optional.empty());
        mineSweeperServiceImpl.playGame(request.getName(), PlayRequest.builder().column(0).row(0).build());
    }

    @Test(expected = GameException.class)
    public void testGetUserGameNotFound() {
        when(gameRepository.findByUserNameAndState(request.getName(), GameStates.ACTIVE)).thenReturn(Optional.empty());
        mineSweeperServiceImpl.getUserGame(request.getName());
    }

    @Test
    public void testPlayWon() {
        mineSweeper.getGridBox()[0][0] = new GridBox(false);
        mineSweeper.getGridBox()[0][1] = new GridBox(true);
        mineSweeper.getGridBox()[1][0] = new GridBox(true);
        mineSweeper.getGridBox()[1][1] = new GridBox(true);
        mineSweeper.setState(GameStates.ACTIVE);
        when(gameRepository.findByUserNameAndState(request.getName(), GameStates.ACTIVE)).thenReturn(Optional.of(mineSweeper));
        when(gameHelper.alreadyWon(mineSweeper.getGridBox())).thenReturn(true);
        mineSweeperServiceImpl.playGame(request.getName(), PlayRequest.builder().column(0).row(0).build());

        ArgumentCaptor<MineSweeper> captor = ArgumentCaptor.forClass(MineSweeper.class);
        verify(gameRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getState()).isEqualTo(GameStates.WON);
    }

    @Test
    public void testPlayBlowUp() {
        mineSweeper.getGridBox()[0][0] = new GridBox(true);
        when(gameRepository.findByUserNameAndState(request.getName(), GameStates.ACTIVE)).thenReturn(Optional.of(mineSweeper));
        mineSweeper.setState(GameStates.ACTIVE);
        when(gameHelper.mineFound(mineSweeper.getGridBox(), 0, 0)).thenReturn(true);
        mineSweeperServiceImpl.playGame(request.getName(), PlayRequest.builder().column(0).row(0).build());
        ArgumentCaptor<MineSweeper> captor = ArgumentCaptor.forClass(MineSweeper.class);
        verify(gameRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getState()).isEqualTo(GameStates.BLOWUP);
    }
}
