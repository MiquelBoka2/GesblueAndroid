package com.sixtemia.gesbluedroid.datamanager.webservices.results.basic;

/**
 * Created by rubengonzalez on 19/8/16.
 */

public class WSResult {


	public static final int RESULTAT_TERMINAL_VALID = 0;
	public static final int RESULTAT_ERROR_DE_LOGIN = -1;
	public static final int RESULTAT_TERMINAL_PENDENT = -2;
	public static final int RESULTAT_TERMINAL_DENEGAT = -3;
	public static final int RESULTAT_CONCESSIO_NO_VALIDA = -4;
	public static final int RESULTAT_ERROR_DESCONEGUT = -99; //Per si no ens tornen res

	public static final String TRUE = "SI";
	public static final String FALSE = "NO";

	public static final String IMPRESORA_AMPLE = "Ample";
	public static final String IMPRESORA_ESTRETA = "Estreta";

	public static boolean isTrue(String param) {
		return TRUE.equals(param);
	}

	public static boolean isFalse(String param) {
		return FALSE.equals(param);
	}


	public static boolean isFalse(int v) {
		return v == 0;
	}
}