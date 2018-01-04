package com.sixtemia.gesbluedroid.ticketstyle;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.datecs.api.printer.Printer;
import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.sixtemia.spushnotifications.db.DataContext.mContext;

/**
 * Imprimir tiquet
 *
 * Característiques DPP-350C / DPP-350 (Model més bàsic)
 */

//TODO externalitzar strings
public class TicketPrinter {

    private static final String CHARSET_ENCODING = "Windows-1252";

    private static final int SAMPEL_TICKET_WIDTH_MM = 70;

    private static final int PAGE_WIDTH       = 572;
    private static final int FINAL_FEED_PAPER = 110;
    private static final int CELLA_INSET      = 4;
    private static final int MAX_CHAR_LINE    = 47;
    private static final int CHAR_SIZE        = 12;
    private static final int LINE_HEIGHT      = 32; //Altura del text per defecte: 26px
    private static final  int LINE_HEIGHT_BIG = LINE_HEIGHT * 2 - 12; //Altura reajustada per a la font {h}{w}

    private static final  int  BARCODE_NUM_LINES = 3;

    //private static final int LOGOTIP_WIDTH = 258;
    //private static final int LOGOTIP_HEIGHT = 169;

    private Printer printer;


    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public TicketPrinter(Printer printer, Context _context) {
        mContext = _context;
        this.printer = printer;
    }

