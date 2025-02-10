package io.github.sinuscosinustan.hetznercloud.objects.pricing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.enums.IPType;

import java.util.List;

@Data
public class Pricing {

    @JsonProperty("vat_rate")
    private Double vatRate;
    private String currency;
    @JsonProperty("floating_ip")
    private Pricing.FloatingIP floatingIP;
    @JsonProperty("floating_ips")
    private List<Pricing.FloatingIPs> floatingIPs;
    private Pricing.Image image;
    @JsonProperty("load_balancer_types")
    private List<Pricing.LoadBalancerType> loadBalancerTypes;
    @JsonProperty("primary_ips")
    private List<Pricing.PrimaryIP> primaryIPs;
    @JsonProperty("server_backup")
    private Pricing.ServerBackup serverBackup;
    @JsonProperty("server_types")
    private List<Pricing.ServerType> serverTypes;
    private Pricing.Traffic traffic;
    private Pricing.Volume volume;

    @Data
    private class FloatingIP {
        @JsonProperty("price_monthly")
        private Price priceMonthly;
    }

    @Data
    private class FloatingIPs {
        private IPType type;
        private List<LocationPrice> prices;
    }

    @Data
    private class Image {
        @JsonProperty("price_per_gb_month")
        private Price pricePerGBMonth;
    }

    @Data
    private class LoadBalancerType {
        private Long id;
        private String name;
        private List<LocationPrice> prices;
    }
    @Data
    private class PrimaryIP {
        private IPType type;
        private List<LocationPrice> prices;
    }

    @Data
    private class ServerBackup {
        private Double percentage;
    }

    @Data
    private class ServerType {
        private Long id;
        private String name;
        private List<LocationPrice> prices;
    }

    @Data
    private class Traffic {
        @JsonProperty("price_per_tb")
        private Price pricePerTB;
    }

    @Data
    private class Volume {
        @JsonProperty("price_per_gb_month")
        private Price pricePerGBMonth;
    }
}