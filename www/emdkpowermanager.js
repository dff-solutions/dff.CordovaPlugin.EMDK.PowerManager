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

/**
 * Activate wwan.
 *
 */
self.wwanTurnOn = function (success, error, args) {
    cordova.exec(success, error, feature, "wwanTurnOn", [args]);
};

/**
 * Activate wwan.
 *
 */
self.wwanTurnOff = function (success, error, args) {
    cordova.exec(success, error, feature, "wwanTurnOff", [args]);
};

module.exports = self;