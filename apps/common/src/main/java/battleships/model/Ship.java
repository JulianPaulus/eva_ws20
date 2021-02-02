package battleships.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

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

	public boolean isAt(final int x, final int y) {
		return x >= xCoordinate && x <= getEndXCoordinate()
			&& y >= yCoordinate && y <= getEndYCoordinate();
	}

	public void writeToStream(final DataOutputStream stream) throws IOException {
		stream.writeInt(getType().getSize());
		stream.writeInt(getXCoordinate());
		stream.writeInt(getYCoordinate());
		stream.writeBoolean(isHorizontal());
	}

	public static Ship fromStream(final DataInputStream stream) throws IOException {
		int shipSize = stream.readInt();
		Optional<ShipType> oShipType = Arrays.stream(ShipType.values())
			.filter(x -> x.getSize() == shipSize).findFirst();
		if (!oShipType.isPresent()) {
			//TODO error -> write serverexceptionpacket and goodbye?
		}
		return new Ship(oShipType.get(), stream.readInt(), stream.readInt(), stream.readBoolean());
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
