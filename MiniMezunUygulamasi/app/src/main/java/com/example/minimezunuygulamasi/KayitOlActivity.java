package com.example.minimezunuygulamasi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.minimezunuygulamasi.Model.Kullanici;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;



public class KayitOlActivity extends AppCompatActivity {

    String path;
    private Kullanici mKullanici;
    private LinearLayout mLinear;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private TextInputLayout inputIsim, inputSoyisim, inputEmail, inputGiris, inputMezuniyet, inputSifre;
    private EditText editIsim, editSoyisim, editEmail, editGiris, editMezuniyet, editSifre;
    private String txtIsim, txtSoyisim, txtEmail, txtGiris, txtMezuniyet, txtSifre;

    private void init(){
        mLinear = (LinearLayout)findViewById(R.id.kayit_ol_linear);
        inputIsim = (TextInputLayout)findViewById(R.id.kayit_ol_input_isim);
        inputSoyisim = (TextInputLayout)findViewById(R.id.kayit_ol_input_soy_isim);
        inputEmail = (TextInputLayout)findViewById(R.id.kayit_ol_input_email);
        inputGiris = (TextInputLayout)findViewById(R.id.kayit_ol_input_giris_yili);
        inputMezuniyet = (TextInputLayout)findViewById(R.id.kayit_ol_input_mezuniyet_yili);
        inputSifre = (TextInputLayout)findViewById(R.id.kayit_ol_input_sifre);
        editIsim = (EditText) findViewById(R.id.kayit_ol_editIsim);
        editSoyisim = (EditText) findViewById(R.id.kayit_ol_editSoyIsim);
        editEmail = (EditText) findViewById(R.id.kayit_ol_editEMail);
        editGiris = (EditText) findViewById(R.id.kayit_ol_editGirisYili);
        editMezuniyet = (EditText) findViewById(R.id.kayit_ol_editMezuniyetYili);
        editSifre = (EditText) findViewById(R.id.kayit_ol_editSifre);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        init();
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

    }

    public void btnKayitOl(View v){
        txtIsim = editIsim.getText().toString();
        txtSoyisim = editSoyisim.getText().toString();
        txtEmail = editEmail.getText().toString();
        txtGiris = editGiris.getText().toString();
        txtMezuniyet = editMezuniyet.getText().toString();
        txtSifre = editSifre.getText().toString();

        if(!TextUtils.isEmpty(txtIsim)){
            if(!TextUtils.isEmpty(txtSoyisim)){
                if(!TextUtils.isEmpty(txtEmail)){
                    if(!TextUtils.isEmpty(txtGiris)){
                        if(!TextUtils.isEmpty(txtMezuniyet)){
                            if(!TextUtils.isEmpty(txtSifre)){
                                mAuth.createUserWithEmailAndPassword(txtEmail, txtSifre)
                                        .addOnCompleteListener(KayitOlActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    mUser = mAuth.getCurrentUser();
                                                    mKullanici = new Kullanici(txtIsim, txtSoyisim, txtEmail, txtGiris, txtMezuniyet, "default");
                                                    mFirestore.collection("Kullanıcılar").document(mUser.getUid())
                                                            .set(mKullanici)
                                                            .addOnCompleteListener(KayitOlActivity.this, new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Toast.makeText(KayitOlActivity.this, "Başarıyla Kayıt Oldunuz!", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                        startActivity(new Intent(KayitOlActivity.this, GirisYapActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                    }else
                                                                        Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }else
                                                    Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }else
                                inputSifre.setError("Şifre Giriniz!");
                        }else
                            inputMezuniyet.setError("Mezuniyet Yılı Giriniz!");
                    }else
                        inputGiris.setError("Giriş Yılı Giriniz!");
                }else
                    inputEmail.setError("E-Mail Giriniz!");
            }else
                inputSoyisim.setError("Soy İsim Giriniz!");
        }else
            inputIsim.setError("İsim Giriniz!");

    }

}