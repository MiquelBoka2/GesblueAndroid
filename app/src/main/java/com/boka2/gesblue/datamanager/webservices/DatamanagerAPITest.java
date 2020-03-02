package com.boka2.gesblue.datamanager.webservices;

/**
 * Created by Boka2.
 */

public class DatamanagerAPITest {
//
//
//	public void testDadesBasiques() {
//		crida_NouTerminal();
//		crida_login();
//		crida_TipusVehicles();
//		crida_marques();
//		crida_models();
//		crida_colors();
//		crida_carrers();
//		crida_Infraccions();
//		crida_RecuperaData();
//	}
//
//	public void testAll() {
//		testDadesBasiques();
////		testOperativa();
//	}
//
//	public void testOperativa() {
//		crida_ComprovaMatricula();
//		crida_NovaDenuncia();
//		crida_Posicio();
//		crida_RecuperaDenuncia();
//		crida_EstablirComptadorDenuncia();
//		crida_RecuperaComptadorDenuncia();
//	}
//
//	public void crida_NouTerminal() {
//		System.out.println("Comença crida: " + "crida_NouTerminal");
//		DatamanagerAPI.crida_NouTerminal(new NouTerminalRequest("admin@sixtemia.com", "test12345", 99, "agent"), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_NouTerminal" + " SUCCESS");
//				NouTerminalResponse response = DatamanagerAPI.parseJson(result, NouTerminalResponse.class);
//
//				if(response != null) DLog("NouTerminalResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_NouTerminal" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//
//	public void crida_login() {
//		System.out.println("Comença crida: " + "crida_login");
//		DatamanagerAPI.crida_Login(new LoginRequest("admin@sixtemia.com", "test12345", 99, "agent", "6.0.1", "1.0", 19900101), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_login" + " SUCCESS");
//				LoginResponse response = DatamanagerAPI.parseJson(result, LoginResponse.class);
//
//				if(response != null) DLog("LoginResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_login" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//
//	public void crida_TipusVehicles() {
//		System.out.println("Comença crida: " + "crida_TipusVehicles");
//		DatamanagerAPI.crida_TipusVehicles(new TipusVehiclesRequest("19900101"), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_TipusVehicles" + " SUCCESS");
//				TipusVehiclesResponse response = DatamanagerAPI.parseJson(result, TipusVehiclesResponse.class);
//
//				if(response != null) DLog("TipusVehiclesResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_TipusVehicles" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//
//	public void crida_marques() {
//		System.out.println("Comença crida: " + "crida_marques");
//		DatamanagerAPI.crida_Marques(new MarquesRequest("19900101"), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_Marques" + " SUCCESS");
//				MarquesResponse response = DatamanagerAPI.parseJson(result, MarquesResponse.class);
//
//				if(response != null) DLog("MarquesResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_marques" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//
//	public void crida_models() {
//		System.out.println("Comença crida: " + "crida_models");
//		DatamanagerAPI.crida_Models(new ModelsRequest("19900101"), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_models" + " SUCCESS");
//				ModelsResponse response = DatamanagerAPI.parseJson(result, ModelsResponse.class);
//
//				if(response != null) DLog("ModelsResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_models" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//
//	public void crida_colors() {
//		System.out.println("Comença crida: " + "crida_colors");
//		DatamanagerAPI.crida_Colors(new ColorsRequest(19900101), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_colors" + " SUCCESS");
//				ColorsResponse response = DatamanagerAPI.parseJson(result, ColorsResponse.class);
//
//				if(response != null) DLog("ColorsResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_colors" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//
//	public void crida_carrers() {
//		System.out.println("Comença crida: " + "crida_carrers");
//		DatamanagerAPI.crida_Carrers(new CarrersRequest(99, Long.parseLong("19900101")), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_carrers" + " SUCCESS");
//				CarrersResponse response = DatamanagerAPI.parseJson(result, CarrersResponse.class);
//
//				if (response != null) DLog("CarrersResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_carrers" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//	public void crida_Infraccions() {
//		System.out.println("Comença crida: " + "crida_Infraccions");
//		DatamanagerAPI.crida_Infraccions(new InfraccionsRequest(99, "19900101"), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_Infraccions" + " SUCCESS");
//				InfraccionsResponse response = DatamanagerAPI.parseJson(result, InfraccionsResponse.class);
//
//				if(response != null) DLog("InfraccionsResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_Infraccions" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//
//	private void crida_RecuperaData() {
//		System.out.println("Comença crida: " + "crida_RecuperaData");
//		DatamanagerAPI.crida_RecuperaData(new RecuperaDataRequest(), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_RecuperaData" + " SUCCESS");
//				RecuperaDataResponse response = DatamanagerAPI.parseJson(result, RecuperaDataResponse.class);
//
//				if (response != null) DLog("RecuperaDataResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_RecuperaData" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR:
//						Log.v("JSoapExample", "Network error");
//						break;
//					case JsoapError.PARSE_ERROR:
//						Log.v("JSoapExample", "Parsing error");
//						break;
//					default:
//						Log.v("JSoapExample", "Unknown error: " + error);
//						break;
//				}
//			}
//		});
//	}
//
//	public void crida_ComprovaMatricula() {
//		System.out.println("Comença crida: " + "crida_ComprovaMatricula");
//		DatamanagerAPI.crida_ComprovaMatricula(new ComprovaMatriculaRequest(99,"99","B0000AA", 19900101), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_ComprovaMatricula" + " SUCCESS");
//				ComprovaMatriculaResponse response = DatamanagerAPI.parseJson(result, ComprovaMatriculaResponse.class);
//
//				if(response != null) DLog("ComprovaMatriculaResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_ComprovaMatricula" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//	public void crida_NovaDenuncia() {
//		System.out.println("Comença crida: " + "crida_NovaDenuncia");
//		DatamanagerAPI.crida_NovaDenuncia(new NovaDenunciaRequest("1", 20160908, 1, 1, "52", "pos", "B0000AA", 23, 24, 24, 24, 24, 24, "24", 24, 24, "6.0.1", "1.0"), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_NovaDenuncia" + " SUCCESS");
//				NovaDenunciaResponse response = DatamanagerAPI.parseJson(result, NovaDenunciaResponse.class);
//
//				if(response != null) DLog("NovaDenunciaResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_ComprovaMatricula" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//	public void crida_Posicio() {
//		System.out.println("Comença crida: " + "crida_Posicio");
//		DatamanagerAPI.crida_Posicio(new PosicioRequest(99, 1, 1, "pos", 19900101), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_Posicio" + " SUCCESS");
//				PosicioResponse response = DatamanagerAPI.parseJson(result, PosicioResponse.class);
//
//				if(response != null) DLog("PosicioResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_Posicio" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//	public void crida_RecuperaDenuncia() {
//		System.out.println("Comença crida: " + "crida_RecuperaDenuncia");
//		DatamanagerAPI.crida_RecuperaDenuncia(new RecuperaDenunciaRequest("B0000AA", 99, 99, 19900101), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_RecuperaDenuncia" + " SUCCESS");
//				RecuperaDataResponse response = DatamanagerAPI.parseJson(result, RecuperaDataResponse.class);
//
//				if(response != null) DLog("RecuperaDataResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_RecuperaDenuncia" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//	public void crida_EstablirComptadorDenuncia() {
//		System.out.println("Comença crida: " + "crida_EstablirComptadorDenuncia");
//		DatamanagerAPI.crida_EstablirComptadorDenuncia(new EstablirComptadorDenunciaRequest(99, "99", "99", 10), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_EstablirComptadorDenuncia" + " SUCCESS");
//				EstablirComptadorDenunciaResponse response = DatamanagerAPI.parseJson(result, EstablirComptadorDenunciaResponse.class);
//
//				if(response != null) DLog("EstablirComptadorDenunciaResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_EstablirComptadorDenuncia" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//	public void crida_RecuperaComptadorDenuncia() {
//		System.out.println("Comença crida: " + "crida_RecuperaComptadorDenuncia");
//		DatamanagerAPI.crida_RecuperaComptadorDenuncia(new RecuperaComptadorDenunciaRequest("99", "1", "99"), new JSoapCallback() {
//			@Override
//			public void onSuccess(String result) {
//				DLog("crida_RecuperaComptadorDenuncia" + " SUCCESS");
//				RecuperaComptadorDenunciaResponse response = DatamanagerAPI.parseJson(result, RecuperaComptadorDenunciaResponse.class);
//
//				if(response != null) DLog("RecuperaComptadorDenunciaResponse: " + response);
//			}
//
//			@Override
//			public void onError(int error) {
//				DLog("crida_RecuperaComptadorDenuncia" + " FAIL");
//				switch (error) {
//					case JsoapError.NETWORK_ERROR: Log.v("JSoapExample", "Network error"); break;
//					case JsoapError.PARSE_ERROR: Log.v("JSoapExample", "Parsing error"); break;
//					default: Log.v("JSoapExample", "Unknown error: " + error); break;
//				}
//			}
//		});
//	}
//
//	public void DLog(String what) {
//		Log.d("GesblueDroid", what);
//	}
//

}
