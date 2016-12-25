package com.example.brian.chinesechesscopy.controllers;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.brian.chinesechesscopy.R;
import com.example.brian.chinesechesscopy.model.ChineseChessModel;
import com.example.brian.chinesechesscopy.model.Position;
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
 * Created by Brian on 16/12/2.
 */
public class SquareListener implements View.OnClickListener {

    private ChineseChessModel model;
    private int row;
    private int col;
    View imageView;
    Piece[][] board;

    ImageView innerView;
    GridView gridView;
    SquareAdapter squareAdapter;
    Context mContext;





    public SquareListener(ChineseChessModel model, int row, int col, View imageView, GridView gridView, SquareAdapter squareAdapter,Context mContext) {
        this.model = model;
        this.row = row;
        this.col = col;
        this.imageView = imageView;
        this.squareAdapter = squareAdapter;
        board = model.getBoard();
        this.gridView = gridView;
        this.mContext = mContext;

    }

    int count = 0;

    @Override
    public void onClick(View v) {
        if(model.isGameOver()) {
            return;
        }
        //squareAdapter.repaint();

        // Weird error case
        if(count == 0 && row == 0 && col == 0) {
            model.getImageViewGrid()[0][0] = v;//.findViewById(R.id.innersquare);;
            count++;

        }
        //imageView.setImageResource(R.color.colorClear);
        /*
        possible moves = 141, 208, 13, 242
        clear = 0,0,0,0
        selected =
         */

        for(int i = 0; i < model.getComputerHighlight().size(); i++) {
            model.getImageViewGrid()[model.getComputerHighlight().get(i).getRow()][model.getComputerHighlight().get(i).getCol()].setBackgroundColor(Color.argb(0,0,0,0));
        }
        model.getComputerHighlight().clear();


        ///////////////////////////////// Highlighting stuff
        // Selecting stuff1
        if(model.getCurrentPossibleMoves().size() != 0 && validMove()) {

            // Move the piece
            Position source = model.getSelectedPosition();
            Piece takenPiece = board[row][col];

            // Remove stuff
            if(takenPiece.getType() != '-') {
                model.getBlackPieces().remove(takenPiece);
                model.getRedPieces().remove(takenPiece);
            }

            // Remove stuff
            Piece updated = board[source.getRow()][source.getCol()];
            updated.setPosition(new Position(row,col));
            board[row][col] = updated;//createPieceCopy(source);
            board[source.getRow()][source.getCol()] = new Empty(model, '-', new Position(source.getRow(), source.getCol()));



            // fix the pictures up
            //fixImages(source, takenPiece);
            squareAdapter.repaint();


            // Unhighlight everything

            model.getImageViewGrid()[source.getRow()][source.getCol()].setBackgroundColor(Color.argb(0, 0, 0, 0));
            //model.getImageViewGrid()[source.getRow()][source.getCol()].setBackgroundResource(0);

            squareAdapter.repaint();


            for (Position p : model.getCurrentPossibleMoves()) {

                model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundColor(Color.argb(0, 0, 0, 0));
                //model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundResource(0);

            }
            model.getCurrentPossibleMoves().clear();

            model.switchTurn();

        }
        else {
            // If a piece is selected
            if (board[row][col] != null && model.getTurn() == board[row][col].getColor()) {
                if (model.getSelectedPosition() == null) {
                    model.getImageViewGrid()[row][col].setBackgroundColor(Color.argb(141, 0, 4, 255));
                    //model.getImageViewGrid()[row][col].setBackgroundResource(R.drawable.select);

                    // possible moves
                    ArrayList<Position> validMoves = board[row][col].getPossibleMoves();
                    model.setCurrentPossibleMoves(validMoves);
                    for (Position p : validMoves) {
                        model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundColor(Color.argb(150, 255, 64, 129));
                       // model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundResource(R.drawable.select);

                    }
                    View tempView = model.getSelectedImageView();
                    if (tempView != null) {
                        tempView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                        //tempView.setBackgroundResource(0);
                        // undo possible moves

                        for (Position p : model.getCurrentPossibleMoves()) {
                            model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundColor(Color.argb(0, 0, 0, 0));
                            //model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundResource(0);

                        }
                        model.getCurrentPossibleMoves().clear();
                    }
                    model.setSelectedPosition(new Position(row, col));
                } else {

                    model.getSelectedImageView().setBackgroundColor(Color.argb(0, 0, 0, 0));
                    //model.getSelectedImageView().setBackgroundResource(0);

                    // undo possible moves

                    for (Position p : model.getCurrentPossibleMoves()) {
                        model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundColor(Color.argb(0, 0, 0, 0));
                       // model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundResource(0);

                    }


                    model.getCurrentPossibleMoves().clear();
                    model.setSelectedPosition(new Position(row, col));
                    model.getSelectedImageView().setBackgroundColor(Color.argb(141, 0, 4, 255));
                    //model.getImageViewGrid()[row][col].setBackgroundResource(R.drawable.select);

                    // possible moves
                    ArrayList<Position> validMoves = board[row][col].getPossibleMoves();
                    model.setCurrentPossibleMoves(validMoves);
                    for (Position p : validMoves) {
                        model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundColor(Color.argb(150, 255, 64, 129));
                        //model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundResource(R.drawable.select);

                    }
                }

            }
            // If an empty space is selected
            else {
                View tempView = model.getSelectedImageView();
                if (tempView != null) {

                    tempView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                    //tempView.setBackgroundResource(0);


                    model.setSelectedPosition(null);
                }
                for (Position p : model.getCurrentPossibleMoves()) {

                    model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundColor(Color.argb(0, 0, 0, 0));
                   // model.getImageViewGrid()[p.getRow()][p.getCol()].setBackgroundResource(0);

                }

                model.getCurrentPossibleMoves().clear();
            }
        }
        ///////////////////////////////// Highlighting stuff




    }

