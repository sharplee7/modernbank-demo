$(function() {
	var contextPath = $('#contextPath').val();
	var transferApiUrl = $('#transferApiUrl').val();
	var accountApiUrl = $('#accountApiUrl').val();
	$('#transferResult_container').hide();
	$("#wthdAcntNo").focus();
	$('#transferMenu').addClass('selected-menu');
	$("#btnSearch").on("click", function(){
		var wthdAcntNo = $('#wthdAcntNo').val();
		if(wthdAcntNo == null || wthdAcntNo == ""){
			alert("계좌번호를 입력해주세요.");
			alert(wthdAcntNo);
			$('#wthdAcntNo').focus();
		}
		else{
			retrieveAccountInfo(wthdAcntNo);
		}
	});
	$('#wthdAcntNo').on('change',function(){
		$('#btnCreate').hide();
	});
	$('#btnCreate').on("click",function(){
		if(isValidInputValue() == true){
			var wthdAcntNo = $('#wthdAcntNo').val();
			var dpstAcntNo = $('#dpstAcntNo').val();
			var trnfAmt = $('#trnfAmt').val();
			var rcvMm = $('#rcvMm').val();
			var sndMm = $('#sndMm').val();
			var cstmId = $('td[name=cstmId]').text();
			var transferData = {"wthdAcntNo" : wthdAcntNo,
							    "dpstAcntNo" : dpstAcntNo,
							    "trnfAmt" : trnfAmt,
							    "rcvMm" : rcvMm,
							    "sndMm" : sndMm,
							    "cstmId" : cstmId};
			transfer(transferData);
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
			success : function(data, textStatus, jqXHR) {
				$('td[name=acntNm]').text(data.acntNm);
				$('#acntBlnc').text(data.acntBlnc);
				$('td[name=cstmId]').text(data.cstmId);
				$('td[name=cstmNm]').text(data.cstmNm);
				$('#rcvMm').val(data.cstmNm);
				retrieveTransferLimit(data.cstmId);
			},
		    error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
				$(location).attr("href", contextPath + "/transfer/view/transfer");
		    }
		});
	}

	function retrieveTransferLimit(cstmId){
		$.ajax({
			type : 'GET',
			url : transferApiUrl+'/limits/' + cstmId + "/available",
			contentType: 'application/json',
			datatype : 'json',
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
			success : function(data) {
				$('#oneDyTrnfLmt').text(data.oneDyTrnfLmt);
				$('#oneTmTrnfLmt').text(data.oneTmTrnfLmt);
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
				$(location).attr("href", contextPath + "/transfer/view/transfer");
		    }
		});
	}
	function isValidInputValue(){
		var dpstAcntNo = $('#dpstAcntNo').val();
		var trnfAmt = $('#trnfAmt').val();
		var acntBlnc = $('#acntBlnc').text();
		var oneDyTrnfLmt = $('#oneDyTrnfLmt').text();
		var oneTmTrnfLmt = $('#oneTmTrnfLmt').text();
		
		if(dpstAcntNo == null || dpstAcntNo == ""){
			alert("입금계좌번호를 입력해주세요.");
			$('#dpstAcntNo').focus();
			return false;
		}
		if(trnfAmt == null || trnfAmt == ""){
			alert("이체금액을 입력해주세요.");
			$('#trnfAmt').focus();
			return false;
		}
		if(isNaN(trnfAmt) == true){
			alert("이체금액은 숫자만 입력해주세요.");
			$('#trnfAmt').focus();
			return false;
		}
		if(trnfAmt <= 0){
			alert("이체금액은 0 보다 큰 금액을 입력해주세요.");
			$('#trnfAmt').focus();
			return false;
		}
		if(trnfAmt > acntBlnc * 1){
			alert("잔액이 부족합니다.");
			$('#trnfAmt').focus();
			return false;
		}
		if(trnfAmt > oneTmTrnfLmt * 1){
			alert("1회 이체 한도를 초과하였습니다.");
			$('#trnfAmt').focus();
			return false;
		}
		if(trnfAmt > oneDyTrnfLmt * 1){
			alert("1일 이체 한도를 초과하였습니다.");
			$('#trnfAmt').focus();
			return false;
		}
		
		return true;
	}
	function transfer(transferData){
		$.ajax({
			type : 'POST',
			url : transferApiUrl+'/internal',
			contentType: 'application/json',
			datatype : 'json',
			data : JSON.stringify(transferData),
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
		    	$('#transferResult_container').show();
		    	$('#transfer_container').hide();
		    	$('#resultWthdAcntNo').text(data.wthdAcntNo);
		    	$('#resultDpstAcntNo').text(data.dpstAcntNo);
		    	$('#resultRcvCstmNm').text(data.rcvCstmNm);
		    	$('#resultTrnfAmt').text(data.trnfAmt);
		    	$('#resultRcvMm').text(data.rcvMm);
		    	$('#resultSndMm').text(data.sndMm);
		    	
		   },
		   error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
				$(location).attr("href", contextPath + "/transfer/view/transfer");
		    }
		});
	}
});