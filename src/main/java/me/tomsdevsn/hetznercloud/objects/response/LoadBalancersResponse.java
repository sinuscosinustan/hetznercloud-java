package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.LoadBalancer;
import me.tomsdevsn.hetznercloud.objects.general.Meta;

import java.util.List;

@Data
public class LoadBalancersResponse {

    @JsonProperty("load_balancers")
    private List<LoadBalancer> loadBalancers;
    private Meta meta;
}
