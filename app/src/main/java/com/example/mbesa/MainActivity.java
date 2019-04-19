package com.example.mbesa;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;

import static com.example.mbesa.MbesaConstants.CONSUMER_SECRET;
import static com.example.mbesa.MbesaConstants.accountReference;
import static com.example.mbesa.MbesaConstants.amount;
import static com.example.mbesa.MbesaConstants.businessShortCode;
import static com.example.mbesa.MbesaConstants.partyA;
import static com.example.mbesa.MbesaConstants.partyB;
import static com.example.mbesa.MbesaConstants.passKey;
import static com.example.mbesa.MbesaConstants.transactionDescription;

public class MainActivity extends AppCompatActivity {

    EditText editTextPhoneNumber;
    Button sendButton;

    Daraja daraja;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        sendButton = findViewById(R.id.sendButton);

        daraja = Daraja.with(MbesaConstants.CONSUMER_KEY,CONSUMER_SECRET, new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(MainActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(MainActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(MainActivity.this.getClass().getSimpleName(), error);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = editTextPhoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    editTextPhoneNumber.setError("Please Provide a Phone Number");
                    return;
                }
                initiateInteraction();
            }
        });

    }

    public void initiateInteraction(){
        LNMExpress lnmExpress = new LNMExpress(
                businessShortCode,
                passKey,  //https://developer.safaricom.co.ke/test_credentials
                TransactionType.CustomerPayBillOnline,
                amount,
                partyA,
                partyB,
                phoneNumber,
                "http://mycallbackurl.com/checkout.php",
                accountReference,
                transactionDescription
        );

        daraja.requestMPESAExpress(lnmExpress,
                new DarajaListener<LNMResult>() {
                    @Override
                    public void onResult(@NonNull LNMResult lnmResult) {
                        Log.i(MainActivity.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                    }

                    @Override
                    public void onError(String error) {
                        Log.i(MainActivity.this.getClass().getSimpleName(), error);
                    }
                }
        );
    }
}
