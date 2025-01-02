$(function() {
	var contextPath = $('#contextPath').val();
	var accountApiUrl = $('#accountApiUrl').val();
	$("#acntNo").focus();
	$('#accountMenu').addClass('selected-menu');
	$("#btnSearch").on("click", function(){
		if(isValidInputValue() == true){
			var acntNm = $('#acntNm').val();
			var acntNo = $('#acntNo').val();
			var cstmId = $('#cstmId').val();
			var dpstAmt= $("#dpstAmt").val();
			var trnsBrnch = $("#trnsBrnch").val();
			var accountData = {"acntNo" : acntNo,
					           "acntNm" : acntNm,
							   "cstmId" : cstmId,
							   "trnsAmt" : dpstAmt,
							   "trnsBrnch" : trnsBrnch};
			createAccount(accountData);
		}
	});
	function isValidInputValue(){
		var acntNo = $('#acntNo').val();
		var acntNm = $('#acntNm').val();
		var dpstAmt = $('#dpstAmt').val();
		var cstmId = $("#cstmId").val();
		
		if( acntNo == "" || acntNo == null ){
			alert("계좌번호를 입력해주세요.");
			$('#acntNo').focus();
			return false;
		}
		if( dpstAmt == "" || dpstAmt == null ){
			alert("입금 금액을 입력해주세요.");
			$('#dpstAmt').focus();
			return false;
		}
		if(isNaN(dpstAmt) == true){
			alert('숫자만 입력해주세요.');
			$('#dpstAmt').focus();
			return false;
		}
		if(dpstAmt <= 0){
			alert('0 보다 큰 수를 입력해주세요.');
			$('#dpstAmt').focus();
			return false;
		}
		if( cstmId == "" ||  cstmId == null ){
			alert("고객ID를 입력해주세요.");
			$('#cstmId').focus();
			return false;
		}
		return true;
	}
	function createAccount(accountData){
		$.ajax({
			type : 'POST',
			url : accountApiUrl+'/',
			contentType: 'application/json',
			datatype : 'json',
			data : JSON.stringify(accountData),
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
			success : function(data, textStatus, jqXHR) {
		        alert("성공적으로 계좌를 개설했습니다.");
		        deposit(accountData);
			},
		    error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
				$(location).attr("href", contextPath + "/account/view/createAccount");
		    }
		});
	}
	function deposit(depositData){
		$.ajax({
			type : 'POST',
			url : accountApiUrl+'/deposit/',
			contentType: 'application/json',
			datatype : 'json',
			data : JSON.stringify(depositData),
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
				$(location).attr("href", contextPath + "/account/view/createAccount");
		    },
		    error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
				$(location).attr("href", contextPath + "/account/view/createAccount");
		    }
		});
		
	}
});