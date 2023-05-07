package com.example.minimezunuygulamasi;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minimezunuygulamasi.Model.Kullanici;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import io.grpc.Context;


public class ProfilFragment extends Fragment {

    private static final int IZIN_KODU = 0;
    private static final int IZIN_ALINDI_KODU = 1;
    private EditText txtIsimSoyisim;
    private ImageView imgProfil;

    private ImageView imgYeniResim;

    private View v;
    private FirebaseFirestore mFireStore;
    private DocumentReference mRef;
    private FirebaseUser mUser;
    private Kullanici user;
    private Intent galeryIntent;
    private Uri mUri;
    private Bitmap gelenResim;
    private ImageDecoder.Source imgSource;
    private ByteArrayOutputStream outputStream;
    private byte[] imgByte;
    private StorageReference mStorageRef, yeniRef, sRef;
    private String path, downloadLink;
    private HashMap<String, Object> mData;
    private EditText emailEditText;


    private Spinner educationSpinner;
    private EditText countryEditText;
    private EditText cityEditText;
    private EditText companyEditText;
    private EditText facebookEditText;
    private EditText instagramEditText;
    private EditText twitterEditText;
    private EditText phoneNumberEditText;

    private String spinnerEduc, countryString, cityString, companyString, faceString, instaString, twitString, emailString, phoneNumstring;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



       v=inflater.inflate(R.layout.fragment_profil, container, false);
       emailEditText = v.findViewById(R.id.editTextEmail);
       txtIsimSoyisim = v.findViewById(R.id.profil_isim);
       imgProfil = v.findViewById(R.id.profil_frag_image);
       imgYeniResim = v.findViewById(R.id.profil_foto_sec);
       Spinner spinner = v.findViewById(R.id.spinnereduc);
       countryEditText = v.findViewById(R.id.edittext_country);
       cityEditText = v.findViewById(R.id.edittext_city);
       companyEditText = v.findViewById(R.id.edittext_company);
       facebookEditText = v.findViewById(R.id.facebook_input);
       instagramEditText = v.findViewById(R.id.instagram_input);
       twitterEditText = v.findViewById(R.id.twitter_input);
       emailEditText = v.findViewById(R.id.editTextEmail);
       phoneNumberEditText = v.findViewById(R.id.editTextPhoneNumber);
       spinnerEduc = spinner.getSelectedItem().toString();
       countryString =countryEditText.toString();
       cityString = cityEditText.toString();
       companyString = companyEditText.toString();
       faceString = facebookEditText.toString();
       instaString = instagramEditText.toString();
       twitString = twitterEditText.toString();
       phoneNumstring = phoneNumberEditText.toString();

       mUser= FirebaseAuth.getInstance().getCurrentUser();
       mFireStore = FirebaseFirestore.getInstance();
       mStorageRef = FirebaseStorage.getInstance().getReference();

       mRef = mFireStore.collection("Kullanıcılar").document(mUser.getUid());
       mRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(value != null && value.exists()){
                    user = value.toObject(Kullanici.class);
                    if(user != null){
                        Toast.makeText(v.getContext(), user.getkIsim(), Toast.LENGTH_SHORT).show();
                        txtIsimSoyisim.setText(user.getkIsim());
                        emailEditText.setText(user.getkEmail());

                    if(user.getkProfil().equals("default"))
                        imgProfil.setImageResource(R.mipmap.ic_launcher);
                    else
                        Picasso.get().load(user.getkProfil()).resize(150,150).into(imgProfil);

                    }
                }
           }
       });

       imgYeniResim.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(ContextCompat.checkSelfPermission(v.getContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                   ActivityCompat.requestPermissions((Activity) v.getContext(),new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, IZIN_KODU);
               else{
                   galeriyeGit();
               }

           }
       });
        return v;
    }

    private void galeriyeGit(){
        galeryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galeryIntent, IZIN_ALINDI_KODU);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == IZIN_KODU){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                galeriyeGit();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == IZIN_ALINDI_KODU){
            if(resultCode == RESULT_OK && data != null && data.getData() != null){
                mUri = data.getData();


                try {
                    if(Build.VERSION.SDK_INT >= 28){
                        imgSource=ImageDecoder.createSource(v.getContext().getContentResolver(), mUri);
                        gelenResim = ImageDecoder.decodeBitmap(imgSource);
                    }else
                        gelenResim = MediaStore.Images.Media.getBitmap(v.getContext().getContentResolver(), mUri);
                    outputStream = new ByteArrayOutputStream();
                    gelenResim.compress(Bitmap.CompressFormat.PNG, 75, outputStream);
                    imgByte = outputStream.toByteArray();

                    path = "Kullanicilar/" + user.getkEmail() + "/profil.png";
                    sRef = mStorageRef.child(path);
                    sRef.putBytes(imgByte)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    yeniRef = FirebaseStorage.getInstance().getReference(path);
                                    yeniRef.getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    downloadLink = uri.toString();
                                                    mData = new HashMap<>();
                                                    mData.put("kProfil", downloadLink);
                                                    mFireStore.collection("Kullanıcılar").document(mUser.getUid())
                                                            .update(mData)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(!task.isSuccessful())
                                                                        Toast.makeText(v.getContext(), "Uygulama bir sorunla karşılaştı!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}