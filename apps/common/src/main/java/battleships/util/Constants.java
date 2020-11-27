package battleships.util;

public class Constants {

	public static final int DEFAULT_PORT = 5555;

	public static final int UUID_BYTE_COUNT = 16;

	public static class Identifiers {

		public static final byte LOBBY_LIST_REQUEST = 0x1;
		public static final byte LOBBY_LIST_RESPONSE = 0x2;

		public static final byte LOGIN_REQUEST = 0x3;
		public static final byte LOGIN_RESPONSE = 0x4;

		public static final byte REGISTER_REQUEST = 0x5;
		public static final byte REGISTER_ERROR_RESPONSE = 0x6;

		public static final byte CREATE_GAME_REQUEST = 0x7;

	}

}
