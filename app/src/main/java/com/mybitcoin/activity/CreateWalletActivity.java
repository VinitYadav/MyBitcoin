package com.mybitcoin.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mybitcoin.R;
import com.mybitcoin.databinding.ActivityCreateWalletBinding;
import com.mybitcoin.util.Utility;

public class CreateWalletActivity extends AppCompatActivity {

    private ActivityCreateWalletBinding mBinding;
    private boolean termsPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_wallet);
        mBinding.setActivity(this);
        setToolBar();
    }

    /**
     * Set toolbar
     */
    private void setToolBar() {
        setSupportActionBar(mBinding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        mBinding.toolBar.setTitleTextColor(ContextCompat.getColor(CreateWalletActivity.this,
                R.color.colorPrimary));
        mBinding.toolBar.setTitle("Create Wallet");
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Click on terms & privacy policy link
     */
    public void onClickTermsPrivacy() {
        termsPrivacy = !termsPrivacy;
        if (termsPrivacy) {
            mBinding.textViewTermsPrivacy.setCompoundDrawablesWithIntrinsicBounds
                    (R.drawable.check_icon, 0, 0, 0);
        } else {
            mBinding.textViewTermsPrivacy.setCompoundDrawablesWithIntrinsicBounds
                    (R.drawable.uncheck_icon, 0, 0, 0);
        }
    }

    /**
     * Click on continue
     */
    public void onClickContinue() {
        if (termsPrivacy) {
            Utility.showToast(CreateWalletActivity.this, "Wallet create successfully");
            Intent intent = new Intent(CreateWalletActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Utility.showToast(CreateWalletActivity.this, "Please select terms & privacy policy");
        }
    }
}
