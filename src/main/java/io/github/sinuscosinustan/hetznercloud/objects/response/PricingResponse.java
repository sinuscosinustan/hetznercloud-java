package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.pricing.Pricing;

@Data
public class PricingResponse {

    @JsonProperty("pricing")
    private Pricing pricing;
}
