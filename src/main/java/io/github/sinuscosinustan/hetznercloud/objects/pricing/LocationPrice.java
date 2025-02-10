package io.github.sinuscosinustan.hetznercloud.objects.pricing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LocationPrice {

    private String location;
    @JsonProperty("price_hourly")
    private Price priceHourly;
    @JsonProperty("price_monthly")
    private Price priceMonthly;
}
