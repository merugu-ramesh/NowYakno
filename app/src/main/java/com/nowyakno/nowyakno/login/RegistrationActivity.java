package com.nowyakno.nowyakno.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nowyakno.nowyakno.R;
import com.nowyakno.nowyakno.login.model.User;

/**
 * Created by LENOVO on 24-03-2017.
 */

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText firstNameEdt, lastNameEdt, emailEdt, mobileEdt, cityEdt,
            stateEdt, addressEdt, ZipCodeEdt;
    Button submitBtn, cancelBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        getViews();
    }

    private void getViews() {
        firstNameEdt = (EditText) findViewById(R.id.firstnamellEdt);
        lastNameEdt = (EditText) findViewById(R.id.lastNameEdt);
        emailEdt = (EditText) findViewById(R.id.emaiIdEdt);
        mobileEdt = (EditText) findViewById(R.id.mobileNumEdt);
        cityEdt = (EditText) findViewById(R.id.cityEdt);
        stateEdt = (EditText) findViewById(R.id.stateEdt);
        addressEdt = (EditText) findViewById(R.id.addressEdt);
        ZipCodeEdt = (EditText) findViewById(R.id.zipcodeEdt);
        submitBtn = (Button) findViewById(R.id.submitBtnReg);
        submitBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.cancelBtnReg);
        cancelBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtnReg:
                User userDetail = new User();
                userDetail.firstName = firstNameEdt.getText().toString();
                userDetail.lastName = lastNameEdt.getText().toString();
                userDetail.emailId = emailEdt.getText().toString();
                userDetail.mobileNumber = mobileEdt.getText().toString();
                userDetail.city = cityEdt.getText().toString();
                userDetail.state = stateEdt.getText().toString();
                userDetail.address = addressEdt.getText().toString();
                userDetail.zipCode = ZipCodeEdt.getText().toString();
                if (userDetail.firstName.isEmpty()) {
                    firstNameEdt.setError("Enter FirstName");
                    return;
                }
                if (userDetail.emailId.isEmpty()) {
                    emailEdt.setError("Enter EmailId");
                    return;
                }
                if (userDetail.mobileNumber.isEmpty()) {
                    mobileEdt.setError("Enter MobileNumber");
                    return;
                }
                if (userDetail.city.isEmpty()) {
                    cityEdt.setError("Enter City");
                    return;
                }
                if (userDetail.state.isEmpty()) {
                    stateEdt.setError("Enter State");
                    return;
                }
                if (userDetail.address.isEmpty()) {
                    addressEdt.setError("Enter address");
                    return;
                }
                if (userDetail.zipCode.isEmpty()) {
                    ZipCodeEdt.setError("Enter Zip code");
                    return;
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegistrationActivity.this);

                alertDialog.setMessage("Saved Successfully");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
                break;
            case R.id.cancelBtnReg:
                 finish();
                break;
        }
    }
}
