class Card {
    private String color;
    private String value;

    public Card(String color, String value) {
        this.color = color;
        this.value = value;
    }

    public String getColor() { return color; }
    public String getValue() { return value; }

    public boolean isDrawTwo() {
        return "Draw Two".equals(value);
    }

    public boolean isWildDrawFour() {
        return "Wild Draw Four".equals(value);
    }

    public boolean isWild() {
        return "Wild".equals(value);
    }

    @Override
    public String toString() {
        return color + " " + value;
    }
}