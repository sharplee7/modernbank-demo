$(function() {
	var contextPath = $('#contextPath').val();
	var accountApiUrl = $('#accountApiUrl').val();
	$('#withdrawResult_container').hide();
	$("#acntNo").focus();
	$('#accountMenu').addClass('selected-menu');
	$("#btnSearch").on("click", function(){
		var acntNo = $('#acntNo').val();
		if(acntNo == null || acntNo == ""){
			alert("계좌번호를 입력해주세요.");
			$('#acntNo').focus();
		}
		else{
			retrieveAccountInfo(acntNo);
		}
	});
	$('#acntNo').on('change',function(){
		$('#btnCreate').hide();
	});
	$('#btnCreate').on("click",function(){
		if(isValidInputValue() == true){
			var trnsAmt = $('#trnsAmt').val();
			var acntNo = $('#acntNo').val();
			var trnsBrnch = $('#trnsBrnch').val();
			var withdrawData = {"acntNo" : acntNo,
							   "trnsAmt" : trnsAmt,
							   "trnsBrnch" : trnsBrnch};
			withdraw(withdrawData);
		}
	});
	function retrieveAccountInfo(acntNo){
		$.ajax({
			type : 'GET',
			url : accountApiUrl+'/'+acntNo,
			contentType: 'application/json',
			datatype : 'json',
			xhrFields : {
				withCredentials : true
			},
			crossDmain: true,
			beforeSend : function(){
		        $('.wrap-loading').removeClass('display-none');
			},
			complete:function(){
		        $('.wrap-loading').addClass('display-none');
			},
			success : function(data) {
				$('td[name=acntNm]').text(data.acntNm);
				$('td[name=acntNo]').text(data.acntNo);
				$('td[name=cstmId]').text(data.cstmId);
				$('td[name=cstmNm]').text(data.cstmNm);
				$('#acntBlnc').text(data.acntBlnc);
				$('#btnCreate').show();
			},
		    error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
		    }
		});
		
	}
	function withdraw(withdrawData){
		$.ajax({
			type : 'POST',
			url : accountApiUrl+'/withdraw/',
			contentType: 'application/json',
			datatype : 'json',
			data : JSON.stringify(withdrawData),
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
		    	$('#withdrawResult_container').show();
		    	$('#withdraw_container').hide();
		    	$('#resultAcntNo').text(data.acntNo);
		    	$('#resultFormerBlnc').text(data.formerBlnc);
		    	$('#resultTrnsAmt').text(data.trnsAmt);
		    	$('#resultAcntBlnc').text(data.acntBlnc);
		   },
		   error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
				$(location).attr("href", contextPath + "/account/view/withdraw");
		    }
		});
		
	}
	function isValidInputValue(){
		var trnsAmt = $('#trnsAmt').val();
		var acntNo = $('#acntNo').val();
		var acntBlnc = $('#acntBlnc').text();
		if(acntNo== "" || acntNo == null ){
			alert("계좌번호를 입력해주세요.")
			$('#acntNo').focus();
			return false;
		}
		if(trnsAmt == "" || trnsAmt == null ){
			alert("입금 금액을 입력해주세요.")
			$('#trnsAmt').focus();
			return false;
		}
		if(isNaN(trnsAmt) == true){
			alert("숫자만 입력해주세요.");
			$('#trnsAmt').focus();
			return false;
		}
		if(trnsAmt <= 0){
			alert("0 보다 큰 수를 입력해주세요.");
			$('#trnsAmt').focus();
			return false;
		}
		if(trnsAmt > acntBlnc*1){
			alert("계좌 잔액 보다 작은 값을 입력해주세요.");
			$('#trnsAmt').focus();
			return false;
		}
		return true
	}

});