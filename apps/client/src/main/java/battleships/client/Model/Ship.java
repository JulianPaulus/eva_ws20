package battleships.client.Model;

public class Ship {
	private ShipType type;
	private int xCoordinate;
	private int yCoordinate;
	private boolean isHorizontal;

	private int Live;

	public Ship(ShipType type, int xCoordinate, int yCoordinate, boolean isHorizontal) {
		this.type = type;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.isHorizontal=isHorizontal;
		Live=type.getValue();
	}

	public int getLive() {
		return Live;
	}

	public ShipType getType() {
		return type;
	}

	public void setType(ShipType type) {
		this.type = type;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public void hit(){
		Live--;
	}

	public boolean isDestroyed(){
		return Live==0;
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

	public void setHorizontal(boolean horizontal) {
		isHorizontal = horizontal;
	}

}
