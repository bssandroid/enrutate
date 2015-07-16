package com.enrutatemio.planificador;

//Mensajes de estado entre el servidor y la app

public class MessageCodes {
	public static final int
		CONNECTED = 0,
		COMPUTED_ROUTE = 1,
		DISCONNECTED = 2,
		PROGRESS_ROUTE = 3,
		FIND_STATIONS  = 4,
		
		
		ERROR = -1,
		PARSE_ERROR = -2,
		SOCKET_ERROR = -3,
		UNKNOWN_ERROR = -4;
}
