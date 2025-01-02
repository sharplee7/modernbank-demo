var CONTEXT_ROOT = "modernbank";

/**
 * 거래관련 함수를 제공하는 모듈이다.
 */
var transaction = (function() {
	var _public = {};
	var _private = {
		//전역 거래처리식별자
		GLOBAL_TRAN_TRAD_KEY : undefined,
		
			
		/**
		 * 거래를 실행하고 AJAX 거래처리 경우 성공여부에 따른 콜백을 호출해주고 결과를 반환해준다.
		 * 만약 FORM 거래처리 이면 화면이 전환된다.
		 * 
		 * @param tranProp {Object} 거래처리속성
		 * @return {Object} AJAX 거래처리 경우에만 결과가 반환된다.
		 */
		execute : function(tranProp) {
			urlpath = tranProp.ajaxUrl ? tranProp.ajaxUrl : ".ajax";
			tranProp.url += (tranProp.ajaxFlag ? urlpath : ".form");
			//AJAX 요청이면..
			if(tranProp.ajaxFlag) {
				//파라메터에 폼요소들을 합친다.
				if(typeof tranProp.dataForm != "undefined") {
					tranProp.params = (tranProp.params ? tranProp.params + "&" + tranProp.dataForm.serialize() 
							: tranProp.dataForm.serialize());
				}
				else {
					tranProp.params = (tranProp.params ? tranProp.params : "");
				}
				
				//성공 또는 실패에 대한 결과메시지를 작성하여, 콜백함수를 호출한다.
				var resultMessageFunc = function(recvTranData) {
					var isSuccess = (recvTranData.prcSts === "N");

					var message = "ERR-CODE : " + recvTranData.resCode + "\nERR-MESSAGE : " + recvTranData.resMsg;

					if(recvTranData.resAddMsg && 0 < recvTranData.resAddMsg.totalRowSize) {
						message += "\n\n부가메세지";
						for(var i=0, addMsgLen = recvTranData.resAddMsg.totalRowSize; i < addMsgLen; i += 1) {
							message += ("\n  - " + recvTranData.resAddMsg.rows[i].addCode 
								+ " : " + recvTranData.resAddMsg.rows[i].addMsg);
						}
					}

					if(recvTranData.errCause) {
						message += "\n\Casue";
						message += "\n  - File : " + recvTranData.errCause.fileName;
						message += "\n  - Line Number : " + recvTranData.errCause.lineNumber;
						message += "\n  - Class : " + recvTranData.errCause.className;
						message += "\n  - Method : " + recvTranData.errCause.methodName;
					}
					if(recvTranData.errTrace) {
						message += "\n\n추적\r\n\t" + recvTranData.errTrace;
					}
					
					if(isSuccess) {
						if(tranProp.success) {
							tranProp.success(recvTranData, message);
						}
					}
					else {
						if(tranProp.failure) {
							tranProp.failure(recvTranData, message);
						}
					}
				};
				var resData = jQuery.ajax({
					type    : tranProp.method ,
					url     : CONTEXT_ROOT + "/" + tranProp.url,
					data    : tranProp.params,
					dataType: "text",				
					async   : tranProp.asyncFlag,
					success : function(data, resStatus) {
						_private.GLOBAL_TRAN_TRAD_KEY = undefined;
						
						//화면 블로킹 해제
						if(tranProp.blokingFlag) {
							$.unblockUI();
						}
						
						var resultData = "";
						try {
							try {
								resultData = JSON.parse(data);
							}
							catch(jsonError) {
								throw {
									name : "JsonError", 
									message : "Error occured", 
									cause : jsonError
								};
							}
						}
						catch(tError) {
							//응답 형식이 올바르지 않습니다
							resultData = JSON.parse(
									"{\"resCode\":\"9999\",\"prcSts\":\"F\",\"resMsg\":\"" 
										+ tError.name + " : " + tError.message + "\"}");
						}
						resultMessageFunc(resultData);
					},
					error : function(xhr, resStatus, err) {
						xhr.abort();
						
						_private.GLOBAL_TRAN_TRAD_KEY = undefined;
						//화면 블로킹 해제
						if(tranProp.blokingFlag) {
							$.unblockUI();
						}
						debugger;
						var recvTranData = JSON.parse(
								"{\"resCode\":\""+ resStatus +"\",\"prcSts\":\"F\"," +
										"\"resMsg\":\"An unexpected error has occurred.\"}");
						
						if(recvTranData.resCode == 404) {
							recvTranData.resMsg = "Not found Page";
								recvTranData.resMsg += "\nCheck Url";
						}
						else {
							if(recvTranData.resCode == 500) {
								recvTranData.resMsg = "Internal server error was encountered.";
							}
							else {
								recvTranData.resMsg += "\n" + "9999";
							}
							
							recvTranData.resMsg += "\n" + "Please contact the system administrator in case of continued failure .";
						}
						
						resultMessageFunc(recvTranData);
					}
				});

				//동기화 요청인 경우이면...
				if(!tranProp.asyncFlag) {
					try {
						try {
							resData = JSON.parse(resData.responseText);
						}
						catch(e) {
							throw {
								name : "JsonError", 
								message : "Error occured", 
								cause : e
							};
						}
					}
					catch(e) {
						//응답 형식이 올바르지 않습니다
						resData = JSON.parse(
								"{\"resCode\":\"9999\",\"prcSts\":\"F\",\"resMsg\":\"" 
									+ e.name + " : " + e.message + "\"}");
					}
					return resData;
				}
			}
			else {
                
                tranProp.dataForm.removeAttr("onsubmit");
                tranProp.dataForm[0].method = tranProp.method;
				tranProp.dataForm[0].action = CONTEXT_ROOT + "/" + tranProp.url;
				
				// MenuId를 유지하기 위하여 menuId를  tranProp.params에 추가한다. (Form거래시)
				tranProp.params = (tranProp.params ? (tranProp.params +("&menuId=" + menuId)) : ("menuId=" + menuId)); 
				
				tranProp.dataForm.append("<input type=\"hidden\" id=\"menuId\" name=\"menuId\" value=\"" + menuId + "\" />");
				//폼 주소에 파라메터을 추가한다.
				if("GET" == tranProp.method.toUpperCase()) {
					tranProp.dataForm[0].action +(tranProp.params ? "?" + tranProp.params : "");
				} else {
					//폼에 파라메터를 요소로 추가한다.
					var inpts = tranProp.params.split("&");
					for(var idx=0; idx < inpts.length; idx++) {
						var inpt = inpts[idx].split("=");
                        //먼저 폼요소에 있는지 해당 엘레먼트가 있는지 확인 후 없을때만 추가 한다.
                        //추가 파라메터를 배열로 올리는건 불가.
                        var inputEle = $("input[name='" + inpt[0] + "']", tranProp.dataForm);
                        if (!inputEle[0]) {
                            inputEle = $("<input type=\"hidden\" name=\"" + inpt[0] + "\" value=\"" + inpt[1] + "\" />");
                            tranProp.dataForm.append(inputEle);
                        }
                        inputEle.val(inpt[1]);
					}
				}
				tranProp.dataForm[0].submit();
				
				//Form 전송 후 화면 블로킹 해제
				if(tranProp.blokingFlag) {
					$.unblockUI();
				}
			}
		},

		/**
		 * 거래 처리가 성공할 경우 호출되어 지는 함수이다.
		 * 
		 * @param recvTranData {Object} 응답거래 데이터
		 */
		recvTranForSuccess : function(recvTranData) {
		},

		/**
		 * 거래 처리가 성공할 경우 호출되어 지는 함수이다.
		 * 
		 * @param recvTranData {Object} 응답거래 데이터
		 */
		recvTranForFailure : function(recvTranData){
		}
	};
	
	
	/** 
	 * 거래공통 기본 오브젝트이다.
	 * 
	 * [선택 : Both] 거래방식 - ajaxFlag : true,
	 * [선택 : Both] 화면잠금여부 - blokingFlag : true or false,
	 * [필수 : Both] 거래주소 - url : undefined,
	 * [필수 : Both] 거래식별자 - tradeKey : undefined,
	 * [선택 : AJAX] 비동기여부 - asyncFlag : true,
	 * [선택 : Both] 폼메서드 - method : "POST",
	 * [필수 : Both] 데이터폼 - dataForm : undefined,
	 * [선택 : AJAX] 데이터파라메터 - params : "",
	 * [선택 : AJAX] 성공처리콜백 - success : _private.recvTranForSuccess,
	 * [선택 : AJAX] 실패처리콜백 - failure : _private.recvTranForFailure
	 */
	_public.TRAN_COMM_PROP = {
		ajaxFlag    : true,
		blokingFlag : true,
		url         : undefined,
		tradeKey    : undefined,
		asyncFlag   : true,
		method      : "POST",
		dataForm    : undefined,
		params      : "",
		success     : _private.recvTranForSuccess,
		failure     : _private.recvTranForFailure
	};
	
	/**
	 * 거래를 실행하고 AJAX 거래처리 경우 성공여부에 따른 콜백을 호출해주고 결과를 반환해준다.
	 * 만약 FORM 거래처리 이면 화면이 전환된다.
	 * 
	 * @param tranProp {Object} 거래처리속성
	 * @return {Object} 동기화 AJAX 거래처리 경우에만 결과가 반환된다.
	 */
	_public.callTran = function (tranProp) {
		try {
			//처리할 폼 오브젝트가 없는 경우 처리중단....
			if(typeof tranProp.dataForm[0] == "undefined") {
				(function() {
					var thrown = {
						name : "TransactionError", 
						message : "Form data to be processed does not exist."
					};
					throw thrown;
				}());
			}
			
			_private.GLOBAL_TRAN_TRAD_KEY = tranProp.tradeKey;
			
			//화면이 잠겨있지 않으면 화면을 잠근다.
			if(tranProp.blokingFlag) {
				$.blockUI({message:"<div class=\"processing\"><img src=\"/modernbank/resource/tiles/images/loading.gif\" /></div>"});
			}

			//거래처리을 요청한다.
			return _private.execute(tranProp);
		}
		catch(e) {
			(function(causeErr) {
				var thrown = {
					name : "TransactionError", 
					message : "Transaction Error", 
					cause : causeErr
				};
				throw thrown;
			}(e));
		}
	};
	
	/**
	 * 폼이나 거래중복 체크을 하지 않는 AJAX 거래를 실행하고 성공여부에 따른 콜백을 호출해주고 결과를 반환해준다.
	 * 
	 * @param tranProp {Object} 거래처리속성
	 * @return {Object} 처리결과
	 */
	_public.callLaxTran = function(tranProp) {
		try {
			tranProp.ajaxFlag = true;
			return _private.execute(tranProp);
		}
		catch(e) {
			(function(causeErr) {
				var thrown = {
					name : "TransactionError", 
					message : "Transaction Error", 
					cause : causeErr
				};
				throw thrown;
			}(e));
		}
	};
	
	_public.clone = function(scrObj) {
		var cloneObj = {};
		$.extend(cloneObj, scrObj);
		return cloneObj;
	};
	
	return _public;
})();

