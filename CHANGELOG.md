# For next release
  * **Tan Siewert**
    * server: add userData property to rebuild request
    * storagebox: add new name field to subaccount
    * error: add new must_be_unassigned code

*Not released yet*

# Patch Release v5.0.1 (2025-10-24)
  * **Tan Siewert**
    * server: add new SCO tariffs

*Released by Tan Siewert <tan@siewert.io>*

# Major Release v5.0.0 (2025-10-07)
  * **Tan Siewert**
    * runtime: bump java requirement to jdk 17
    * api: send proper user-agent
    * api: add support for api.hetzner.com
    * servertype: add locations and category attributes
    * loadbalancer: fix wrong mapping, add missing items
    * loadbalancer: deprecate old network attachment methods
    * storagebox: add implementation
    * dns: add implementation

*Released by Tan Siewert <tan@siewert.io>*

# Patch Release v4.0.1 (2025-02-10)
  * **Tan Siewert**
    * maven: fix deployment process

*Released by Tan Siewert <tan@siewert.io>*

# Major Release v4.0.0 (2025-02-10)
  * **Tan Siewert**
    * Change namespace to io.github.sinuscosinustan

*Released by Tan Siewert <tan@siewert.io>*

# Patch Release v3.2.7 (2024-11-02)
  * **n0ise9914**
    * api: add missing error codes
  * **ogdabou**
    * pricing: make Price class public

*Released by Tan Siewert <tan@siewert.io>*

# Patch Release v3.2.6 (2024-06-07)
  * **Tom Siewert**
    * servertype: add new intel types to enum

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v3.2.5 (2024-03-27)
  * **Tom Siewert**
    * Update dependencies
  * **litetex**
    * firewall: make getFirewall public

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v3.2.4 (2024-02-01)
  * **easystartup-io**
    * servertype: add getServerTypes paginated

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v3.2.3 (2024-01-16)
  * **Christian Pelster**
    * certificate: add missing type field
  * **easyStartup-pulse**
    * loadbalancer: add getByName

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v3.2.2 (2024-01-10)
  * **easyStartup-pulse**
    * loadbalancer: fix algorithm flag in create request

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v3.2.1 (2024-01-08)
  * **Tom Siewert**
    * ISO: Remove "deprecated" field
    * ISO: add missing deprecation field
  * **easyStartup-pulse**
    * Adding support for firewall as input parameter for create server

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v3.2.0 (2023-08-23)
  * **Tom Siewert**
    * ServerType: Add CCX*3 types
    * ServerType: Add new deprecation flag
    * API: Deprecate getActions functions
    * Network: Add `expose_routes_to_vswitch` flag
    * ServerType: Add `included_traffic` flag

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v3.1.0 (2023-04-24)
  * **Tom Siewert**
    * api: add arm support

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v3.0.2 (2023-01-10)
  * **Arzhang Mosaffa**
    * PrimaryIP: Fix update function

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v3.0.1 (2022-12-29)
  * **Arzhang Mosaffa**
    * API: Prevent OkHttp POST with null body
    
      OkHttp doesn't let to send post requests with null body, and also the body (with empty json object) is required by hetzner cloud api.
  * **Tom Siewert**
    * API: Add method to define OkHttpClient instance
    * PrimaryIP: Do not include null values for PublicNetRequest
    * test: Add server reset test

*Released by Tom Siewert <tom@siewert.io>*

