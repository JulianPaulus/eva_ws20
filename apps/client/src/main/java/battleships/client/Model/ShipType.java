package battleships.client.Model;

public enum ShipType {
	twoTiles (2),
	threeTiles (3),
	forTiles(4),
	fiveTiles(5);

	private int value;

	private ShipType(int v)
	{
		value=v;
	}

	public int getValue() {
		return value;
	}
}