    private Piece createPieceCopy(Position source) {

        switch (board[source.getRow()][source.getCol()].getType()) {
            case 'r': return new Rook(model, 'b', new Position(row, col));
            case 'h': return new Horse(model, 'b', new Position(row, col));
            case 'e': return new Elephant(model, 'b', new Position(row, col));
            case 'b': return new Bishop(model, 'b', new Position(row, col));
            case 'k': return new King(model, 'b', new Position(row, col));
            case 'c': return new Cannon(model, 'b', new Position(row, col));
            case 'p': return new Pawn(model, 'b', new Position(row, col));


            case 'R': return new Rook(model, 'r', new Position(row, col));
            case 'H': return new Horse(model, 'r', new Position(row, col));
            case 'E': return new Elephant(model, 'r', new Position(row, col));
            case 'B': return new Bishop(model, 'r', new Position(row, col));
            case 'K': return new King(model, 'r', new Position(row, col));
            case 'C': return new Cannon(model, 'r', new Position(row, col));
            case 'P': return new Pawn(model, 'r', new Position(row, col));

            default: return null;
        }
    }

    private boolean validMove() {
        for(Position p : model.getCurrentPossibleMoves()) {
            if(p.getRow() == row && p.getCol() == col) {
                return true;
            }
        }
        return false;
    }

    private void fixImages(Position source, Piece takenPiece) {

        ImageView oImageView = (ImageView) model.getImageViewGrid()[row][col];

        switch (board[row][col].getType()) {
            case 'r':
                oImageView.setImageResource(R.drawable.br);
                break;
            case 'h':
                oImageView.setImageResource(R.drawable.bn);
                break;
            case 'e':
                oImageView.setImageResource(R.drawable.be);
                break;
            case 'b':
                oImageView.setImageResource(R.drawable.bb);
                break;
            case 'k':
                oImageView.setImageResource(R.drawable.bk);
                break;
            case 'c':
                oImageView.setImageResource(R.drawable.bc);
                break;
            case 'p':
                oImageView.setImageResource(R.drawable.bp);
                break;


            case 'R':
                oImageView.setImageResource(R.drawable.rr);
                break;
            case 'H':
                oImageView.setImageResource(R.drawable.rn);
                break;
            case 'E':
                oImageView.setImageResource(R.drawable.re);
                break;
            case 'B':
                oImageView.setImageResource(R.drawable.rb);
                break;
            case 'K':
                oImageView.setImageResource(R.drawable.rk);
                break;
            case 'C':
                oImageView.setImageResource(R.drawable.rc);
                break;
            case 'P':
                oImageView.setImageResource(R.drawable.rp);
                break;

        }
        ((ImageView) model.getImageViewGrid()[source.getRow()][source.getCol()]).setImageResource(R.color.colorClear);
        ((ImageView) model.getImageViewGrid()[source.getRow()][source.getCol()]).setBackgroundResource(0);

    }

    private void gameOver(char c) {

    }


}
