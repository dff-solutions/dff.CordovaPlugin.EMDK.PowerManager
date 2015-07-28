## Sygic Navigation Cordova Plugin - JavaScript Interface

### Modules

The interface is devided in several modules:
- com/dff/cordova/plugins/sygic
- com/dff/cordova/plugins/sygic/api
- com/dff/cordova/plugins/sygic/api/dialog
- com/dff/cordova/plugins/sygic/api/info
- com/dff/cordova/plugins/sygic/api/itinerary
- com/dff/cordova/plugins/sygic/api/location
- com/dff/cordova/plugins/sygic/api/maps
- com/dff/cordova/plugins/sygic/api/navigation
- com/dff/cordova/plugins/sygic/api/options
- com/dff/cordova/plugins/sygic/api/tts
- com/dff/cordova/plugins/sygic/model

these can be loaded with **`require`** e.g.

    var sygicmodel = require('com.dff.cordova.plugins.sygic.model');
    var sygicapi = require('com.dff.cordova.plugins.sygic.api');

### Global object

There is also a global **`Sygic`** object that already includes the model module:

    Sygic.model.SygicAddress
    Sygic.model.SygicPosition
    Sygic.model.SygicRectangle
    Sygic.model.SygicWayPoint
    Sygic.model.SygicStopOffPoint

So, you can create a new SygicAddress like:

    var sygicAddress = new Sygic.model.SygicAddress(
        "DEU",
        "Göttingen",
        "Berliner Str.",
        "12"
    );

It can be used to get a new instance of the javascript sygic javascript interface:

    var sygicTruck = Sygic.getInstance(Sygic.ApplicationId.SygicTruck);

An instance already includes all api modules:

    sygicTruck.api
    sygicTruck.api.dialog
    sygicTruck.api.info
    sygicTruck.api.itinerary
    sygicTruck.api.location
    sygicTruck.api.maps
    sygicTruck.api.navigation
    sygicTruck.api.options
    sygicTruck.api.tts

For example, if sound should be played via **TTS** use:

    sygicTruck.api.tts.playSound(success, error, {
            text: "Hello Sygic!"
        });

### Api Parameters

As it can be seen from the example above all api functions require three arguments
- `success` **function** - callback for success
- `error`   **function** - callback for error
- `args`    **Object**   - arguments for the sygic functionality

### Events

Events from Sygic Navigation are fired to the DOM document object.
The types of events are:
- `<ApplicationId> + "ServiceEvent"`
- `<ApplicationId> + "AppEvent"`

To listen to sygic events the **`getFeature()`** function of the sygic instance can be used. E.g.:

    document.addEventListener(sygicTruck.getFeature() + "ServiceEvent", onServiceEvent, false);
    document.addEventListener(sygicTruck.getFeature() + "AppEvent", onAppEvent, false);

#### Service Events
Service events just contain a message property which says `"connected"` or `"disconnected"`.

#### Application Events
Application events are based on the [Java API](http://developers.sygic.com/reference/java3d/html/classcom_1_1sygic_1_1sdk_1_1remoteapi_1_1events_1_1_api_events.html).
An app event object contains a **`message`** property related to the Java API and also a **`code`** property.
Codes are drescripted in [Sygic.events](module-com_dff_cordova_plugins_sygic-Sygic.html#.events).
Some events send data in the **`data`** property.
