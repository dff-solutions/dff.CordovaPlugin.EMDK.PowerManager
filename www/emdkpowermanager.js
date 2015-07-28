/**
 * JavaScript interface to abstract
 * the usage of the cordova EMDK Power Manager plugin.
 *
 * @module com/dff/cordova/plugins/emdk/powermanager
 */

'use strict';

var cordova = require('cordova');

var feature = "EMDKPowerManager";
var self = {};

/**
 * Reboot device.
 *
 */
self.reboot = function (success, error, args) {
    cordova.exec(success, error, feature, "reboot", [args]);
};

module.exports = self;