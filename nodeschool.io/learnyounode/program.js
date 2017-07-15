var fs = require('fs');
var path = require('path');

fs.readdir(process.argv[2], function(err, list){
	for(var i = 0; i < list.length; i++){
		var item = list[i];

		if(path.extname(item) == "." + process.argv[3]){
			console.log(item);
		}
	}
});
