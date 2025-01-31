$(function() {
	var contextPath = $('#contextPath').val();
	var customerApiUrl = $('#customerApiUrl').val();
	$("#cstmId").focus();
	$('#customerMenu').addClass('selected-menu');
	$("#btnSearch").on("click", function(){
	    $('#accountList td').remove();
		var cstmId = $('#cstmId').val();
		if(cstmId != "" && cstmId != null){
			retrieveCustomer(cstmId);
		}
		else{
			alert('고객ID를 입력해주세요.');
			$("#cstmId").focus();
		}
	});

	function retrieveCustomer(cstmId){
		var resMsg = "";
		$.ajax({
			type : 'GET',
			url : customerApiUrl+'/' + cstmId + '/details',
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
				$("#cstmId").val(data.cstmId);
				$("td[name=cstmId]").html(data.cstmId);
				$("td[name=cstmNm]").html(data.cstmNm);
				if(data.cstmGnd == '1')
					$("td[name=cstmGnd]").html('남');
				else
					$("td[name=cstmGnd]").html('여');
				$("td[name=cstmAge]").html(data.cstmAge);
				$("td[name=cstmAdr]").html(data.cstmAdr);
				$("td[name=cstmPn]").html(data.cstmPn);

				$("td[name=oneDyTrnfLmt]").html(data.oneDyTrnfLmt);
				$("td[name=oneTmTrnfLmt]").html(data.oneTmTrnfLmt);
				
				for(var i = 0; i < data.accounts.length; i++)
					appendAccountInfo(data.accounts[i].acntNm, 
							data.accounts[i].acntNo, 
							data.accounts[i].newDtm,
							data.accounts[i].acntBlnc);
				
			},
		    error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else {
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
		    	}
				$(location).attr("href", contextPath + "/customer/view/retrieveCustomer");
		    }
		});
		return resMsg;
	}
	function appendAccountInfo(acntNm, acntNo, newDtm, acntBlnc){
		var html = "<tr style='text-align:center;'>"
				+"<td>"+acntNm+"</td>"
				+"<td>"+acntNo+"</td>"
				+"<td>"+acntBlnc+"</td>"
				+"<td>"+newDtm+"</td>"
				"</tr>";
		$('#accountList').append(html);
	}
});