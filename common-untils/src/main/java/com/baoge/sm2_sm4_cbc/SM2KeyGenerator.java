package com.baoge.sm2_sm4_cbc;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.security.SecureRandom;

public class SM2KeyGenerator {
    public static AsymmetricCipherKeyPair generateKeyPair() {
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("sm2p256v1");
        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(
                new ECDomainParameters(ecSpec.getCurve(), ecSpec.getG(), ecSpec.getN()),
                new SecureRandom());
        
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        generator.init(keyGenParams);
        return generator.generateKeyPair();
    }
}