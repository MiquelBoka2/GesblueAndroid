package com.boka2.gesblue.ticketstyle;

import android.graphics.Bitmap;

import java.util.Calendar;
import java.util.Date;

/*
 *
 *
 * Created by Boka2.
 */
public class TicketConfiguration {

    //Separat per linies del tiquet
    private Bitmap logo;

    private Bitmap imatgePeu;

    private String textCap;

    private String butlleta;
    private Calendar data;
    private String matricula;

    private Bitmap logoCotxe;
    private String vehicle;
    private String marcaModel;
    private String color;

    private String llocInfraccio;

    private String precepteInfringit;

    private String fetDenunciat;

    private float _import;
    private float dte;
    private int punts;
    private String agent;

    private String codiBarresServiCaixa;

    private String emissora;
    private String mod;
    private String referencia;
    private String identificacio;
    private float impDte;

    private Calendar dataLimitPagament;

    private String codiAnullacio;

    private DataAnulacio[] dataAnulacioArray;

    private String codiBarres;

    private String textAnulacio;
    private String textpeu;

    private String qr;

    private Date dataCreacio;

    public TicketConfiguration() {
    }

    public String getTextCap() {
        return textCap;
    }

    public TicketConfiguration setTextCap(String _textCap) {
        this.textCap = _textCap;

        return this;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public Bitmap getImatgePeu() {
        return imatgePeu;
    }

    public String getButlleta() {
        return butlleta;
    }

    public Calendar getData() {
        return data;
    }

    public String getMatricula() {
        return matricula;
    }

    public Bitmap getLogoCotxe() {
        return logoCotxe;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getMarcaModel() {
        return marcaModel;
    }

    public String getColor() {
        return color;
    }

    public String getLlocInfraccio() {
        return llocInfraccio;
    }

    public String getPrecepteInfringit() {
        return precepteInfringit;
    }

    public String getFetDenunciat() {
        return fetDenunciat;
    }

    public float getImportCamp() {
        return _import;
    }

    public float getDte() {
        return dte;
    }

    public int getPunts() {
        return punts;
    }

    public String getAgent() {
        return agent;
    }

    public String getEmissora() {
        return emissora;
    }

    public String getMod() {
        return mod;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getIdentificacio() {
        return identificacio;
    }

    public float getImpDte() {
        return impDte;
    }

    public Calendar getDataLimitPagament() {
        return dataLimitPagament;
    }

    public String getCodiAnullacio() {
        return codiAnullacio;
    }

    public String getCodiBarresServiCaixa() {
        return codiBarresServiCaixa;
    }

    public DataAnulacio[] getDataAnulacioArray() {
        return dataAnulacioArray;
    }

    public String getCodiBarres() {
        return codiBarres;
    }

    public TicketConfiguration setCodiBarres(String codiBarres) {
        this.codiBarres = codiBarres;

        return this;
    }

    public String getTextAnulacio() {
        return textAnulacio;
    }

    public String getTextpeu() {
        return textpeu;
    }

    public TicketConfiguration setLogo(Bitmap logo) {
        this.logo = logo;

        return this;
    }

    public TicketConfiguration setImatgePeu(Bitmap imatgePeu) {
        this.imatgePeu = imatgePeu;

        return this;
    }

    public TicketConfiguration setButlleta(String butlleta) {
        this.butlleta = butlleta;

        return this;
    }

    public TicketConfiguration setData(Calendar data) {
        this.data = data;

        return this;
    }

    public TicketConfiguration setMatricula(String matricula) {
        this.matricula = matricula;

        return this;
    }

    public TicketConfiguration setLogoCotxe(Bitmap logoCotxe) {
        this.logoCotxe = logoCotxe;

        return this;
    }

    public TicketConfiguration setVehicle(String vehicle) {
        this.vehicle = vehicle;

        return this;
    }

    public TicketConfiguration setMarcaModel(String imatge, String marcaModel) {
        this.marcaModel = marcaModel;

        return this;
    }

    public TicketConfiguration setColor(String color) {
        this.color = color;

        return this;
    }

    public TicketConfiguration setLlocInfraccio(String llocInfraccio) {
        this.llocInfraccio = llocInfraccio;

        return this;
    }

    public TicketConfiguration setPrecepteInfringit(String precepteInfringit) {
        this.precepteInfringit = precepteInfringit;

        return this;
    }

    public TicketConfiguration setFetDenunciat(String fetDenunciat) {
        this.fetDenunciat = fetDenunciat;

        return this;
    }

    public TicketConfiguration setImport(float _import) {
        this._import = _import;

        return this;
    }

    public TicketConfiguration setDte(float dte) {
        this.dte = dte;

        return this;
    }

    public TicketConfiguration setPunts(int punts) {
        this.punts = punts;

        return this;
    }

    public TicketConfiguration setAgent(String agent) {
        this.agent = agent;

        return this;
    }

    public TicketConfiguration setEmissora(String emissora) {
        this.emissora = emissora;

        return this;
    }

    public TicketConfiguration setMod(String mod) {
        this.mod = mod;

        return this;
    }

    public TicketConfiguration setReferencia(String referencia) {
        this.referencia = referencia;

        return this;
    }

    public TicketConfiguration setIdentificacio(String identificacio) {
        this.identificacio = identificacio;

        return this;
    }

    public TicketConfiguration setImpDte(float impDte) {
        this.impDte = impDte;

        return this;
    }

    public TicketConfiguration setDataLimitPagament(Calendar dataLimitPagament) {
        this.dataLimitPagament = dataLimitPagament;

        return this;
    }

    public TicketConfiguration setCodiAnulacio(String codiAnullacio) {
        this.codiAnullacio = codiAnullacio;

        return this;
    }

    public TicketConfiguration setDataAnulacioArray(DataAnulacio[] dataAnnulacioArray) {
        this.dataAnulacioArray = dataAnnulacioArray;

        return this;
    }

    public TicketConfiguration setTextAnulacio(String _textAnulacio) {
        this.textAnulacio = _textAnulacio;

        return this;
    }

    public TicketConfiguration setCodiBarresServiCaixa(String _codiBarresServiCaixa) {
        this.codiBarresServiCaixa = _codiBarresServiCaixa;

        return this;
    }

    public TicketConfiguration setTextPeu(String _textpeu) {
        this.textpeu = _textpeu;

        return this;
    }

    public String getQr() {
        return qr;
    }

    public TicketConfiguration setQr(String _qr) {
        this.qr = _qr;

        return this;
    }

    public Date getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(Date dataCreacio) {
        this.dataCreacio = dataCreacio;
    }
}
