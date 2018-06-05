package com.mybitcoin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mybitcoin.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.setActivity(this);
    }

    /**
     * Click on login button
     */
    public void onClickLogin() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Click on create wallet
     */
    public void onClickCreateWallet() {
        Intent intent = new Intent(LoginActivity.this, CreateWalletActivity.class);
        startActivity(intent);
    }
}
