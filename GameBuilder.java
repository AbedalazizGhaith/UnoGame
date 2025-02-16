class GameBuilder {
    private int initialCards = 7;
    private boolean allowStacking = false;
    private int cardsToDrawIfNoPlay = 1; // Default to 1 card

    public GameBuilder setInitialCards(int initialCards) {
        this.initialCards = initialCards;
        return this;
    }

    public GameBuilder setAllowStacking(boolean allowStacking) {
        this.allowStacking = allowStacking;
        return this;
    }

    public GameBuilder setCardsToDrawIfNoPlay(int cardsToDraw) {
        this.cardsToDrawIfNoPlay = cardsToDraw;
        return this;
    }

    public Game build() {
        return new UnoGame(initialCards, allowStacking, cardsToDrawIfNoPlay);
    }
}