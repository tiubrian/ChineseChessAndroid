package com.example.brian.chinesechesscopy.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.brian.chinesechesscopy.ai.ComputerPlayer;
import com.example.brian.chinesechesscopy.ai.Move;
import com.example.brian.chinesechesscopy.model.pieces.Bishop;
import com.example.brian.chinesechesscopy.model.pieces.Cannon;
import com.example.brian.chinesechesscopy.model.pieces.Elephant;
import com.example.brian.chinesechesscopy.model.pieces.Empty;
import com.example.brian.chinesechesscopy.model.pieces.Horse;
import com.example.brian.chinesechesscopy.model.pieces.King;
import com.example.brian.chinesechesscopy.model.pieces.Pawn;
import com.example.brian.chinesechesscopy.model.pieces.Piece;
import com.example.brian.chinesechesscopy.model.pieces.Rook;
import com.example.brian.chinesechesscopy.views.SquareAdapter;

import java.util.ArrayList;

/**
 * Created by Brian on 16/12/1.
 */
public class ChineseChessModel {

    private Piece[][] board;
    private final int ROW = 10;
    private final int COL = 9;
    private ArrayList<Piece> redPieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();

    private Position selectedPosition;

    private View[][] imageViewGrid = new View[ROW][COL];
    private ArrayList<Position> currentPossibleMoves = new ArrayList<>();

    private char turn = 'r';

    private ComputerPlayer redComputer = null;
    private ComputerPlayer blackComputer = null;
    private ArrayList<Position> computerHighlight = new ArrayList<>();

    SquareAdapter squareAdapter;
    boolean gameOver = false;
    Context context;



    public ChineseChessModel(Context mContext, boolean redcomp, boolean blackcomp) {
        this.context = mContext;
        board = new Piece[ROW][COL];
        initializeBoard();
        if(blackcomp) {
            blackComputer = new ComputerPlayer('b', this);
        }
        if(redcomp) {
            redComputer = new ComputerPlayer('r', this);
        }

    }

    public void reset() {
        board = new Piece[ROW][COL];
        redPieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        selectedPosition = null;
        //imageViewGrid = new View[ROW][COL];
        currentPossibleMoves = new ArrayList<>();
        turn = 'r';
        redComputer = null;
        blackComputer = null;
        computerHighlight = new ArrayList<>();
        // square adapter
        gameOver = false;
        initializeBoard();
    }

    public void play() {
        if(redComputer != null) {
            Move move = redComputer.getMove();
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
            board[beforeRow][beforeCol] = new Empty(this, '-', new Position(beforeRow, beforeCol));


            //imageViewGrid[beforeRow][beforeCol].setBackgroundColor(Color.argb(141, 0, 4, 255));
            //imageViewGrid[afterRow][afterCol].setBackgroundColor(Color.argb(141, 0, 4, 255));
            computerHighlight.add(new Position(beforeRow, beforeCol));
            computerHighlight.add(new Position(afterRow, afterCol));


            // Update the gui
            squareAdapter.repaint();
            switchTurn();
            //imageViewGrid[beforeRow][beforeCol].setBackgroundColor(Color.argb(141, 0, 4, 255));
            //imageViewGrid[afterRow][afterCol].setBackgroundColor(Color.argb(141, 0, 4, 255));


        }
    }

