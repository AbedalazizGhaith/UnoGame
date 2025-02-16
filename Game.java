import java.util.*;

abstract class Game {
    protected int initialCards;
    protected boolean allowStacking;
    protected List<Player> players;
    protected Stack<Card> drawPile;
    protected Stack<Card> discardPile;
    protected boolean unoCalled;

    public Game(int initialCards, boolean allowStacking) {
        this.initialCards = initialCards;
        this.allowStacking = allowStacking;
        this.players = new ArrayList<>();
        this.drawPile = new Stack<>();
        this.discardPile = new Stack<>();
        this.unoCalled = false;
    }

    public abstract void play();
}