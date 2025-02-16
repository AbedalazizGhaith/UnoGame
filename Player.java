import java.util.*;
class Player {
    private String name;
    private List<Card> hand;
    private boolean unoCalled;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.unoCalled = false;
    }

    public void drawCard(Card card) {
        hand.add(card);
    }

    public void showHand() {
        System.out.println(name + "'s hand: " + hand);
    }

    public String getName() { return name; }
    public List<Card> getHand() { return hand; }
    public boolean hasOneCard() { return hand.size() == 1; }
    public void callUno() { this.unoCalled = true; }
    public boolean isUnoCalled() { return unoCalled; }
}
