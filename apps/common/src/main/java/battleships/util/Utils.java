package battleships.util;

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
}
