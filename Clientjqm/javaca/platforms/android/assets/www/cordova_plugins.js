cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/org.hygieiasoft.cordova.uid/www/uid.js",
        "id": "org.hygieiasoft.cordova.uid.uid",
        "clobbers": [
            "cordova.plugins.uid"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "org.hygieiasoft.cordova.uid": "1.1.0"
}
// BOTTOM OF METADATA
});