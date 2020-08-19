var exec = require( 'cordova/exec' );

exports.open = function ( arg0, success, error ) 
{
    exec( success, error, 'minipdvm8', 'open', [ arg0 ] );
};

exports.close = function ( arg0, success, error ) 
{
    exec( success, error, 'minipdvm8', 'close', [ arg0 ] );
};

exports.printText = function ( arg0, success, error ) 
{
    exec( success, error, 'minipdvm8', 'printText', [ arg0 ] );
};

exports.printQRCode = function ( arg0, success, error ) 
{
    exec( success, error, 'minipdvm8', 'printQRCode', [ arg0 ] );
};

exports.printTextBema = function ( arg0, success, error ) 
{
    exec( success, error, 'minipdvm8', 'printTextBema', [ arg0 ] );
};