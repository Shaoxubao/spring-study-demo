package com.baoge.weather.NASAPOWER;
   
import com.fasterxml.jackson.databind.JsonNode;   
import com.fasterxml.jackson.databind.ObjectMapper;
   
import java.io.*;   
import java.net.HttpURLConnection;   
import java.net.URLEncoder;   
import java.net.URL;   
import java.nio.charset.StandardCharsets;   
import java.time.*;   
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;
   
/**
   * NASA POWER hourly point fetcher for several cities in Shaanxi.
   * Java 8 compatible.
   */   
public class NasaPowerFetcher {

    // 要拉取的参数
    private static final String PARAMETERS = "T2M,WS2M,ALLSKY_SFC_SW_DWN";
    private static final String API_BASE = "https://power.larc.nasa.gov/api/temporal/hourly/point";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DateTimeFormatter NASA_HOUR_KEY_FMT = DateTimeFormatter.ofPattern("yyyyMMddHH");
    private static final DateTimeFormatter OUT_DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC);

    public static void main(String[] args) throws Exception {
        // 城市经纬度（可根据需要增减或改为读取文件）
        // 经度、纬度使用十进制度数，注意 NASA POWER 接受经度/纬度。
        Map<String, double[]> cities = new LinkedHashMap<>();
        cities.put("XiAn", new double[]{108.9398, 34.3416});     // 西安
//        cities.put("Baoji", new double[]{107.2378, 34.3433});    // 宝鸡
//        cities.put("Xianyang", new double[]{108.7026, 34.3455}); // 咸阳
//        cities.put("Tongchuan", new double[]{109.0900, 35.0812}); // 铜川
//        cities.put("Weinan", new double[]{109.4869, 34.4994});   // 渭南
//        cities.put("Yanan", new double[]{109.4898, 36.6033});    // 延安
//        cities.put("Hanzhong", new double[]{107.0239, 33.0675}); // 汉中
//        cities.put("Yulin", new double[]{109.7353, 38.2794});    // 榆林
//        cities.put("Ankang", new double[]{109.0193, 32.6835});   // 安康
//        cities.put("Shangluo", new double[]{109.9342, 33.8726}); // 商洛

        // 计算时间范围（以 UTC 的今天为 end，start = today - 3 months）
        LocalDate todayUtc = LocalDate.now(ZoneOffset.UTC);
        LocalDate startDate = todayUtc.minusMonths(3);
        String start = startDate.format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
        String end = todayUtc.format(DateTimeFormatter.BASIC_ISO_DATE);

        System.out.println("Fetching NASA POWER hourly data for Shaanxi cities");
        System.out.println("Time range (UTC): " + start + " -> " + end);
        System.out.println("Parameters: " + PARAMETERS);
        System.out.println();

        for (Map.Entry<String, double[]> e : cities.entrySet()) {
            String city = e.getKey();
            double lon = e.getValue()[0];
            double lat = e.getValue()[1];
            System.out.println("Fetching city: " + city + " (lat=" + lat + ", lon=" + lon + ") ...");
            try {
                JsonNode root = fetchCityDataWithRetry(lat, lon, end, end, 3);
                if (root == null) {
                    System.err.println("  Failed to get data for " + city);
                    continue;
                }
                // NASA POWER JSON typical path: properties -> parameter -> {VAR: {timestamp: value}}
                JsonNode parameterNode = root.path("properties").path("parameter");
                if (parameterNode.isMissingNode()) {
                    // fallback try older key names
                    parameterNode = root.path("parameters");
                }
                if (parameterNode.isMissingNode() || parameterNode.size() == 0) {
                    System.err.println("  No 'parameter' node found in response for " + city);
                    System.err.println("  Raw response: " + root.toString().substring(0, Math.min(400, root.toString().length())));
                    continue;
                }

                // collect timestamps
                Set<String> timeKeys = new TreeSet<>();
                String[] vars = PARAMETERS.split(",");
                Map<String, Map<String, String>> varToMap = new HashMap<>();
                for (String var : vars) {
                    JsonNode varNode = parameterNode.path(var);
                    Map<String, String> map = new HashMap<>();
                    if (!varNode.isMissingNode()) {
                        Iterator<Map.Entry<String, JsonNode>> fields = varNode.fields();
                        while (fields.hasNext()) {
                            Map.Entry<String, JsonNode> f = fields.next();
                            String tkey = f.getKey();
                            String val = f.getValue().isNull() ? "" : f.getValue().asText();
                            map.put(tkey, val);
                            timeKeys.add(tkey);
                        }
                    }
                    varToMap.put(var, map);
                }

                if (timeKeys.isEmpty()) {
                    System.err.println("  No hourly data returned for " + city);
                    continue;
                }

                String outFileName = city + "_" + start + "_" + end + ".csv";
                writeCsv(outFileName, timeKeys, vars, varToMap);
                System.out.println("  Saved to " + outFileName);
                // 为避免触发速率限制，稍作 sleep
                Thread.sleep(800);
            } catch (Exception ex) {
                System.err.println("  Error for city " + city + ": " + ex.getMessage());
                ex.printStackTrace(System.err);
            }
        }

        System.out.println("Done.");
    }

    private static JsonNode fetchCityDataWithRetry(double lat, double lon, String start, String end, int maxAttempts) throws InterruptedException {
        int attempt = 0;
        while (attempt < maxAttempts) {
            attempt++;
            try {
                JsonNode root = fetchCityData(lat, lon, start, end);
                return root;
            } catch (IOException ioe) {
                System.err.println("  Request failed (attempt " + attempt + "): " + ioe.getMessage());
                if (attempt < maxAttempts) {
                    Thread.sleep(1500L * attempt);
                } else {
                    System.err.println("  Max attempts reached.");
                }
            }
        }
        return null;
    }

    private static JsonNode fetchCityData(double lat, double lon, String start, String end) throws IOException {
        String paramsEncoded = URLEncoder.encode(PARAMETERS, "UTF-8");
        String urlStr = API_BASE +
                "?start=" + start +
                "&end=" + end +
                "&latitude=" + lat +
                "&longitude=" + lon +
                "&parameters=" + paramsEncoded +
                "&community=RE" +
                "&format=JSON" +
                "&units=metric";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(60000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Java-NasaPowerFetcher/1.0");
        int code = conn.getResponseCode();
        InputStream in = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String response = readAll(in, StandardCharsets.UTF_8);
        if (code < 200 || code >= 300) {
            throw new IOException("HTTP " + code + " : " + response);
        }
        JsonNode root = MAPPER.readTree(response);
        return root;
    }

    private static String readAll(InputStream in, java.nio.charset.Charset cs) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, cs))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        }
    }

    private static void writeCsv(String fileName, Set<String> timeKeys, String[] vars, Map<String, Map<String, String>> varToMap) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            // header
            bw.write("datetime_UTC");
            for (String v : vars) {
                bw.write("," + v);
            }
            bw.newLine();

            for (String tkey : timeKeys) {
                // NASA hourly key: yyyyMMddHH (assumption). If NASA returns different length, try to parse robustly.
                String outTime = tkey;
                String formatted;
                try {
                    TemporalAccessor ta = NASA_HOUR_KEY_FMT.parse(tkey);
                    LocalDateTime ldt = LocalDateTime.of(
                            ta.get(ChronoField.YEAR),
                            ta.get(ChronoField.MONTH_OF_YEAR),
                            ta.get(ChronoField.DAY_OF_MONTH),
                            ta.get(ChronoField.HOUR_OF_DAY),
                            0
                    );
                    Instant ins = ldt.toInstant(ZoneOffset.UTC);
                    formatted = OUT_DATETIME_FMT.format(ins);
                } catch (Exception ex) {
                    // fallback: output raw key
                    formatted = outTime;
                }

                bw.write(formatted);
                for (String v : vars) {
                    Map<String, String> map = varToMap.get(v);
                    String val = map == null ? "" : map.getOrDefault(tkey, "");
                    bw.write("," + (val == null ? "" : val));
                }
                bw.newLine();
            }
            bw.flush();
        }
    }
}
