$.urlParam = function(name){
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results==null){
       return null;
    }
    else{
       return results[1] || 0;
    }
};
$(function() {
	var contextPath = $('#contextPath').val();
	var transferApiUrl = $('#transferApiUrl').val();
	var searchCstmId = $.urlParam('cstmId');
	$("#cstmId").focus();
	$('#transferMenu').addClass('selected-menu');
	$("#btnSearch").on("click", function(){
		var cstmId = $('#cstmId').val();
		if(cstmId == null || cstmId == ""){
			alert("고객ID를 입력해주세요.");
			$('#cstmId').focus();
		}
		else{
			$('#transferHistoryList td').remove();
			retrieveTransferHistoryList(cstmId);
		}
	});
	if(searchCstmId != null && searchCstmId != ''){
		$('#cstmId').val(searchCstmId);
		$('#btnSearch').click();
	}
	function retrieveTransferHistoryList(cstmId){
		$.ajax({
			type : 'GET',
			url : transferApiUrl+'/transfer-history/'+ cstmId,
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
					appendTransferHistoryHtml(data[i].trnfDtm, data[i].wthdAcntNo
							,data[i].dpstAcntNo, data[i].rcvCstmNm, data[i].trnfAmt
							,data[i].rcvMm, data[i].sndMm, data[i].divCd, data[i].stsCd);
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
				$(location).attr("href", contextPath + "/transfer/view/retrieveTransferHistory");
		    }
		});
	}
	function appendTransferHistoryHtml(trnfDtm, wthdAcntNo, dpstAcntNo, rcvCstmNm, trnfAmt, rcvMm, sndMm, divCd, stsCd){
		
		var html = "<tr style='text-align : center;'>"
			     + "<td>"+((divCd=="1")?"당행":"타행")+"</td>"
			     + "<td>"+trnfDtm+"</td>"
			     + "<td>"+wthdAcntNo+"</td>"
			     + "<td>"+dpstAcntNo+"</td>"
			     + "<td>"+rcvCstmNm+"</td>"
			     + "<td>"+trnfAmt+"</td>"
			     + "<td>"+rcvMm+"</td>"
			     + "<td>"+sndMm+"</td>"
			     + "<td>"+((stsCd=="1")?"이체 요청":((stsCd=="2")?"이체 실패":"이체 완료"))+"</td>"
				 + "</tr>";
		$('#transferHistoryList').append(html);
				
	}
});