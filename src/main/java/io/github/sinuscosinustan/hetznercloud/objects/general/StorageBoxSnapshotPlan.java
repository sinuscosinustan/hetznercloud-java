package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StorageBoxSnapshotPlan {

    private Boolean enabled;
    private String timezone;

    @JsonProperty("hour_of_day")
    private Integer hourOfDay;

    @JsonProperty("keep_hourly")
    private Integer keepHourly;

    @JsonProperty("keep_daily")
    private Integer keepDaily;

    @JsonProperty("keep_weekly")
    private Integer keepWeekly;

    @JsonProperty("keep_monthly")
    private Integer keepMonthly;
}
