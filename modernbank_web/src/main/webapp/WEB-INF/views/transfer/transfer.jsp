<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="container">
	<div id="transfer_container">
		<br />
		<h2>
			<span>당행 이체</span>
		</h2>
		출금정보
		<form id="form1" onsubmit="return false;">
			<table class="global_table" cellpadding="0" cellspacing="0">
				<colgroup>
					<col style="width:20%;" />
					<col style="width:30%;" />
					<col style="width:20%;" />
					<col style="width:30%;" />
				</colgroup>
				<tbody>
					<tr>
						<th class="width100">출금계좌번호</th>
						<td colspan="5">
							<input class="txt" id="wthdAcntNo" type="text" />
							<input id="btnSearch" type="button" value="조회" class="search_btn"/>
						</td>
					</tr>
					<tr>
						<th class="width100">계좌명</th>
						<td name="acntNm"></td> 
						<th class="width100">계좌잔액</th>
						<td name="acntBlnc"><span id="acntBlnc"></span> 원</td>
					</tr>
					<tr>
						<th class="width100">고객ID</th>
						<td name="cstmId"></td>
						<th class="width100">고객명</th>
						<td name="cstmNm"></td>
					</tr>
					<tr>
						<th class="width100">잔여 1일 이체한도</th>
						<td name="oneDyTrnfLmt"><span id="oneDyTrnfLmt"></span> 원</td> 
						<th class="width100">1회 이체한도</th>
						<td name="oneTmTrnfLmt"><span id="oneTmTrnfLmt"></span> 원</td>
					</tr>
				</tbody>
			</table>
		</form>
		<br />
		 입금정보
		<table class="global_table" cellpadding="0" cellspacing="0">
			<colgroup>
				<col style="width:20%;" />
				<col style="width:30%;" />
				<col style="width:20%;" />
				<col style="width:30%;" />
			</colgroup>
			<tbody>
				<tr>
					<th class="width100">입금계좌번호</th>
					<td><input class="txt" id="dpstAcntNo" type="text"/></td>
					<th class="width100">이체금액</th>
					<td><input class="txt" id="trnfAmt" type="text"/></td>
				</tr>
				<tr>
					<th class="width100">받는통장메모</th>
					<td colspan="3"><input class="txt" id="rcvMm" type="text"/> 7자 이내 입력</td>
				</tr>
				<tr>
					<th class="width100">내통장메모</th>
					<td colspan="3"><input class="txt" id="sndMm" type="text"/> 7자 이내 입력</td>
				</tr>
			</tbody>
		</table>
		<div class="g_tablebtn">
			<input id="btnCreate" type="button" value="이체실행" class="excute_btn" hidden="hidden" />
		</div>
	</div>
	<div id="transferResult_container">
		<br />
		<h2>
			<span>이체 결과</span>
		</h2>
		<form id="form1" onsubmit="return false;">
			<table class="search_results" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<th class="width135">출금계좌</th>
						<th class="width135">입금계좌</th>
						<th class="width135">받는 분</th>
						<th class="width135">이체금액</th>
						<th class="width135">받는 통장 메모</th>
						<th class="width135">내 통장 메모</th>
					</tr>
					<tr style="text-align:center;">
						<td><span id="resultWthdAcntNo"></span></td>
						<td><span id="resultDpstAcntNo"></span></td>
						<td><span id="resultRcvCstmNm"></span></td>
						<td><span id="resultTrnfAmt"></span> 원</td>
						<td><span id="resultRcvMm"></span></td>
						<td><span id="resultSndMm"></span></td>
					</tr>
					
				</tbody>
			</table>
		</form>
		<br />
		<a  style="float : right;" href="<%=request.getContextPath()%>/transfer/view/transfer">돌아가기</a>
	</div>
</div>
<script type="text/javascript"
		src="<%=request.getContextPath()%>/resource/js/transfer/transfer.js"></script>

