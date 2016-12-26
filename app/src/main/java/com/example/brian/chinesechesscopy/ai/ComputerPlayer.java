package com.example.brian.chinesechesscopy.ai;

import android.graphics.Color;
import android.util.Log;

import com.example.brian.chinesechesscopy.model.ChineseChessModel;
import com.example.brian.chinesechesscopy.model.Position;
import com.example.brian.chinesechesscopy.model.pieces.Empty;
import com.example.brian.chinesechesscopy.model.pieces.Piece;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Brian on 16/12/4.
 */
public class ComputerPlayer {
    private char color;
    private ChineseChessModel model;
    private Piece[][] board;
    private ArrayList<Piece> redPieces;
    private ArrayList<Piece> blackPieces;
    int depth = 2;

    private int[][] fowardRook = {
            {14,14,12,18,16,18,12,14,14},
            {16,20,18,24,26,24,18,20,16},
            {12,12,12,18,18,18,12,12,12},
            {12,18,16,22,22,22,16,18,12},
            {12,14,12,18,18,18,12,14,12},
            {12,16,14,20,20,20,14,16,12},
            {6 ,10,8 ,14,14,14,8 ,10,6 },
            {4 ,8 ,6 ,14,12,14,6 ,8 ,4 },
            {8 ,4 ,8 ,16,8 ,16,8 ,4 ,8 },
            {-2,10,6 ,14,12,14,6 ,10,-2}
    };
    private int[][] fowardCannon = {
            {6  ,4  ,0  ,-10,-12,-10,0  ,4  ,6  },
            {2  ,2  ,0  ,-4 ,-14,- 4,0  ,2  ,2  },
            {2  ,2  ,0  ,-10,-8 ,-10,0  ,2  ,2  },
            {0  ,0  ,-2 ,4  ,10 ,4  ,-2 ,0  ,0  },
            {0  ,0  ,0  ,2  ,8  ,2  ,0  ,0  ,0  },
            {-2 ,0  ,4  ,2  ,6  ,2  ,4  ,0  ,-2 },
            {0  ,0  ,0  ,2  ,4  ,2  ,0  ,0  ,0  },
            {4  ,0  ,8  ,6  ,10 ,6  ,8  ,0  ,4  },
            {0  ,2  ,4  ,6  ,6  ,6  ,4  ,2  ,0  },
            {0  ,0  ,2  ,6  ,6  ,6  ,2  ,0  ,0  }
    };

    private int[][] fowardHorse = {
            {4 ,8 ,16,12,4 ,12,16,8 ,4 },
            {4 ,10,28,16,8 ,16,28,10,4 },
            {12,14,16,20,18,20,16,14,12},
            {8 ,24,18,24,20,24,18,24,8 },
            {6 ,26,14,18,16,18,14,16,6 },
            {4 ,12,16,14,12,14,16,12,4 },
            {2 ,6 ,8 ,6 ,10,6 ,8 ,6 ,2 },
            {4 ,2 ,8 ,8 ,4 ,8 ,8 ,2 ,4 },
            {0 ,2 ,4 ,4 ,-2,4 ,4 ,2 ,0 },
            {0 ,-4,0 ,0 ,0 ,0 ,0 ,-4,0 }
    };

    private int[][] fowardPawn = {
            {0  ,3  ,6  ,9  ,12 ,9  ,6  ,3  ,0  },
            {18 ,36 ,56 ,80 ,120,80 ,56 ,36 ,18 },
            {14 ,26 ,42 ,60 ,80 ,60 ,42 ,26 ,14 },
            {10 ,20 ,30 ,34 ,40 ,34 ,30 ,20 ,10 },
            {6  ,12 ,18 ,18 ,20 ,18 ,18 ,12 ,6  },
            {2  ,0  ,8  ,0  ,8  ,0  ,8  ,0  ,2  },
            {0  ,0  ,-2 ,0  ,4  ,0  ,-2 ,0  ,0  },
            {0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  },
            {0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  },
            {0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  ,0  }
    };

    private int[][] backwardRook;
    private int[][] backwardCannon;
    private int[][] backwardHorse;
    private int[][] backwardPawn;

    public ComputerPlayer(char color, ChineseChessModel model) {
        this.color = color;
        this.model = model;
        board = model.getBoard();
        redPieces = model.getRedPieces();
        blackPieces = model.getBlackPieces();
        backwardRook = new int[model.getROW()][model.getCOL()];
        backwardCannon = new int[model.getROW()][model.getCOL()];
        backwardHorse = new int[model.getROW()][model.getCOL()];
        backwardPawn = new int[model.getROW()][model.getCOL()];
        createBackwardsMatrices();
    }

