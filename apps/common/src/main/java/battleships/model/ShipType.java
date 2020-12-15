package battleships.model;

public enum ShipType {
	FIVE_TILES(5, 1, null),
	FOUR_TILES(4, 1, FIVE_TILES),
	THREE_TILES(3, 2, FOUR_TILES),
	TWO_TILES(2, 1, THREE_TILES);

	private final int size;
	private final int nrPerField;
	private final ShipType next;

	ShipType(final int size, final int nrPerField, ShipType next) {
		this.size = size;
		this.nrPerField = nrPerField;
		this.next = next;
	}

	public int getSize() {
		return size;
	}

	public int getNrPerField() {
		return nrPerField;
	}

	public ShipType getNext() {
		return next;
	}

	public static ShipType getPrev(ShipType shipType) {
		for(ShipType current : values()) {
			if(current.getNext() == shipType) {
				return current;
			}
		}
		return null;
	}


}
