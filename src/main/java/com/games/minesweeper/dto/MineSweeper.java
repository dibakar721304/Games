package com.games.minesweeper.dto;

import javax.persistence.*;

import com.games.minesweeper.model.GameStates;
import com.games.minesweeper.model.GridBox;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Data
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MineSweeper {

    @Id
    @GeneratedValue(generator = "id_generator")
    @SequenceGenerator(
            name = "id_generator",
            sequenceName = "id_generator",
            initialValue = 10
    )
    private long id;

    @Column
    private String userName;

    @Column
    @Enumerated(EnumType.STRING)
    private GameStates state;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private GridBox[][] gridBox;

    public MineSweeper(GridBox[][] gridBox, String userName) {
        this.gridBox = gridBox;
        this.userName = userName;
        this.state = GameStates.ACTIVE;
    }
}
