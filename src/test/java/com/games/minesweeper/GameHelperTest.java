package com.games.minesweeper;


import java.util.Optional;
import java.util.Random;

import com.games.minesweeper.exception.GameException;
import com.games.minesweeper.model.*;
import com.games.minesweeper.repository.GameRepository;
import com.games.minesweeper.service.MineSweeperServiceImpl;
import com.games.minesweeper.dto.MineSweeper;
import com.games.minesweeper.util.GameHelper;
import org.assertj.core.api.Assertions;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GameHelper.class})
public class GameHelperTest {

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private ModelMapper modelMapper;

    private MineSweeper mineSweeper;
    private BoardRequest request;

    private GameHelper gameHelper;
    private GridBox[][] gridBox;
    @MockBean
    private Random random;
    @Before
    public void setUp() {
       gameHelper= new GameHelper();
        gridBox = new GridBox[5][5];
        mineSweeper = MineSweeper.builder().id(20L).userName("MineSweeperTest").gridBox(gridBox).build();

        request = BoardRequest.builder().columns(5).rows(5).mines(5).name("MinesweeperTest").build();
    }

    @Test
    public void testSettingGameWizardSuccessfully() {

        GridBox[][] gridBoxActual=gameHelper.setGameWizard(request);
        assertThat(gridBoxActual.length).isEqualTo(gridBox.length);
    }
//    @Test
//    public void testSettingRandomMinesSuccessfully() {
//        when(gridBox[1][1].isBoxShown()).thenReturn(false);
//        gameHelper.placeMinesRandomly(request,gridBox);
//        assertThat(request.getMines()).isEqualTo(5);
//    }
}
