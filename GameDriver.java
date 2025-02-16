public class GameDriver {
    public static void main(String[] args) {
        Game unoGame = new GameBuilder()
                .setInitialCards(7)
                .setAllowStacking(false)
                .setCardsToDrawIfNoPlay(1)
                .build();

        unoGame.play();
    }
}