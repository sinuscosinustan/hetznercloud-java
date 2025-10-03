package io.github.sinuscosinustan.hetznercloud;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sinuscosinustan.hetznercloud.exception.APIRequestException;
import io.github.sinuscosinustan.hetznercloud.objects.enums.ImageType;
import io.github.sinuscosinustan.hetznercloud.objects.enums.ActionStatus;
import io.github.sinuscosinustan.hetznercloud.objects.enums.Architecture;
import io.github.sinuscosinustan.hetznercloud.objects.enums.APIType;
import io.github.sinuscosinustan.hetznercloud.objects.general.FWApplicationTarget;
import io.github.sinuscosinustan.hetznercloud.objects.general.FirewallRule;
import io.github.sinuscosinustan.hetznercloud.objects.enums.PlacementGroupType;
import io.github.sinuscosinustan.hetznercloud.objects.pagination.PaginationParameters;
import io.github.sinuscosinustan.hetznercloud.objects.request.*;
import io.github.sinuscosinustan.hetznercloud.objects.response.*;
import io.github.sinuscosinustan.hetznercloud.util.VersionUtil;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HetznerCloudAPI {

    private static final String DEFAULT_USER_AGENT = "sinuscosinustan-hetznercloud-java/%s".formatted(VersionUtil.getLibraryVersion());

    private final String apiUrl;
    private final OkHttpClient client;
    private final String hcloudToken;
    private final String userAgent;
    private final ObjectMapper objectMapper;

    /**
     * Initial method to use the API with default Cloud API endpoint
     *
     * @param hcloudToken API-Token for Hetzner Cloud API
     * @see HetznerCloudAPI(String, OkHttpClient)
     */
    public HetznerCloudAPI(String hcloudToken) {
        this(hcloudToken, APIType.CLOUD);
    }

    /**
     * Initial method to use the API with default Cloud API endpoint
     *
     * @param hcloudToken API-Token for Hetzner Cloud API
     *              The API token can be created within the Hetzner Console
     * @param client OkHttpClient instance to be used
     */
    public HetznerCloudAPI(String hcloudToken, OkHttpClient client) {
        this(hcloudToken, APIType.CLOUD, client);
    }

    /**
     * Initial method to use the API with specified API type
     *
     * @param hcloudToken API-Token for Hetzner API
     * @param apiType Type of API to use (Cloud or Hetzner Online)
     */
    public HetznerCloudAPI(String hcloudToken, APIType apiType) {
        this(hcloudToken, apiType, new OkHttpClient());
    }

    /**
     * Initial method to use the API with custom base URL
     *
     * @param hcloudToken API-Token for Hetzner API
     * @param customApiUrl Custom API base URL
     */
    public HetznerCloudAPI(String hcloudToken, String customApiUrl) {
        this(hcloudToken, customApiUrl, new OkHttpClient(), null);
    }

    /**
     * Initial method to use the API with specified API type and custom client
     *
     * @param hcloudToken API-Token for Hetzner API
     * @param apiType Type of API to use (Cloud or Hetzner Online)
     * @param client OkHttpClient instance to be used
     */
    public HetznerCloudAPI(String hcloudToken, APIType apiType, OkHttpClient client) {
        this(hcloudToken, apiType.getBaseUrl(), client, null);
    }

    /**
     * Initial method to use the API with custom base URL and client
     *
     * @param hcloudToken API-Token for Hetzner API
     * @param customApiUrl Custom API base URL
     * @param client OkHttpClient instance to be used
     */
    public HetznerCloudAPI(String hcloudToken, String customApiUrl, OkHttpClient client) {
        this(hcloudToken, customApiUrl, client, null);
    }

    /**
     * Initial method to use the API with custom base URL, client and user agent prefix
     *
     * @param hcloudToken API-Token for Hetzner API
     * @param customApiUrl Custom API base URL
     * @param client OkHttpClient instance to be used
     * @param userAgentPrefix Prefix that should be added in front of the default user agent
     */
    private HetznerCloudAPI(String hcloudToken, String apiUrl, OkHttpClient client, String userAgentPrefix) {
        if (hcloudToken == null || hcloudToken.isBlank()) {
            throw new RuntimeException("no Hetzner cloud token provided");
        }
        if (apiUrl == null || apiUrl.isBlank()) {
            throw new RuntimeException("API URL cannot be null or blank");
        }

        this.hcloudToken = hcloudToken;
        this.apiUrl = apiUrl;
        this.client = client != null ? client : new OkHttpClient();
        this.userAgent = buildUserAgent(userAgentPrefix);

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static String buildUserAgent(String userAgentPrefix) {
        if (userAgentPrefix != null && !userAgentPrefix.isBlank()) {
            return userAgentPrefix + " " + DEFAULT_USER_AGENT;
        }
        return DEFAULT_USER_AGENT;
    }

    /**
     * Get all actions in a project.
     *
     * @deprecated This function has been deprecated by Hetzner
     * @return All Actions without pagination
     */
    @Deprecated
    public ActionsResponse getActions() {
        return getActions(null, new PaginationParameters(null, null));
    }

    /**
     * Get all action in a project filtered by its status.
     *
     * @deprecated This function has been deprecated by Hetzner
     * @param actionStatus Action status type
     * @return ActionsResponse containing all actions without pagination filtered by its status
     */
    @Deprecated
    public ActionsResponse getActions(ActionStatus actionStatus) {
        return getActions(actionStatus, new PaginationParameters(null, null));
    }

    /**
     * Get all actions in a project.
     *
     * @deprecated This function has been deprecated by Hetzner
     * @param actionStatus Query only actions with the specified status
     * @param paginationParameters Pagination parameters
     * @return ActionsResponse
     */
    @Deprecated
    public ActionsResponse getActions(ActionStatus actionStatus, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/actions")
                        .queryParamIfPresent("status", Optional.ofNullable(actionStatus))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                ActionsResponse.class);
    }

    /**
     * Get an action by id.
     *
     * @param id ID of the action
     * @return ActionResponse
     */
    public ActionResponse getAction(long id) {
        return get(
                apiUrl + "/actions/" + id,
                ActionResponse.class);
    }

    /**
     * Get all servers in a project.
     *
     * @return All servers as Servers object without pagination
     */
    public ServersResponse getServers() {
        return getServers(null, new PaginationParameters(null, null));
    }

    /**
     * Get all servers in a project filtered by a label selector
     *
     * @param labelSelector Label selector
     * @return ServersResponse containing all servers which match the label selector
     */
    public ServersResponse getServers(String labelSelector) {
        return getServers(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all servers in a project.
     *
     * @param labelSelector Label selector filter
     * @param paginationParameters Pagination parameters
     * @return All servers as Servers object
     */
    public ServersResponse getServers(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/servers")
                        .queryParamIfPresent("label_selector", Optional.ofNullable(labelSelector))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                ServersResponse.class);
    }

    /**
     * Get servers by name.
     *
     * @param name Name of the server
     * @return Matching servers as Servers object
     */
    public ServersResponse getServer(String name) {
        return get(
                UrlBuilder.from(apiUrl + "/servers")
                        .queryParam("name", name)
                        .toUri(),
                ServersResponse.class);
    }

    /**
     * Get a server by id
     *
     * @param id id of the server
     * @return GetServerResponse
     */
    public ServerResponse getServer(long id) {
        return get(
                apiUrl + "/servers/" + id,
                ServerResponse.class);
    }

    /**
     * Create a server.
     *
     * @param createServerRequest Parameters for server creation.
     * @return ServerResponse including Action status, Server object and (if no ssh key defined) root password.
     */
    public CreateServerResponse createServer(CreateServerRequest createServerRequest) {
        createServerRequest.setServerType(createServerRequest.getServerType().toLowerCase());   // Case-sensitive fix
        return post(
                apiUrl + "/servers",
                createServerRequest,
                CreateServerResponse.class);
    }

    /**
     * Delete a server
     *
     * @param id id of the server.
     * @return ActionResponse object
     */
    public ActionResponse deleteServer(long id) {
        return delete(
                apiUrl + "/servers/" + id,
                ActionResponse.class);
    }

    /**
     * Update a server's name and its labels.
     *
     * @param id            id of the server.
     * @param updateServerRequest request
     * @return ServerResponse object
     */
    public ServerResponse updateServer(long id, UpdateServerRequest updateServerRequest) {
        return put(
                apiUrl + "/servers/" + id,
                updateServerRequest,
                ServerResponse.class);
    }

    /**
     * Request a WebSocket URL for a server.
     *
     * @param id id of the server
     * @return ConsoleResponse object
     */
    public ConsoleResponse requestConsole(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/request_console",
                ConsoleResponse.class);
    }

    /**
     * Change the protection configuration of a server.
     *
     * @param id               id of the server
     * @param changeProtection Request Object (both optional)
     * @return ActionResponse object
     */
    public ActionResponse changeServerProtection(long id, ChangeProtectionRequest changeProtection) {
        return post(
                apiUrl + "/servers/" + id + "/actions/change_protection",
                changeProtection,
                ActionResponse.class);
    }

    /**
     * Add a server to a placement group.
     * Server has to be stopped.
     *
     * @param serverId         server id
     * @param placementGroupId placement group id
     * @return ActionResponse
     */
    public ActionResponse addServerToPlacementGroup(long serverId, long placementGroupId) {
        return post(
                apiUrl + "/servers/" + serverId + "/actions/add_to_placement_group",
                new PlacementGroupAddServerRequest(placementGroupId),
                ActionResponse.class);
    }

    /**
     * Remove a server from a placement group.
     *
     * @param serverId server id
     * @return ActionResponse
     */
    public ActionResponse removeServerFromPlacementGroup(long serverId) {
        return post(
                apiUrl + "/servers/" + serverId + "/actions/remove_from_placement_group",
                ActionResponse.class);
    }

    /**
     * Get all performed Actions of a server.
     *
     * @param id id of the server
     * @return ActionsResponse object
     */
    public ActionsResponse getServerActions(long id) {
        return get(
                apiUrl + "/servers/" + id + "/actions",
                ActionsResponse.class);
    }

    /**
     * Get all performed Actions of a Floating IP
     *
     * @param id ID of the FloatingIP
     * @return ActionsResponse object
     */
    public ActionsResponse getFloatingIPActions(long id) {
        return get(apiUrl + "/floating_ips/" + id + "/actions",
                ActionsResponse.class);
    }

    /**
     * Power on a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse powerOnServer(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/poweron",
                ActionResponse.class);
    }

    /**
     * Force power off a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse powerOffServer(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/poweroff",
                ActionResponse.class);
    }

    /**
     * Reboot a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse rebootServer(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/reboot",
                ActionResponse.class);
    }

    /**
     * Reset a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse resetServer(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/reset",
                ActionResponse.class);
    }

    /**
     * Shutdown a specific server via ACPI with the id
     *
     * @param id ID of the server
     * @return respond
     */
    public ActionResponse shutdownServer(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/shutdown",
                ActionResponse.class);
    }

    /**
     * Resets the root password from a specific server with the id
     *
     * @param id ID of the server
     * @return respond
     */
    public ResetRootPasswordResponse resetRootPassword(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/reset_password",
                ResetRootPasswordResponse.class);
    }

    /**
     * Enables the rescue mode from the server
     *
     * @param id ID of the server
     * @return respond
     */
    public EnableRescueResponse enableRescue(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/enable_rescue",
                EnableRescueResponse.class);
    }

    /**
     * Enables the rescue mode from the server
     *
     * @param id                  ID of the server
     * @param enableRescueRequest Request object
     * @return respond
     */
    public EnableRescueResponse enableRescue(long id, EnableRescueRequest enableRescueRequest) {
        return post(
                apiUrl + "/servers/" + id + "/actions/enable_rescue",
                enableRescueRequest,
                EnableRescueResponse.class);
    }

    /**
     * Disables the rescue mode from the server.
     * <p>
     * Only needed, if the server doesn't booted into the rescue mode.
     *
     * @param id ID of the server
     * @return respond
     */
    public ActionResponse disableRescue(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/disable_rescue",
                ActionResponse.class);
    }

    /**
     * Rebuild a server, with the specific image.
     * <p>
     * example: ubuntu-16.04
     *
     * @param id                   ID of the server
     * @param rebuildServerRequest Request object
     * @return respond
     */
    public RebuildServerResponse rebuildServer(long id, RebuildServerRequest rebuildServerRequest) {
        return post(
                apiUrl + "/servers/" + id + "/actions/rebuild",
                rebuildServerRequest,
                RebuildServerResponse.class);
    }

    /**
     * Change the type from the server
     * <p>
     * example: from cx11 to cpx21
     *
     * @param id                ID of the server
     * @param changeTypeRequest Request object
     * @return respond
     */
    public ActionResponse changeServerType(long id, ChangeTypeRequest changeTypeRequest) {
        return post(
                apiUrl + "/servers/" + id + "/actions/change_type",
                changeTypeRequest,
                ActionResponse.class);
    }

    /**
     * Get the metrics from a server
     *
     * @param id         ID of the server
     * @param metricType like cpu, disk or network (but also cpu,disk possible)
     * @param start      of the metric
     * @param end        of the metric
     * @return respond
     */
    public MetricsResponse getServerMetrics(long id, String metricType, String start, String end) {
        return get(
                UrlBuilder.from(
                        apiUrl + "/servers/" + id + "/metrics")
                        .queryParam("type", metricType)
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .toUri(),
                MetricsResponse.class);
    }

    /**
     * Create an image from a server
     *
     * @param id                 ID of the server
     * @param createImageRequest Request object
     * @return respond
     */
    public CreateImageResponse createImage(long id, CreateImageRequest createImageRequest) {
        return post(
                apiUrl + "/servers/" + id + "/actions/create_image",
                createImageRequest,
                CreateImageResponse.class);
    }


    /**
     * Enable or disable the Protection of an Image
     *
     * @param id                ID of the image
     * @param protectionRequest Only the delete parameter!
     * @return ActionResponse object
     */
    public ActionResponse changeImageProtection(long id, ChangeProtectionRequest protectionRequest) {
        return post(
                apiUrl + "/images/" + id + "/actions/change_protection",
                protectionRequest,
                ActionResponse.class);
    }

    /**
     * Enable the backups from a server
     *
     * Please note that this action will increase the price of the server by 20%
     *
     * @param id ID of the server
     * @return response
     */
    public ActionResponse enableBackup(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/enable_backup",
                ActionResponse.class);
    }

    /**
     * Get all available ISO's.
     *
     * @return ISOSResponse
     */
    public ISOSResponse getISOS() {
        return getISOS(null, new PaginationParameters(null, null));
    }


    /**
     * Get all available ISO's by architecture.
     *
     * @param architecture {@link Architecture}
     * @return {@link ISOSResponse}
     */
    public ISOSResponse getISOS(Architecture architecture) {
        return getISOS(architecture, new PaginationParameters(null, null));
    }

    /**
     * Get all available ISO's with pagination.
     * @param paginationParameters Pagination
     * @return {@link ISOSResponse}
     */
    public ISOSResponse getISOS(PaginationParameters paginationParameters) {
        return getISOS(null, new PaginationParameters(null, null));
    }

    /**
     * Get all available ISO's.
     *
     * @param architecture {@link Architecture}
     * @param paginationParameters Pagination parametres
     * @return {@link ISOSResponse}
     */
    public ISOSResponse getISOS(Architecture architecture, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/isos")
                        .queryParamIfPresent("architecture", Optional.ofNullable(architecture))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                ISOSResponse.class);
    }

    /**
     * Get an ISO by ID
     *
     * @param id ID of the ISO
     * @return ISOResponse Object
     */
    public ISOResponse getISO(long id) {
        return get(
                apiUrl + "/isos/" + id,
                ISOResponse.class);
    }

    /**
     * Attach an ISO to a server.
     * <p>
     * To get all ISO's {@link #getISOS}
     *
     * @param id               of the server
     * @param attachISORequest Request object
     * @return ActionResponse object
     */
    public ActionResponse attachISO(long id, AttachISORequest attachISORequest) {
        return post(
                apiUrl + "/servers/" + id + "/actions/attach_iso",
                attachISORequest,
                ActionResponse.class);
    }

    /**
     * Detach an ISO from a server.
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse detachISO(long id) {
        return post(
                apiUrl + "/servers/" + id + "/actions/detach_iso",
                ActionResponse.class);
    }

    /**
     * Changes the reverse DNS entry from a server.
     * <p>
     * Floating IPs assigned to the server are not affected!
     *
     * @param id                      ID of the server
     * @param changeReverseDNSRequest Request object
     * @return respond
     */
    public ActionResponse changeDNSPTR(long id, ChangeReverseDNSRequest changeReverseDNSRequest) {
        return post(
                apiUrl + "/servers/" + id + "/actions/change_dns_ptr",
                changeReverseDNSRequest,
                ActionResponse.class);
    }

    /**
     * Get a Datacenter by ID
     *
     * @param id of the Datacenter
     * @return respond
     */
    public DatacenterResponse getDatacenter(long id) {
        return get(
                apiUrl + "/datacenters/" + id,
                DatacenterResponse.class);
    }

    /**
     * Get all available datacenters and the recommendation
     *
     * @return respond
     */
    public DatacentersResponse getDatacenters() {
        return get(
                apiUrl + "/datacenters",
                DatacentersResponse.class);
    }

    /**
     * Get a datacenter by name
     *
     * @param name of the datacenter
     * @return DatacentersResponse
     */
    public DatacentersResponse getDatacenter(String name) {
        return get(
                UrlBuilder.from(apiUrl + "/datacenters")
                        .queryParam("name", name)
                        .toUri(),
                DatacentersResponse.class);
    }

    /**
     * Returns 25 Firewall objects.
     *
     * @return a FirewallsResponse containing all Firewalls of the requested page and paging metadata
     * @see #getFirewalls(String, PaginationParameters)
     */
    public FirewallsResponse getFirewalls() {
        return getFirewalls(null, new PaginationParameters(null, null));
    }

    /**
     * Get all Firewalls in a project by label selector.
     *
     * @param labelSelector Label Selector
     * @return FirewallsResponse
     * @see #getFirewalls(String, PaginationParameters)
     */
    public FirewallsResponse getFirewalls(String labelSelector) {
        return getFirewalls(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all Firewalls in a project.
     *
     * @param paginationParameters Pagination parametres
     * @return FirewallsResponse
     * @see #getFirewalls(String, PaginationParameters)
     */
    public FirewallsResponse getFirewalls(PaginationParameters paginationParameters) {
        return getFirewalls(null, paginationParameters);
    }

    /**
     * Returns all Firewall objects.
     *
     * @param paginationParameters
     * @return a FirewallsResponse containing all Firewalls of the requested page and paging metadata
     */
    public FirewallsResponse getFirewalls(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/firewalls")
                        .queryParamIfPresent("label_selector", Optional.ofNullable(labelSelector))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                FirewallsResponse.class);
    }

    /**
     * Creates a new Firewall.
     *
     * @param createFirewallRequest the config of the Firewall you want to create
     * @return a FirewallResponse containing the created Firewall and taken Actions
     */
    public CreateFirewallResponse createFirewall(CreateFirewallRequest createFirewallRequest) {
        return post(
                apiUrl + "/firewalls",
                createFirewallRequest,
                CreateFirewallResponse.class);
    }

    /**
     * Deletes a Firewall.
     *
     * @param id
     */
    public void deleteFirewall(long id) {
        delete(
                apiUrl + "/firewalls/" + id,
                String.class);
    }

    /**
     * Gets a specific Firewall.
     *
     * @param id
     * @return the FirewallResponse containing the searched Firewall
     */
    public CreateFirewallResponse getFirewall(long id) {
        return get(
                apiUrl + "/firewalls/" + id,
                CreateFirewallResponse.class);
    }

    /**
     * Updates the Firewall. This replaces the current labels with the given
     *
     * @param id
     * @param updateFirewallRequest the changes you want to perform
     * @return the FirewallResponse of the request, containing the new Firewall and Metadata
     */
    public CreateFirewallResponse updateFirewall(long id, UpdateFirewallRequest updateFirewallRequest) {
        return put(
                apiUrl + "/firewalls/" + id,
                updateFirewallRequest,
                CreateFirewallResponse.class);
    }

    /**
     * Returns all Action objects for a Firewall.
     *
     * @param id
     * @return an ActionsResponse with the executed actions
     */
    public ActionsResponse getFirewallActions(long id) {
        return get(
                String.format("%s/firewalls/%s/actions", apiUrl, id),
                ActionsResponse.class);
    }

    /**
     * Applies one Firewall to multiple resources.
     *
     * @param id of the firewall you want to add to resources
     * @param applicationTargets you want to add
     * @return an ActionsResponse with the executed actions
     */
    public ActionsResponse applyFirewallToResources(long id, List<FWApplicationTarget> applicationTargets) {
        return post(
                apiUrl + "/firewalls/" + id + "/actions/apply_to_resources",
                Map.of("apply_to", applicationTargets),
                ActionsResponse.class);
    }

    /**
     * Removes one Firewall from multiple resources.
     *
     * @param id of the firewall you want to remove resources from
     * @param removalTargets you want to remove
     * @return an ActionsResponse with the executed actions
     */
    public ActionsResponse removeFirewallFromResources(long id, List<FWApplicationTarget> removalTargets) {
        return post(
                apiUrl + "/firewalls/" + id + "/actions/remove_from_resources",
                Map.of("remove_from", removalTargets),
                ActionsResponse.class);
    }

    /**
     * Removes all rules of a Firewall.
     *
     * @param id the firewall you want to remove the rules from
     * @return an ActionsResponse with the executed actions
     * @see #setFirewallRules(long, List)
     */
    public ActionsResponse removeAllRulesFromFirewall(long id) {
        return setFirewallRules(id, Collections.emptyList());
    }

    /**
     * Sets the rules of a Firewall. All existing rules will be overwritten.
     * If the firewallRules are empty, all rules are deleted.
     *
     * @param id of the Firewall you want to set the Rules on.
     * @param firewallRules you want to set.
     * @return an ActionsResponse with the executed actions
     */
    public ActionsResponse setFirewallRules(long id, List<FirewallRule> firewallRules) {
        return post(
                apiUrl + "/firewalls/" + id + "/actions/set_rules",
                Map.of("rules", firewallRules),
                ActionsResponse.class);
    }

    /**
     * Get all prices from the products
     *
     * @return PricingResponse
     */
    public PricingResponse getPricing() {
        return get(
                apiUrl + "/pricing",
                PricingResponse.class);
    }

    /**
     * Get all Primary IPs in a project
     *
     * @return PrimaryIPsResponse
     */
    public PrimaryIPsResponse getPrimaryIPs() {
        return getPrimaryIPs(null, new PaginationParameters(null, null));
    }

    /**
     * Get all Primary IPs in a project by label selector
     *
     * @param labelSelector Label selector
     * @return PrimaryIPsResponse
     */
    public PrimaryIPsResponse getPrimaryIPs(String labelSelector) {
        return getPrimaryIPs(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all Primary IPs in a project
     *
     * @param labelSelector Label selector
     * @param paginationParameters Pagination parametres
     * @return PrimaryIPsResponse
     */
    public PrimaryIPsResponse getPrimaryIPs(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/primary_ips")
                        .queryParamIfPresent("label_selector", Optional.ofNullable(labelSelector))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                PrimaryIPsResponse.class);
    }

    /**
     * Get a Primary IP by its name in a project
     *
     * @param name Name of the Primary IP
     * @return PrimaryIPsResponse
     */
    public PrimaryIPsResponse getPrimaryIPByName(String name) {
        return get(
                UrlBuilder.from(apiUrl + "/primary_ips")
                        .queryParam("name", name)
                        .toUri(),
                PrimaryIPsResponse.class);
    }

    /**
     * Get a Primary IP by the IP address itself
     *
     * @param ip IP address
     * @return PrimaryIPsResponse
     */
    public PrimaryIPsResponse getPrimaryIP(String ip) {
        return get(
                UrlBuilder.from(apiUrl + "/primary_ips")
                        .queryParam("ip", ip)
                        .toUri(),
                PrimaryIPsResponse.class);
    }

    /**
     * Get a Primary IP by its id
     *
     * @param id id of the Primary IP
     * @return PrimaryIPResponse
     */
    public PrimaryIPResponse getPrimaryIP(long id) {
        return get(
                apiUrl + "/primary_ips/" + id,
                PrimaryIPResponse.class);
    }

    /**
     * Create a Primary IP
     *
     * @param createPrimaryIPRequest Primary IP request
     * @return CreatePrimaryIPResponse
     */
    public CreatePrimaryIPResponse createPrimaryIP(CreatePrimaryIPRequest createPrimaryIPRequest) {
        return post(
                apiUrl + "/primary_ips",
                createPrimaryIPRequest,
                CreatePrimaryIPResponse.class);
    }

    /**
     * Update a Primary IP
     * @param updatePrimaryIPRequest Primary IP Update request
     * @return PrimaryIPResponse
     */
    public PrimaryIPResponse updatePrimaryIP(long id, UpdatePrimaryIPRequest updatePrimaryIPRequest) {
        return put(
                apiUrl + "/primary_ips/" + id,
                updatePrimaryIPRequest,
                PrimaryIPResponse.class);
    }

    /**
     * Assign a Primary IP to a resource.
     *
     * @param id id of the Primary IP
     * @param assignPrimaryIPRequest Primary IP Resource Assignment request
     * @return ActionResponse
     */
    public ActionResponse assignPrimaryIP(long id, AssignPrimaryIPRequest assignPrimaryIPRequest) {
        return post(
                apiUrl + "/primary_ips/" + id + "/actions/assign",
                assignPrimaryIPRequest,
                ActionResponse.class);
    }

    /**
     * Unassign a Primary IP from a resource.
     *
     * @param id id of the Primary IP
     * @return ActionResponse
     */
    public ActionResponse unassignPrimaryIP(long id) {
        return post(
                apiUrl + "/primary_ips/" + id + "/actions/unassign",
                ActionResponse.class);
    }

    /**
     * Update a reverse DNS entry for a Primary IP
     *
     * @param id id of the Primary IP
     * @param changeReverseDNSRequest Reverse DNS update change
     * @return ActionResponse
     */
    public ActionResponse changePrimaryIPReverseDNS(long id, ChangeReverseDNSRequest changeReverseDNSRequest) {
        return post(
                apiUrl + "/primary_ips/" + id + "/actions/change_dns_ptr",
                changeReverseDNSRequest,
                ActionResponse.class);
    }

    public ActionResponse changePrimaryIPProtection(long id, ChangeProtectionRequest changeProtectionRequest) {
        return post(
                apiUrl + "/primary_ips/" + id + "/actions/change_protection",
                changeProtectionRequest,
                ActionResponse.class);
    }

    /**
     * Delete a Primary IP
     *
     * @param id id of the Primary IP
     * @return nothing
     */
    public String deletePrimaryIP(Long id) {
        return delete(
                apiUrl + "/primary_ips/" + id,
                String.class);
    }

    /**
     * Get all Floating IPs in a project.
     *
     * @return FloatingIPsResponse
     */
    public FloatingIPsResponse getFloatingIPs() {
        return getFloatingIPs(new PaginationParameters(null, null));
    }

    /**
     * Get all Floating IPs in a project.
     *
     * @param paginationParameters Pagination parametres
     * @return FloatingIPsResponse
     */
    public FloatingIPsResponse getFloatingIPs(PaginationParameters paginationParameters) {
        return getFloatingIPs(null, paginationParameters);
    }

    /**
     * Get all Floating IPs in a project by label selector.
     * A label selector can be e.g. env=prod
     *
     * @param labelSelector Label selector
     * @return FloatingIPsResponse
     */
    public FloatingIPsResponse getFloatingIPs(String labelSelector) {
        return getFloatingIPs(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all Floating IPs in a project.
     * A label selector can be e.g. env=prod
     *
     * @param labelSelector        Label selector
     * @param paginationParameters Pagination parametres
     * @return FloatingIPsResponse
     */
    public FloatingIPsResponse getFloatingIPs(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/floating_ips")
                        .queryParamIfPresent("label_selector", Optional.ofNullable(labelSelector))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                FloatingIPsResponse.class);
    }

    /**
     * Get a specific Floating IP.
     *
     * @param id ID of the Floating IP
     * @return GetFloatingIPResponse
     */
    public FloatingIPResponse getFloatingIP(long id) {
        return get(
                apiUrl + "/floating_ips/" + id,
                FloatingIPResponse.class);
    }

    /**
     * Create a Floating IP for the project or for a Server.
     *
     * @param createFloatingIPRequest Request object
     * @return FloatingIPResponse object
     */
    public CreateFloatingIPResponse createFloatingIP(CreateFloatingIPRequest createFloatingIPRequest) {
        return post(
                apiUrl + "/floating_ips",
                createFloatingIPRequest,
                CreateFloatingIPResponse.class);
    }

    /**
     * Enable or disable the Protection of a Floating IP
     *
     * @param id                ID of the Floating IP
     * @param protectionRequest Only the delete parameter!
     * @return ActionResponse object
     */
    public ActionResponse changeFloatingIPProtection(long id, ChangeProtectionRequest protectionRequest) {
        return post(
                apiUrl + "/floating_ips/" + id + "/actions/change_protection",
                protectionRequest,
                ActionResponse.class);
    }

    /**
     * Assign a Floating IP to a server
     *
     * @param id                      ID of the Floating IP
     * @param assignFloatingIPRequest Request object
     * @return ActionResponse object
     */
    public ActionResponse assignFloatingIP(long id, AssignFloatingIPRequest assignFloatingIPRequest) {
        return post(
                apiUrl + "/floating_ips/" + id + "/actions/assign",
                assignFloatingIPRequest,
                ActionResponse.class);
    }

    /**
     * Unassign a Floating IP from a server
     *
     * @param id ID of the Floating IP
     * @return ActionResponse object
     */
    public ActionResponse unassignFloatingIP(long id) {
        return post(
                apiUrl + "/floating_ips/" + id + "/actions/unassign",
                ActionResponse.class);
    }

    /**
     * Change the reverse DNS entry for a Floating IP
     *
     * @param id                      ID of the Floating IP
     * @param changeReverseDNSRequest Request object
     * @return ActionResponse object
     */
    public ActionResponse changeFloatingReverseDNS(long id, ChangeReverseDNSRequest changeReverseDNSRequest) {
        return post(
                apiUrl + "/floating_ips/" + id + "/actions/change_dns_ptr",
                changeReverseDNSRequest,
                ActionResponse.class);
    }

    /**
     * Delete a Floating IP.
     * <p>
     * This object does not have a respond!
     *
     * @param id ID of the Floating ID
     * @return String
     */
    public String deleteFloatingIP(long id) {
        return delete(
                apiUrl + "/floating_ips/" + id,
                String.class);
    }

    /**
     * Update the description or labels of a Floating IP.
     *
     * @param id                      ID of the Floating IP
     * @param updateFloatingIPRequest Request Object
     * @return Response from API (Action will be null)
     */
    public CreateFloatingIPResponse updateFloatingIP(long id, UpdateFloatingIPRequest updateFloatingIPRequest) {
        return put(
                apiUrl + "/floating_ips/" + id,
                updateFloatingIPRequest,
                CreateFloatingIPResponse.class);
    }

    /**
     * Get all SSH keys.
     *
     * @return SSHKeysResponse
     */
    public SSHKeysResponse getSSHKeys() {
        return getSSHKeys(new PaginationParameters(null, null));
    }

    /**
     * Get all SSH keys.
     *
     * @param paginationParameters Pagination parameters
     * @return SSHKeysResponse
     */
    public SSHKeysResponse getSSHKeys(PaginationParameters paginationParameters) {
        return getSSHKeys(null, paginationParameters);
    }

    /**
     * Get all SSH keys by label.
     *
     * @param labelSelector Label selector
     * @return SSHKeysResponse
     */
    public SSHKeysResponse getSSHKeys(String labelSelector) {
        return getSSHKeys(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all SSH keys by label.
     *
     * @param labelSelector Label selector
     * @param paginationParameters Pagination parameters
     * @return SSHKeysResponse
     */
    public SSHKeysResponse getSSHKeys(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/ssh_keys")
                        .queryParamIfPresent("label_selector", Optional.ofNullable(labelSelector))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                SSHKeysResponse.class);
    }

    /**
     * Get an SSH key by ID.
     *
     * @param id ID of the SSH key
     * @return SSHKeyResponse
     */
    public SSHKeyResponse getSSHKey(long id) {
        return get(
                apiUrl + "/ssh_keys/" + id,
                SSHKeyResponse.class);
    }

    /**
     * Get an SSH key by name.
     *
     * @param name name of the SSH key
     * @return SSHKeysResponse object
     */
    public SSHKeysResponse getSSHKey(String name) {
        return get(
                UrlBuilder.from(apiUrl + "/ssh_keys")
                        .queryParam("name", name)
                        .toUri(),
                SSHKeysResponse.class);
    }

    /**
     * Get a SSH key by the fingerprint.
     *
     * @param fingerprint Fingerprint of the SSH key
     * @return SSHKeysResponse object
     */
    public SSHKeysResponse getSSHKeyByFingerprint(String fingerprint) {
        return get(
                UrlBuilder.from(apiUrl + "/ssh_keys")
                        .queryParam("fingerprint", fingerprint)
                        .toUri(),
                SSHKeysResponse.class);
    }

    /**
     * Create an SSH key.
     *
     * @param createSshKeyRequest Request object
     * @return SSHKeyResponse object
     */
    public SSHKeyResponse createSSHKey(CreateSSHKeyRequest createSshKeyRequest) {
        return post(
                apiUrl + "/ssh_keys",
                createSshKeyRequest,
                SSHKeyResponse.class);
    }

    /**
     * Update parameters of an SSH key
     *
     * @param id                  ID of the SSH key
     * @param updateSSHKeyRequest Request Object
     * @return SSHKeyResponse object
     */
    public SSHKeyResponse updateSSHKey(long id, UpdateSSHKeyRequest updateSSHKeyRequest) {
        return put(
                apiUrl + "/ssh_keys/" + id,
                updateSSHKeyRequest,
                SSHKeyResponse.class);
    }

    /**
     * Delete an SSH key.
     * <p>
     * This object does not have a response!
     *
     * @param id ID of the SSH key
     * @return String
     */
    public String deleteSSHKey(long id) {
        return delete(
                apiUrl + "/ssh_keys/" + id,
                String.class);
    }

    /**
     * Get all Server types.
     *
     * @return ServerTypesResponse object
     */
    public ServerTypesResponse getServerTypes() {
        return get(
                apiUrl + "/server_types",
                ServerTypesResponse.class);
    }

    /**
     * Get Server types Paginated.
     *
     * @return ServerTypesResponse object
     */
    public ServerTypesResponse getServerTypes(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/server_types")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                ServerTypesResponse.class);
    }

    /**
     * Get all Load Balancer types.
     *
     * @return LoadBalancerTypesResponse object
     */
    public LoadBalancerTypesResponse getLoadBalancerTypes() {
        return get(
                apiUrl + "/load_balancer_types",
                LoadBalancerTypesResponse.class);
    }

    /**
     * Get Load Balancer type by name.
     *
     * @param name Name of the Load Balancer type
     * @return LoadBalancerTypesResponse object
     */
    public LoadBalancerTypesResponse getLoadBalancerTypeByName(String name) {
        return get(
                UrlBuilder.from(apiUrl + "/load_balancer_types")
                        .queryParam("name", name)
                        .toUri(),
                LoadBalancerTypesResponse.class);
    }

    /**
     * Get a Load Balancer type by id.
     *
     * @param id ID of the load balancer type
     * @return LoadBalancerTypeResponse
     */
    public LoadBalancerTypeResponse getLoadBalancerType(long id) {
        return get(String.format("%s/load_balancer_types/%s", apiUrl, id), LoadBalancerTypeResponse.class);
    }

    /**
     * Get a Server type by name.
     *
     * @param name name of the Server type
     * @return ServerTypesResponse object
     */
    public ServerTypesResponse getServerTypeByName(String name) {
        return get(
                UrlBuilder.from(apiUrl + "/server_types")
                        .queryParam("name", name)
                        .toUri(),
                ServerTypesResponse.class);
    }

    /**
     * Get a Server type by id.
     *
     * @param id id of the Server type
     * @return ServerTypeResponse object
     */
    public ServerTypeResponse getServerType(long id) {
        return get(
                apiUrl + "/server_types/" + id,
                ServerTypeResponse.class);
    }

    /**
     * Get all available Locations.
     *
     * @return LocationsResponse object
     */
    public LocationsResponse getLocations() {
        return get(
                apiUrl + "/locations",
                LocationsResponse.class);
    }

    /**
     * Get a Location by name.
     *
     * @param name Name of the location
     * @return LocationsResponse object
     */
    public LocationsResponse getLocationByName(String name) {
        return get(
                UrlBuilder.from(apiUrl + "/locations")
                        .queryParam("name", name)
                        .toUri(),
                LocationsResponse.class);
    }

    /**
     * Get a location by id.
     *
     * @param id id of the location
     * @return LocationResponse object
     */
    public LocationResponse getLocation(long id) {
        return get(
                apiUrl + "/locations/" + id,
                LocationResponse.class);
    }

    /**
     * Get all available images.
     *
     * @return ImagesResponse object
     */
    public ImagesResponse getImages() {
        return getImages(null, null, new PaginationParameters(null, null));
    }

    /**
     * Get all images by label selector.
     *
     * @param labelSelector Label Selector
     * @return {@link ImagesResponse}
     */
    public ImagesResponse getImages(String labelSelector) {
        return getImages(labelSelector, null, new PaginationParameters(null, null));
    }

    /**
     * Get all images by architecture.
     *
     * @param architecture Architecture of the Image
     * @return {@link ImagesResponse}
     */
    public ImagesResponse getImages(Architecture architecture) {
        return getImages(null, architecture, new PaginationParameters(null, null));
    }

    /**
     * Get all images by label selector and architecture.
     *
     * @param labelSelector Label Selector
     * @param architecture Architecture of the Image
     * @return {@link ImagesResponse}
     */
    public ImagesResponse getImages(String labelSelector, Architecture architecture) {
        return getImages(labelSelector, architecture, new PaginationParameters(null, null));
    }

    /**
     * Get all images by label selector with pagination.
     *
     * @param labelSelector Label Selector
     * @param paginationParameters Pagination parametres
     * @return {@link ImagesResponse}
     */
    public ImagesResponse getImages(String labelSelector, PaginationParameters paginationParameters) {
        return getImages(labelSelector, null, paginationParameters);
    }

    /**
     * Get all images by architecture with pagination.
     *
     * @param architecture Architecture
     * @param paginationParameters Pagination parametres
     * @return {@link ImagesResponse}
     */
    public ImagesResponse getImages(Architecture architecture, PaginationParameters paginationParameters) {
        return getImages(null, architecture, paginationParameters);
    }

    /**
     * Get all available images.
     *
     * @param labelSelector Label selector
     * @param architecture Architecture of the image
     * @param paginationParameters Pagination parametres
     * @return {@link ImagesResponse}
     */
    public ImagesResponse getImages(String labelSelector, Architecture architecture, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/images")
                        .queryParamIfPresent("label_selector", Optional.ofNullable(labelSelector))
                        .queryParamIfPresent("architecture", Optional.ofNullable(architecture))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                ImagesResponse.class);
    }

    /**
     * Get all images by type.
     *
     * @param type Type of image
     * @return ImagesResponse object
     */
    public ImagesResponse getImagesByType(ImageType type) {
        return getImagesByType(type, new PaginationParameters(null, null));
    }

    /**
     * Get all images by type.
     *
     * @param type                 Type of image
     * @param paginationParameters Pagination parametres
     * @return ImagesResponse object
     */
    public ImagesResponse getImagesByType(ImageType type, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/images")
                        .queryParam("type", type.toString())
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                ImagesResponse.class);
    }

    /**
     * Get an image by name.
     *
     * @param name Name of the image
     * @return ImagesResponse object
     */
    public ImagesResponse getImageByName(String name) {
        return get(
                UrlBuilder.from(apiUrl + "/images")
                        .queryParam("name", name)
                        .toUri(),
                ImagesResponse.class);
    }

    /**
     * Get image by ID.
     *
     * @param id ID of the image
     * @return ImageResponse object
     */
    public ImageResponse getImage(long id) {
        return get(
                apiUrl + "/images/" + id,
                ImageResponse.class);
    }

    /**
     * Update the description or the type of a image.
     *
     * @param id                 ID of the image
     * @param updateImageRequest Request object
     * @return ImageResponse object
     */
    public ImageResponse updateImage(long id, UpdateImageRequest updateImageRequest) {
        return put(
                apiUrl + "/images/" + id,
                updateImageRequest,
                ImageResponse.class);
    }

    /**
     * Delete an image,
     * <p>
     * This object does not have a respond!
     *
     * @param id ID of the image
     * @return String
     */
    public String deleteImage(long id) {
        return delete(
                apiUrl + "/images/" + id,
                String.class);
    }

    /**
     * Get all volumes in a project.
     *
     * @return VolumesResponse
     */
    public VolumesResponse getVolumes() {
        return getVolumes(null, new PaginationParameters(null, null));
    }

    /**
     * Get all volumes in a project.
     *
     * @param paginationParameters Pagination parametres
     * @return VolumesResponse
     */
    public VolumesResponse getVolumes(PaginationParameters paginationParameters) {
        return getVolumes(null, paginationParameters);
    }

    /**
     * Get all volumes in a project filtered by volumes
     *
     * @param labelSelector Label selector
     * @return VolumesResponse
     */
    public VolumesResponse getVolumes(String labelSelector) {
        return getVolumes(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all volumes in a project.
     *
     * @param labelSelector Filter response by label selector
     * @param paginationParameters Pagination parametres
     * @return VolumesResponse
     */
    public VolumesResponse getVolumes(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/volumes")
                        .queryParamIfPresent("label_selector", Optional.ofNullable(labelSelector))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                VolumesResponse.class);
    }

    /**
     * Get a specific volume by id.
     *
     * @param id ID of the volume
     * @return Volume object
     */
    public VolumeResponse getVolume(long id) {
        return get(
                apiUrl + "/volumes/" + id,
                VolumeResponse.class);
    }

    /**
     * Create a new volume.
     *
     * @param createVolumeRequest Volume request object
     * @return Volume object with action
     */
    public CreateVolumeResponse createVolume(CreateVolumeRequest createVolumeRequest) {
        if ((createVolumeRequest.getFormat() != null))
            createVolumeRequest.setFormat(createVolumeRequest.getFormat().toLowerCase());   // case-sensitive fix
        return post(
                apiUrl + "/volumes",
                createVolumeRequest,
                CreateVolumeResponse.class);
    }

    /**
     * Update some specific options of a volume.
     *
     * @param id                  ID of the volume
     * @param updateVolumeRequest Update volume request object
     * @return GetVolume object
     */
    public VolumeResponse updateVolume(long id, UpdateVolumeRequest updateVolumeRequest) {
        return put(
                apiUrl + "/volumes/" + id,
                updateVolumeRequest,
                VolumeResponse.class);
    }

    /**
     * Delete a volume
     *
     * @param id ID of the volume
     * @return no return object
     */
    public String deleteVolume(long id) {
        return delete(
                apiUrl + "/volumes/" + id,
                String.class);
    }

    /**
     * Get all actions of a volume.
     *
     * @param id ID of the volume
     * @return Action array
     */
    public ActionsResponse getVolumeActions(long id) {
        return get(
                apiUrl + "/volumes/" + id + "/actions",
                ActionsResponse.class);
    }

    /**
     * Attach a volume to a server.
     *
     * @param id                  ID of the volume
     * @param attachVolumeRequest Request object
     * @return Action object
     */
    public ActionResponse attachVolumeToServer(long id, AttachVolumeRequest attachVolumeRequest) {
        return post(
                apiUrl + "/volumes/" + id + "/actions/attach",
                attachVolumeRequest,
                ActionResponse.class);
    }

    /**
     * Detach a volume from a server.
     *
     * @param id ID of the volume
     * @return Action object
     */
    public ActionResponse detachVolume(long id) {
        return post(
                apiUrl + "/volumes/" + id + "/actions/detach",
                ActionResponse.class);
    }

    /**
     * Resize a volume.
     * Downsizing not possible!
     *
     * @param id                  ID of the volume
     * @param resizeVolumeRequest Request object
     * @return Action object
     */
    public ActionResponse resizeVolume(long id, ResizeVolumeRequest resizeVolumeRequest) {
        return post(
                apiUrl + "/volumes/" + id + "/actions/resize",
                resizeVolumeRequest,
                ActionResponse.class);
    }

    /**
     * Change the protection mode of the volume.
     * Only deletion protection is available!
     *
     * @param id                      ID of the volume
     * @param changeProtectionRequest Request object
     * @return Action object
     */
    public ActionResponse changeVolumeProtection(long id, ChangeProtectionRequest changeProtectionRequest) {
        return post(
                apiUrl + "/volumes/" + id + "/actions/change_protection",
                changeProtectionRequest,
                ActionResponse.class);
    }

    /**
     * Get all Private networks in a project.
     *
     * @return NetworksResponse
     */
    public NetworksResponse getNetworks() {
        return getNetworks(null, new PaginationParameters(null, null));
    }

    /**
     * Get all Private networks in a project.
     *
     * @param paginationParameters Pagination parametres
     * @return NetworksResponse
     */
    public NetworksResponse getNetworks(PaginationParameters paginationParameters) {
        return getNetworks(null, paginationParameters);
    }

    /**
     * Get all Private Networks in a project with a label selector.
     *
     * @param labelSelector Label Selector
     * @return NetworksResponse
     */
    public NetworksResponse getNetworks(String labelSelector) {
        return getNetworks(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all Private networks in a project.
     *
     * @param labelSelector Label selector
     * @param paginationParameters Pagination parametres
     * @return NetworksResponse
     */
    public NetworksResponse getNetworks(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/networks")
                        .queryParamIfPresent("label_selector", Optional.ofNullable(labelSelector))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                NetworksResponse.class);
    }

    /**
     * Get all networks with specific name.
     *
     * @param name Name of the network
     * @return Response from API
     */
    public NetworksResponse getNetworksByName(String name) {
        return get(
                UrlBuilder.from(apiUrl + "/networks")
                        .queryParam("name", name)
                        .toUri(),
                NetworksResponse.class);
    }

    /**
     * Get a network with it's specific ID.
     *
     * @param id ID of the network
     * @return Response from API
     */
    public NetworkResponse getNetwork(long id) {
        return get(
                apiUrl + "/networks/" + id,
                NetworkResponse.class);
    }

    /**
     * Update the labels or the name of a network.
     *
     * @param id                   ID of the network
     * @param updateNetworkRequest Request object
     * @return Response from API
     */
    public NetworkResponse updateNetwork(long id, UpdateNetworkRequest updateNetworkRequest) {
        return put(
                apiUrl + "/networks/" + id,
                updateNetworkRequest,
                NetworkResponse.class);
    }


    /**
     * Create a new private network.
     *
     * @param createNetworkRequest Request object
     * @return Response from API
     */
    public NetworkResponse createNetwork(CreateNetworkRequest createNetworkRequest) {
        return post(
                apiUrl + "/networks",
                createNetworkRequest,
                NetworkResponse.class);
    }

    /**
     * Delete a network.
     *
     * @param id ID of the network
     * @return There is no response.
     */
    public String deleteNetwork(long id) {
        return delete(
                apiUrl + "/networks/" + id,
                String.class);
    }

    /**
     * Attaches a server to a private network.
     *
     * @param id                           ID of the server
     * @param attachServerToNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse attachServerToNetwork(long id, AttachServerToNetworkRequest attachServerToNetworkRequest) {
        return post(
                apiUrl + "/servers/" + id + "/actions/attach_to_network",
                attachServerToNetworkRequest,
                ActionResponse.class);
    }


    /**
     * Detaches a server from a private network.
     *
     * @param id                             ID of the server
     * @param detachServerFromNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse detachServerFromNetwork(long id, DetachServerFromNetworkRequest detachServerFromNetworkRequest) {
        return post(
                apiUrl + "/servers/" + id + "/actions/detach_from_network",
                detachServerFromNetworkRequest,
                ActionResponse.class);
    }

    /**
     * Change alias IPs of a network.
     *
     * @param id                             ID of the server
     * @param changeAliasIPsofNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse changeAliasIPsOfNetwork(long id, ChangeAliasIPsofNetworkRequest changeAliasIPsofNetworkRequest) {
        return post(
                apiUrl + "/servers/" + id + "/actions/change_alias_ips",
                changeAliasIPsofNetworkRequest,
                ActionResponse.class);
    }

    /**
     * Change the protection configuration for a network
     *
     * @param id               ID of the network
     * @param changeProtection Request Object
     * @return Response from API
     */
    public ActionResponse changeNetworkProtection(long id, ChangeProtectionRequest changeProtection) {
        return post(
                apiUrl + "/networks/" + id + "/actions/change_protection",
                changeProtection,
                ActionResponse.class);
    }

    /**
     * Get all performed Actions for a network
     *
     * @param id ID of the network
     * @return Response from API
     */
    public ActionsResponse getNetworkActions(long id) {
        return get(
                apiUrl + "/networks/" + id + "/actions",
                ActionsResponse.class);
    }

    /**
     * Add a new subnet to a network.
     *
     * @param id                        ID of the network
     * @param addSubnetToNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse addSubnetToNetwork(long id, AddSubnetToNetworkRequest addSubnetToNetworkRequest) {
        return post(
                apiUrl + "/networks/" + id + "/actions/add_subnet",
                addSubnetToNetworkRequest,
                ActionResponse.class);
    }

    /**
     * Delete a subnet from a network.
     *
     * @param id                      ID of the network
     * @param deleteSubnetFromNetwork Request object
     * @return Response from API
     */
    public ActionResponse deleteSubnetFromNetwork(long id, DeleteSubnetFromNetwork deleteSubnetFromNetwork) {
        return post(
                apiUrl + "/networks/" + id + "/actions/delete_subnet",
                deleteSubnetFromNetwork,
                ActionResponse.class);
    }

    /**
     * Add a route to a network.
     *
     * @param id             ID of the network
     * @param networkRouteRequest Request object
     * @return Response from API
     */
    public ActionResponse addRouteToNetwork(long id, NetworkRouteRequest networkRouteRequest) {
        return post(
                apiUrl + "/networks/" + id + "/actions/add_route",
                networkRouteRequest,
                ActionResponse.class);
    }

    /**
     * Delete a route from a network.
     *
     * @param id             ID of the network
     * @param networkRouteRequest Request object
     * @return Response from API
     */
    public ActionResponse deleteRouteFromNetwork(long id, NetworkRouteRequest networkRouteRequest) {
        return post(
                apiUrl + "/networks/" + id + "/actions/delete_route",
                networkRouteRequest,
                ActionResponse.class);
    }

    /**
     * Change the IP range of a network.
     * Shrinking not possible!
     *
     * @param id                     ID of the network
     * @param changeIPRangeOfNetwork Request object
     * @return Response from API
     */
    public ActionResponse changeIPRangeOfNetwork(long id, ChangeIPRangeOfNetwork changeIPRangeOfNetwork) {
        return post(
                apiUrl + "/networks/" + id + "/actions/change_ip_range",
                changeIPRangeOfNetwork,
                ActionResponse.class);
    }

    /**
     * Get all certificates from the project.
     *
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificates() {
        return getCertificates(new PaginationParameters(null, null));
    }

    /**
     * Get all certificates from the project.
     *
     * @param paginationParameters Pagination parametres
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificates(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/certificates")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                CertificatesResponse.class);
    }

    /**
     * Get all certificates by label selector.
     * A label selector can be e.g. env=prod
     *
     * @param labelSelector Label selector used for filtering
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificates(String labelSelector) {
        return getCertificates(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all certificates by label selector.
     * A label selector can be e.g. env=prod
     *
     * @param labelSelector        Label selector used for filtering
     * @param paginationParameters Pagination parametres
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificates(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/certificates")
                        .queryParam("label_selector", labelSelector)
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                CertificatesResponse.class);
    }

    /**
     * Get a specific certificate by its name.
     *
     * @param name Name of the certificate
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificate(String name) {
        return get(
                UrlBuilder.from(apiUrl + "/certificates")
                        .queryParam("name", name)
                        .toUri(),
                CertificatesResponse.class);
    }

    /**
     * Get a specific certificate by id.
     *
     * @param id ID of the certificate
     * @return CertificateResponse
     */
    public CertificateResponse getCertificate(long id) {
        return get(
                apiUrl + "/certificates/" + id,
                CertificateResponse.class);
    }

    /**
     * Create a new certificate.
     *
     * @param createCertificateRequest CertificateRequest object with name, public- and private-key
     * @return CertificateResponse
     */
    public CertificateResponse createCertificate(CreateCertificateRequest createCertificateRequest) {
        return post(
                apiUrl + "/certificates",
                createCertificateRequest,
                CertificateResponse.class);
    }

    /**
     * Update a certificate.
     * <p>
     * Available options to update:
     * - Name
     * - Labels
     *
     * @param id                       ID of the certificate
     * @param updateCertificateRequest Certificate Update object
     * @return CertificateResponse
     */
    public CertificateResponse updateCertificate(long id, UpdateCertificateRequest updateCertificateRequest) {
        return put(
                apiUrl + "/certificates/" + id,
                updateCertificateRequest,
                CertificateResponse.class);
    }

    /**
     * Retry an issuance or renewal for a managed certificate.
     * <p>
     * This method is only applicable to managed certificate where either the issuance
     * or renewal status is failed.
     *
     * @param id ID of the certificate
     * @return ActionResponse
     */
    public ActionResponse retryCertificate(long id) {
        return post(
                apiUrl + "/certificates/" + id + "/actions/retry",
                ActionResponse.class);
    }

    /**
     * Delete a certificate.
     *
     * @param id ID of the certificate
     * @return nothing...
     */
    public String deleteCertificate(long id) {
        return delete(
                apiUrl + "/certificates/" + id,
                String.class);
    }

    /**
     * Get all Load Balancers.
     *
     * @return LoadBalancersResponse
     */
    public LoadBalancersResponse getLoadBalancers() {
        return getLoadBalancers(null, new PaginationParameters(null, null));
    }

    /**
     * Get Load Balancer by name.
     *
     * @return LoadBalancersResponse
     */
    public LoadBalancersResponse getLoadBalancerByName(String name) {
        return get(
            UrlBuilder.from(apiUrl + "/load_balancers")
                    .queryParam("name", name)
                    .toUri(),
            LoadBalancersResponse.class);
    }

    /**
     * Get all Load Balancers by label selector.
     *
     * @param labelSelector Label Selector
     * @return LoadBalancersResponse
     */
    public LoadBalancersResponse getLoadBalancers(String labelSelector) {
        return getLoadBalancers(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all Load Balancers.
     *
     * @param labelSelector Label Selector
     * @param paginationParameters Pagination parametres
     * @return LoadBalancersResponse
     */
    public LoadBalancersResponse getLoadBalancers(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/load_balancers")
                        .queryParamIfPresent("label_selector", Optional.ofNullable(labelSelector))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                LoadBalancersResponse.class);
    }

    /**
     * Get a specific Load Balancer.
     *
     * @param id ID of the Load Balancer
     * @return LoadBalancerResponse
     */
    public LoadBalancerResponse getLoadBalancer(long id) {
        return get(
                apiUrl + "/load_balancers/" + id,
                LoadBalancerResponse.class);
    }

    /**
     * Create a new Load Balancer.
     *
     * @param createLoadBalancerRequest Load Balancer Request object
     * @return LoadBalancerResponse
     */
    public LoadBalancerResponse createLoadBalancer(CreateLoadBalancerRequest createLoadBalancerRequest) {
        return post(
                apiUrl + "/load_balancers",
                createLoadBalancerRequest,
                LoadBalancerResponse.class);
    }

    /**
     * Update an existing Load Balancer.
     *
     * @param id                        ID of the Load Balancer
     * @param updateLoadBalancerRequest Load Balancer Update Request Object
     * @return LoadBalancerResponse
     */
    public LoadBalancerResponse updateLoadBalancer(long id, UpdateLoadBalancerRequest updateLoadBalancerRequest) {
        return put(
                apiUrl + "/load_balancers/" + id,
                updateLoadBalancerRequest,
                LoadBalancerResponse.class);
    }

    /**
     * Delete a Load Balancer.
     *
     * @param id ID of the Load Balancer
     * @return nothing
     */
    public String deleteLoadBalancer(long id) {
        return delete(
                apiUrl + "/load_balancers/" + id,
                String.class);
    }

    /**
     * Get all actions of a Load Balancer.
     *
     * @param id ID of the Load Balancer
     * @return ActionsResponse
     */
    public ActionsResponse getLoadBalancerActions(long id) {
        return get(
                apiUrl + "/load_balancers/" + id + "/actions",
                ActionsResponse.class);
    }

    /**
     * Add a service to a Load Balancer.
     *
     * @param id               ID of the Load Balancer
     * @param lbServiceRequest Load Balancer Service Request
     * @return LoadBalancerResponse
     */
    public LoadBalancerResponse addServiceToLoadBalancer(long id, LBServiceRequest lbServiceRequest) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/add_service",
                lbServiceRequest,
                LoadBalancerResponse.class);
    }

    /**
     * Update a service of a Load Balancer.
     *
     * @param id               ID of the Load Balancer
     * @param lbServiceRequest Load Balancer Service Request
     * @return LoadBalancerResponse
     */
    public LoadBalancerResponse updateServiceOfLoadBalancer(long id, LBServiceRequest lbServiceRequest) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/update_service",
                lbServiceRequest,
                LoadBalancerResponse.class);
    }

    /**
     * Delete a service of a Load Balancer.
     *
     * @param id         ID of the Load Balancer
     * @param listenPort The desired "listen port" of the service
     * @return ActionResponse
     */
    public ActionResponse deleteServiceOfLoadBalancer(long id, long listenPort) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/delete_service",
                new LoadBalancerDeleteServiceRequest(listenPort),
                ActionResponse.class);
    }

    /**
     * Add a target to a Load Balancer.
     *
     * @param id              ID of the Load Balancer
     * @param lbTargetRequest Load Balancer Target Request
     * @return ActionResponse
     */
    public ActionResponse addTargetToLoadBalancer(long id, LBTargetRequest lbTargetRequest) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/add_target",
                lbTargetRequest,
                ActionResponse.class);
    }

    /**
     * Removes a target from a load balancer.
     *
     * @param id              ID of the Load Balancer
     * @param lbTargetRequest Load Balancer Target Request
     * @return ActionResponse
     */
    public ActionResponse removeTargetFromLoadBalancer(long id, LBTargetRequest lbTargetRequest) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/remove_target",
                lbTargetRequest,
                ActionResponse.class);
    }

    /**
     * Changes the algorithm that determines to which target new requests are sent.
     *
     * @param id            ID of the Load Balancer
     * @param algorithmType Algorithm Type
     * @return ActionResponse
     */
    public ActionResponse changeAlgorithmOfLoadBalancer(long id, String algorithmType) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/change_algorithm",
                new LoadBalancerChangeAlgorithmRequest(algorithmType),
                ActionResponse.class);
    }


    /**
     * Changes the type of a Load Balancer.
     *
     * @param id               ID of the Load Balancer
     * @param loadBalancerType New type of the Load Balancer
     * @return ActionResponse
     */
    public ActionResponse changeTypeOfLoadBalancer(long id, String loadBalancerType) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/change_type",
                new LoadBalancerChangeTypeRequest(loadBalancerType),
                ActionResponse.class);
    }

    /**
     * Attach a network to a Load Balancer.
     *
     * @param id        ID of the Load Balancer
     * @param networkId ID of the Network
     * @param ip        IP for the Load Balancer in this private network
     * @return ActionResponse
     */
    public ActionResponse attachNetworkToLoadBalancer(long id, long networkId, String ip) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/attach_to_network",
                new LoadBalancerNetworkRequest(networkId, ip),
                ActionResponse.class);
    }

    /**
     * Attach a network to a Load Balancer.
     *
     * @param id        ID of the Load Balancer
     * @param networkId ID of the Network
     * @return ActionResponse
     */
    public ActionResponse attachNetworkToLoadBalancer(long id, long networkId) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/attach_to_network",
                new LoadBalancerNetworkRequest(networkId),
                ActionResponse.class);
    }

    /**
     * Detach a network from a Load Balancer.
     *
     * @param id        ID of the Load Balancer
     * @param networkId ID of the Network
     * @return ActionResponse
     */
    public ActionResponse detachNetworkFromLoadBalancer(long id, long networkId) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/detach_from_network",
                new LoadBalancerNetworkRequest(networkId),
                ActionResponse.class);
    }

    /**
     * Enable the public interface of a Load Balancer.
     *
     * @param id ID of the Load Balancer
     * @return ActionResponse
     */
    public ActionResponse enablePublicInterfaceOfLoadBalancer(long id) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/enable_public_interface",
                ActionResponse.class);
    }

    /**
     * Disable the public interface of a Load Balancer.
     *
     * @param id ID of the Load Balancer
     * @return ActionResponse
     */
    public ActionResponse disablePublicInterfaceOfLoadBalancer(long id) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/disable_public_interface",

                ActionResponse.class);
    }

    /**
     * Change the protection configuration of a Load Balancer.
     *
     * @param id     ID of the Load Balancer
     * @param delete Delete protection
     * @return ActionResponse
     */
    public ActionResponse changeProtectionOfLoadBalancer(long id, boolean delete) {
        return post(
                apiUrl + "/load_balancers/" + id + "/actions/change_protection",
                new LoadBalancerChangeProtectionRequest(delete),
                ActionResponse.class);
    }

    /**
     * Get a specific placement group.
     *
     * @param id placement group ID
     * @return PlacementGroupResponse
     */
    public PlacementGroupResponse getPlacementGroup(long id) {
        return get(
                apiUrl + "/placement_groups/" + id,
                PlacementGroupResponse.class);
    }

    /**
     * Get all placement groups.
     *
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroups() {
        return getPlacementGroups(new PaginationParameters(null, null));
    }

    /**
     * Get all placement groups.
     *
     * @param paginationParameters Pagination parametres
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroups(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(apiUrl + "/placement_groups")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                PlacementGroupsResponse.class);
    }

    /**
     * Get placement group by name.
     *
     * @param name name of the placement grouo
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroup(String name) {
        UrlBuilder builder = UrlBuilder.from(apiUrl + "/placement_groups")
                .queryParam("name", name);

        return get(
                builder.toUri(),
                PlacementGroupsResponse.class);
    }

    /**
     * Get placement groups by label selector.
     *
     * @param labelSelector label selector used by resource
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroups(String labelSelector) {
        UrlBuilder builder = UrlBuilder.from(apiUrl + "/placement_groups")
                .queryParam("label_selector", labelSelector);

        return get(
                builder.toUri(),
                PlacementGroupsResponse.class);
    }

    /**
     * Get placement groups by type
     *
     * @param type Type of the placement group
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroup(PlacementGroupType type) {
        return get(
                UrlBuilder.from(apiUrl + "/placement_groups").queryParam("type", type.toString()).toUri(),
                PlacementGroupsResponse.class);
    }

    /**
     * Create a placement group.
     *
     * @param placementGroupRequest PlacementGroupRequest object
     * @return PlacementGroupResponse
     */
    public PlacementGroupResponse createPlacementGroup(CreatePlacementGroupRequest placementGroupRequest) {
        return post(
                apiUrl + "/placement_groups",
                placementGroupRequest,
                PlacementGroupResponse.class);
    }

    /**
     * Delete a placement group.
     *
     * @param id placement group ID
     * @return ActionResponse
     */
    public String deletePlacementGroup(long id) {
        return delete(
                apiUrl + "/placement_groups/" + id,
                String.class);
    }

    /**
     * Converts a Date to the ISO-8601 format
     *
     * @param date Date to be converted
     * @return Date in ISO-8601 format
     */
    public String convertToISO8601(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    private void validateStorageBoxApiUsage() {
        if (!apiUrl.contains("api.hetzner.com") && !isTestMode()) {
            throw new IllegalStateException("Storage Box methods can only be used with APIType.HETZNER_ONLINE. " +
                    "Please create the API instance with: new HetznerCloudAPI(token, APIType.HETZNER_ONLINE)");
        }
    }

    private boolean isTestMode() {
        // Check if we're in test mode by looking for test-related URL patterns or system properties
        return apiUrl.contains("localhost") ||
               apiUrl.contains("127.0.0.1") ||
               "test".equals(System.getProperty("hetzner.test.mode"));
    }

    private <T> T exchange(String url, HttpMethod method, Object body, Class<T> clazz) {
        try(Response response = buildCall(url, method, body).execute()) {
            final String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                String correlationId = response.header("X-Correlation-Id");
                throw new APIRequestException(objectMapper.readValue(responseBody, APIErrorResponse.class), correlationId);
            }

            if (String.class.equals(clazz)) {
                return (T) responseBody;
            } else {
                return objectMapper.readValue(responseBody, clazz);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private Call buildCall(String url, HttpMethod method, Object body) throws JsonProcessingException {
        RequestBody requestBody = null;

        if (body != null) {
            requestBody = RequestBody.create(objectMapper.writeValueAsBytes(body), MediaType.get("application/json"));
        }

        return client.newCall(new Request.Builder()
                .addHeader("Authorization", "Bearer " + hcloudToken)
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", userAgent)
                .url(url)
                .method(method.toString(), requestBody).build());
    }

    private <T> T get(String url, Class<T> clazz) {
        return exchange(url, HttpMethod.GET, null, clazz);
    }

    private <T> T delete(String url, Class<T> clazz) {
        return exchange(url, HttpMethod.DELETE, null, clazz);
    }

    private <T> T put(String url, Object body, Class<T> clazz) {
        return exchange(url, HttpMethod.PUT, body, clazz);
    }

    private <T> T post(String url, Object body, Class<T> clazz) {
        return exchange(url, HttpMethod.POST, body, clazz);
    }

    private <T> T post(String url, Class<T> clazz) {
        return exchange(url, HttpMethod.POST, objectMapper.createObjectNode(), clazz);
    }

    private enum HttpMethod {
        GET, PUT, POST, DELETE
    }

}
