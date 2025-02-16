import java.util.*;


// A sample Uno game variation
class UnoGame extends Game {
    private String currentColor;
    private int cardsToDrawIfNoPlay;

    public UnoGame(int initialCards, boolean allowStacking, int cardsToDrawIfNoPlay) {
        super(initialCards, allowStacking);
        this.cardsToDrawIfNoPlay = cardsToDrawIfNoPlay;
        initializeGame();
    }

    private void initializeGame() {
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        String[] values = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Reverse", "Skip", "Draw Two", "Reverse", "Skip", "Draw Two"};

        for (String color : colors) {
            for (String value : values) {
                drawPile.push(new Card(color, value));
            }
        }
        drawPile.push(new Card("Wild", "Wild"));
        drawPile.push(new Card("Wild", "Wild"));
        drawPile.push(new Card("Wild", "Wild"));
        drawPile.push(new Card("Wild", "Wild"));
        drawPile.push(new Card("Wild", "Wild Draw Four"));
        drawPile.push(new Card("Wild", "Wild Draw Four"));
        drawPile.push(new Card("Wild", "Wild Draw Four"));
        drawPile.push(new Card("Wild", "Wild Draw Four"));
        Collections.shuffle(drawPile);
    }

    private boolean isValidPlay(Card playedCard, Card topCard) {
        return playedCard.getColor().equals(currentColor) ||
                playedCard.getValue().equals(topCard.getValue()) ||
                playedCard.getColor().equals("Wild");
    }


    @Override
    public void play() {
        Scanner scanner = new Scanner(System.in);
        int numPlayers = 0;
        boolean reverseDirection = false;

        while (true) {
            try {
                System.out.print("Enter number of players: ");
                numPlayers = Integer.parseInt(scanner.nextLine());
                if (numPlayers > 0) {
                    break;
                } else {
                    System.out.println("Please enter a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter player " + (i + 1) + " name: ");
            String name = scanner.nextLine();
            Player player = new Player(name);
            players.add(player);
        }

        for (Player player : players) {
            for (int i = 0; i < initialCards; i++) {
                if (!drawPile.isEmpty()) {
                    player.drawCard(drawPile.pop());
                }
            }
        }
        discardPile.push(drawPile.pop());
        currentColor = discardPile.peek().getColor();


        int currentPlayerIndex = 0;
        int drawStack = 0;

        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("\nCurrent discard pile: " + (discardPile.isEmpty() ? "Empty" : discardPile.peek()));
            currentPlayer.showHand();

            boolean validPlay = false;
            while (!validPlay) {
                System.out.print(currentPlayer.getName() + ", choose a card (index) or draw (-1): ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == -1) {
                    refillDrawPile();
                    for (int i = 0; i < cardsToDrawIfNoPlay; i++) {
                        if (!drawPile.isEmpty()) {
                            currentPlayer.drawCard(drawPile.pop());
                        } else {
                            System.out.println("No more cards to draw!");
                            break;
                        }
                    }
                    validPlay = true;
                } else if (choice >= 0 && choice < currentPlayer.getHand().size()) {
                    Card playedCard = currentPlayer.getHand().get(choice);
                    Card topCard = discardPile.peek();

                    if (isValidPlay(playedCard, topCard)) {
                        currentPlayer.getHand().remove(choice);
                        discardPile.push(playedCard);
                        System.out.println(currentPlayer.getName() + " played " + playedCard);
                        validPlay = true;

                        if (playedCard.getColor().equals("Wild")) {
                            while (true) {
                                System.out.print("Choose a color (Red, Blue, Green, Yellow): ");
                                String chosenColor = scanner.nextLine().trim();
                                if (Arrays.asList("Red", "Blue", "Green", "Yellow").contains(chosenColor)) {
                                    currentColor = chosenColor;
                                    break;
                                } else {
                                    System.out.println("Invalid color. Please choose Red, Blue, Green, or Yellow.");
                                }
                            }
                        } else {
                            currentColor = playedCard.getColor();
                        }

                        if (playedCard.isWildDrawFour()) {
                            drawStack += 4;
                        } else if (playedCard.isDrawTwo()) {
                            drawStack += 2;
                        } else if (playedCard.getValue().equals("Skip")) {
                            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                            System.out.println(players.get(currentPlayerIndex).getName() + " is skipped!");
                        } else if (playedCard.getValue().equals("Reverse")) {
                            reverseDirection = !reverseDirection;
                            System.out.println("Direction reversed!");
                        }
                    } else {
                        System.out.println("Invalid play! You must play a card that matches the color or value of the top card, or a wild card.");
                    }
                }
            }

            if (drawStack > 0) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                Player nextPlayer = players.get(currentPlayerIndex);
                if (!allowStacking || !canStack(nextPlayer, drawStack)) {
                    System.out.println(nextPlayer.getName() + " must draw " + drawStack + " cards!");
                    for (int i = 0; i < drawStack; i++) {
                        refillDrawPile(); // Refill if necessary
                        if (!drawPile.isEmpty()) {
                            nextPlayer.drawCard(drawPile.pop());
                        }
                    }
                    drawStack = 0;
                } else {
                    System.out.println(nextPlayer.getName() + " can stack a draw card.");
                }
            } else {
                if (reverseDirection) {
                    currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
                } else {
                    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                }
            }

            if (currentPlayer.hasOneCard()) {
                System.out.print(currentPlayer.getName() + " say 'UNO' (y/n)? ");
                String unoResponse = scanner.nextLine();
                if (unoResponse.equalsIgnoreCase("y")) {
                    currentPlayer.callUno();
                }
            }

            // Check for game end condition
            if (currentPlayer.getHand().isEmpty()) {
                System.out.println(currentPlayer.getName() + " wins the round!");
                break;
            }
        }

        System.out.println("Game over! No more cards in the draw pile.");
        scanner.close();
    }

    private void refillDrawPile() {
        if (drawPile.isEmpty() && discardPile.size() > 1) {
            Card topCard = discardPile.pop();
            List<Card> cardsToShuffle = new ArrayList<>(discardPile);
            Collections.shuffle(cardsToShuffle);
            drawPile.addAll(cardsToShuffle);
            discardPile.clear();
            discardPile.push(topCard);
            System.out.println("Draw pile refilled and shuffled.");
        }
    }

    private boolean canStack(Player player, int drawStack) {
        for (Card card : player.getHand()) {
            if ((drawStack % 4 == 0 && card.isWildDrawFour()) || (drawStack % 2 == 0 && card.isDrawTwo())) {
                return true;
            }
        }
        return false;
    }
}
