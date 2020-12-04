package battleships.util;

import java.util.concurrent.TimeUnit;

public class Constants {


	public static final int UUID_BYTE_COUNT = 16;

	public static class Server {
		public static final int DEFAULT_PORT = 5555;
		public static final int MAX_PLAYER_COUNT = 100;
		public static final long CONNECTION_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(10);
	}

	public static class Identifiers {

		public static final byte LOBBY_LIST_REQUEST = 0x1;
		public static final byte LOBBY_LIST_RESPONSE = 0x2;

		public static final byte LOGIN_REQUEST = 0x3;
		public static final byte LOGIN_RESPONSE = 0x4;

		public static final byte REGISTER_REQUEST = 0x5;
		public static final byte REGISTER_ERROR_RESPONSE = 0x6;

		public static final byte CREATE_GAME_REQUEST = 0x7;

		public static final byte GAME_JOIN_REQUEST = 0x8;
		public static final byte GAME_JOIN_RESPONSE = 0x9;

		public static final byte SERVER_ERROR = 0xa;

	}

}