# Major Release v3.0.0 (2022-12-23)
  * **Christian Pelster**
    * API: Migrate from Spring RestTemplate to OkHttp
  * **Felix Krasemann**
    * API: Add firewall support
    * Datacenter: Fix name filter and remove incorrect field
  * **Tom Siewert**
    * pom: Upgrade nexus staging plugin to 1.6.13
    * ci: Enable tests on push
    * ci: Do not run e2e tests parallel
    * FloatingIP: Drop obsolete HomeLocation class
    * FloatingIP: Use generic IP type enum
    * API: Add Primary IP support
    * Pricing: Make Pricing class more generic
    * Move enums to enums package
    * Actions: Add action status filter
    * API: Remove deprecated methods
    * API: Rename Request classes to make them more consistent

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.16.0 (2022-05-31)
  * **Christian Pelster**
    * Add label support for SSH key creation
  * **Tom Siewert**
    * API: Fix getSSHKey methods for name and fingerprint

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.15.0 (2022-05-21)
  * **Tom Siewert**
    * pom: fix sonatype plugin
    * Update dependencies
    * pom: Add missing version tags to build plugins
    * API: Formatting and cleanup
    * LoadBalancer: Add missing LoadBalancerTypesResponse
    * API: Add pagination support
    * API: Add methods for actions
    * tests: be more resilient against parallel runs

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.14.0 (2022-05-11)
  * **Tom Siewert**
    * ci: Migrate to GitHub workflows
    * project: Use JDK 11 for build target
    * pom: use pinentry mode to make gpg ci ready
    * Certificates: Add missing endpoints for managed certs
    * ChangeType: Add new server types
    * FloatingIP: Use Location instead of separate class
    * Location: Add Network zone
    * Network: Prepare for IP / Server separation
    * Server: Add missing attributes

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v2.13.1 (2022-04-04)
  * **Tom Siewert**
    * Update dependencies

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.13.0 (2021-09-08)
  * **Asaf Ben Natan**
    * Add basic E2E Test suite
    * Remove Shade Plugin
    * Fix several LoadBalancer objects
    * Add Subnet and Target type enums
  * **Tom Siewert**
    * Remove Travis CI
    * Migrate to Gitlab CI
    * Add Placement groups feature

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.12.0 (2021-05-19)
  * **Tom Siewert**
    * Add Load Balancer Type Endpoints

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v2.11.2 (2021-02-28)
  * **Pierre Schwang**
    * DateDeserializer: Use ThreadLocal for faster access time

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v2.11.1 (2021-02-28)
  * **Tom Siewert**
    * pom: Do not use shaded artifact as primary
    * DateDeserializer: Make Formatter thread-safe

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.11.0 (2020-11-10)
  * **Tom Siewert**
    * Add Load Balancer support
    * Add vSwitch ID to Private Network Subnet request

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v2.10.1 (2020-04-23)
  * **Tom Siewert**
    * Update dependencies

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.10.0 (2020-04-15)
  * **Tom Siewert**
    * Add CPX server types

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.8.0 (2019-10-03)
  * **Tom Siewert**
    * Add name key to Floating IP

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v2.7.1 (2019-08-11)
  * **Tom Siewert**
    * Fix wrong endpoint in HetznerCloudAPI#enableBackup

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.7.0 (2019-08-11)
  * **Tom Siewert**
    * Add Private network support
    * Move Action class to general package
    * Add label support to missing objects
    * Add support for updating Floating IPs
    * Add missing variables to general objects

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v2.6.1 (2019-05-11)
  * **Tom Siewert**
    * Use singular lombok annotations for labels and volume requests

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.6.0 (2019-05-11)
  * **Tom Siewert**
    * Add volume mount support on server creation
    * Deprecate HetznerCloudAPI#changeSSHKeyName 
      The replacement will be HetznerCloudAPI#updateSSHKey
    * Fix NullPointerException on volume creation when format is not set

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v2.5.1 (2019-04-05)
  * **Tom Siewert**
    * Fix NullPointerException when creating a server without SSH key
    * Make server type name case-insensitive
    * Add missing parameters for Volumes

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.5.0 (2019-01-27)
  * **Christian Pelster**
    * Add label support

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.4.0 (2018-11-14)
  * **Tom Siewert**
    * Add next_actions parameter to volume and server actions

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.3.0 (2018-10-17)
  * **Tom Siewert**
    * Add Volumes
    * Update jackson-databind for CVE-2018-7489

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v2.2.1 (2018-08-07)
  * **Tom Siewert**
    * Add CCX server types

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.2.0 (2018-04-21)
  * **Tom Siewert**
    * Add Protection-flag support

*Released by Tom Siewert <tom@siewert.io>*

# Patch Release v2.1.1 (2018-04-05)
  * **Tom Siewert**
    * Add deprecated parameter to Image

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v2.1.0 (2018-03-29)
  * **Tom Siewert**
    * Add support for creating Servers with SSH-Key name

*Released by Tom Siewert <tom@siewert.io>*

# Major Release v2.0.0 (2018-03-12)
  * **Tom Siewert**
    * Code Improvements
    * Rename objects to be consistent

*Released by Tom Siewert <tom@siewert.io>*

# Minor Release v0.1.0 (2018-01-26)
  * **Tom Siewert**
    * Init

*Released by Tom Siewert <tom@siewert.io>*
