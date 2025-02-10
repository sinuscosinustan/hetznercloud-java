package io.github.sinuscosinustan.hetznercloud;

import io.github.sinuscosinustan.hetznercloud.objects.enums.*;
import io.github.sinuscosinustan.hetznercloud.objects.enums.ServerType;
import io.github.sinuscosinustan.hetznercloud.objects.general.*;
import io.github.sinuscosinustan.hetznercloud.objects.request.*;
import lombok.extern.slf4j.Slf4j;
import io.github.sinuscosinustan.hetznercloud.objects.response.CreatePrimaryIPResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.ISOResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.ISOSResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.PlacementGroupResponse;
import org.awaitility.Awaitility;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.jclouds.ssh.SshKeys;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class HetznerCloudAPITest {

    private final HetznerCloudAPI hetznerCloudAPI = new HetznerCloudAPI(System.getenv("HCLOUD_TOKEN"));

    private final String testUUID = UUID.randomUUID().toString();
    private final String testUUIDLabelKey = "java/testID";
    private final String testUUIDLabelSelector = String.format("%s=%s", testUUIDLabelKey, testUUID);

    @AfterAll
    public void cleanUpAfter() {
        log.info("cleanup e2e resources");

        log.info("removing certificates");
        hetznerCloudAPI.getCertificates(testUUIDLabelSelector).getCertificates().forEach((certificate -> {
            log.info("removing certificate '{}'", certificate.getName());
            hetznerCloudAPI.deleteCertificate(certificate.getId());
        }));

        log.info("removing floating ips");
        hetznerCloudAPI.getFloatingIPs(testUUIDLabelSelector).getFloatingIps().forEach((floatingIP -> {
            log.info("removing floating ip '{}", floatingIP.getName());
            hetznerCloudAPI.deleteFloatingIP(floatingIP.getId());
        }));

        log.info("removing ssh keys");
        hetznerCloudAPI.getSSHKeys(testUUIDLabelSelector).getSshKeys().forEach((sshKey -> {
            log.info("removing ssh key '{}'", sshKey.getName());
            hetznerCloudAPI.deleteSSHKey(sshKey.getId());
        }));

        log.info("removing volumes");
        hetznerCloudAPI.getVolumes(testUUIDLabelSelector).getVolumes().forEach((volume -> {

            hetznerCloudAPI.getVolumeActions(volume.getId()).getActions().forEach((action -> {
                log.info("waiting for action '{}' to finish for volume '{}'", action.getCommand(), volume.getName());
                Awaitility.await().until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
            }));

            log.info("removing volume '{}'", volume.getName());
            hetznerCloudAPI.deleteVolume(volume.getId());
        }));

        log.info("removing loadbalancers");
        hetznerCloudAPI.getLoadBalancers(testUUIDLabelSelector).getLoadBalancers().forEach((loadBalancer -> {
            hetznerCloudAPI.getLoadBalancerActions(loadBalancer.getId()).getActions().forEach((action -> {
                log.info("waiting for action '{}' to finish for loadbalancer '{}'", action.getCommand(), loadBalancer.getName());
                Awaitility.await().until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
            }));
            log.info("removing loadbalancer '{}'", loadBalancer.getName());
            hetznerCloudAPI.deleteLoadBalancer(loadBalancer.getId());
        }));

        log.info("removing networks");
        hetznerCloudAPI.getNetworks(testUUIDLabelSelector).getNetworks().forEach((network -> {
            log.info("removing network '{}'", network.getName());
            hetznerCloudAPI.deleteNetwork(network.getId());
        }));

        log.info("removing primary ips");
        hetznerCloudAPI.getPrimaryIPs(testUUIDLabelSelector).getPrimaryIPs().forEach(ip -> {
            log.info("removing primary ip '{}'", ip.getId());
            hetznerCloudAPI.deletePrimaryIP(ip.getId());
        });

        log.info("removing servers");
        hetznerCloudAPI.getServers(testUUIDLabelSelector).getServers().forEach((server -> {
            hetznerCloudAPI.getServerActions(server.getId()).getActions().forEach((action -> {
                log.info("waiting for action '{}' to finish for server '{}'", action.getCommand(), server.getName());
                Awaitility.await().until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
            }));
            log.info("removing server '{}'", server.getName());
            hetznerCloudAPI.deleteServer(server.getId());
        }));

        log.info("removing placement groups");
        hetznerCloudAPI.getPlacementGroups(testUUIDLabelSelector).getPlacementGroups().forEach(placementGroup -> {
            log.info("removing placement group '{}'", placementGroup.getId());
            hetznerCloudAPI.deletePlacementGroup(placementGroup.getId());
        });
    }

    @Test
    void testPrimaryIPCreation() {
        var name = UUID.randomUUID().toString();

        CreatePrimaryIPResponse primaryIP = hetznerCloudAPI.createPrimaryIP(
                CreatePrimaryIPRequest.builder()
                        .name(name)
                        .datacenter("fsn1-dc14")
                        .type(IPType.ipv6)
                        .assigneeType(IPAssigneeType.server)
                        .label("ilike", "cheesecake")
                        .label(testUUIDLabelKey, testUUID)
                .build());

        assertThat(primaryIP).isNotNull();
        assertThat(primaryIP.getPrimaryIP()).isNotNull();

        assertThat(primaryIP.getPrimaryIP().getName()).isEqualTo(name);
        assertThat(primaryIP.getPrimaryIP().getType()).isEqualTo(IPType.ipv6);
        assertThat(primaryIP.getPrimaryIP().getLabels().get("ilike")).isEqualTo("cheesecake");

        hetznerCloudAPI.deletePrimaryIP(primaryIP.getPrimaryIP().getId());
    }

    @Test
    void getISOs() {
        ISOSResponse isos = hetznerCloudAPI.getISOS();
        assertNotNull(isos);
        assertNotNull(isos.getIsos());
    }

    @Test
    void getISOById() {
        ISOResponse iso = hetznerCloudAPI.getISO(
                hetznerCloudAPI.getISOS().getIsos().stream().findFirst().get().getId()
        );

        assertNotNull(iso);
        assertNotNull(iso.getIso());
    }

    @Test
    void getFirewalls() {
        final var firewalls = hetznerCloudAPI.getFirewalls();
    }

    @Test
    void createFirewall() {
        final var firewallRequest = CreateFirewallRequest.builder()
                .applicationTarget(FWApplicationTarget.builder()
                        .labelSelector(new FWLabelSelector("env==dev"))
                        .type(TargetType.label_selector)
                        .build())
                //.applicationTarget(FWApplicationTarget.builder()
                //        .server(new FWServerRef(12345678L))
                //        .type(TargetType.server)
                //        .build())
                .label("test", "firewall")
                .label(testUUIDLabelKey, testUUID)
                .name(UUID.randomUUID().toString())
                .firewallRule(FirewallRule.builder()
                        .description("test-fw-rule-http")
                        .desinationIP("0.0.0.0/0")
                        .port("80")
                        .direction(FirewallRule.Direction.out)
                        .protocol(FirewallRule.Protocol.tcp)
                        .build())
                .firewallRule(FirewallRule.builder()
                        .description("test-fw-rule-https")
                        .desinationIP("0.0.0.0/0")
                        .port("443")
                        .direction(FirewallRule.Direction.out)
                        .protocol(FirewallRule.Protocol.tcp)
                        .build())
                .firewallRule(FirewallRule.builder()
                        .description("test-fw-rule-icmp")
                        .desinationIP("0.0.0.0/0")
                        .direction(FirewallRule.Direction.out)
                        .protocol(FirewallRule.Protocol.icmp)
                        .build())
                .build();
        final var response = hetznerCloudAPI.createFirewall(firewallRequest);
    }

    @Test
    void deleteFirewall() {
        final List<Firewall> firewalls = hetznerCloudAPI.getFirewalls(testUUIDLabelSelector).getFirewalls();
        assertThat(firewalls).hasSizeGreaterThanOrEqualTo(1);

        hetznerCloudAPI.deleteFirewall(firewalls.get(0).getId());
        assertThat(hetznerCloudAPI.getFirewalls(testUUIDLabelSelector).getFirewalls())
                .hasSize(firewalls.size() - 1);
    }

    @Test
    void testFloatingIPCreation() {
        String keyId = UUID.randomUUID().toString();

        var floatingIp = hetznerCloudAPI.createFloatingIP(CreateFloatingIPRequest.builder()
                .name(keyId)
                .label(testUUIDLabelKey, testUUID)
                .type(IPType.ipv6)
                .homeLocation("fsn1")
                .build());

        assertNotNull(floatingIp.getFloatingIP());
        assertThat(floatingIp.getFloatingIP().getName()).isEqualTo(keyId);
        assertThat(floatingIp.getFloatingIP().getType()).isEqualTo(IPType.ipv6);
        assertThat(floatingIp.getFloatingIP().getHomeLocation().getName()).isEqualTo("fsn1");
    }

    @Test
    void testManageSSHKeys() {
        Map<String, String> keyPair = SshKeys.generate();

        String keyId = UUID.randomUUID().toString();

        // create key
        var createdKey = hetznerCloudAPI.createSSHKey(
                CreateSSHKeyRequest.builder()
                        .name(keyId)
                        .publicKey(keyPair.get("public"))
                        .label("label1", "value1")
                        .label(testUUIDLabelKey, testUUID)
                        .build());
        assertThat(createdKey).isNotNull();
        assertThat(createdKey.getSshKey().getPublicKey()).isEqualTo(keyPair.get("public"));
        assertThat(createdKey.getSshKey().getFingerprint()).isEqualTo(SshKeys.fingerprintPublicKey(keyPair.get("public")));

        var sshKeys = hetznerCloudAPI.getSSHKey(keyId);

        assertThat(sshKeys.getSshKeys().get(0).getLabels()).hasSize(2);
        assertThat(sshKeys.getSshKeys().get(0).getLabels().get("label1")).isEqualTo("value1");

        // update key
        hetznerCloudAPI.updateSSHKey(
                createdKey.getSshKey().getId(),
                UpdateSSHKeyRequest.builder()
                        .label(testUUIDLabelKey, testUUID)
                        .label("label2", "value2")
                        .build());

        var sshKey1 = hetznerCloudAPI.getSSHKey(createdKey.getSshKey().getId());
        assertThat(sshKey1.getSshKey().getLabels().get("label2")).isEqualTo("value2");
    }

    @Test
    void testManageVolumes() {
        // create volume
        String keyId = UUID.randomUUID().toString();
        var createdVolume = hetznerCloudAPI.createVolume(
                CreateVolumeRequest.builder()
                        .name(keyId)
                        .location("fsn1")
                        .label(testUUIDLabelKey, testUUID)
                        .size(10L)
                        .build());

        // wait for volume to settle
        createdVolume.getNextActions().forEach((action) -> {
            Awaitility.await().until(() -> {
                var actionResponse = hetznerCloudAPI.getAction(action.getId());
                return actionResponse.getAction().getFinished() != null;
            });
        });
        assertThat(hetznerCloudAPI.getVolumes(testUUIDLabelSelector).getVolumes()).hasSize(1);

        // get volume
        var volume = hetznerCloudAPI.getVolume(createdVolume.getVolume().getId());
        assertThat(volume.getVolume().getName()).isEqualTo(keyId);
    }

    @Test
    void testManageCertificates() throws Exception {
        String keyId = UUID.randomUUID().toString();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        X509Certificate certificate;
        certificate = generateCertificate(keyPair, "SHA256withRSA", "test.example.com");

        var privateKey = String.format(
                "-----BEGIN PRIVATE KEY-----\n%s\n-----END PRIVATE KEY-----",
                Base64.getMimeEncoder(64, System.getProperty("line.separator").getBytes())
                        .encodeToString(keyPair.getPrivate().getEncoded()));
        var publicKey = convertCertificateToPem(certificate);

        var createdCertificate = hetznerCloudAPI.createCertificate(
                CreateCertificateRequest.builder()
                        .name(keyId)
                        .privateKey(privateKey)
                        .certificate(publicKey)
                        .label(testUUIDLabelKey, testUUID)
                        .build());

        assertThat(createdCertificate).isNotNull();
        assertThat(createdCertificate.getCertificate().getId()).isGreaterThan(0);
        assertThat(hetznerCloudAPI.getCertificates(testUUIDLabelSelector).getCertificates()).hasSize(1);

        var certificate1 = hetznerCloudAPI.getCertificate(createdCertificate.getCertificate().getId());
        assertThat(certificate1.getCertificate().getId()).isEqualTo(createdCertificate.getCertificate().getId());
        assertThat(certificate1.getCertificate().getName()).isEqualTo(keyId);
    }

    @Test
    void testBasicLoadBalancer() {
        String keyId = UUID.randomUUID().toString();

        var loadBalancerType = hetznerCloudAPI.getLoadBalancerTypes().getLoadBalancerTypes().get(0);
        assertNotNull(loadBalancerType);
        var loadBalancerRequest = CreateLoadBalancerRequest.builder()
                .loadBalancerType(loadBalancerType.getId().toString())
                .publicInterface(true)
                .name(keyId)
                .location("fsn1")
                .label(testUUIDLabelKey, testUUID)
                .build();

        // create loadbalancer
        var createdLoadbalancer = hetznerCloudAPI.createLoadBalancer(loadBalancerRequest);
        assertNotNull(createdLoadbalancer);
        assertNotNull(createdLoadbalancer.getLoadBalancer());
        assertThat(hetznerCloudAPI.getLoadBalancers(testUUIDLabelSelector).getLoadBalancers()).hasSize(1);

        // wait for all actions to finish
        hetznerCloudAPI.getLoadBalancerActions(createdLoadbalancer.getLoadBalancer().getId()).getActions().forEach((action) -> {
            Awaitility.await().until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
        });

        // get loadbalancer
        var loadBalancer = hetznerCloudAPI.getLoadBalancer(createdLoadbalancer.getLoadBalancer().getId());
        assertNotNull(loadBalancer.getLoadBalancer());
        assertThat(loadBalancer.getLoadBalancer().getName()).isEqualTo(keyId);
    }

    @Test
    void testPlacementGroup() {
        String keyId = UUID.randomUUID().toString();
        CreatePlacementGroupRequest placementGroupRequest = CreatePlacementGroupRequest.builder()
                .name(keyId)
                .type(PlacementGroupType.spread)
                .label(testUUIDLabelKey, testUUID)
                .build();

        PlacementGroupResponse placementGroup = hetznerCloudAPI.createPlacementGroup(placementGroupRequest);
        assertNotNull(placementGroup);
        assertNotNull(placementGroup.getPlacementGroup());
    }

    @Test
    void testServerReset() {
        Map<String, String> keyPair = SshKeys.generate();
        String keyId = UUID.randomUUID().toString();

        var createKey = hetznerCloudAPI.createSSHKey(
                CreateSSHKeyRequest.builder()
                        .name(keyId)
                        .publicKey(keyPair.get("public"))
                        .label("label1", "value1")
                        .label(testUUIDLabelKey, testUUID)
                        .build());
        assertThat(createKey).isNotNull();

        var createServer = hetznerCloudAPI.createServer(
                CreateServerRequest.builder()
                        .name(keyId)
                        .serverType(ServerType.cpx11.name())
                        .publicNet(ServerPublicNetRequest.builder()
                                .enableIPv4(false)
                                .enableIPv6(true)
                                .build())
                        .image("ubuntu-22.04")
                        .label(testUUIDLabelKey, testUUID)
                        .sshKey(createKey.getSshKey().getId())
                        .build());

        hetznerCloudAPI.getServerActions(createServer.getServer().getId()).getActions().forEach((action) -> {
            Awaitility.await().until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
        });

        var resetAction = hetznerCloudAPI.resetServer(createServer.getServer().getId());
        assertThat(hetznerCloudAPI.getAction(resetAction.getAction().getId()).getAction().getStatus())
                .isIn("success", "running");
    }

    @Test
    void testDeprecationInfoNotNullInServerType() {
        // ID 11 = CCX11 which is deprecated
        var deprecatedServerType = hetznerCloudAPI.getServerType(11);
        assertThat(deprecatedServerType).isNotNull();

        // Assert that deprecation is not null
        assertThat(deprecatedServerType.getServerType().getDeprecation()).isNotNull();
        assertThat(deprecatedServerType.getServerType().getDeprecation().getAnnounced()).isNotNull();
    }

    @Test
    void testDeprecationInfoNullInServerType() {
        // ID 93 = CAX21 which is not deprecated
        var notDeprecatedServerType = hetznerCloudAPI.getServerType(93);
        assertThat(notDeprecatedServerType).isNotNull();

        // Assert that deprecation is null
        assertThat(notDeprecatedServerType.getServerType().getDeprecation()).isNull();
    }

    private Subnet getDefaultSubnet() {
        var subnet = new Subnet();
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

    private X509Certificate generateCertificate(KeyPair keyPair, String hashAlgorithm, String cn)
            throws Exception {
        Date notBefore = java.sql.Timestamp.valueOf(LocalDateTime.now());
        Date notAfter = java.sql.Timestamp.valueOf(LocalDateTime.now().plusYears(2));
        ContentSigner contentSigner = new JcaContentSignerBuilder(hashAlgorithm).build(keyPair.getPrivate());
        X500Name x500CN = new X500Name("CN=" + cn);
        X509v3CertificateBuilder certificateBuilder =
                new JcaX509v3CertificateBuilder(x500CN,
                        BigInteger.valueOf(System.currentTimeMillis()),
                        notBefore,
                        notAfter,
                        x500CN,
                        keyPair.getPublic())
                        .addExtension(Extension.subjectKeyIdentifier, false, createSubjectKeyId(keyPair.getPublic()))
                        .addExtension(Extension.authorityKeyIdentifier, false, createAuthorityKeyId(keyPair.getPublic()))
                        .addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
        return new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider()).getCertificate(certificateBuilder.build(contentSigner));
    }

    private SubjectKeyIdentifier createSubjectKeyId(final PublicKey publicKey) throws Exception {
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        DigestCalculator digCalc = new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));

        return new X509ExtensionUtils(digCalc).createSubjectKeyIdentifier(publicKeyInfo);
    }

    private AuthorityKeyIdentifier createAuthorityKeyId(final PublicKey publicKey) throws Exception {
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        DigestCalculator digCalc = new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));

        return new X509ExtensionUtils(digCalc).createAuthorityKeyIdentifier(publicKeyInfo);
    }

    private String convertCertificateToPem(final X509Certificate cert) throws IOException {
        final StringWriter writer = new StringWriter();
        final JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        pemWriter.writeObject(cert);
        pemWriter.flush();
        pemWriter.close();
        return writer.toString();
    }
}