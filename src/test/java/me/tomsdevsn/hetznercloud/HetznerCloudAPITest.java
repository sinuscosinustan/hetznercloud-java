package me.tomsdevsn.hetznercloud;

import me.tomsdevsn.hetznercloud.init.App;
import me.tomsdevsn.hetznercloud.objects.general.*;
import me.tomsdevsn.hetznercloud.objects.request.LoadBalancerRequest;
import me.tomsdevsn.hetznercloud.objects.request.NetworkRequest;
import me.tomsdevsn.hetznercloud.objects.request.PlacementGroupRequest;
import me.tomsdevsn.hetznercloud.objects.request.SSHKeyRequest;
import me.tomsdevsn.hetznercloud.objects.response.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HetznerCloudAPITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HetznerCloudAPITest.class);
    private final List<CleanupObject> cleanupObjects = new ArrayList<>();

    private final String testIdentifier = new Random().ints(48, 122)
            .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
            .mapToObj(i -> (char) i)
            .limit(8)
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();

    @Autowired
    private HetznerCloudAPI hetznerCloudAPI;

    @AfterAll
    public void cleanUp() {
        cleanupObjects.forEach(cleanupObject -> {
            Object response = cleanup(cleanupObject);
            LOGGER.info("Cleanup " + cleanupObject + " response: " + response);
        });
    }

    private Object cleanup(CleanupObject cleanupObject) {
        switch (cleanupObject.getCleanupType()) {
            case SERVER:
                return hetznerCloudAPI.deleteServer(cleanupObject.getId());
            case LOAD_BALANCER:
                return hetznerCloudAPI.deleteLoadBalancer(cleanupObject.getId());
            case NETWORK:
                return hetznerCloudAPI.deleteNetwork(cleanupObject.getId());
            case IMAGE:
                return hetznerCloudAPI.deleteImage(cleanupObject.getId());
            case SSH_KEY:
                return hetznerCloudAPI.deleteSSHKey(cleanupObject.getId());
            case FLOATING_IP:
                return hetznerCloudAPI.deleteFloatingIP(cleanupObject.getId());
            case PLACEMENT_GROUP:
                return hetznerCloudAPI.deletePlacementGroup(cleanupObject.getId());
            default:
                return null;
        }
    }

    @Test
    void getServers() {
    }

    @Test
    void getServerByName() {
    }

    @Test
    void getServerById() {
    }

    @Test
    void createServer() {
    }

    @Test
    void deleteServer() {
    }

    @Test
    void changeServerName() {
    }

    @Test
    void requestConsole() {
    }

    @Test
    void changeServerProtection() {
    }

    @Test
    void getAllActionsOfServer() {
    }

    @Test
    void getActionOfServer() {
    }

    @Test
    void getActionsOfFloatingIP() {
    }

    @Test
    void getActionOfFloatingIP() {
    }

    @Test
    void powerOnServer() {
    }

    @Test
    void powerOffServer() {
    }

    @Test
    void softRebootServer() {
    }

    @Test
    void resetServer() {
    }

    @Test
    void shutdownServer() {
    }

    @Test
    void resetRootPassword() {
    }

    @Test
    void enableRescue() {
    }

    @Test
    void testEnableRescue() {
    }

    @Test
    void enableRescueAndReset() {
    }

    @Test
    void testEnableRescueAndReset() {
    }

    @Test
    void disableRescue() {
    }

    @Test
    void rebuildServer() {
    }

    @Test
    void changeServerType() {
    }

    @Test
    void getMetrics() {
    }

    @Test
    void createImage() {
    }

    @Test
    void changeImageProtection() {
    }

    @Test
    void enableBackup() {
    }

    @Test
    void testEnableBackup() {
    }

    @Test
    void disableBackup() {
    }

    @Test
    void getISOs() {
        ISOSResponse isos = hetznerCloudAPI.getISOS();
        assertNotNull(isos);
        assertNotNull(isos.getIsos());
    }

    @Test
    void getISOById() {
        ISOResponse iso = hetznerCloudAPI.getISOById(
                hetznerCloudAPI.getISOS().getIsos().stream().findFirst().get().getId()
        );

        assertNotNull(iso);
        assertNotNull(iso.getIso());
    }

    @Test
    void attachISO() {
    }

    @Test
    void detachISO() {
    }

    @Test
    void changeDNSPTR() {
    }

    @Test
    void getDatacenter() {
    }

    @Test
    void getDatacenters() {
    }

    @Test
    void getPricing() {
    }

    @Test
    void getFloatingIPs() {
    }

    @Test
    void getFloatingIP() {
    }

    @Test
    void createFloatingIP() {
    }

    @Test
    void changeFloatingIPProtection() {
    }

    @Test
    void changeDescriptionOfFloatingIP() {
    }

    @Test
    void assignFloatingIP() {
    }

    @Test
    void unassignFloatingIP() {
    }

    @Test
    void changeFloatingReverseDNS() {
    }

    @Test
    void deleteFloatingIP() {
    }

    @Test
    void updateFloatingIP() {
    }

    @Test
    void getSSHKeys() {
    }

    @Test
    void getSSHKey() {
    }

    @Test
    void getSSHKeyByName() {
    }

    @Test
    void getSSHKeyByFingerprint() {
    }

    @Test
    void createSSHKey() {

        var publicKey = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPDS4NLE+d5jLs32yDDE4/JrCv8t0zk7tK3Z2nSQxPaj pelle@zoidberg";
        var name = "sshkey-" + UUID.randomUUID();

        var request = SSHKeyRequest.builder().name(name).publicKey(publicKey).label("label1", "value1").build();
        var response = hetznerCloudAPI.createSSHKey(request);
        cleanupObjects.add(new CleanupObject(response.getSshKey().getId(), CleanupType.SSH_KEY));

        assertNotNull(response);
        assertNotNull(response.getSshKey());

        var sshKeys = hetznerCloudAPI.getSSHKeyByName(name);

        assertThat(sshKeys.getSshKeys()).hasSize(1);
        assertThat(sshKeys.getSshKeys().get(0).getLabels()).hasSize(1);
        assertThat(sshKeys.getSshKeys().get(0).getLabels().get("label1")).isEqualTo("value1");
    }

    @Test
    void changeSSHKeyName() {
    }

    @Test
    void updateSSHKey() {
    }

    @Test
    void deleteSSHKey() {
    }

    @Test
    void getServerTypes() {
    }

    @Test
    void getAllLoadBalancerTypes() {
    }

    @Test
    void getAllLoadBalancerTypesByName() {
    }

    @Test
    void getServerTypeByName() {
    }

    @Test
    void getServerType() {
    }

    @Test
    void getLocations() {
    }

    @Test
    void getLocationByName() {
    }

    @Test
    void getLocation() {
    }

    @Test
    void getImages() {
    }

    @Test
    void testGetImages() {
    }

    @Test
    void getImageByName() {
    }

    @Test
    void getImage() {
    }

    @Test
    void updateImage() {
    }

    @Test
    void deleteImage() {
    }

    @Test
    void getVolumes() {
    }

    @Test
    void getVolume() {
    }

    @Test
    void createVolume() {
    }

    @Test
    void updateVolume() {
    }

    @Test
    void deleteVolume() {
    }

    @Test
    void getAllActionsOfVolume() {
    }

    @Test
    void getActionOfVolume() {
    }

    @Test
    void attachVolumeToServer() {
    }

    @Test
    void detachVolume() {
    }

    @Test
    void resizeVolume() {
    }

    @Test
    void changeVolumeProtection() {
    }

    @Test
    void getAllNetworks() {
    }

    @Test
    void getNetworksByName() {
    }

    @Test
    void getNetworksByLabel() {
    }

    @Test
    void getNetwork() {
    }

    @Test
    void updateNetwork() {
    }

    @Test
    void createNetwork() {
    }

    @Test
    void deleteNetwork() {
    }

    @Test
    void attachServerToNetwork() {
    }

    @Test
    void detachServerFromNetwork() {
    }

    @Test
    void changeAliasIPsOfNetwork() {
    }

    @Test
    void changeNetworkProtection() {
    }

    @Test
    void getActionsForNetwork() {
    }

    @Test
    void getActionForNetwork() {
    }

    @Test
    void addSubnetToNetwork() {
    }

    @Test
    void deleteSubnetFromNetwork() {
    }

    @Test
    void addRouteToNetwork() {
    }

    @Test
    void deleteRouteFromNetwork() {
    }

    @Test
    void changeIPRangeOfNetwork() {
    }

    @Test
    void getCertificates() {
    }

    @Test
    void getCertificate() {
    }

    @Test
    void createCertificate() {
    }

    @Test
    void updateCertificate() {
    }

    @Test
    void deleteCertificate() {
    }

    @Test
    void getLoadBalancers() {
    }

    @Test
    void getLoadBalancer() {
    }

    @Test
    void createLoadBalancer() {
        LoadBalancerResponse loadBalancer = null;
        NetworkResponse networkResponse = null;
        try {
            String networkName = testIdentifier + "-createLoadBalancer";
            NetworkRequest.NetworkRequestBuilder networkRequest = NetworkRequest.builder()
                    .ipRange("10.0.0.0/16")
                    .subnets(Collections.singletonList(getDefaultSubnet()))
                    .name(networkName);
            networkResponse = hetznerCloudAPI.createNetwork(networkRequest.build());
            assertNotNull(networkResponse);
            assertNotNull(networkResponse.getNetwork());

            LoadBalancerType loadBalancerType = hetznerCloudAPI.getAllLoadBalancerTypes().getLoadBalancerTypes().get(0);
            assertNotNull(loadBalancerType);
            String loadBalancerName = testIdentifier + "-createLoadBalancer";
            LoadBalancerRequest loadBalancerRequest = LoadBalancerRequest.builder()
                    .networkZone("eu-central")
                    .network(networkResponse.getNetwork().getId())
                    .loadBalancerType(loadBalancerType.getId().toString())
                    .publicInterface(true)
                    .services(Collections.singletonList(getHttpService()))
                    .targets(Collections.singletonList(getLabelSelector(loadBalancerName)))
                    .name(loadBalancerName)
                    .build();

            loadBalancer = hetznerCloudAPI.createLoadBalancer(loadBalancerRequest);
            assertNotNull(loadBalancer);
            assertNotNull(loadBalancer.getLoadBalancer());
        } catch (HttpClientErrorException e) {
            Assertions.fail(e.getResponseBodyAsString());
        } finally {
            if (loadBalancer != null) {
                cleanupObjects.add(new CleanupObject(loadBalancer.getLoadBalancer().getId(), CleanupType.LOAD_BALANCER));
            }
            if (networkResponse != null) {
                cleanupObjects.add(new CleanupObject(networkResponse.getNetwork().getId(), CleanupType.NETWORK));
            }
        }
    }

    private Subnet getDefaultSubnet() {
        Subnet subnet = new Subnet();
        subnet.setType(SubnetType.cloud);
        subnet.setIpRange("10.0.0.0/24");
        subnet.setGateway("10.0.0.1");
        subnet.setNetworkZone("eu-central");
        return subnet;
    }

    private LBService getHttpService() {
        LBService lbService = new LBService();
        lbService.setProtocol("http");
        lbService.setDestinationPort(8080L);
        lbService.setListenPort(80L);
        lbService.setProxyprotocol(false);
        LBHealthCheck lbHealthCheck = new LBHealthCheck();
        LBHealthCheck.Http http = new LBHealthCheck.Http();
        http.setPath("/actuator/health");
        http.setDomain("");
        http.setResponse("");
        http.setTls(false);
        http.setStatusCodes(Arrays.asList("2??", "3??"));
        lbHealthCheck.setHttp(http);
        lbHealthCheck.setPort(8080L);
        lbHealthCheck.setProtocol("http");
        lbHealthCheck.setInterval(15L);
        lbHealthCheck.setTimeout(10L);
        lbHealthCheck.setRetries(3L);
        lbService.setHealthCheck(lbHealthCheck);
        return lbService;

    }

    private LBTarget getLabelSelector(String uniqueHostName) {
        LBTarget lbTarget = new LBTarget();
        lbTarget.setType(TargetType.label_selector);
        lbTarget.setLabelSelector(new LBTargetLabelSelector(uniqueHostName));
        lbTarget.setUsePrivateIp(true);
        return lbTarget;
    }

    @Test
    void updateLoadBalancer() {
    }

    @Test
    void deleteLoadBalancer() {
    }

    @Test
    void getAllActionsOfLoadBalancer() {
    }

    @Test
    void getActionOfLoadBalancer() {
    }

    @Test
    void addServiceToLoadBalancer() {
    }

    @Test
    void updateServiceOfLoadBalancer() {
    }

    @Test
    void deleteServiceOfLoadBalancer() {
    }

    @Test
    void addTargetToLoadBalancer() {
    }

    @Test
    void removeTargetFromLoadBalancer() {
    }

    @Test
    void changeAlgorithmOfLoadBalancer() {
    }

    @Test
    void changeTypeOfLoadBalancer() {
    }

    @Test
    void attachNetworkToLoadBalancer() {
    }

    @Test
    void testAttachNetworkToLoadBalancer() {
    }

    @Test
    void detachNetworkFromLoadBalancer() {
    }

    @Test
    void enablePublicInterfaceOfLoadBalancer() {
    }

    @Test
    void disablePublicInterfaceOfLoadBalancer() {
    }

    @Test
    void changeProtectionOfLoadBalancer() {
    }

    @Test
    void createPlacementGroup() {
        PlacementGroupResponse placementGroup = null;
        try {
            String name = testIdentifier + "-createPlacementGroup";
            PlacementGroupRequest placementGroupRequest = PlacementGroupRequest.builder()
                    .name(name)
                    .type(PlacementGroupType.spread)
                    .build();

            placementGroup = hetznerCloudAPI.createPlacementGroup(placementGroupRequest);
            assertNotNull(placementGroup);
            assertNotNull(placementGroup.getPlacementGroup());
        } catch (HttpClientErrorException ex) {
            Assertions.fail(ex.getResponseBodyAsString());
        } finally {
            if (placementGroup != null)
                cleanupObjects.add(new CleanupObject(
                        placementGroup.getPlacementGroup().getId(),
                        CleanupType.PLACEMENT_GROUP));
        }
    }

    @Test
    void convertToISO8601() {
    }
}
