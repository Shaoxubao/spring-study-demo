package com.baoge.sm2_hutool;

import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.util.encoders.Hex;


public class SM2Example {
    public void sm2Verify1Test() {
        // 私钥（16 进制，64 字符）
        final String privateKey = "4BD9A450D7E68A5D7E08EB7A0BFA468FD3EB32B71126246E66249A73A9E4D44A";
        // 公钥（非压缩格式，130 字符，以04开头）
        final String publicKey = "04970AB36C3B870FBC04041087DB1BC36FB4C6E125B5EA406DB0EC3E2F80F0A55D8AFF28357A0BB215ADC2928BE76F1AFF869BF4C0A3852A78F3B827812C650AD3";
        // 待签名数据
        final String data = "旧信";
        // 初始化 SM2 实例
        final SM2 sm2 = new SM2(privateKey, publicKey);
        // 签名（可选）
         final String signHex = new String(Hex.encode(sm2.sign(data.getBytes())));
         System.out.println("Hutool签名的hex:" + signHex);

        // 示例签名（TypeScript 生成的签名）
        final String tsSignHex = "304402207a86be624a8b299084fec9c014eed60e1f2c277407dbfb9c4d4f644e8b603362022012771fbf1c86c6cd02ce273249243e2ce81408766f93ef81012113740af21665";
        // 验签
        final boolean verify1 = sm2.verify(data.getBytes(), Hex.decode(tsSignHex));
        System.out.println("验签结果: " + verify1);

        final String encrypt = new String(Hex.encode(sm2.encrypt(data.getBytes())));
        System.out.println("Hutool加密的hex:" + encrypt);
        final String decrypt = new String(sm2.decrypt(Hex.decode(encrypt)));
        System.out.println("Hutool解密的hex:" + decrypt);
    }

    public static void main(String[] args) {
        SM2Example example = new SM2Example();
        example.sm2Verify1Test();
    }
}
