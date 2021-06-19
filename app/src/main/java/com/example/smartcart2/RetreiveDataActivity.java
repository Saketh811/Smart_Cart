package com.example.smartcart2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RetreiveDataActivity extends AppCompatActivity implements PaymentResultListener {
    ListView myListView;
    List<ucart> List;
    DatabaseReference DbRef, DbRef2;
    private FirebaseAuth firebaseAuth;
    TextView tvtotal;
    Button bt;
    int total=0;
    String dbChild;
    ucart temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_retreive_data);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
         dbChild = firebaseAuth.getUid();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FC415A"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        Checkout.preload(getApplicationContext());

        bt=(Button)findViewById(R.id.btp);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });


        myListView =findViewById(R.id.mylistView);
        tvtotal = findViewById(R.id.tvtotal);
        List =new ArrayList<>();
        DbRef= FirebaseDatabase.getInstance().getReference("ucart").child(dbChild);
        DbRef.addValueEventListener(new ValueEventListener() {
            @Override


            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List.clear();
                for(DataSnapshot cartDatasnap : dataSnapshot.getChildren()){
                    ucart cart=cartDatasnap.getValue(ucart.class);
                    List.add(cart);
                }
                total=0;
                System.out.println("list size1"+List.size());
                for(int i=0; i< List.size();i++)
                {
                    temp = List.get(i);
                    total = total + temp.getPrice();
                }
                System.out.println(total);

                tvtotal.setText(Integer.toString(total));

                final TextView helloTextView = (TextView) findViewById(R.id.tvtotal);
                


                ListAdapter adapter=new ListAdapter(RetreiveDataActivity.this,List);
                myListView.setAdapter(adapter);
                System.out.println("total"+total);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ucart temp1 = List.get(position);
                System.out.println(temp1.getBrcode());
                showUpdateDialog(temp1.getBrcode());
                return false;
            }
        });
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUpdateDialog(Long id) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(RetreiveDataActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.delete_item, null);

        mDialog.setView(mDialogView);

        Button btnDelete = mDialogView.findViewById(R.id.btnDelete);

        mDialog.setTitle("Deleting Record");
        //mDialog.show();
        final AlertDialog alertDialog = mDialog.create();
        alertDialog.show();


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord(id);
                alertDialog.dismiss();
            }

        });
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void deleteRecord(Long id) {

//        System.out.println(id);
        DbRef2 = FirebaseDatabase.getInstance().getReference("ucart").child(dbChild).child(Long.toString(id));
//        System.out.println(DbRef.toString());
        Task<Void> mTask = DbRef2.removeValue();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                showToast("Deleted record");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Error deleting record");
            }
        });

    }

    public void startPayment() {


        Checkout checkout = new Checkout();
        checkout.setKeyID("Your Key Here!");

        checkout.setImage(R.drawable.ic_cart);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Smart Cart");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "100");//pass amount in currency subunits
            options.put("prefill.contact", "9999999999");

            // put email
            options.put("prefill.email", "xyz@gmail.com");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        // this method is called on payment success.
        Toast.makeText(this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();
        DbRef.removeValue();
    }

    @Override
    public void onPaymentError(int i, String s) {
        // on payment failed.
        Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
    }

}
