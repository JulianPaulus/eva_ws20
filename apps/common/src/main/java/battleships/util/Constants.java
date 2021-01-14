package battleships.util;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Constants {


	public static final int UUID_BYTE_COUNT = 16;
	public static final int SHIP_COUNT = 5;
	public static final int BOARD_SIZE = 10;
	public static final long HEARTBEAT_SEND_INTERVAL_IN_S = TimeUnit.SECONDS.toSeconds(5);
	public static final long HEARTBEAT_TIMEOUT_IN_S = TimeUnit.SECONDS.toSeconds(10);

	public static class Server {
		public static final int DEFAULT_PORT = 5555;
		public static final int MAX_PLAYER_COUNT = 100;
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

		public static final byte SERVER_ERROR = 0xA;

		public static final byte SEND_CHAT_MESSAGE = 0xB;
		public static final byte BROADCAST_CHAT_MESSAGE = 0xC;

		public static final byte GAME_PLAYER_DO_SETUP_MESSAGE = 0xD;
		public static final byte GAME_WAIT_FOR_OTHER_PLAYER_SETUP_MESSAGE = 0xE;
		public static final byte GAME_PLAYER_ILLEGAL_SHIP_POSITION = 0xF;

		public static final byte PLAYER_READY_REQUEST = 0x10;
		public static final byte GAME_PLAYERS_TURN = 0x11;
		public static final byte GAME_ENEMIES_TURN = 0x12;
		public static final byte GAME_SHOOT_REQUEST = 0x13;
		public static final byte GAME_SHOOT_RESPONSE = 0x14;

		public static final byte PLAYER_DISCONNECTED = 0x15;
		public static final byte HEARTBEAT = 0x16;

	}

}
