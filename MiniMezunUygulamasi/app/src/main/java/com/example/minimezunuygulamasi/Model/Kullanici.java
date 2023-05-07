package com.example.minimezunuygulamasi.Model;

import android.net.Uri;

public class Kullanici {
    private String kIsim, kSoyisim, kEmail, kGiris, kMezuniyet, kProfil;

    public Kullanici(String kIsim, String kSoyisim, String kEmail, String kGiris, String kMezuniyet, String kProfil) {
        this.kIsim = kIsim;
        this.kSoyisim = kSoyisim;
        this.kEmail = kEmail;
        this.kGiris = kGiris;
        this.kMezuniyet = kMezuniyet;
        this.kProfil = kProfil;
    }

    public Kullanici() {
    }

    public String getkProfil() {
        return kProfil;
    }

    public void setkProfil(String kProfil) {
        this.kProfil = kProfil;
    }

    public String getkIsim() {
        return kIsim;
    }

    public void setkIsim(String kIsim) {
        this.kIsim = kIsim;
    }

    public String getkSoyisim() {
        return kSoyisim;
    }

    public void setkSoyisim(String kSoyisim) {
        this.kSoyisim = kSoyisim;
    }

    public String getkEmail() {
        return kEmail;
    }

    public void setkEmail(String kEmail) {
        this.kEmail = kEmail;
    }


    public String getkGiris() {
        return kGiris;
    }

    public void setkGiris(String kGiris) {
        this.kGiris = kGiris;
    }

    public String getkMezuniyet() {
        return kMezuniyet;
    }

    public void setkMezuniyet(String kMezuniyet) {
        this.kMezuniyet = kMezuniyet;
    }
}
