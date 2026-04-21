UPDATE weather_data t1
    JOIN (
    WITH data_with_prev_next AS (
    SELECT
    id,
    temp,
    humidity,
    weather_code,
    is_rain,
    shortwave_radiation,
    wind_level,
    wind_direction,
    wind_gusts,
    wind_speed,

    LAG(temp, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_temp,
    LEAD(temp, 1) OVER (PARTITION BY city_no ORDER BY time) AS next_temp,
    LAG(humidity, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_humidity,
    LAG(weather_code, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_weather_code,
    LAG(is_rain, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_is_rain,
    LAG(shortwave_radiation, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_shortwave_radiation,
    LAG(wind_level, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_wind_level,
    LAG(wind_direction, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_wind_direction,
    LAG(wind_gusts, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_wind_gusts,
    LAG(wind_speed, 1) OVER (PARTITION BY city_no ORDER BY time) AS prev_wind_speed
    FROM weather_data
    )
    SELECT
    id,
    CASE WHEN temp IS NULL THEN (prev_temp + next_temp)/2 ELSE temp END AS new_temp,
    CASE WHEN temp IS NULL THEN prev_humidity ELSE humidity END AS new_humidity,
    CASE WHEN temp IS NULL THEN prev_weather_code ELSE weather_code END AS new_weather_code,
    CASE WHEN temp IS NULL THEN prev_is_rain ELSE is_rain END AS new_is_rain,
    CASE WHEN temp IS NULL THEN prev_shortwave_radiation ELSE shortwave_radiation END AS new_shortwave_radiation,
    CASE WHEN temp IS NULL THEN prev_wind_level ELSE wind_level END AS new_wind_level,
    CASE WHEN temp IS NULL THEN prev_wind_direction ELSE wind_direction END AS new_wind_direction,
    CASE WHEN temp IS NULL THEN prev_wind_gusts ELSE wind_gusts END AS new_wind_gusts,
    CASE WHEN temp IS NULL THEN prev_wind_speed ELSE wind_speed END AS new_wind_speed
    FROM data_with_prev_next
    ) t2 ON t1.id = t2.id
    SET
        t1.temp = t2.new_temp,
        t1.humidity = t2.new_humidity,
        t1.weather_code = t2.new_weather_code,
        t1.is_rain = t2.new_is_rain,
        t1.shortwave_radiation = t2.new_shortwave_radiation,
        t1.wind_level = t2.new_wind_level,
        t1.wind_direction = t2.new_wind_direction,
        t1.wind_gusts = t2.new_wind_gusts,
        t1.wind_speed = t2.new_wind_speed
WHERE t1.temp IS NULL; -- 只更新 temp 为 NULL 的行