package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum APIErrorCode {

    //General error codes
    forbidden,
    invalid_input,
    json_error,
    locked,
    not_found,
    rate_limit_exceeded,
    resource_limit_exceeded,
    resource_unavailable,
    service_error,
    uniqueness_error,
    @JsonAlias("protected")
    _protected,
    maintenance,
    conflict,
    unsupported_error,
    token_readonly,
    unavailable,
    unauthorized,

    //Certificate related
    caa_record_does_not_allow_ca,
    ca_dns_validation_failed,
    ca_too_many_authorizations_failed_recently,
    ca_too_many_certificates_issued_for_registered_domain,
    ca_too_many_duplicate_certificates,
    could_not_verify_domain_delegated_to_zone,
    dns_zone_not_found,
    dns_zone_is_secondary_zone,

    //Firewall related
    server_already_added,
    incompatible_network_type,
    firewall_resource_not_found,
    resource_in_use,
    firewall_already_applied,
    firewall_already_removed,
    firewall_managed_by_label_selector,

    //Load Balancer related
    cloud_resource_ip_not_allowed,
    ip_not_owned,
    load_balancer_not_attached_to_network,
    robot_unavailable,
    server_not_attached_to_network,
    source_port_already_used,
    target_already_defined,
    load_balancer_already_attached,
    ip_not_available,
    no_subnet_available,
    invalid_load_balancer_type,
    targets_without_use_private_ip,

    //Primary IP related
    server_not_stopped,
    server_has_ipv4,
    server_has_ipv6,
    primary_ip_already_assigned,
    server_is_load_balancer_target,
    server_error,
    
    //Server related
    placement_error,
    primary_ip_assigned,
    primary_ip_datacenter_mismatch,
    primary_ip_version_mismatch,
    server_already_attached,
    networks_overlap,
    invalid_server_type,

    //Volume related
    no_space_left_in_location

}
