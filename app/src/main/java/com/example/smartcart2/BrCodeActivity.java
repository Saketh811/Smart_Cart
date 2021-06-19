package com.example.smartcart2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;

import java.lang.reflect.Field;

import androidmads.library.qrgenearator.QRGContents;
        import androidmads.library.qrgenearator.QRGEncoder;

public class BrCodeActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private ImageView qrCodeIV;
    public Button btnCart;

    // private Button generateQrBtn;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_br_code);
        firebaseAuth = FirebaseAuth.getInstance();
        //Intent receivedIntent = getIntent();
        //Bundle extras = receivedIntent.getExtras();
        //String temp = extras.getString("string-key", "");
        //int i = temp.indexOf("@");
        //System.out.println(i);
        String eid = firebaseAuth.getUid();

        System.out.println(eid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        qrCodeIV = findViewById(R.id.idIVQrcode);
        btnCart = findViewById(R.id.btnCart);
        //generateQrBtn = findViewById(R.id.idBtnGenerateQR);
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);


        Display display = manager.getDefaultDisplay();

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();


        Point point = new Point();
        display.getSize(point);


        int width = point.x;
        int height = point.y;


        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        qrgEncoder = new QRGEncoder(eid, null, QRGContents.Type.TEXT, dimen);
        try {

            bitmap = qrgEncoder.encodeAsBitmap();

            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {

            Log.e("Tag", e.toString());
        }

        btnCart.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        startActivity(new Intent(BrCodeActivity.this, RetreiveDataActivity.class));
                    }
                }
        );


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater ();
        inflater.inflate(R.menu.my_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                firebaseAuth.signOut();
                startActivity(new Intent(BrCodeActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
    }