    private String backwardRookToString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 9; j++) {
                sb.append(backwardRook[i][j] + ", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }




    private int evaluateRed() {
        int value = 0;

        // Evaluate the pieces
        for(int i = 0; i < redPieces.size(); i++) {
            switch (redPieces.get(i).getType()) {
                case 'R': value += 600;
                    value += fowardRook[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                    break;
                case 'H': value += 270;
                    value += fowardHorse[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                    break;
                case 'E': value += 120;
                    break;
                case 'B': value += 120;
                    break;
                case 'K': value += 6000;
                    break;
                case 'C': value += 285;
                    value += fowardCannon[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                    break;
                case 'P': value += 30;
                    value += fowardPawn[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                    break;
            }
        }
        return value;
    }

    private int evaluateBlack() {
        int value = 0;

        // Evaluate the pieces
        for(int i = 0; i < redPieces.size(); i++) {
            switch (redPieces.get(i).getType()) {
                case 'r': value += 600;
                    //value += backwardRook[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                    break;
                case 'h': value += 270;
                    //value += backwardHorse[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                    break;
                case 'e': value += 120;
                    break;
                case 'b': value += 120;
                    break;
                case 'k': value += 6000;
                    break;
                case 'c': value += 285;
                    //value += backwardCannon[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                    break;
                case 'p': value += 30;
                    //value += backwardPawn[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                    break;
            }
        }
        return value;
    }


    private int evaluate(char color) {
        int value = 0;

        if (this.color == 'r') {
            for (int i = 0; i < redPieces.size(); i++) {
                switch (redPieces.get(i).getType()) {
                    case 'R':
                        value += 600;
                        //value += fowardRook[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                        break;
                    case 'H':
                        value += 270;
                        //value += fowardHorse[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                        break;
                    case 'E':
                        value += 120;
                        break;
                    case 'B':
                        value += 120;
                        break;
                    case 'K':
                        value += 6000;
                        break;
                    case 'C':
                        value += 285;
                        //value += fowardCannon[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                        break;
                    case 'P':
                        value += 30;
                        //value += fowardPawn[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                        break;
                }
            }
            for (int i = 0; i < model.getBlackPieces().size(); i++) {
                switch (model.getBlackPieces().get(i).getType()) {
                    case 'R':
                        value -= 600;
                        //value -= fowardRook[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                        break;
                    case 'H':
                        value -= 270;
                        //value -= fowardHorse[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                        break;
                    case 'E':
                        value -= 120;
                        break;
                    case 'B':
                        value -= 120;
                        break;
                    case 'K':
                        value -= 6000;
                        break;
                    case 'C':
                        value -= 285;
                        //value -= fowardCannon[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                        break;
                    case 'P':
                        value -= 30;
                        //value -= fowardPawn[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                        break;
                }
            }
        } else{
            for (int i = 0; i < redPieces.size(); i++) {
                switch (redPieces.get(i).getType()) {
                    case 'R':
                        value -= 600;
                        //value -= fowardRook[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                        break;
                    case 'H':
                        value -= 270;
                        //value -= fowardHorse[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                        break;
                    case 'E':
                        value -= 120;
                        break;
                    case 'B':
                        value -= 120;
                        break;
                    case 'K':
                        value -= 6000;
                        break;
                    case 'C':
                        value -= 285;
                        //value -= fowardCannon[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                        break;
                    case 'P':
                        value -= 30;
                        //value -= fowardPawn[redPieces.get(i).getPosition().getRow()][redPieces.get(i).getPosition().getCol()];
                        break;
                }
            }
            for (int i = 0; i < model.getBlackPieces().size(); i++) {
                switch (model.getBlackPieces().get(i).getType()) {
                    case 'R':
                        value += 600;
                        //value += fowardRook[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                        break;
                    case 'H':
                        value += 270;
                        //value += fowardHorse[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                        break;
                    case 'E':
                        value += 120;
                        break;
                    case 'B':
                        value += 120;
                        break;
                    case 'K':
                        value += 6000;
                        break;
                    case 'C':
                        value += 285;
                        //value += fowardCannon[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                        break;
                    case 'P':
                        value += 30;
                        //value += fowardPawn[blackPieces.get(i).getPosition().getRow()][blackPieces.get(i).getPosition().getCol()];
                        break;
                }
            }
        }
        /*
        if(this.color == 'r') {
            for(int i = 0; i< 10; i++) {
                for (int j = 0; j < 9; j++) {
                    switch (board[i][j].getType()) {
                        case 'R':
                            value += 600;
                            value += fowardRook[i][j];
                            break;
                        case 'H':
                            value += 270;
                            value += fowardHorse[i][j];
                            break;
                        case 'E':
                            value += 120;
                            break;
                        case 'B':
                            value += 120;
                            break;
                        case 'K':
                            value += 6000;
                            break;
                        case 'C':
                            value += 285;
                            value += fowardCannon[i][j];
                            break;
                        case 'P':
                            value += 30;
                            value += fowardPawn[i][j];
                            break;


                        case 'r': value -= 600;
                            value -= backwardRook[i][j];
                            break;
                        case 'h': value -= 270;
                            value -= backwardHorse[i][j];
                            break;
                        case 'e': value -= 120;
                            break;
                        case 'b': value -= 120;
                            break;
                        case 'k': value -= 6000;
                            break;
                        case 'c': value -= 285;
                            value -= backwardCannon[i][j];
                            break;
                        case 'p': value -= 30;
                            value -= backwardPawn[i][j];
                            break;
                    }
                }
            }
        } else {
            for(int i = 0; i< 10; i++) {
                for (int j = 0; j < 9; j++) {
                    switch (board[i][j].getType()) {
                        case 'R':
                            value -= 600;
                            //value -= fowardRook[i][j];
                            break;
                        case 'H':
                            value -= 270;
                            //value -= fowardHorse[i][j];
                            break;
                        case 'E':
                            value -= 120;
                            break;
                        case 'B':
                            value -= 120;
                            break;
                        case 'K':
                            value -= 6000;
                            break;
                        case 'C':
                            value -= 285;
                            //value -= fowardCannon[i][j];
                            break;
                        case 'P':
                            value -= 30;
                            //value -= fowardPawn[i][j];
                            break;


                        case 'r': value += 600;
                            //value += backwardRook[i][j];
                            break;
                        case 'h': value += 270;
                            //value += backwardHorse[i][j];
                            break;
                        case 'e': value += 120;
                            break;
                        case 'b': value += 120;
                            break;
                        case 'k': value += 6000;
                            break;
                        case 'c': value += 285;
                            //value += backwardCannon[i][j];
                            break;
                        case 'p': value += 30;
                            //value += backwardPawn[i][j];
                            break;
                    }
                }
            }
        }*/
        return value;
    }


    public Piece simulateTheMove(Move move) {
        // Setup
        int beforeRow = move.getBefore().getRow();
        int beforeCol = move.getBefore().getCol();
        int afterRow = move.getAfter().getRow();
        int afterCol = move.getAfter().getCol();

        Piece movingPiece = board[beforeRow][beforeCol];
        Piece takenPiece = board[afterRow][afterCol];

        // Remove stuff
        if(takenPiece.getType() != '-') {
            blackPieces.remove(takenPiece);
            redPieces.remove(takenPiece);
        }

        // Update the move
        Piece updated = board[beforeRow][beforeCol];
        updated.setPosition(new Position(afterRow,afterCol));
        board[afterRow][afterCol] = updated;
        board[beforeRow][beforeCol] = new Empty(model, '-', new Position(beforeRow, beforeCol));



/*
        Log.d("tag", this.toString());
        Log.d("tag", "redSize = "  + redPieces.size());
        Log.d("tag", "blackSize = " + blackPieces.size());*/

        return takenPiece;

    }

    private void undoMove(Move move, Piece taken) {
        int beforeRow = move.getBefore().getRow();
        int beforeCol = move.getBefore().getCol();
        int afterRow = move.getAfter().getRow();
        int afterCol = move.getAfter().getCol();

        board[beforeRow][beforeCol] = board[afterRow][afterCol];
        board[beforeRow][beforeCol].setPosition(new Position(beforeRow, beforeCol));

        board[afterRow][afterCol] = taken;

        if(taken.getColor() == 'r') {
            redPieces.add(taken);
        } else if(taken.getColor() == 'b'){
            blackPieces.add(taken);
        }

    }



    private double alphaBeta(char turn, double alpha, double beta, int depth, ArrayList<Move> possibleMoves) {
        depth--;
        if(depth == 0) {
            return evaluate(color);
        }
        //ArrayList<Integer> possibleMoves = getPossibleMoves();

        if(turn == color) {
            for(Move m : possibleMoves) {
                /*
                Cell tempMove = findSquare(m);
                tempMove.setValue(turn);*/
                Piece taken = simulateTheMove(m);
/*
                if(win(turn,tempMove.getRow(), tempMove.getCol())) {
                    tempMove.setValue(null);
                    return 10000;
                }*/

                char otherPlayer;
                if(turn == 'b') {
                    otherPlayer ='r';
                } else {
                    otherPlayer = 'b';
                }
                ArrayList<Move> newPossibleMoves = getPossibleMoves(otherPlayer);

                if(newPossibleMoves.size() == 0) {
                    undoMove(m,taken);
                    return 10000;
                }
                double score = alphaBeta(otherPlayer, alpha, beta, depth,newPossibleMoves);
                if(score > alpha) {
                    alpha = score;
                }
                if(alpha >= beta) {
                    //tempMove.setValue(null);
                    undoMove(m,taken);
                    return alpha;
                }
                //tempMove.setValue(null);
                undoMove(m,taken);

            }
            return alpha;
        } else {
            for(Move m : possibleMoves) {
                /*
                Cell tempMove = findSquare(m);
                tempMove.setValue(turn);
                */
                Piece taken = simulateTheMove(m);

                /*
                if(win(turn,tempMove.getRow(), tempMove.getCol())) {
                    tempMove.setValue(null);
                    return -10000;
                }*/

                char otherPlayer;
                if(turn == 'b') {
                    otherPlayer = 'r';
                } else {
                    otherPlayer = 'b';
                }
                ArrayList<Move> newPossibleMoves = getPossibleMoves(otherPlayer);

                if(newPossibleMoves.size() == 0) {
                    undoMove(m,taken);
                    return -10000;
                }
                double score = alphaBeta(otherPlayer, alpha, beta, depth,newPossibleMoves);
                if(score < beta) {
                    beta = score;
                }
                if(alpha >= beta) {
                    //tempMove.setValue(null);
                    undoMove(m,taken);
                    return beta;
                }
                //tempMove.setValue(null);
                undoMove(m,taken);

            }
            return beta;
        }
    }








    private void createBackwardsMatrices() {
        for(int i = 0; i < model.getROW(); i++) {
            for(int j = 0; j < model.getCOL(); j++) {
                backwardRook[i][j] = fowardRook[model.getROW()-1-i][j];
                backwardHorse[i][j] = fowardHorse[model.getROW()-1-i][j];
                backwardCannon[i][j] = fowardCannon[model.getROW()-1-i][j];
                backwardPawn[i][j] = fowardPawn[model.getROW()-1-i][j];
            }
        }
    }


    public Move getMove() {
        Move move = null;
        double score = Integer.MIN_VALUE;
        double alpha = Integer.MIN_VALUE;
        double beta = Integer.MAX_VALUE;
        ArrayList<Move> moves = getPossibleMoves(color);

        if(moves.size() == -1){
            return null;
        }

        //Collections.shuffle(moves);

        char otherPlayer;
        if(color == 'b') {
            otherPlayer = 'r';
        } else {
            otherPlayer = 'b';
        }

        for(Move m : moves) {
            // Make move
            /*
            Cell tempMove = findSquare(i);
            tempMove.setValue(color);
            */
            Piece taken = simulateTheMove(m);


            // Evaluate move
            /*
            if(win(color,tempMove.getRow(), tempMove.getCol())) {
                tempMove.setValue(null);
                return i;
            }*/

            ArrayList<Move> possibleMoves = getPossibleMoves(otherPlayer);
            if(possibleMoves.size()==0){
                return m;
            }
            double value = alphaBeta(otherPlayer, alpha, beta, depth,possibleMoves);
            if(value > score) {
                score = value;
                move = m;
            }



            // undo move
            //tempMove.setValue(null);
            undoMove(m,taken);
        }

        if(score == -1) {
            Collections.shuffle(moves);
            move = moves.get(0);
        }

        //System.out.println(evaluate(color));
        Log.d("tag",evaluate('b')+"");
        return move;


    }

    private ArrayList<Move> getPossibleMoves(char tempColor) {

        ArrayList<Piece> pieces;
        if(tempColor == 'r') {
            pieces = model.getRedPieces();
        } else {
            pieces = model.getBlackPieces();
        }

        ArrayList<Move> moves = new ArrayList<>();
        for(int i = 0; i < pieces.size(); i++) {
            int sourceRow = pieces.get(i).getPosition().getRow();
            int sourceCol = pieces.get(i).getPosition().getCol();
            ArrayList<Position> possibleMoves = pieces.get(i).getPossibleMoves();
            for(int j = 0; j < possibleMoves.size(); j++) {
                int targetRow = possibleMoves.get(j).getRow();
                int targetCol = possibleMoves.get(j).getCol();
                moves.add(new Move(new Position(sourceRow, sourceCol), new Position(targetRow, targetCol)));



            }
        }

        return moves;
    }


}
