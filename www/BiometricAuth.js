var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'BiometricAuth', 'coolMethod', [arg0]);
};

exports.testBiometric = function (arg0, success, error) {
    exec(success, error, 'BiometricAuth', 'testBiometric', [arg0]);
};
