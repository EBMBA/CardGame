package com.example.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameDTO {
    private String name;
    private String player1;
    private String player2;
    private String player1Card;
    private String player2Card;
    private String mise;
    private String status;

    private String winner;

    @Override
    public String toString() {

    final StringBuilder builder = new StringBuilder();
        builder.append("GameDTO [name=")
                .append(name)
                .append(", player1=")
                .append(player1)
                .append(", player2=")
                .append(player2)
                .append(", player1Card=")
                .append(player1Card)
                .append(", player2Card=")
                .append(player2Card)
                .append(", mise=")
                .append(mise)
                .append(", status=")
                .append(status)
                .append("]");
        return builder.toString() ;
    }
}
