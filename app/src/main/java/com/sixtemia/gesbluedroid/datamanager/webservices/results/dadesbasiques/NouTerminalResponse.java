package com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class NouTerminalResponse {

	public static final String TRUE = "SI";
	public static final String FALSE = "NO";

	public static final String IMPRESORA_AMPLE = "Ample";
	public static final String IMPRESORA_ESTRETA = "Estreta";

	@JSoapResField(name = "resultat")
	@SerializedName("resultat")
	private long resultat;

	@JSoapResField(name = "terminal")
	@SerializedName("terminal")
	private long terminal;

	@JSoapResField(name = "logo")
	@SerializedName("logo")
	private String logo;

	@JSoapResField(name = "imatgepeu")
	@SerializedName("imatgepeu")
	private String imatgepeu;

	@JSoapResField(name = "textcap")
	@SerializedName("textcap")
	private String textcap;

	@JSoapResField(name = "codibarresvisible")
	@SerializedName("codibarresvisible")
	private String codibarresvisible;

	@JSoapResField(name = "textpeuvisible")
	@SerializedName("textpeuvisible")
	private String textpeuvisible;

	@JSoapResField(name = "textpeu")
	@SerializedName("textpeu")
	private String textpeu;

	@JSoapResField(name = "pass")
	@SerializedName("pass")
	private String pass;

	@JSoapResField(name = "importanulacio")
	@SerializedName("importanulacio")
	private String importanulacio;

	@JSoapResField(name = "logosmarques")
	@SerializedName("logosmarques")
	private String logosmarques;

	@JSoapResField(name = "logoqr")
	@SerializedName("logoqr")
	private String logoqr;

	@JSoapResField(name = "adrecaqr")
	@SerializedName("adrecaqr")
	private String adrecaqr;

	@JSoapResField(name = "iconesgenerics")
	@SerializedName("iconesgenerics")
	private String iconesgenerics;

	@JSoapResField(name = "impresora")
	@SerializedName("impresora")
	private String impresora;

	@JSoapResField(name = "codibarresservicaixa")
	@SerializedName("codibarresservicaixa")
	private String codibarresservicaixa;

	@JSoapResField(name = "valorservicaixa")
	@SerializedName("valorservicaixa")
	private String valorservicaixa;

	@JSoapResField(name = "textanulacio")
	@SerializedName("textanulacio")
	private String textanulacio;

	@JSoapResField(name = "continguttextanulacio")
	@SerializedName("continguttextanulacio")
	private String continguttextanulacio;

	@JSoapResField(name = "costlimitanulacio")
	@SerializedName("costlimitanulacio")
	private String costlimitanulacio;

	@JSoapResField(name = "dataimporttiquet")
	@SerializedName("dataimporttiquet")
	private String dataimporttiquet;

	@JSoapResField(name = "precepteinfringit")
	@SerializedName("precepteinfringit")
	private String precepteinfringit;

	@JSoapResField(name = "longitudinfraccio")
	@SerializedName("longitudinfraccio")
	private long longitudinfraccio;

	@JSoapResField(name = "comptadordenuncia")
	@SerializedName("comptadordenuncia")
	private String comptadordenuncia;

	@JSoapResField(name = "tiquetusuari")
	@SerializedName("tiquetusuari")
	private String tiquetusuari;
	@JSoapResField(name = "codiexportadora")
	@SerializedName("codiexportadora")
	private int codiexportadora;
	@JSoapResField(name = "coditipusbutlleta")
	@SerializedName("coditipusbutlleta")
	private String coditipusbutlleta;
	@JSoapResField(name = "codiinstitucio")
	@SerializedName("codiinstitucio")
	private String codiinstitucio;

	public NouTerminalResponse() {

	}

	public boolean hasCodiBarresVisible() {
		return isTrue(getCodibarresvisible());
	}

	public boolean hasTextPeuVisible() {
		return isTrue(getTextpeuvisible());
	}

	public String hasTextPeu() {
		return getTextpeu();
	}
	public boolean hasImportAnulacio() {
		return isTrue(getImportanulacio());
	}

	public boolean hasLogosMarques() {
		return isTrue(getLogosmarques());
	}

	public boolean hasIconesGenerics() {
		return isTrue(getIconesgenerics());
	}

	public boolean isImpresoraEstreta() {
		return IMPRESORA_ESTRETA.equals(getImpresora());
	}

	public boolean isImpresoraAmple() {
		return IMPRESORA_AMPLE.equals(getImpresora());
	}

	public boolean hasCodiBarresServicaixa() {
		return isTrue(getCodibarresservicaixa());
	}

	public boolean hasTextAnulacio() {
		return isTrue(getTextanulacio());
	}

	public boolean hasCostLimitAnulacio() {
		return isTrue(getCostlimitanulacio());
	}

	public boolean hasDataImportTiquet() {
		return isTrue(getDataimporttiquet());
	}

	public boolean hasTiquetUsuari() {
		return isTrue(getTiquetusuari());
	}

	public int hasCodiExportadora() {
		return getCodiExportadora();
	}

	public String hasCodiTipusButlleta() {
		return getCodiTipusButlleta();
	}
	public String hasCodiInstitucio() {
		return getCodiInstitucio();
	}

	public boolean hasLogoQr() {
		return isTrue(getLogoqr());
	}

	public long getTerminal() {
		return terminal;
	}

	public void setTerminal(long terminal) {
		this.terminal = terminal;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getImatgepeu() {
		return imatgepeu;
	}

	public void setImatgepeu(String imatgepeu) {
		this.imatgepeu = imatgepeu;
	}

	public String getTextcap() {
		return textcap;
	}

	public void setTextcap(String textcap) {
		this.textcap = textcap;
	}

	public String getCodibarresvisible() {
		return codibarresvisible;
	}

	public void setCodibarresvisible(String codibarresvisible) {
		this.codibarresvisible = codibarresvisible;
	}

	public String getTextpeuvisible() {
		return textpeuvisible;
	}

	public void setTextpeuvisible(String textpeuvisible) {
		this.textpeuvisible = textpeuvisible;
	}

	public String getTextpeu() {
		return textpeu;
	}

	public void setTextpeu(String textpeu) {
		this.textpeu = textpeu;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getImportanulacio() {
		return importanulacio;
	}

	public void setImportanulacio(String importanulacio) {
		this.importanulacio = importanulacio;
	}

	public String getLogosmarques() {
		return logosmarques;
	}

	public void setLogosmarques(String logosmarques) {
		this.logosmarques = logosmarques;
	}

	public String getLogoqr() {
		return logoqr;
	}

	public void setLogoqr(String logoqr) {
		this.logoqr = logoqr;
	}

	public String getAdrecaqr() {
		return adrecaqr;
	}

	public void setAdrecaqr(String adrecaqr) {
		this.adrecaqr = adrecaqr;
	}

	public String getIconesgenerics() {
		return iconesgenerics;
	}

	public void setIconesgenerics(String iconesgenerics) {
		this.iconesgenerics = iconesgenerics;
	}

	public String getImpresora() {
		return impresora;
	}

	public void setImpresora(String impresora) {
		this.impresora = impresora;
	}

	public String getCodibarresservicaixa() {
		return codibarresservicaixa;
	}

	public void setCodibarresservicaixa(String codibarresservicaixa) {
		this.codibarresservicaixa = codibarresservicaixa;
	}

	public String getValorservicaixa() {
		return valorservicaixa;
	}

	public void setValorservicaixa(String valorservicaixa) {
		this.valorservicaixa = valorservicaixa;
	}

	public String getTextanulacio() {
		return textanulacio;
	}

	public void setTextanulacio(String textanulacio) {
		this.textanulacio = textanulacio;
	}

	public String getContinguttextanulacio() {
		return continguttextanulacio;
	}

	public void setContinguttextanulacio(String continguttextanulacio) {
		this.continguttextanulacio = continguttextanulacio;
	}

	public String getCostlimitanulacio() {
		return costlimitanulacio;
	}

	public void setCostlimitanulacio(String costlimitanulacio) {
		this.costlimitanulacio = costlimitanulacio;
	}

	public String getDataimporttiquet() {
		return dataimporttiquet;
	}

	public void setDataimporttiquet(String dataimporttiquet) {
		this.dataimporttiquet = dataimporttiquet;
	}

	public String getPrecepteinfringit() {
		return precepteinfringit;
	}

	public void setPrecepteinfringit(String precepteinfringit) {
		this.precepteinfringit = precepteinfringit;
	}

	public long getLongitudinfraccio() {
		return longitudinfraccio;
	}

	public void setLongitudinfraccio(long longitudinfraccio) {
		this.longitudinfraccio = longitudinfraccio;
	}

	public String getComptadordenuncia() {
		return comptadordenuncia;
	}

	public void setComptadordenuncia(String comptadordenuncia) {
		this.comptadordenuncia = comptadordenuncia;
	}

	public long getResultat() {
		return resultat;
	}

	public String getTiquetusuari() {
		return tiquetusuari;
	}

	public int getCodiExportadora() {
		return codiexportadora;
	}

	public String getCodiTipusButlleta() {
		return coditipusbutlleta;
	}
	public String getCodiInstitucio() {
		return codiinstitucio;
	}

	public void setTiquetusuari(String tiquetusuari) {
		this.tiquetusuari = tiquetusuari;
	}

	public static boolean isTrue(String param) {
		return TRUE.equals(param);
	}

	@Override
	public String toString() {
		return "NouTerminalResponse{" +
				"resultat=" + resultat +
				", terminal=" + terminal +
				", logo='" + logo + '\'' +
				", textcap='" + textcap + '\'' +
				", codibarresvisible='" + codibarresvisible + '\'' +
				", textpeuvisible='" + textpeuvisible + '\'' +
				", textpeu='" + textpeu + '\'' +
				", pass='" + pass + '\'' +
				", importanulacio='" + importanulacio + '\'' +
				", logosmarques='" + logosmarques + '\'' +
				", logoqr='" + logoqr + '\'' +
				", adrecaqr='" + adrecaqr + '\'' +
				", iconesgenerics='" + iconesgenerics + '\'' +
				", impresora='" + impresora + '\'' +
				", codibarresservicaixa='" + codibarresservicaixa + '\'' +
				", valorservicaixa='" + valorservicaixa + '\'' +
				", textanulacio='" + textanulacio + '\'' +
				", continguttextanulacio='" + continguttextanulacio + '\'' +
				", costlimitanulacio='" + costlimitanulacio + '\'' +
				", dataimporttiquet='" + dataimporttiquet + '\'' +
				", precepteinfringit='" + precepteinfringit + '\'' +
				", longitudinfraccio=" + longitudinfraccio +
				", comptadordenuncia='" + comptadordenuncia + '\'' +
				", tiquetusuari='" + tiquetusuari + '\'' +
				", codiexportadora='" + codiexportadora + '\'' +
				", coditipusbutlleta='" + coditipusbutlleta + '\'' +
				", codiinstitucio='" + codiinstitucio + '\'' +
				", imatgepeu='" + imatgepeu + '\'' +
				'}';
	}
}