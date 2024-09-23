package com.baoge.controller;

import com.baoge.model.BaseCertificateInfo;
import com.baoge.utils.BouncyCastleUtils;
import com.baoge.utils.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cert")
public class CertController {

    @PostMapping(value = "/generatorCerKey")
    public JsonResponse generatorCerKey(@RequestBody BaseCertificateInfo info, HttpServletResponse response) {
        try {
            Map<String, Object> rt = new HashMap<>();
            info.setCommonName(info.getUserName());
            Map<String, String> result = BouncyCastleUtils.saveKeyStore(BouncyCastleUtils.createKeyPair(info), info);
            KeyPair keyPair = BouncyCastleUtils.getKeyPair(result.get("alias"), BouncyCastleUtils.getStoreFile(result.get("keyStorePath")));
            Map<String, Object> parentResult = BouncyCastleUtils.createCert(result.get("alias"), result.get("keyStorePath"), null, info);
            String certPath = (String) parentResult.get("certPath");
            response.setHeader("publicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            FileInputStream fileInputStream = new FileInputStream(BouncyCastleUtils.getStoreFile(certPath));
            // 获取响应的输出流
            OutputStream outputStream = response.getOutputStream();
            // 将文件转成字节数组，再将数组写入响应的输出流
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            // 刷新输出流
            outputStream.flush();
            // 关闭流
            fileInputStream.close();
            outputStream.close();
            return JsonResponse.success(rt);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.fail("生成失败");
        }
    }
}
