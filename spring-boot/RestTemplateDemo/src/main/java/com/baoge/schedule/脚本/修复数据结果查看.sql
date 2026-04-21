-- 数据修复
-- temp 为 NULL 的行进行修复：
-- temp = 前一小时 temp 和后一小时 temp 的平均值
-- 其他列 humidity/weather_code/is_rain/... 全部取 ** 前一小时（2026-04-20 05:00:00）** 的值

WITH data_with_prev_next AS (
    SELECT
        id,
    time,
    city_no,
    city_name,
    load_mw,
    temp,
    humidity,
    weather_code,
    is_rain,
    shortwave_radiation,
    direct_radiation,
    diffuse_radiation,
    sunshine_duration,
    wind_level,
    wind_direction,
    wind_gusts,
    wind_speed,
    create_time,
    hour,
    day_of_week,
    is_holiday,

-- 前一小时所有字段
    LAG(temp, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_temp,
    LAG(humidity, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_humidity,
    LAG(weather_code, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_weather_code,
    LAG(is_rain, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_is_rain,
    LAG(shortwave_radiation, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_shortwave_radiation,
    LAG(wind_level, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_wind_level,
    LAG(wind_direction, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_wind_direction,
    LAG(wind_gusts, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_wind_gusts,
    LAG(wind_speed, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_wind_speed,

-- 后一小时温度（仅用于 temp 修复）
    LEAD(temp, 1) OVER (PARTITION BY city_no ORDER BY time) AS next_temp
FROM weather_data
    )
SELECT
    id,
    time,
    city_no,
    city_name,
    load_mw,

    -- 核心：仅当 temp 为 NULL 时，用前后一小时平均值修复；否则保持原值
    CASE
    WHEN temp IS NULL THEN (prev_temp + next_temp) / 2
    ELSE temp
END AS temp,

    -- 核心：仅当 temp 为 NULL 时，用前一小时的值覆盖；否则保持原值
    CASE WHEN temp IS NULL THEN prev_humidity ELSE humidity END AS humidity,
    CASE WHEN temp IS NULL THEN prev_weather_code ELSE weather_code END AS weather_code,
    CASE WHEN temp IS NULL THEN prev_is_rain ELSE is_rain END AS is_rain,
    CASE WHEN temp IS NULL THEN prev_shortwave_radiation ELSE shortwave_radiation END AS shortwave_radiation,
    direct_radiation,
    diffuse_radiation,
    sunshine_duration,
    CASE WHEN temp IS NULL THEN prev_wind_level ELSE wind_level END AS wind_level,
    CASE WHEN temp IS NULL THEN prev_wind_direction ELSE wind_direction END AS wind_direction,
    CASE WHEN temp IS NULL THEN prev_wind_gusts ELSE wind_gusts END AS wind_gusts,
    CASE WHEN temp IS NULL THEN prev_wind_speed ELSE wind_speed END AS wind_speed,

    hour,
    day_of_week,
    is_holiday,
    create_time

FROM data_with_prev_next
ORDER BY city_no, time DESC;