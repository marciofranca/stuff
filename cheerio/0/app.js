const request = require('request');
const cheerio = require('cheerio');
const fs = require('fs');

const ROOT_DIR = '/Users/marciofranca/dev/src/github/sysmapsolutions/sysmapsolutions.github.io/vagas';


request('http://sysmap.peoplenect.com/ats/external_applicant/?page=view_all_jobs', function (error, response, html) {
	if (!error && response.statusCode == 200) {
		var $all = cheerio.load(html);
		var items = $all('table.gridarea tr').has('td[class!="manu"]').map(function(index, element){
			var td = $all(element).find('td');
			var id = $all(td.get(1)).text().trim();
			var titulo = $all(td.get(2)).text().trim();
			var local = $all(td.get(3)).text().trim();
			var empresa = $all(td.get(0)).text().trim();

			return {
				"Código": id,
				"Título da vaga": titulo,
				"Local": local,
				"Empresa": empresa
			};
		}).get();
		write('all', items);

		for (var i = 0, len = items.length; i < len; i++) {
			const it = items[i];
			request('http://sysmap.peoplenect.com/ats/external_applicant/?page=view_jobs_details&job_id=' + it["Código"], function (error, response, html) {
				if (!error && response.statusCode == 200) {
					var $detail = cheerio.load(html);

					const object = {};

					$detail('.btmpadd table tr').has('td[align="left"] b').each(function(index, element){
						var td = $detail(element).find('td');
						var nome = $detail(td.get(0)).text().trim().replace(':', '');
						var valor = $detail(td.get(1)).text().trim();

						object[nome] = valor;
					});
					write(it["Código"], object);
				}else{
					console.error('ERROR: Error requesting view_jobs_details page for id '+ it["Código"] +'.', error || {code: response.statusCode});
				}
			});
		}
	}else{
		console.error('ERROR: Error requesting view_all_jobs page.', error || {code: response.statusCode});
	}
});

function write(name, object){
	fs.writeFile(ROOT_DIR + "/" + name + ".json", JSON.stringify(object, undefined, 2), function(error) {
		if(error) {
			console.error('ERROR: Error saving file ' + name + '.', error);
		}
	});
}