package com.mybitcoin.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.mybitcoin.R;
import com.mybitcoin.bean.CreateWalletBean;
import com.mybitcoin.bean.GetNewAddressBean;
import com.mybitcoin.databinding.ActivityCreateWalletBinding;
import com.mybitcoin.model.CreateWalletModel;
import com.mybitcoin.network.CallbackListener;
import com.mybitcoin.network.QueryManager;
import com.mybitcoin.util.Constants;
import com.mybitcoin.util.ProgressHelper;
import com.mybitcoin.util.Utility;

public class CreateWalletActivity extends AppCompatActivity {

    private ActivityCreateWalletBinding mBinding;
    private boolean termsPrivacy;
    private ProgressHelper progressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_wallet);
        mBinding.setActivity(this);
        setToolBar();
        setModel();
        init();
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
     * Set model
     */
    private void setModel() {
        mBinding.setModel(new CreateWalletModel());
    }

    private void init() {
        progressHelper = new ProgressHelper(CreateWalletActivity.this);
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
     * Validation for all fields
     */
    private boolean validation() {
        if (TextUtils.isEmpty(mBinding.getModel().getName())) {
            Utility.showToast(CreateWalletActivity.this,
                    "Please enter name");
            return false;
        } else if (TextUtils.isEmpty(mBinding.getModel().getEmail())) {
            Utility.showToast(CreateWalletActivity.this,
                    "Please enter email");
            return false;
        } else if (TextUtils.isEmpty(mBinding.getModel().getPassword())) {
            Utility.showToast(CreateWalletActivity.this,
                    "Please enter password");
            return false;
        } else if (TextUtils.isEmpty(mBinding.getModel().getConfirmPassword())) {
            Utility.showToast(CreateWalletActivity.this,
                    "Please enter confirm password");
            return false;
        } else if (!mBinding.getModel().getPassword()
                .equals(mBinding.getModel().getConfirmPassword())) {
            Utility.showToast(CreateWalletActivity.this,
                    "Password does not match");
            return false;
        }
        return true;
    }

    /**
     * Click on continue
     */
    public void onClickContinue() {
        if (termsPrivacy) {
            if (validation()) {
                getAddress();
            }
        } else {
            Utility.showToast(CreateWalletActivity.this,
                    "Please select terms & privacy policy");
        }
    }

    /**
     * Get new wallet address
     */
    private void getAddress() {
        progressHelper.show();
        //https://block.io/api/v2/get_new_address/?api_key=a60e-91cd-f996-5db3&label=dasdfsdasdd
        String label = mBinding.getModel().getName() + "_label";
        String request = Constants.BASE_URL + Constants.METHOD_GET_NEW_ADDRESS
                + Constants.PARAM_API_KEY + Constants.BITCOIN_API_KEY
                + Constants.PARAM_AND + Constants.PARAM_LABEL + label;
        QueryManager.getInstance().getResponseBlockIO(CreateWalletActivity.this,
                request, new CallbackListener() {
                    @Override
                    public void onResult(Exception e, final String result) {
                        if (!isDestroyed()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    parseGetAddress(result);
                                }
                            });
                        }
                    }
                });
    }

    /**
     * Parse get address response
     */
    private void parseGetAddress(String response) {
        if (!TextUtils.isEmpty(response)) {
            GetNewAddressBean bean = new Gson().fromJson(response, GetNewAddressBean.class);
            if (bean != null) {
                if (bean.getStatus().equals(Constants.SUCCESS)) {
                    mBinding.getModel().setUsrId(bean.getData().getUserId());
                    mBinding.getModel().setWalletAddress(bean.getData().getAddress());
                    mBinding.getModel().setWalletLabel(bean.getData().getLabel());
                    createWallet();
                } else if (bean.getStatus().equals(Constants.FAIL)) {
                    progressHelper.dismiss();
                    Utility.showToast(CreateWalletActivity.this,
                            getString(R.string.something_wrong));
                }
            }
        } else {
            progressHelper.dismiss();
            Utility.showToast(CreateWalletActivity.this,
                    getString(R.string.server_not_respond));
        }
    }

    /**
     * Create wallet
     */
    private void createWallet() {
        QueryManager.getInstance().createWallet(mBinding.getModel(),
                new CallbackListener() {
                    @Override
                    public void onResult(Exception e, final String result) {
                        if (!isDestroyed()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    parseCreateWallet(result);
                                }
                            });
                        }
                    }
                });
    }

    /**
     * Parse create wallet response
     */
    private void parseCreateWallet(String response) {
        progressHelper.dismiss();
        if (!TextUtils.isEmpty(response)) {
            CreateWalletBean bean = new Gson().fromJson(response, CreateWalletBean.class);
            if (bean != null) {
                if (bean.getCode().equals(Constants.CODE_SUCCESS)) {
                    Utility.showToast(CreateWalletActivity.this, "Wallet create successfully");
                    Intent intent = new Intent(CreateWalletActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (bean.getCode().equals(Constants.CODE_FAILED)) {
                    Utility.showToast(CreateWalletActivity.this,
                            bean.getMessage());
                }
            }
        } else {
            Utility.showToast(CreateWalletActivity.this,
                    getString(R.string.server_not_respond));
        }
    }
}
