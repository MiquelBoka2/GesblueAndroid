package com.boka2.gesblue.ticketstyle;

import java.util.Calendar;

/*
 * Data d'anulació. Tupla de import + data.
 */
public class DataAnulacio {

    private float _import;
    private Calendar data;

    public DataAnulacio(float _import, Calendar data) {
        this._import = _import;
        this.data = data;
    }

    public float getImport() {
        return _import;
    }

    public Calendar getData() {
        return data;
    }
}