## EMDK PowerManager Cordova Plugin - JavaScript Interface

### Module

Module can be loaded with **`require`**

    var emdkpowermanager = require('com.dff.cordova.plugins.emdk.powermanager.emdkpowermanager')

### Global object

There is a global **`EMDKPowerManager`** object that can be used to access the EMDK PowerManager functionality

### Api

#### Reboot
To reboot a device use **`EMDKPowerManager.reboot(success, error)`**

    EMDKPowerManager
        .reboot(function (value) {
            console.log("value", value);
        }, function (reason) {
       	     console.error("reason", reason);
       	 });
