package com.mybitcoin.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.mybitcoin.BR;

public class CreateWalletModel extends BaseObservable {
    private String usrId = "";
    private String name = "";
    private String email = "";
    private String password = "";
    private String confirmPassword = "";
    private String walletAddress = "";
    private String walletLabel = "";

    public CreateWalletModel() {
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getWalletLabel() {
        return walletLabel;
    }

    public void setWalletLabel(String walletLabel) {
        this.walletLabel = walletLabel;
    }
}
