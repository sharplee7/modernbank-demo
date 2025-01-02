$(function() {
	var customerApiUrl = $('#customerApiUrl').val();
	var contextPath = $('#contextPath').val();
	$("#cstmId").focus();
	$('#customerMenu').addClass('selected-menu');
	$("#btnCreate").on("click", function(){
		if(isValidInputValue() == true){
			var cstmId = $('#cstmId').val();
			var cstmNm = $('#cstmNm').val();
			var cstmAge = $("#cstmAge").val();
			var cstmGnd = $("input[name=cstmGnd]:checked").val();
			var cstmAdr = $('#cstmAdr').val();
			var cstmPn = $('#cstmPn').val();
			var customerData = {"cstmId" : cstmId,
							"cstmNm" : cstmNm,
							"cstmAge" : cstmAge,
							"cstmGnd" : cstmGnd,
							"cstmAdr" : cstmAdr,
							"cstmPn" : cstmPn};
			createCustomer(customerData);
		}
	});
	function isValidInputValue(){
		var cstmId = $('#cstmId').val();
		var cstmNm = $('#cstmNm').val();
		var cstmAge = $("#cstmAge").val();
		var cstmAdr = $('#cstmAdr').val();
		var cstmPn = $('#cstmPn').val();
		
		if(cstmId == "" || cstmId == null ){
			alert("고객ID를 입력해주세요.")
			$('#cstmId').focus();
			return false;
		}
		if(cstmNm == "" || cstmNm == null ){
			alert("고객명을 입력해주세요.")
			$('#cstmNm').focus();
			return false;
		}
		if( cstmAge == "" ||  cstmAge == null ){
			alert("나이를 입력해주세요.")
			$('#cstmAge').focus();
			return false;
		}
		if(isNaN(cstmAge) == true){
			alert("나이는 숫자만 입력해주세요.");
			$('#cstmAge').focus();
			return false;
		}
		if(cstmAge >= 999){
			alert("나이는 999 이하로 입력해주세요.");
			$('#cstmAge').focus();
			return false;
		}
		if(cstmAdr == "" || cstmAdr == null ){
			alert("주소를 입력해주세요.")
			$('#cstmAdr').focus();
			return false;
		}
		if(cstmPn == "" || cstmPn == null ){
			alert("전화번호를 입력해주세요.")
			$('#cstmPn').focus();
			return false;
		}
		return true;
	}
	function createCustomer(customerData){
		$.ajax({
			type : 'POST',
			url : customerApiUrl+'/',
			contentType: 'application/json',
			datatype : 'json',
			data : JSON.stringify(customerData),
			xhrFields : {
				withCredentials : true
			},
			crossDmain: true,
			beforeSend : function(xhr){
		        $('.wrap-loading').removeClass('display-none');
			},
			complete:function(){
		        $('.wrap-loading').addClass('display-none');
			},
			success: function(data, textStatus, jqXHR){
				alert("성공적으로 저장하였습니다.");
				$(location).attr("href", contextPath + "/customer/view/createCustomer");
		    },
		    error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
				$(location).attr("href", contextPath + "/customer/view/createCustomer");
		    }
		});
		
	}

});