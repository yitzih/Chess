package chess.controller.twod;


import chess.model.ChessGame;
import chess.model.BoardSpace;
import chess.model.Move;
import chess.model.Position;
import chess.model.pieces.ChessPiece;
import chess.view.twod.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import java.util.List;

public class BoardController {

    private ChessGameUI gameUI;
    private ChessGame game;
    private Stage mainStage;

    public BoardController(Stage mainStage, ChessGameUI gameUI, ChessGame game) {

        this.gameUI = gameUI;
        this.game = game;
        this.mainStage = mainStage;

        for (int row = 0; row < gameUI.getGrid().length; row++) {
            for (int col = 0; col < gameUI.getGrid().length; col++) {
                gameUI.getBoardPosition(new Position(row, col)).addEventListener(new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        BoardPosition gridSpace = (BoardPosition) event.getSource();
                        Position position = gridSpace.getPosition();
                        BoardSpace space = BoardController.this.game.getBoardSpace(position);

                        if (gridSpace.isSelected()) {
                            //remove the selection and all highlights on the board
                            deselectPositionAtVector(gridSpace.getPosition());
                            removeHighlightFromAllBoardPositions();
                        }
                        else if (space.isOccupied() && (space).getPiece().getPieceColor() == BoardController.this.game.getCurrentTurn()){
                            //select the space and highlight all legal moves

                            deselectAllBoardPositions();
                            removeHighlightFromAllBoardPositions();
                            selectPositionAtVector(gridSpace.getPosition());

                            //if space is not empty highlight legal moves
                            if (space.isOccupied()) {
                                List<Position> legalMoves = getLegalMoves(position);

                                if (legalMoves.size() > 0)
                                    highlightPositions(legalMoves);
                            }
                        }
                        else if (gridSpace.isHighlighted()) {
                            //move the selected piece to the selected position and clear all highlights
                           // ChessPiece piece = BoardController.this.game.getSelectedPiece();
                            Position from = BoardController.this.game.getSelectedPosition();
                            Position to = position;

                            BoardController.this.game.makeMove(from, to);
                            BoardController.this.gameUI.updateBoard(BoardController.this.game);

                            deselectAllBoardPositions();
                            removeHighlightFromAllBoardPositions();

                            List<Move> gameHistory = game.getGameHistory();
                            gameUI.addMove(gameHistory.size(), gameHistory.get(gameHistory.size() - 1));
                            updateGraveyards();
                        }
                    }
                });
            }
        }

        //add action listeners to menubar items
        addEventHandlerToMenuExitItem();
        addEventHandlerToToggleHistoryVisibilityItem();
        addEventHandlerToToggleGraveyardVisibilityItem();
        addEventHandlerToInvertBoardItem();

    }

    private void addEventHandlerToToggleHistoryVisibilityItem() {
        gameUI.getToggleGameHistoryItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameUI.gameHistoryIsVisible())
                    hideGameHistory();
                else
                    showGameHistory();

            }
        });
    }

    private void addEventHandlerToToggleGraveyardVisibilityItem() {
        gameUI.getToggleGraveyardItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameUI.graveyardIsVisible())
                    hideGraveyard();
                else
                    showGraveyard();
            }
        });
    }

    private void addEventHandlerToMenuExitItem() {
        gameUI.getExitMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exitApplication();
            }
        });
    }

    private void addEventHandlerToInvertBoardItem() {
        gameUI.getInvertBoardItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deselectAllBoardPositions();
                removeHighlightFromAllBoardPositions();
                gameUI.invertBoard(game);
            }
        });
    }

    private void exitApplication() {
        System.exit(0);
    }

    private List<Position> getLegalMoves(Position position) {
        ChessPiece piece = game.getBoardSpace(position).getPiece();
        return piece.getLegalMoves(game, true);
    }

    private void highlightPositions(List<Position> positions) {
        for (Position position : positions) {
            gameUI.getBoardPosition(position).highlight(true);
        }
    }

    private void selectPositionAtVector(Position position) {
        gameUI.getBoardPosition(position).select(true);

        game.selectNewPosition(position);
    }

    private void deselectPositionAtVector(Position position) {
        gameUI.getBoardPosition(position).select(false);

        if (game.getSelectedPosition().equals(position))
            game.selectNewPosition(null);
    }

    private void highlightPositionAtVector(Position position) {
        gameUI.getBoardPosition(position).highlight(true);
    }

    private void removeHighlightFromPositionAtVector(Position position) {
        gameUI.getBoardPosition(position).highlight(false);
    }

    private void deselectAllBoardPositions() {
        gameUI.removeAllBoardSelections();
        game.selectNewPosition(null);
    }

    private void removeHighlightFromAllBoardPositions() {
        gameUI.removeAllBoardHighlights();
    }

    private void showGameHistory() {
        if (gameUI.getBottom() == null) {
            gameUI.showGameHistory();
            mainStage.sizeToScene();
        }
    }

    private void hideGameHistory() {
        gameUI.hideGameHistory();
        mainStage.sizeToScene();
    }

    private void showGraveyard() {
        if (gameUI.getLeft() == null) {
            gameUI.showGraveyards();
            gameUI.resetGameHistoryWidth();
            mainStage.sizeToScene();
        }
    }

    private void hideGraveyard() {
        gameUI.hideGraveyards();
        gameUI.resetGameHistoryWidth();
        mainStage.sizeToScene();
    }

    private void updateGraveyards() {
        gameUI.getBlackGraveyard().update(game.getBlackPieces());
        gameUI.getWhiteGraveyard().update(game.getWhitePieces());
    }


}
