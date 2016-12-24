package com.example.brian.chinesechesscopy.model.pieces;

import com.example.brian.chinesechesscopy.model.ChineseChessModel;
import com.example.brian.chinesechesscopy.model.Position;

import java.util.ArrayList;

/**
 * Created by Brian on 16/12/5.
 */
public class Empty extends Piece {

    public Empty(ChineseChessModel model, char color, Position position) {
        super(model, color, position);
        type = '-';
    }

    @Override
    public ArrayList<Position> getPossibleMoves() {
        return null;
    }

    @Override
    public ArrayList<Position> noFilterGetPossibleMoves() {
        return null;
    }

    @Override
    public char getType() {
        return type;
    }
}
