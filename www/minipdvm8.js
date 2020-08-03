var exec = require( 'cordova/exec' );

exports.printText = function ( arg0, success, error ) 
{
    exec( success, error, 'minipdvm8', 'printText', [ arg0 ] );
};

exports.printQRCode = function ( arg0, success, error ) 
{
    exec( success, error, 'minipdvm8', 'printQRCode', [ arg0 ] );
};