    private void initializeBoard() {
        // Create empty spots
        for(int i = 0; i < ROW; i++) {
            for(int j = 0; j < COL; j++) {
                board[i][j] = new Empty(this, '-', new Position(i,j));
            }
        }

        // Initialize the red piece
        Rook redRook1 = new Rook(this, 'r', new Position(9,0));
        board[9][0] = redRook1;
        redPieces.add(redRook1);
        Rook redRook2 = new Rook(this, 'r', new Position(9,8));
        board[9][8] = redRook2;
        redPieces.add(redRook2);
        Cannon redCannon1 = new Cannon(this, 'r', new Position(7,1));
        board[7][1] = redCannon1;
        redPieces.add(redCannon1);
        Cannon redCannon2 = new Cannon(this, 'r', new Position(7,7));
        board[7][7] = redCannon2;
        redPieces.add(redCannon2);
        Horse redHorse1 = new Horse(this, 'r', new Position(9,1));
        board[9][1] = redHorse1;
        redPieces.add(redHorse1);
        Horse redHorse2 = new Horse(this, 'r', new Position(9,7));
        board[9][7] = redHorse2;
        redPieces.add(redHorse2);
        Elephant redElephant1 = new Elephant(this, 'r', new Position(9,2));
        board[9][2] = redElephant1;
        redPieces.add(redElephant1);
        Elephant redElephant2 = new Elephant(this, 'r', new Position(9,6));
        board[9][6] = redElephant2;
        redPieces.add(redElephant2);
        Bishop redBishop1 = new Bishop(this, 'r', new Position(9,3));
        board[9][3] = redBishop1;
        redPieces.add(redBishop1);

        Bishop redBishop2 = new Bishop(this, 'r', new Position(9,5));
        board[9][5] = redBishop2;
        redPieces.add(redBishop2);


        Pawn redPawn1 = new Pawn(this, 'r', new Position(6,0));
        board[6][0] = redPawn1;
        redPieces.add(redPawn1);
        Pawn redPawn2 = new Pawn(this, 'r', new Position(6,2));
        board[6][2] = redPawn2;
        redPieces.add(redPawn2);
        Pawn redPawn3 = new Pawn(this, 'r', new Position(6,4));
        board[6][4] = redPawn3;
        redPieces.add(redPawn3);
        Pawn redPawn4 = new Pawn(this, 'r', new Position(6,6));
        board[6][6] = redPawn4;
        redPieces.add(redPawn4);
        Pawn redPawn5 = new Pawn(this, 'r', new Position(6,8));
        board[6][8] = redPawn5;
        redPieces.add(redPawn5);
        King redKing = new King(this, 'r', new Position(9,4));
        board[9][4] = redKing;
        redPieces.add(redKing);

        // Initialize the black pieces
        Rook blackRook2 = new Rook(this,'b',new Position(0,8));
        board[0][8] = blackRook2;
        blackPieces.add(blackRook2);
        Rook blackRook1 = new Rook(this, 'b', new Position(0,0));
        board[0][0] = blackRook1;
        blackPieces.add(blackRook1);
        Cannon blackCannon1 = new Cannon(this,'b',new Position(2,1));
        board[2][1] = blackCannon1;
        blackPieces.add(blackCannon1);
        Cannon blackCannon2 = new Cannon(this,'b',new Position(2,7));
        board[2][7] = blackCannon2;
        blackPieces.add(blackCannon2);
        Horse blackHorse2 = new Horse(this,'b',new Position(0,7));
        board[0][7] = blackHorse2;
        blackPieces.add(blackHorse2);
        Horse blackHorse1 = new Horse(this,'b',new Position(0,1));
        board[0][1] = blackHorse1;
        blackPieces.add(blackHorse1);

        Elephant blackElephant1 = new Elephant(this, 'b', new Position(0,2));
        board[0][2] = blackElephant1;
        blackPieces.add(blackElephant1);
        Elephant blackElephant2 = new Elephant(this,'b', new Position(0,6));
        board[0][6] = blackElephant2;
        blackPieces.add(blackElephant2);
        Bishop blackBishop1 = new Bishop(this, 'b', new Position(0,3));
        board[0][3] = blackBishop1;
        blackPieces.add(blackBishop1);

        Bishop blackBishop2 = new Bishop(this, 'b', new Position(0,5));
        board[0][5] = blackBishop2;
        blackPieces.add(blackBishop2);



        Pawn blackPawn1 = new Pawn(this,'b', new Position(3,0));
        board[3][0] = blackPawn1;
        blackPieces.add(blackPawn1);
        Pawn blackPawn2 = new Pawn(this,'b', new Position(3,2));
        board[3][2] = blackPawn2;
        blackPieces.add(blackPawn2);
        Pawn blackPawn3 = new Pawn(this,'b' ,new Position(3,4));
        board[3][4] = blackPawn3;
        blackPieces.add(blackPawn3);
        Pawn blackPawn4 = new Pawn(this, 'b', new Position(3,6));
        board[3][6] = blackPawn4;
        blackPieces.add(blackPawn4);
        Pawn blackPawn5 = new Pawn(this, 'b', new Position(3,8));
        board[3][8] = blackPawn5;
        blackPieces.add(blackPawn5);
        King blackKing = new King(this,'b', new Position(0,4));
        board[0][4] = blackKing;
        blackPieces.add(blackKing);

    }




    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < ROW; i++) {
            for(int j = 0; j < COL; j++) {
                if(board[i][j] == null) {
                    sb.append("N ");
                } else {
                    sb.append(board[i][j].getType() + " ");
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public View getSelectedImageView() {
        if(selectedPosition == null) {
            return null;
        }
        return imageViewGrid[selectedPosition.getRow()][selectedPosition.getCol()];
    }


    public boolean isRedKingInCheck() {
        for(Piece p : blackPieces) {
            for(Position pos : p.noFilterGetPossibleMoves()) {
                int row = pos.getRow();
                int col = pos.getCol();
                if(board[row][col].getType() == 'K') {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBlackKingInCheck() {
        for(Piece p : redPieces) {
            for(Position pos : p.noFilterGetPossibleMoves()) {
                int row = pos.getRow();
                int col = pos.getCol();
                if(board[row][col].getType() == 'k') {
                    return true;
                }
            }
        }
        return false;
    }

    class AITurn implements Runnable {

        @Override
        public void run() {

            synchronized (this) {
                long time = System.currentTimeMillis();
                if (System.currentTimeMillis() - time < 1000L) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception localException) {

                    }
                }
                final Move move = blackComputer.getMove();
                //simulateMove(move);
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        simulateMove(move);
                    }
                });


                if (System.currentTimeMillis() - time < 1000L) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception localException) {

                    }
                }

            }
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //squareAdapter.repaint();
                }
            });

            switchTurn();

        }
    }

    class AITurnRed implements Runnable {

        @Override
        public void run() {

            synchronized (this) {
                long time = System.currentTimeMillis();
                if (System.currentTimeMillis() - time < 1000L) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception localException) {

                    }
                }
                final Move move = redComputer.getMove();
                //simulateMove(move);
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        simulateMove(move);
                    }
                });


                if (System.currentTimeMillis() - time < 1000L) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception localException) {

                    }
                }

            }
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //squareAdapter.repaint();
                }
            });

            switchTurn();

        }
    }

    public synchronized void switchTurn() {

        if(turn == 'r') {
            turn = 'b';

            if(generatePossibleMoves('b').size() == 0) {
                CharSequence text = "Red wins!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                gameOver = true;
                return;
            }

            if(blackComputer != null) {

                CharSequence text = "Computer is thinking...";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //Move move = blackComputer.getMove();
                //simulateMove(move);
                //switchTurn();
                new Thread(new AITurn()).start();

            }

        } else {
            turn = 'r';

            if(generatePossibleMoves('r').size() == 0) {

                CharSequence text = "Black Wins!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                gameOver = true;
                return;
            }

            if(redComputer != null) {
                CharSequence text = "Computer is thinking...";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                /*
                Move move = redComputer.getMove();
                simulateMove(move);
                switchTurn();*/
                new Thread(new AITurnRed()).start();


            }


        }
    }

    public synchronized void simulateMove(Move move) {
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
        board[beforeRow][beforeCol] = new Empty(this, '-', new Position(beforeRow, beforeCol));


        imageViewGrid[beforeRow][beforeCol].setBackgroundColor(Color.argb(141, 0, 4, 255));
        imageViewGrid[afterRow][afterCol].setBackgroundColor(Color.argb(141, 0, 4, 255));
        computerHighlight.add(new Position(beforeRow, beforeCol));
        computerHighlight.add(new Position(afterRow, afterCol));


        // Update the gui
        squareAdapter.repaint();
/*
        Log.d("tag", this.toString());
        Log.d("tag", "redSize = "  + redPieces.size());
        Log.d("tag", "blackSize = " + blackPieces.size());*/



    }




    private ArrayList<Move> generatePossibleMoves(char tempColor) {

        ArrayList<Piece> pieces;
        if(tempColor == 'r') {
            pieces = this.getRedPieces();
        } else {
            pieces = this.getBlackPieces();
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


    // Getters and setters
    public Piece[][] getBoard() {
        return board;
    }

    public void setBoard(Piece[][] board) {
        this.board = board;
    }

    public int getROW() {
        return ROW;
    }

    public int getCOL() {
        return COL;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    public void setBlackPieces(ArrayList<Piece> blackPieces) {
        this.blackPieces = blackPieces;
    }

    public ArrayList<Piece> getRedPieces() {
        return redPieces;
    }

    public void setRedPieces(ArrayList<Piece> redPieces) {
        this.redPieces = redPieces;
    }

    public View[][] getImageViewGrid() {
        return imageViewGrid;
    }

    public void setImageViewGrid(View[][] imageViewGrid) {
        this.imageViewGrid = imageViewGrid;
    }

    public Position getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(Position selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public ArrayList<Position> getCurrentPossibleMoves() {
        return currentPossibleMoves;
    }

    public void setCurrentPossibleMoves(ArrayList<Position> currentPossibleMoves) {
        this.currentPossibleMoves = currentPossibleMoves;
    }

    public char getTurn() {
        return turn;
    }

    public void setTurn(char turn) {
        this.turn = turn;
    }

    public ComputerPlayer getRedComputer() {
        return redComputer;
    }

    public void setRedComputer(ComputerPlayer redComputer) {
        this.redComputer = redComputer;
    }

    public ComputerPlayer getBlackComputer() {
        return blackComputer;
    }

    public void setBlackComputer(ComputerPlayer blackComputer) {
        this.blackComputer = blackComputer;
    }

    public SquareAdapter getSquareAdapter() {
        return squareAdapter;
    }

    public void setSquareAdapter(SquareAdapter squareAdapter) {
        this.squareAdapter = squareAdapter;
    }

    public ArrayList<Position> getComputerHighlight() {
        return computerHighlight;
    }

    public void setComputerHighlight(ArrayList<Position> computerHighlight) {
        this.computerHighlight = computerHighlight;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
