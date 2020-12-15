package battleships.model;

public enum ShipType {
	TWO_TILES(2),
	THREE_TILES(3),
	FOUR_TILES(4),
	FIVE_TILES(5);

	private final int value;

	ShipType(final int v)
	{
		value = v;
	}

	public int getValue() {
		return value;
	}
}