    /**
     * Print all ticket.
     *
     * @throws IOException
     */
    public boolean print(Context _context, TicketConfiguration printConfiguration) throws IOException {
        if(printer != null) {
            printer.reset();
            printer.flush();

            printer.selectPageMode();
            Integer y = 0;

            // inicialitzem a 40 perquè és el número de línies que hi ha fixes(no dinàmiques)
            // al tiquet si s'imprimeixen tots els blocs, s'afegeix un bloc, que no sigui
            // dinàmic s'haurà de modificar aquest valor.
            Integer pageHeight = calcPageHeight(_context, 40, printConfiguration);
            printer.setPageRegion(0, y, PAGE_WIDTH, pageHeight, Printer.PAGE_LEFT);

            //------------------
            // Logotip concessió
            //------------------
            Bitmap bitmap = printConfiguration.getLogo();
            int widthBitmap  = bitmap.getWidth();
            int heightBitmap = bitmap.getHeight();
            int[] argb = new int[widthBitmap * heightBitmap];
            bitmap.getPixels(argb, 0, widthBitmap, 0, 0, widthBitmap, heightBitmap);
            bitmap.recycle();
            printer.printImage(argb, widthBitmap, heightBitmap, Printer.ALIGN_LEFT, true);
            printer.flush();
            printer.printTaggedText("{reset}" + "{br}", CHARSET_ENCODING);
            y = newLine(y, 1);


            //------------------
            // Nom concessió
            //------------------
            printer.printTaggedText("{reset}{center}{b}" + mContext.getString(R.string.butlletaDeDenuncia) + "{br}", CHARSET_ENCODING);
            y = newLine(y, 1);
            printer.printTaggedText("{reset}{center}" + printConfiguration.getTextCap() + "{br}", CHARSET_ENCODING);
            y = newLine(y, 2);

            //------------------
            // Dades Butlleta
            //------------------
            //
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
            Cela[] dadesButlletaArray  = new Cela[]{
                    new Cela(mContext.getString(R.string.butlleta), printConfiguration.getButlleta(), 185),
                    new Cela(mContext.getString(R.string.titol_data), sdf.format(date), 145),
                    new Cela(mContext.getString(R.string.titol_hora), sdf2.format(date), 89),
                    new Cela(mContext.getString(R.string.titol_matricula), printConfiguration.getMatricula())
            };
            printCellaBlancaArray(dadesButlletaArray, y);
            y = newLine(y, 2);

            //------------------
            // Dades Vehicle
            //------------------
            Cela[] dadesVehicleArray = new Cela[]{
                    new Cela(mContext.getString(R.string.cela_vehicle), printConfiguration.getVehicle(), 147),
                    new Cela(mContext.getString(R.string.cela_marcaIModel), printConfiguration.getMarcaModel(), 294),
                    new Cela(mContext.getString(R.string.cela_color), printConfiguration.getColor())
            };
            printCellaBlancaArray(dadesVehicleArray, y);
            y = newLine(y, 2);

            //------------------
            // Dades Ubicació
            //------------------
            String llocInfraccio = printConfiguration.getLlocInfraccio();
            Integer numLinesLloc = calcNumLines(llocInfraccio, MAX_CHAR_LINE);
            Cela[] dadesUbicacioArray = new Cela[]{
                    new Cela(mContext.getString(R.string.cela_llocInfraccio), llocInfraccio)
            };
            printCellaBlancaArray(dadesUbicacioArray, y);
            y = newLine(y, numLinesLloc+1);

            //------------------
            // Precepte infringit
            //------------------
           if(!TextUtils.isEmpty(printConfiguration.getPrecepteInfringit())) {
               String precepteInfringit = printConfiguration.getPrecepteInfringit();
               Integer numLines         = calcNumLines(precepteInfringit, MAX_CHAR_LINE);
               Cela[] precepteArray = new Cela[]{
                       new Cela(mContext.getString(R.string.cela_precepteInfringit), precepteInfringit)
                };
                printCellaBlancaArray(precepteArray, y);
                y = newLine(y, numLines + 1);
            }

            //------------------
            // Fet Denunciat
            //------------------
            String fetDenunciat = printConfiguration.getFetDenunciat();
            Integer numLines = calcNumLines(fetDenunciat, MAX_CHAR_LINE);
            Cela[] fetDenunciatArray = new Cela[]{
                    new Cela(mContext.getString(R.string.cela_fetDenunciat), printConfiguration.getFetDenunciat())
            };
            printCellaBlancaArray(fetDenunciatArray, y);
            y = newLine(y, numLines + 1);

            //------------------
            // Informació denuncia
            //------------------
            float importCamp = printConfiguration.getImportCamp();
            float dte        = printConfiguration.getDte();
            Cela[] InfoDenunciaArray = new Cela[]{
                    new Cela(mContext.getString(R.string.cela_import_dte), toEuros(importCamp) + " / " + toEuros(dte), sampleMilimetersToPixels(35)),
                    new Cela(mContext.getString(R.string.cela_agent), printConfiguration.getAgent())
            };
            printCellaBlancaArray(InfoDenunciaArray, y);
            y = newLine(y, 2);
            y = newLine(y, 0.5f);


            //------------------
            // Dades entitat bancaria
            //------------------
            if(PreferencesGesblue.getCodiBarresServiCaixa(_context)) {
                int dia, mes, any;
                Calendar c = printConfiguration.getDataLimitPagament();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH)+1;
                any = c.get(Calendar.YEAR);
                String data = "";
                if(dia < 10) data += "0";
                data += dia + "/";
                if(mes < 10) data += "0";
                data += mes + "/";
                data += any;

                String mod           =  PreferencesGesblue.getMod(mContext);
                String strImporteDte = PreferencesGesblue.getImpDte(mContext);
                float impDte         = Float.parseFloat(strImporteDte);

                Integer textY = y + CELLA_INSET;
                printer.setPageXY(0, textY);
                printer.printTaggedText("{reset}{center}{b}" + mContext.getString(R.string.cela_pagamentEntitatBancaria) + "{br}", CHARSET_ENCODING);

                printer.drawPageFrame(0, y, PAGE_WIDTH, LINE_HEIGHT, Printer.FILL_BLACK, 0);
                printer.drawPageRectangle(0, y, PAGE_WIDTH, LINE_HEIGHT, Printer.FILL_INVERTED);

                y = newLine(y, 1);

                Cela[] celaArray = new Cela[]{
                        new Cela(mContext.getString(R.string.cela_emisora), PreferencesGesblue.getEmisora(mContext), sampleMilimetersToPixels(12)),
                        new Cela(mContext.getString(R.string.cela_mod), String.valueOf(mod), sampleMilimetersToPixels(5)),
                        new Cela(mContext.getString(R.string.cela_referencia), PreferencesGesblue.getReferencia(mContext), sampleMilimetersToPixels(18)),
                        new Cela(mContext.getString(R.string.cela_identificacio), PreferencesGesblue.getIdentificacio(mContext), sampleMilimetersToPixels(20)),
                        new Cela(mContext.getString(R.string.cela_imp_dte), getFormatedImport(impDte))
                };

                printCellaBlancaArray(celaArray, y);
                y = newLine(y, 2);

                textY = y + CELLA_INSET;
                printer.setPageXY(0, textY);
                printer.printTaggedText("{reset}" + mContext.getString(R.string.cela_dataLimitPagamentAmbAquestDoc) + " " + data + "{br}", CHARSET_ENCODING);
                y = newLine(y, 2);
            }



            //------------------
            // Import anul·lació
            //------------------
            String butlleta = printConfiguration.getButlleta();
            if(PreferencesGesblue.getImportAnulacio(mContext)) {
                String codiAnulacio = "";
                if(butlleta.length() > 9) {
                    codiAnulacio = butlleta.substring(butlleta.length()-10, butlleta.length());
                } else {
                    codiAnulacio = butlleta.substring(butlleta.length()-(butlleta.length()), butlleta.length());
                }

                 printCelaNegreFontGran(0, y, PAGE_WIDTH, mContext.getString(R.string.cela_codi_anulacio), codiAnulacio);
                 y = newLine(y, 3.5f);

                DataAnulacio[] dataAnulacioArray = printConfiguration.getDataAnulacioArray();
                if(PreferencesGesblue.getImportAnulacio(_context)) {
                    for(DataAnulacio dataAnulacio : dataAnulacioArray) {
                        if(dataAnulacio.getImport() != 0 && dataAnulacio.getData() != null) {

                            Calendar now = Calendar.getInstance();

                            Calendar tmp = (Calendar) now.clone();
                            tmp.add(Calendar.MINUTE, 1440);
                            SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yy HH:mm");
                            Cela[] celaArray = new Cela[]{
                                    new Cela(mContext.getString(R.string.cela_import), getFormatedImport(dataAnulacio.getImport()), sampleMilimetersToPixels(27)),
                                    new Cela(mContext.getString(R.string.cela_dataLimit), formatter.format(tmp.getTime()))
                            };
                            printCellaNegraArray(celaArray, y);
                            y = newLine(y, 2);
                        }
                    }
                }
            }
            y = newLine(y, 0.4f);

            //------------------
            // Codi barres servicaixa
            //------------------
            if(PreferencesGesblue.getCodiBarresServiCaixa(_context) && !TextUtils.isEmpty(printConfiguration.getCodiBarresServiCaixa())) {
                int heightCodi = LINE_HEIGHT*BARCODE_NUM_LINES;
                printer.drawPageFrame(0, y, PAGE_WIDTH, heightCodi + 2, Printer.FILL_WHITE, 1);
                printer.setBarcode(Printer.ALIGN_CENTER, false, 2, Printer.HRI_BELOW, heightCodi);
                printer.printBarcode(Printer.BARCODE_EAN128, printConfiguration.getCodiBarresServiCaixa());
                y = newLine(y, BARCODE_NUM_LINES + 0.5f);
            }

            y = newLine(y, 1);

            //------------------
            // Codi barres estàndard
            //------------------
            if(PreferencesGesblue.getCodiBarresVisible(_context) && !TextUtils.isEmpty(butlleta)) {
                int heightCodiBarres = LINE_HEIGHT*BARCODE_NUM_LINES;
                printer.drawPageFrame(0, y, PAGE_WIDTH, heightCodiBarres + 2, Printer.FILL_WHITE, 1);
                printer.setBarcode(Printer.ALIGN_CENTER, false, 2, Printer.HRI_BELOW, heightCodiBarres);
                printer.printBarcode(Printer.BARCODE_EAN128, butlleta);
                y = newLine(y, BARCODE_NUM_LINES + 0.5f);
            }

            y = newLine(y, 1);

            //------------------
            // Firma
            //------------------
            printer.setPageXY(CELLA_INSET, y+CELLA_INSET);
            printer.printTaggedText("{reset}{s}" + mContext.getString(R.string.firma) + "{br}", CHARSET_ENCODING);
            int heightFirma = LINE_HEIGHT*6;
            printer.drawPageFrame(0, y, PAGE_WIDTH, heightFirma, Printer.FILL_BLACK, 1);
            y = newLine(y, 6);
            y = newLine(y, 0.5f);

            //------------------
            // Text anul·lació
            //------------------
            if(PreferencesGesblue.getTextAnulacio(_context)) {
                String textAnulacio = printConfiguration.getTextAnulacio();
                numLines            = calcNumLines(textAnulacio, MAX_CHAR_LINE);
                printer.printTaggedText("{reset}{left}{b}{s}" + textAnulacio , CHARSET_ENCODING);
                y = newLine(y, numLines+1);
            }


            if(PreferencesGesblue.getTextPeuVisible(_context)) {
                String textPeu = printConfiguration.getTextpeu();
                numLines       = calcNumLines(textPeu, MAX_CHAR_LINE);

                printer.printTaggedText("{reset}{left}{s}" + textPeu + "{br}", CHARSET_ENCODING);
                y = newLine(y, numLines + 1);
            }


            if(PreferencesGesblue.getLogoQr(_context) && !TextUtils.isEmpty(PreferencesGesblue.getAdrecaQr(_context))) {
                int heightQr = LINE_HEIGHT*BARCODE_NUM_LINES;
                printer.drawPageFrame(0, y, PAGE_WIDTH, heightQr + 2, Printer.FILL_WHITE, 1);
                printer.setBarcode(Printer.ALIGN_CENTER, false, 4, Printer.HRI_NONE, heightQr);
                printer.printQRCode(5, 3, printConfiguration.getQr());
                y = newLine(y, BARCODE_NUM_LINES + 0.5f);
            }

            printer.printPage();
            printer.flush();
            printer.selectStandardMode();
            printer.feedPaper(LINE_HEIGHT*5);
            printer.flush();
            return true;
        } else {
            return false;
        }

    }

    private Integer newLine(Integer y, float numLines) throws IOException {
        y += Math.round(LINE_HEIGHT*numLines);
        printer.setPageXY(0, y);
        return y;
    }

    private Integer calcNumLines(String str, Integer positions) {

        Integer numLines = 0;
        if (!TextUtils.isEmpty(str) && positions > 0) {
            Integer numChars = str.length();
            numLines = (numChars / positions) + 1;
        }
        return numLines;
    }

    private Integer calcPageHeight(Context _context, Integer fixedLines, TicketConfiguration printConfiguration ) {
        Integer pageHeight = fixedLines*LINE_HEIGHT;
        Integer numLines   = 0;

        String llocInfraccio = printConfiguration.getLlocInfraccio();
        numLines             = calcNumLines(llocInfraccio, MAX_CHAR_LINE);
        pageHeight += numLines*LINE_HEIGHT;

        if(!TextUtils.isEmpty(PreferencesGesblue.getPrecepteInfringit(_context))) {
            numLines   = calcNumLines(printConfiguration.getPrecepteInfringit(), MAX_CHAR_LINE);
            pageHeight += numLines*LINE_HEIGHT;
        }

        String fetDenunciat = printConfiguration.getFetDenunciat();
        numLines            = calcNumLines(fetDenunciat, MAX_CHAR_LINE);
        pageHeight          += numLines*LINE_HEIGHT;

        if(PreferencesGesblue.getTextAnulacio(_context)) {
            String textAnulacio = printConfiguration.getTextAnulacio();
            numLines            = calcNumLines(textAnulacio, MAX_CHAR_LINE);
            pageHeight          += numLines * LINE_HEIGHT;
        }

        if(PreferencesGesblue.getTextPeuVisible(_context)) {
            String textPeu = printConfiguration.getTextpeu();
            numLines       = calcNumLines(textPeu, MAX_CHAR_LINE);
            pageHeight     += numLines*LINE_HEIGHT;
        }

        return pageHeight;
    }

    private String toEuros(float f) {
        return (String.format("%.02f", f) + "€");
    }

    /**
     * Obté la data en format DD/MM/YYYY
     *
     * @param calendar
     * @return data
     */
    private String getCalendarDataFormat(Calendar calendar) {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        if(calendar != null) {
            return calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) +  "/" + calendar.get(Calendar.YEAR);
        } else return s.toString();
    }

    /**
     * Obté la data en format DD/MM/YYYY
     *
     * @param calendar
     * @return data
     */
    private String getCalendarShortDataFormat(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) +  "/" + String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
    }

    /**
     * Obté l'hora en format HH:MM
     *
     * @param calendar
     * @return hora
     */
    private String getCalendarHourFormat(Calendar calendar) {

        SimpleDateFormat s = new SimpleDateFormat("HH:mm");

        if(calendar != null) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return ((hour < 10) ? ("0" + hour) : hour) + ":" + ((minute < 10) ? ("0" + minute) : minute);
        } else {
            return s.toString();
        }
    }

    /**
     * Imprimeix una filera de cel·les alineades horitzontalment. Estil de cel·les blances.
     * No es respectarà la propietat "y" de la cel·la.
     * -------------
     * |title|title|
     * -------------
     * |body |body |
     * ------------- ...
     * [0]   [1]
     *
     * @param celaArray
     * @throws IOException
     */
    private void printCellaBlancaArray(Cela[] celaArray, Integer y) throws IOException {

        int lastX = 0;

        for (Cela cela : celaArray) {
            Integer width = cela.getWidth();
            if(width == null) {
                width = new Integer(PAGE_WIDTH - lastX);
            }

            defineCellaBlanca(lastX, y, width, cela.getTitle(), cela.getBody());

            lastX += width;
        }

    }

    /**
     * Imprimeix una filera de cel·les alineades horitzontalment. Estil de cel·les negres.
     * No es respectarà la propietat "y" de la cel·la.
     * -----------------
     * |*title*|*title*|
     * -----------------
     * | body  | body  |
     * ----------------- ...
     * [0]   [1]
     *
     * @param celaArray
     * @throws IOException
     */
    private void printCellaNegraArray(Cela[] celaArray, Integer y) throws IOException {
        //printer.reset();
        //printer.selectPageMode();

        int lastX = 0;

        for (Cela cela : celaArray) {
            Integer width = cela.getWidth();
            if(width == null) {
                width = new Integer(PAGE_WIDTH - lastX);
            }

            defineCellaNegra(lastX, y, width, cela.getTitle(), cela.getBody());

            lastX += width;
        }


    }

    /**
     * Imprimeix la cel·la amb estil. Text del títol sagnat amb fons blanc.
     * -------
     * |title|
     * -------
     * |body |
     * -------
     *
     * @param x position
     * @param y position
     * @param width
     * @param height
     * @param title text superior
     * @param body text inferior
     * @throws IOException
     */
    private void printCellaBlanca(int x, int y , int width, int height, String title, String body) throws IOException {
        printer.reset();
        printer.selectPageMode();

        defineCellaBlanca(x, y, width, title, body);

        printer.printPage();
        printer.selectStandardMode();
        printer.flush();
    }

    private void defineCellaBlanca(int x, int y , int width, String title, String body) throws IOException {

        int textX    = x + CELLA_INSET;
        int textY    = y + CELLA_INSET;

        // només retallem si l'ample de la cel·la és inferior al del tiquet
        // perquè en aquest cas només es pot mostrar 1 línia
        int numChars = width / CHAR_SIZE;
        if (width < PAGE_WIDTH && body.length() > numChars) {
            body = body.substring(0, numChars); // capem el body als caràcters exàctes que hi càben, només si la caixa és inferior a tot l'ample
        }

        printer.setPageXY(textX, textY);
        printer.printTaggedText("{reset}{b}" + title +"{br}", CHARSET_ENCODING);

        textY += LINE_HEIGHT;
        printer.setPageXY(textX, textY);
        printer.printTaggedText("{reset}" + body + "{br}", CHARSET_ENCODING);

        printer.drawPageFrame(x, y, width, LINE_HEIGHT, Printer.FILL_BLACK, 1);

        // només calculem el número de línies en el cas que l'ample
        // de la cel·la sigui igual o superior a la del tiquet
        // en qualsevol altre cas només permetem una línia
        Integer numLines = 1;
        if (width >= PAGE_WIDTH) {
            numLines = calcNumLines(body, numChars);
        }
        int height  = (numLines+1)*LINE_HEIGHT;
        printer.drawPageFrame(x, y, width, height, Printer.FILL_BLACK, 1);
    }

    /**
     * Imprimeix la ce·la amb estil. Text del títol sagnat amb fons blang
     * ---------
     * |*title*|
     * ---------
     * |  body |
     * ---------
     *
     * @param x position
     * @param y position
     * @param width
     * @param title text superior
     * @param body text inferior
     * @throws IOException
     */
    private void printCellaNegre(int x, int y , int width, String title, String body) throws IOException {
        printer.reset();
        printer.selectPageMode();

        defineCellaNegra(x, y, width, title, body);

        printer.printPage();
        printer.selectStandardMode();
        printer.flush();
    }

    private void defineCellaNegra(int x, int y , int width, String title, String body) throws IOException {

        int textX     = x + CELLA_INSET;
        int textY     = y + CELLA_INSET;
        int numChars = width / CHAR_SIZE;

        if (width < PAGE_WIDTH && body.length() > numChars) {
            body = body.substring(0, numChars); // capem el body als caràcters exàctes que hi càben, només si la caixa és inferior a tot l'ample
        }

        printer.setPageXY(textX, textY);
        printer.printTaggedText("{reset}{b}" + title +"{br}", CHARSET_ENCODING);

        textY += LINE_HEIGHT;
        printer.setPageXY(textX, textY);
        printer.printTaggedText("{reset}{h}{w}" + body + "{br}", CHARSET_ENCODING);

        printer.drawPageFrame(x, y, width, LINE_HEIGHT, Printer.FILL_BLACK, 1);

        Integer numLines = calcNumLines(body, numChars);
        int height       = (numLines+1)*LINE_HEIGHT_BIG;
        printer.drawPageFrame(x, y, width, height, Printer.FILL_BLACK, 1);

        printer.drawPageRectangle(x, y, width, LINE_HEIGHT, Printer.FILL_INVERTED);
    }

    /**
     * Imprimeix cel·la amb estil. Títol amb sagnat. Cel·la gran.
     * ---------
     * |*title*|
     * |       |
     * ---------
     * |  body |
     * |       |
     * ---------
     *
     * @param x position
     * @param y position
     * @param width
     * @param title text superior
     * @param body text inferior
     * @throws IOException
     */
    private void printCelaNegreFontGran(int x, int y, int width, String title, String body) throws IOException {

        Integer textX = x + CELLA_INSET;
        Integer textY = y + CELLA_INSET;

        printer.setPageXY(textX, textY);
        printer.printTaggedText("{reset}{center}{b}{h}{w}" + title +"{br}", CHARSET_ENCODING);

        textY += LINE_HEIGHT_BIG;
        printer.setPageXY(textX, textY);

        printer.printTaggedText("{reset}{center}{h}{w}" + body + "{br}", CHARSET_ENCODING);

        printer.drawPageFrame(x, y, width, LINE_HEIGHT_BIG, Printer.FILL_BLACK, 1);
        printer.drawPageFrame(x, y, width, LINE_HEIGHT_BIG * 2, Printer.FILL_BLACK, 1);

        printer.drawPageRectangle(x, y, width, LINE_HEIGHT_BIG, Printer.FILL_INVERTED);

    }

    /**
     * Trnasforma milimetres a píxels. Els milimetres són del tiquet d'exemple, no del tiquet que s'imprimeix.
     *
     * @param milimeters milimetres del tiquet d'exemple
     */
    private int sampleMilimetersToPixels(int milimeters) {
        return milimeters * PAGE_WIDTH / SAMPEL_TICKET_WIDTH_MM;
    }

    private String getFormatedImport(float _import) {
        return decimalFormat.format(_import) + "€";
    }

    /**
     * Cel·la. Deifinició de les seves propietats.
     */
    private class Cela {

        private String title;
        private String body;
        private Integer width;
        private Integer height;

        /**
         * Defineix la cel·la amb l'altura per defecte. L'amplada serà tan gran com pugui.
         * @param title text superior
         * @param body text inferior
         */
        public Cela(String title, String body) {
            this.title = title;
            this.body = body;
            this.width = null;
            this.height = LINE_HEIGHT;
        }

        /**
         * Dexinfeix la cel·la amb l'altura per defecte.
         *
         * @param title text superior
         * @param body text inferior
         * @param width
         */
        public Cela(String title, String body, Integer width) {
            this.title = title;
            this.body = body;
            this.width = width;
            this.height = LINE_HEIGHT;
        }

        /**
         * Defineix la cel·la amb totes les seves propietats.
         *
         * @param title text superior
         * @param body text inferior
         * @param width
         * @param height
         */
        public Cela(String title, String body, Integer width, Integer height) {
            this.title = title;
            this.body = body;
            this.width = width;
            this.height = height;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }

        public Integer getWidth() {
            return width;
        }

        public Integer getHeight() {
            return height;
        }
    }
}
