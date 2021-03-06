package chess.view.twod;

import chess.model.BoardSpace;
import chess.model.Position;
import chess.model.pieces.*;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoardPosition extends Button {

    private Position position;
    private boolean highlight = false;
    private boolean selected = false;

    BoardPosition(Position position) {
        super();
        this.setPosition(position);
        this.getStyleClass().add("boardSpace");
    }

    private void setPosition(Position position) {
        this.position = position;
    }

    public void invertPosition() {
        int currRow = this.getPosition().getRow();
        int currCol = this.getPosition().getCol();

        this.setPosition(new Position(7 - currRow, 7 - currCol));
    }

    public Position getPosition() {
        return this.position;
    }

    public void highlight(boolean highlight) {
        this.highlight = highlight;

        if (highlight)
            this.getStyleClass().add("highlight");
        else
            this.getStyleClass().remove("highlight");
    }

    public boolean isHighlighted() {
        return this.highlight;
    }

    public void select(boolean select) {
        this.selected = select;

        if (select) {
            this.getStyleClass().remove("highlight");
            this.getStyleClass().add("selected");
        }
        else{
            this.getStyleClass().remove("selected");
        }
    }

    public boolean isSelected() {
        return this.selected;
    }

    private void updateImage(ChessPiece piece) {
        if (piece == null) {
            this.setGraphic(null);
        }
        else {
            String pieceColor = piece.getPieceColor().toString().toLowerCase();
            String pieceType = piece.getClass().getSimpleName().toLowerCase();

            Image image = new Image(getClass().getResourceAsStream("/images/pieces/" +
                    pieceColor + "/" +
                    pieceType + ".png"));

            ImageView imgView = new ImageView();
            imgView.setPreserveRatio(true);
            imgView.setFitWidth(this.getWidth() * .7);
            imgView.setFitHeight(this.getHeight() * .7);
            imgView.setImage(image);

            this.setGraphic(imgView);
        }

    }

    public void update(BoardSpace space) {
        updateImage(space.getPiece());
    }

    public void addEventListener(EventHandler handler) {
        this.setOnAction(handler);
    }

}
