package battleships.model;

public class Ship {
	private ShipType type;
	private int xCoordinate;
	private int yCoordinate;
	private boolean isHorizontal;

	private int life;

	public Ship(final ShipType type, final int xCoordinate, final int yCoordinate, final boolean isHorizontal) {
		this.type = type;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.isHorizontal = isHorizontal;
		this.life = type.getSize();
	}

	public int getLife() {
		return life;
	}

	public ShipType getType() {
		return type;
	}

	public void setType(final ShipType type) {
		this.type = type;
	}

	public int getXCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(final int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getYCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(final int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public void hit() {
		life--;
	}

	public boolean isDestroyed() {
		return life == 0;
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

	public void setHorizontal(final boolean horizontal) {
		isHorizontal = horizontal;
	}

	public int getEndXCoordinate() {
		return xCoordinate + (isHorizontal ? type.getSize() - 1 : 0);
	}

	public int getEndYCoordinate() {
		return yCoordinate + (isHorizontal ? 0 : type.getSize() - 1);
	}

	@Override
	public boolean equals(final Object other) {
		if (other == this)
			return true;

		if (!(other instanceof Ship))
			return false;

		Ship o = (Ship) other;

		return (o.xCoordinate == xCoordinate) && (o.yCoordinate == yCoordinate) && (o.isHorizontal == isHorizontal) && (o.type == type);
	}
}
