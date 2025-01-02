$(function() {
	var contextPath = $('#contextPath').val();
	var cqrsApiUrl = $('#cqrsApiUrl').val();
	$.ajax({
		type : 'GET',
		url : cqrsApiUrl+'/dormat-customer/list/',
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
			for(var i = 0; i < data.length; i++)
				appendDormantCustomerRecentWork(data[i].cstmId, 
						data[i].cstmNm, 
						data[i].rcntWorkDtm,
						data[i].rcntWorkDivCd,
						data[i].workAcntNo,
						data[i].workTrnsAmt,
						data[i].workStsCd);
			
		},
	    error: function (jqXHR, textStatus, errorThrown) {
	    	if(jqXHR.status == '417' || jqXHR.status == '500'){
		    	var responseText = jqXHR.responseText;
		    	var body = JSON.parse(responseText);
		    	alert(body.message);
	    	}
	    	else
	    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
			$(location).attr("href", contextPath + "/");
	    }
	});
	function appendDormantCustomerRecentWork(cstmId, cstmNm, rcntWorkDtm, rcntWorkDivCd, workAcntNo, workTrnsAmt, workStsCd){
		var rcntWorkDiv = rcntWorkDivCd=="1"?"고객등록"
						:(rcntWorkDivCd=="2"?"계좌개설"
						:(rcntWorkDivCd=="3"?"입금"
						:(rcntWorkDivCd=="4"?"출금"
					    :(rcntWorkDivCd=="5"?"당행이체"
					    :(rcntWorkDivCd=="6"?"타행이체":"")))));
		var workSts = workStsCd=="1"?"요청"
				    :(workStsCd=="2"?"실패"
				    :(workStsCd=="3"?"완료":""));
		var html = "<tr style='text-align:center;'>"
				+"<td>"+cstmId+"</td>"
				+"<td>"+cstmNm+"</td>"
				+"<td>"+rcntWorkDtm+"</td>"
				+"<td>"+rcntWorkDiv+"</td>"
				+"<td>"+workAcntNo+"</td>"
				+"<td>"+workTrnsAmt+"</td>"
				+"<td>"+workSts+"</td>"
				"</tr>";
		$('#dormantCustomerList').append(html);
	}
});