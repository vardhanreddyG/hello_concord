import static groovyx.net.http.HttpBuilder.configure

import groovy.json.JsonBuilder
import groovy.json.internal.LazyMap
import groovyx.net.http.FromServer
import groovyx.net.http.HttpException
import java.util.regex.Matcher
import java.util.regex.Pattern

@SuppressWarnings('unused')
class AirWatchAPI {

    def http = null
    public AirWatchAPI(String apiUrl, String apiKey, String apiUser, String apiPass) {
        http = configure {
            request.uri = apiUrl
            request.headers['aw-tenant-code'] = apiKey
            request.headers['Content-Type'] = 'application/json'
            request.headers['Accept'] = 'application/json'
            request.auth.basic apiUser, apiPass
        }
    }
    ArrayList<LazyMap> getUserGroupMembers(Long userGroupDbId) {
        ArrayList<LazyMap> users = []
        def keepLooking = true
        int page = 0
        int size = 1000
        while (keepLooking) {
            try {
                LazyMap resp = http.get {
                    request.uri.path = "/api/system/usergroups/$userGroupDbId/users"
                    request.uri.query = [page: page, pagesize: size]
                    response.when(204) { FromServer fs ->
                        // this is okay, means no users found
                        [Total: 0, Products: []]
                    }
                }
                if (resp == null) {
                    // no results
                    return users
                }
                def total = resp['Total'] as Integer
                for (u in resp['EnrollmentUser']) {
                    users.add(u as LazyMap)
                }
                page++
                if (page * size >= total) {
                    keepLooking = false
                }
} catch (Exception ex) {
                println 'GET api/mdm/products/search rest call failed' + ex.getMessage()
                keepLooking = false
            }
        }
        return users
    }
/**
* Searches for Products
* @param productName Search value
* @param exact Provide as true to only return one Product that matches the search value exactly
* @return List of Products or an empty list
*/
    ArrayList<LazyMap> getProducts(String productName, boolean exact) {
        ArrayList<LazyMap> products = []
        def keepLooking = true
        int page = 0
        int size = 5
        while (keepLooking) {
            try {
                LazyMap resp = http.get {
                    request.uri.path = '/api/mdm/products/search'
                    request.uri.query = [name: productName, page: page, pagesize: size]
                    response.when(204) { FromServer fs ->
                        // this is okay, means no groups found
                        [Total: 0, Products: []]
                    }
                }
                if (resp == null) {
                    // no results
                    return products
                }
                def total = resp['Total'] as Integer
                for (p in resp['Products']) {
                    if (!exact || (exact && p['Name'] == productName)) {
                        products.add(p as LazyMap)
                    }
                }
                page++
                if (page * size >= total) {
                    keepLooking = false
                }
} catch (Exception ex) {
                println 'GET api/mdm/products/search rest call failed: ' + ex.getMessage()
                keepLooking = false
            }
        }
        return products
    }
/**
* Searches for Smart Groups
* @param smartGroupName Search value
* @param exact Provide as true to only return one Smart Group that matches the search value exactly
* @return List of Smart Groups or an empty list
*/
    ArrayList<LazyMap> getSmartGroup(String smartGroupName, boolean exact) {
        ArrayList<LazyMap> smartGroups = []
        def keepLooking = true
        int page = 0
        int size = 50
        while (keepLooking) {
            try {
                LazyMap resp = http.get {
                    request.uri.path = '/api/mdm/smartgroups/search'
                    request.uri.query = [name: smartGroupName, page: page, pagesize: size]
                    response.when(204) { FromServer fs ->
                        // this is okay, means no groups found
                        [Total: 0, Products: []]
                    }
                }
                if (resp == null) {
                    // no results
                    return smartGroups
                }
                def total = resp['Total'] as Integer
                for (sg in resp['SmartGroups']) {
                    if (!exact || (exact && sg['Name'] == smartGroupName)) {
                        smartGroups.add(sg as LazyMap)
                    }
                }
                page++
                if (page * size >= total) {
                    keepLooking = false
                }
} catch (Exception ex) {
                println 'GET api/mdm/smartgroups/search rest call failed' + ex.getMessage()
                keepLooking = false
            }
        }
        return smartGroups
    }
/**
* Searches for Organization Groups
* @param name Name of the Organization Group to search for
* @return List of Organization Groups or an empty list
*/
    ArrayList<LazyMap> getOrganizationGroups(String ogName, boolean exact) {
        ArrayList<LazyMap> groups = []
        def keepLooking = true
        int page = 0
        int size = 20
        while (keepLooking) {
            try {
                LazyMap resp = http.get {
                    request.uri.path = '/api/system/groups/search'
                    request.uri.query = [name: ogName, page: page, pagesize: size]
                    response.when(204) { FromServer fs ->
                        // this is okay, means no groups found
                        [Total: 0, LocationGroups: []]
                    }
                }
                if (resp == null) {
                    // no results
                    return groups
                }
                def total = resp['Total'] as Integer
                for (g in resp['LocationGroups']) {
                    if (!exact || (exact && g['Name'] == ogName)) {
                        groups.add(g as LazyMap)
                    }
                }
                page++
                if (page * size >= total) {
                    keepLooking = false
                }
} catch (Exception ex) {
                println 'GET api/system/groups/search rest call failed: ' + ex.getMessage()
                keepLooking = false
            }
        }
        return groups
    }
/**
* Searches for devices in a given organization group
* @param obDbId Organization Group database ID
* @return List of Devices or an empty list
*/
    ArrayList<LazyMap> getDevicesExtensiveSearch(Long ogDbId) {
        ArrayList<LazyMap> devices = []
        def keepLooking = true
        int page = 0
        int size = 20
        while (keepLooking) {
            try {
                LazyMap resp = http.get {
                    request.uri.path = '/api/mdm/devices/extensivesearch'
                    request.uri.query = [organizationgroupid: ogDbId, page: page, pagesize: size]
                    response.when(204) { FromServer fs ->
                        // this is okay, means no groups found
                        [Total: 0, LocationGroups: []]
                    }
                }
                if (resp == null) {
                    // no results
                    return devices
                }
                def total = resp['Total'] as Integer
                for (g in resp['Devices']) {
                    devices.add(g as LazyMap)
                }
                page++
                if (page * size >= total) {
                    keepLooking = false
                }
} catch (Exception ex) {
                println 'GET api/mdm/devices/extensivesearch rest call failed: ' + ex.getMessage()
                keepLooking = false
            }
        }
        return devices
    }
/**
* Gets Product details
* @param productId Database ID of the Product
* @return LazyMap representing Product attributes
*/
    LazyMap getProduct(Long productId) {
        try {
            def resp = http.get {
                request.uri.path = '/api/mdm/products/' + productId
                request.contentType = 'application/json'
            }
            return resp
} catch (Exception ex) {
            println 'GET api/mdm/products/productId rest call failed ' + ex.getMessage()
            ex.printStackTrace()
        }
        return null
    }
/**
* Gets Application details
* @param appId Database ID of the Application
* @return LazyMap representing Application attributes
*/
    LazyMap getApplication(Long appId) {
        try {
            def resp = http.get {
                request.uri.path = "/api/mam/apps/internal/$appId"
                request.contentType = 'application/json'
            }
            return resp
} catch (Exception ex) {
            println "GET api/mam/apps/internal/$appId rest call failed " + ex.getMessage()
            ex.printStackTrace()
        }
        return null
    }
/**
* Gets Provisioning Queue data
* @param groupId Database ID of the Organization Group to query
* @return LazyMap representing Provisioning Queue data or null
*/
    LazyMap getProvisioningQueueCounts(Long groupId) {
        return retry(2, { return new ArrayList<LazyMap>(0) }) {
            LazyMap jobInfo = null
            def retryableError
            try {
                jobInfo = http.get {
                    request.uri.path = "/api/mdm/products/$groupId/provisioningqueuecounts"
                    request.contentType = 'application/json'
                }
                if (jobInfo == null) { // no results
                    retryableError = 'No value returned for policy engine job count. Retrying...'
                }
} catch (HttpException ex) {
                println 'Error getting provisioning queue count: ' + ex.getMessage()
} catch (Exception ex) {
                if (ex.message.contains('Connection reset')) {
                    retryableError = "Socket interruption while getting provisioning queue count'. Retrying..."
                    println 'Ooof hit a socket interruption get provisioning queue count'
} else {
                    println 'Get provisioning queue count failed: ' + ex.getMessage()
                }
            }
            if (retryableError) {
                throw new Exception(retryableError as String)
            }
            return jobInfo
} as LazyMap
}
/**
* Gets Smart Group details
* @param smartGroupId Database ID of the Smart Group
* @return LazyMap representing Smart Group attributes
*/
    LazyMap getSmartGroup(Long smartGroupId) {
        try {
            def resp = http.get {
                request.uri.path = "/api/mdm/smartgroups/$smartGroupId"
                request.contentType = 'application/json'
            }
            return resp
} catch (Exception ex) {
            println "GET api/mdm/smartgroups/$smartGroupId rest call failed " + ex.getMessage()
            ex.printStackTrace()
        }
        return null
    }
/**
* Searches for User Groups
* @param ugName User Group name for which to perform the search
* @param exact Provide as true to only return one User Group that matches the search value exactly
* @return List of User Groups or an empty List
*/
    ArrayList<LazyMap> getUserGroups(String ugName, boolean exact) {
        def userGroups = []
        try {
            LazyMap resp = http.get {
                request.uri.path = '/api/system/usergroups/custom/search'
                request.uri.query = [groupname: ugName]
                response.when(204) { FromServer fs ->
                    // this is okay, means no groups found
                    [Total: 0, UserGroup: []]
                }
            }
            if (resp == null) {
                // no results
                return userGroups
            }
            for (ug in resp['UserGroup']) {
                if (!exact || (exact && ug['UserGroupName'] == ugName)) {
                    userGroups.add(ug as LazyMap)
                }
            }
} catch (Exception ex) {
            println 'GET api/system/usergroups/custom/search rest call failed' + ex.getMessage()
        }
        return userGroups
    }
    boolean changeDeviceOrganizationGroup(Long deviceId, Long groupId) {
        boolean success = false
        try {
            http.put {
                request.uri.path = "/api/mdm/devices/$deviceId/commands/changeorganizationgroup/$groupId"
                request.contentType = 'application/json'
                request.body = ''
            }
            success = true
} catch (HttpException ex) {
            println 'Change Device OG REST Call failed: ' + ex.getMessage()
} catch (Exception ex) {
            println 'Change Device OG REST Call failed: ' + ex.getMessage()
        }
        return success
    }
    boolean addSmartGroupToProduct(Long smartGroupId, Long productId) {
        boolean success = false
        try {
            http.post {
                request.uri.path = "/api/mdm/products/$productId/addsmartgroup/$smartGroupId"
                request.contentType = 'application/json'
                request.body = ''
            }
            success = true
} catch (HttpException ex) {
            println 'Add Smart Group to Product REST Call failed: ' + ex.getMessage()
} catch (Exception ex) {
            println 'Add Smart Group to Product failed: ' + ex.getMessage()
        }
        return success
    }
/**
* Removes an existing Smart Group from a Product
* @param smartGroupId database ID of the Smart Group to remove
* @param productId database ID of the Product from which the Smart Group will be removed
* @return true on succsessful removal
*/
    boolean removeSmartGroupFromProduct(Long smartGroupId, Long productId) {
        boolean success = false
        try {
            http.post {
                request.uri.path = "/api/mdm/products/$productId/removesmartgroup/$smartGroupId"
                request.contentType = 'application/json'
                request.body = ''
            }
            success = true
} catch (HttpException ex) {
            println 'Remove Smart Group from Product REST Call failed: ' + ex.getMessage()
} catch (Exception ex) {
            println 'Remove Smart Group from Product failed: ' + ex.getMessage()
        }
        return success
    }
/**
* Searches for Smart Groups
* @param sgName Search value
* @param exact Provide as true to only return one Smart Group that matches the search value exactly
* @return List of Smart Groups or empty List
*/
    ArrayList<LazyMap> getSmartGroups(String sgName, boolean exact) {
        def smartGroups = []
        try {
            LazyMap resp = http.get {
                request.uri.path = '/api/mdm/smartgroups/search'
                request.uri.query = [name: sgName]
            }
            if (resp == null) { // no results
                return smartGroups
            }
            for (sg in resp['SmartGroups']) {
                if (!exact || (exact && sg.get('Name').equals(sgName))) {
                    smartGroups.add(sg as LazyMap)
                }
            }
} catch (Exception ex) {
            println 'getSmartGroups failed: ' + ex.getMessage()
        }
        return smartGroups
    }
/**
* Searches for Users
* @param userSearchVal Search value
* @param exact Provide as true to only return one User that matches the search value exactly
* @return List of Users or empty List
*/
    ArrayList<LazyMap> getUsers(String userSearchVal, boolean exact) {
        return retry(2, { return new ArrayList<LazyMap>(0) }) {
            def users = []
            def retryableError
            try {
                LazyMap resp = http.get {
                    request.uri.path = '/api/system/users/search'
                    request.uri.query = [username: userSearchVal]
                }
                if (resp == null) { // no results
                    return users
                }
                for (u in resp['Users']) {
                    if (!exact || (exact && u.get('UserName').equals(userSearchVal))) {
                        users.add(u as LazyMap)
                    }
                }
} catch (HttpException ex) {
                println 'Error looking up users: ' + ex.getMessage()
} catch (Exception ex) {
                if (ex.message.contains('Connection reset')) {
                    retryableError = "Socket interruption on '$userSearchVal'. Retrying..."
                    println "Ooof hit a socket interruption on '$userSearchVal'"
} else {
                    println "getUsers failed on '$userSearchVal': " + ex.getMessage()
                }
            }
            if (retryableError) {
                throw new Exception(retryableError as String)
            }
            return users
} as ArrayList<LazyMap>
}
    def retry(int times = 5, Closure errorHandler = { e -> println e.message }
, Closure body) {
        int retries = 0
        ArrayList<Exception> exceptions = []
        while (retries++ < times) {
            try {
                return body.call()
} catch (e) {
                exceptions.add(e)
                errorHandler.call(e)
            }
        }
        throw new MultipleFailureException("Failed after $times retries", exceptions)
}
    class MultipleFailureException extends Exception {
        MultipleFailureException(String msg, ArrayList<Exception> exceptions) {
super(msg)
        }

    }
/**
* Creates a User Group
* @param name Name of the User Group
* @param description Optional description of the User Group
* @param locationGroupId Root group database ID. If not a customer group,
* API will default to nearest customer group
* @return Database ID of the new User Group or -1 on error
*/
    Long createUserGroup(String name, String description = '', long locationGroupId) {
        try {
            def ugJson = new JsonBuilder()
            ugJson GroupName: name,
Description: description,
ManagedByOrganizationGroupID: locationGroupId
            def resp = http.post {
                request.uri.path = '/api/system/usergroups/createcustomusergroup'
                request.contentType = 'application/json'
                request.body = ugJson.toString()
            }
            return resp['Value'] as Integer
} catch (HttpException ex) {
            println 'User Group Creation failed: ' + ex.getMessage()
} catch (Exception ex) {
            println 'User Group Creation failed: ' + ex.getMessage()
        }
        return -1
    }
/**
* Creates a Smart Group
@param sgName Name of new Smart Group
@param userGroupIds Array of User Group database IDs to add
@param organizationGroupIds Array of Organization Group database IDs to allow
@param locationGroupId Managing Organization Group database ID
@return New Smart Group ID or -1 on failure
*/
    Long createSmartGroup(String sgName, Long[] userGroupIds, Long[] organizationGroupIds, Long locationGroupId) {
        try {
            def sgJson = new JsonBuilder()
            sgJson Name: sgName,
CriteriaType: 'All',
ManagedByOrganizationGroupId: locationGroupId,
OrganizationGroups:
organizationGroupIds.collect { ogId -> [Id: ogId] as LazyMap },
UserGroups:
userGroupIds.collect { ugId -> [Id: ugId] as LazyMap }
            def resp = http.post {
                request.uri.path = '/api/mdm/smartgroups'
                request.contentType = 'application/json'
                request.body = sgJson.toString()
            }
            return resp['Value'] as Integer
} catch (Exception ex) {
            println 'Smart Group Creation failed: ' + ex.getMessage()
        }
        return -1L
    }
/**
* Uploads an apk to AirWatch
* @param apkName filename of the apk (e.g. Test_App_1.2.apk)
* @param rootGroup Organization Group to which the blob will be installed
* @param apkFile {@link File} object to upload
* @return Database ID of the blob or -1 on error
*/
    Long uploadBlob(String apkName, Long rootGroup, File apkFile) {
        try {
            def resp = http.post {
                request.uri.path = '/api/mam/blobs/uploadblob'
                request.contentType = 'application/json'
                request.uri.query = [FileName: apkName, organizationgroupid: rootGroup]
                request.body = apkFile.bytes
            }
            return resp['Value'] as Long
} catch (Exception ex) {
            println 'Upload blob failed: ' + ex.getMessage()
        }
        return -1L
    }
/**
* Installs a blob as an application
* @param appName Name of the app
* @param blobId Database ID of the blob to install (get with installBlob)
* @param rootGroup Organization Group in which the application will be created
* @param apkName File name of the apk (same as one used with installBlob)
* @return Database ID of the application or -1 on error
*/
    Long installBlob(String appName, Long blobId, Long rootGroup, String apkName) {
        try {
            def json = new JsonBuilder()
            json ApplicationName: appName,
BlobId: blobId,
EnableProvisioning: true,
DeviceType: 'Android',
LocationGroupId: rootGroup,
PushMode: 'Auto',
SupportedModels:
[Model:[[ModelName: 'Android'] as LazyMap]] as LazyMap
            def resp = http.post {
                request.uri.path = '/api/mam/apps/internal/begininstall'
                request.uri.query = [FileName: apkName, organizationgroupid: rootGroup]
                request.contentType = 'application/json'
                request.body = json.toString()
            }
            return resp['Id']['Value'] as Long
} catch (HttpException ex) {
            println 'Install blob failed: ' + ex.getMessage()
            LazyMap body = ex.getBody() as LazyMap
            if (body != null && body.containsKey('message')) {
                println ex['body']['message']
            }
} catch (Exception ex) {
            println 'Install blob failed: ' + ex.getMessage()
        }
        return -1L
    }
/**
* Creates a product with app actions
* @param smartGroupIds Array of smart group database IDs
* @param appIds Array of Application database IDs
* @param rootGroup Organization Group database ID in which the Product will be maintained
* @param productName Name of the product
* @return Database ID of the new Product or -1 on error
*/
    Long createProduct(Long[] smartGroupIds, Long[] appIds, Long rootGroup, String productName) {
        // Build JSON
        def productJson = new JsonBuilder()
        int stepSequence = 0
        productJson MaintainGeneralInput: [
LocationGroupID: rootGroup,
InsertOnly: false
] as LazyMap,
Product: [
Description: 'Auto-created Product',
Name: productName,
PauseResume: false,
Platform: 5,
ProductType: 0,
SmartGroups:
smartGroupIds.collect { sgId -> [SmartGroupID: sgId] as LazyMap },
Steps: appIds.collect { id ->
[
StepType: 3,
SequenceNumber: stepSequence++,
ApplicationID: id,
Persist: false
]
}
] as LazyMap
        try {
            def resp = http.post {
                request.uri.path = '/api/mdm/products/maintainproduct'
                request.contentType = 'application/json'
                request.body = productJson.toString()
            }
            String regexPattern = 'Successfully created ' + productName + "\\s?\\((\\d+)\\) in Location Group"
            Pattern p = Pattern.compile(regexPattern)
            Matcher m = p.matcher(resp.toString())
            if (m.find()) {
                return m.group(1) as Long
} else {
                println 'Failed to parse Product ID.'
                return 0
            }
} catch (Exception ex) {
            println 'Create Product call call failed: ' + ex.getMessage()
        }
        return -1L
    }
/**
* Activates a Product
* @param productId Database ID of the Product to be activated
* @return true on success, false on error
*/
    boolean activateProduct(Long productId) {
        try {
            String productUrl = "/api/mdm/products/$productId/activate"
            http.post {
                request.uri.path = productUrl
                request.contentType = 'application/json'
                request.body = ''
            }
            return true
} catch (Exception ex) {
            println 'Activate Product call failed: ' + ex.getMessage()
        }
        return false
    }
/**
* Deactivates a Product
* @param productId Database ID of the Product to be deactivated
* @return true on success, false on error
*/
    boolean deactivateProduct(Long productId) {
        try {
            String productUrl = "/api/mdm/products/$productId/deactivate"
            http.post {
                request.uri.path = productUrl
                request.contentType = 'application/json'
                request.body = ''
            }
            return true
} catch (Exception ex) {
            println 'Deactivate Product call failed: ' + ex.getMessage()
        }
        return false
    }
/**
* Adds a User to a User Group
* @param userDbId Database ID of User
* @param userGroupDbID Database ID of User Group
* @return true on success, false on error
*/
    boolean addUserToUserGroup(long userDbId, long userGroupDbId) {
        return retry(2, { return false }) {
            def retryError
            try {
                http.post {
                    request.uri.path = "/api/system/usergroups/$userGroupDbId/user/$userDbId/addusertogroup"
                    request.contentType = 'application/json'
                    request.body = ''
                }
                return true
} catch (HttpException ex) {
                if ((ex.body['message'] as String).contains('Enrollment User is already assigned to the User Group')) {
                    println "User ($userDbId) already in User Group ($userGroupDbId)"
                    return true
                }
} catch (Exception ex) {
                if (ex.message.contains('Connection reset')) {
                    retryError = 'Socket interruption. Retrying...'
} else {
                    println 'Add User to User Group failed: ' + ex.getMessage()
                }
            }
            if (retryError) {
                throw new Exception(retryError as String)
            }
    }
}
/**
* Deletes a Product
* @param productId Database ID of the Product to be deleted
* @return true on success, false on error
*/
    boolean deleteProduct(Long productId) {
        try {
            http.delete {
                request.uri.path = "/api/mdm/products/$productId"
                request.contentType = 'application/json'
            }
            return true
} catch (Exception ex) {
            println 'Delete Product call failed: ' + ex.getMessage()
            LazyMap body = ex.getBody() as LazyMap
            if (body != null && body.containsKey('message')) {
                println 'Error Code: ' + ex['body']['errorCode']
                String errormsg = ex['body']['message']
                println 'Message: ' + errormsg
                execution.setVariable('execError', errormsg)
            }
        }
        return false
    }
/**
* Deletes an Application
* @param appId Database ID of the Application to be deleted
* @return true on success, false on error
*/
    boolean deleteApplication(Long appId) {
        try {
            http.delete {
                request.uri.path = "/api/mam/apps/internal/$appId"
                request.contentType = 'application/json'
            }
            return true
} catch (Exception ex) {
            println 'Delete Application call failed: ' + ex.getMessage()
        }
        return false
    }
/**
* Deletes Smart Group
* @param smartGroupId Database ID of the Smart Group to be deleted
* @return true on success, false on error
*/
    boolean deleteSmartGroup(Long smartGroupId) {
        try {
            http.delete {
                request.uri.path = "/api/mdm/smartgroups/$smartGroupId"
                request.contentType = 'application/json'
            }
            return true
} catch (Exception ex) {
            println 'Delete Smart Group call failed: ' + ex.getMessage()
            LazyMap body = ex.getBody() as LazyMap
            if (body != null && body.containsKey('message')) {
                println 'Error Code: ' + ex['body']['errorCode']
                String errormsg = ex['body']['message']
                println 'Message: ' + errormsg
                execution.setVariable('execError', errormsg)
            }
        }
        return false
    }
/**
* Deletes User Group
* @param userGroupId Database ID of the User Group to be deleted
* @return true on success, false on error
*/
    boolean deleteUserGroup(Long userGroupId) {
        try {
            http.delete {
                request.uri.path = "/api/system/usergroups/$userGroupId/delete"
                request.contentType = 'application/json'
            }
            return true
} catch (Exception ex) {
            println 'Delete User Group call failed: ' + ex.getMessage()
        }
        return false
    }

}
