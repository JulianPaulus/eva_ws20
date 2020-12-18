package battleships.util;

import battleships.model.CoordinateState;
import battleships.model.Ship;
import battleships.net.exception.IllegalShipPositionException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class Utils {

	// https://gist.github.com/jeffjohnson9046/c663dd22bbe6bb0b3f5e
	public static byte[] getBytesFromUUID(final UUID uuid) {
		ByteBuffer bb = ByteBuffer.wrap(new byte[Constants.UUID_BYTE_COUNT]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());

		return bb.array();
	}

	// https://gist.github.com/jeffjohnson9046/c663dd22bbe6bb0b3f5e
	public static UUID getUUIDFromBytes(final byte[] bytes) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		long high = byteBuffer.getLong();
		long low = byteBuffer.getLong();

		return new UUID(high, low);
	}

	public static void writeUUIDToStream(final DataOutputStream dos, final UUID uuid) throws IOException {
		byte[] idBytes = Utils.getBytesFromUUID(uuid);
		for (int i = 0; i < Constants.UUID_BYTE_COUNT; i++) {
			dos.writeByte(idBytes[i]);
		}
	}

	public static UUID readUUIDFromStream(final DataInputStream dos) throws IOException {
		byte[] idBytes = new byte[Constants.UUID_BYTE_COUNT];
		for (int i = 0; i < idBytes.length; i++) {
			idBytes[i] = dos.readByte();
		}

		return Utils.getUUIDFromBytes(idBytes);
	}

	public static void validateShip(final Ship ship, final CoordinateState[][] gameField) throws IllegalShipPositionException{
		//Check that ships stay in fieldBounds and cause no ArrayOutOfBounds
		int startX = ship.getXCoordinate();
		int startY = ship.getYCoordinate();
		int endX = ship.getEndXCoordinate();
		int endY = ship.getEndYCoordinate();
		if(startX < 0 || startX >= Constants.BOARD_SIZE
			|| endX < 0 || endX >= Constants.BOARD_SIZE
			|| startY < 0 || startY >= Constants.BOARD_SIZE
			|| endY < 0 || endY >= Constants.BOARD_SIZE) {
			throw new IllegalShipPositionException("Ship out of gamefield bounds!");
		}
		//Set ship to field and make sure that a distance of one cell is kept between the
		// ships (horizontal, vertical, diagonal)
		for(int i = startX; i <= endX; i++) {
			for(int j = startY; j <= endY; j++) {
				boolean isPositionLegal = true;
				//Horizontal (with outOfBounds guard)
				isPositionLegal &= (i - 1) < 0 || gameField[i - 1][j] == CoordinateState.EMPTY;
				isPositionLegal &= (i + 1 ) >= Constants.BOARD_SIZE || gameField[i + 1][j] == CoordinateState.EMPTY;
				isPositionLegal &= gameField[i][j] == CoordinateState.EMPTY;
				//Vertical
				isPositionLegal &= (j - 1) < 0 || gameField[i][j - 1] == CoordinateState.EMPTY;
				isPositionLegal &= (j + 1 ) >= Constants.BOARD_SIZE || gameField[i][j + 1] == CoordinateState.EMPTY;
				//Diagonal
				isPositionLegal &= (i - 1) < 0 || (j - 1) < 0 || gameField[i - 1][j - 1] == CoordinateState.EMPTY;
				isPositionLegal &= (i + 1) >= Constants.BOARD_SIZE || (j + 1) >= Constants.BOARD_SIZE || gameField[i + 1][j + 1] == CoordinateState.EMPTY;
				isPositionLegal &= (i - 1) < 0 || (j + 1) >= Constants.BOARD_SIZE || gameField[i - 1][j + 1] == CoordinateState.EMPTY;
				isPositionLegal &= (i + 1) >= Constants.BOARD_SIZE || (j - 1) < 0 || gameField[i + 1][j - 1] == CoordinateState.EMPTY;
				if(!isPositionLegal) {
					throw new IllegalShipPositionException("Position Illegal! Some ships are too close together or overlap!");
				}
			}
		}
	}
}
