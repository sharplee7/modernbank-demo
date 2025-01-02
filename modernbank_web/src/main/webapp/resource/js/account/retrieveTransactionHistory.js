$(function() {
	var contextPath = $('#contextPath').val();
	var accountApiUrl = $('#accountApiUrl').val();
	$("#acntNo").focus();
	$('#accountMenu').addClass('selected-menu');
	$("#btnSearch").on("click", function(){
		var acntNo = $('#acntNo').val();
		if(acntNo == null || acntNo == ""){
			alert("계좌번호를 입력해주세요.");
			$('#acntNo').focus();
		}
		else{
			$('#trnsHst td').remove();
			retrieveAccountInfo(acntNo);
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
				$('td[name=acntBlnc]').text(data.acntBlnc);
				$('td[name=cstmId]').text(data.cstmId);
				$('td[name=cstmNm]').text(data.cstmNm);
				$('td[name=newDtm]').text(data.newDtm);
				retrieveTransactionHistory(acntNo);
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
	function retrieveTransactionHistory(acntNo){
		$.ajax({
			type : 'GET',
			url : accountApiUrl+'/transaction-history/'+acntNo,
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
				for(var i=0; i < data.length; i++){
					appendTrnsHstHtml(data[i].trnsDtm, data[i].trnsAmt, data[i].divCd
							, data[i].acntBlnc, data[i].trnsBrnch, data[i].stsCd);
				}
				
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
	function appendTrnsHstHtml(trnsDtm, trnsAmt, divCd, acntBlnc, trnsBrnch, stsCd){
		var trnsDate = trnsDtm.substring(0,11);
		var trnsTime = trnsDtm.substring(11);
		var trnsAmtHtml;
		var style = "";
		var statusText;
		if(stsCd == '1')
			statusText = "완료";
		else{
			statusText = "실패";
			style = " style='color:red;'";
		}
		if(divCd == "D")
			trnsAmtHtml = "<td></td>"
					    + "<td"+style+">"+trnsAmt+"</td>";
		else
			trnsAmtHtml = "<td"+style+">"+trnsAmt+"</td>"
			    + "<td></td>";
		var html = "<tr style='text-align:center;'>"
			     + "<td>"+trnsDate+"</td>"
			     + "<td>"+trnsTime+"</td>"
			     + trnsAmtHtml
			     + "<td>"+acntBlnc+"</td>"
			     + "<td>"+trnsBrnch+"</td>"
			     + "<td>"+statusText+"</td>"
				 + "</tr>";
		$('#trnsHst').append(html);
	}
});