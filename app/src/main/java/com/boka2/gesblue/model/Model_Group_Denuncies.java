package com.boka2.gesblue.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.boka2.gesblue.datamanager.database.model.Model_Denuncia;
import com.boka2.gesblue.datamanager.webservices.results.operativa.RecuperaDenunciaResponse;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Model_Group_Denuncies {

    public List<Model_Denuncia> getLlistat() {
        return llistat;
    }

    public void setLlistat(List<Model_Denuncia> llistat) {
        this.llistat = llistat;
    }

    private List<Model_Denuncia> llistat;

    public int getPendents_imprimir() {
        return pendents_imprimir;
    }

    public void setPendents_imprimir(int pendents_imprimir) {
        this.pendents_imprimir = pendents_imprimir;
    }

    public int getPendents_enviar() {
        return pendents_enviar;
    }

    public void setPendents_enviar(int pendents_enviar) {
        this.pendents_enviar = pendents_enviar;
    }

    public int getEnviades() {
        return enviades;
    }

    public void setEnviades(int enviades) {
        this.enviades = enviades;
    }

    public int getFallides() {
        return fallides;
    }

    public void setFallides(int fallides) {
        this.fallides = fallides;
    }

    private int pendents_imprimir;
    private int pendents_enviar;
    private int enviades;
    private int fallides;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    public Model_Group_Denuncies(List<Model_Denuncia> denuncies,String data) {
        this.date=data;
        this.llistat=denuncies;
        this.pendents_imprimir=0;
        this.pendents_enviar=0;
        this.enviades=0;
        this.fallides=0;

    }


    public Model_Group_Denuncies() {
        this.date="";
        this.llistat=new List<Model_Denuncia>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Model_Denuncia> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(Model_Denuncia model_denuncia) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Model_Denuncia> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends Model_Denuncia> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Model_Denuncia get(int index) {
                return null;
            }

            @Override
            public Model_Denuncia set(int index, Model_Denuncia element) {
                return null;
            }

            @Override
            public void add(int index, Model_Denuncia element) {

            }

            @Override
            public Model_Denuncia remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<Model_Denuncia> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Model_Denuncia> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<Model_Denuncia> subList(int fromIndex, int toIndex) {
                return null;
            }
        };

        this.pendents_imprimir=0;
        this.pendents_enviar=0;
        this.enviades=0;
        this.fallides=0;
    }
}
