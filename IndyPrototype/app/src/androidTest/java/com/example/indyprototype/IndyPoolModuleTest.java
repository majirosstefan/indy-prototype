package com.example.indyprototype;

import org.hyperledger.indy.sdk.ErrorCode;
import org.hyperledger.indy.sdk.InvalidParameterException;
import org.hyperledger.indy.sdk.InvalidStructureException;
import org.hyperledger.indy.sdk.crypto.Crypto;
import org.hyperledger.indy.sdk.crypto.CryptoJSONParameters;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class IndyPoolModuleTest extends IndyIntegrationTest {

    public Wallet wallet;

    @Before
    public void createWallet() throws Exception {
        Wallet.createWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
        this.wallet = Wallet.openWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
    }


    @Test
    public void testErrors() throws Exception {
        try {
            String paramJson = new CryptoJSONParameters.CreateKeyJSONParameter("invalidSeedLength", null).toJson();
            Crypto.createKey(this.wallet, paramJson).get();
        } catch (ExecutionException e) {
            InvalidStructureException ex = (InvalidStructureException) e.getCause();
            assertEquals(ex.getSdkErrorCode(), ErrorCode.CommonInvalidStructure.value());
            assertFalse(ex.getMessage().isEmpty());
        }

        try {
            byte[] message = {};
            Crypto.cryptoSign(this.wallet, VERKEY, message).get();
        } catch (ExecutionException e) {
            InvalidParameterException ex = (InvalidParameterException) e.getCause();
            assertEquals(ex.getSdkErrorCode(), ErrorCode.CommonInvalidParam5.value());
            assertFalse(ex.getMessage().isEmpty());
        }
    }

    @After
    public void deleteWallet() throws Exception {
        wallet.closeWallet().get();
        Wallet.deleteWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
    }
}