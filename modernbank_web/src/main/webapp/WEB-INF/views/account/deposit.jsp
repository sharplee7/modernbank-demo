<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="container">
	<div id ="deposit_container">
		<br />
		<h2>
			<span>입금</span>
		</h2>
		<form id="form1" onsubmit="return false;">
			<table class="global_table" cellpadding="0" cellspacing="0">
				<colgroup>
					<col style="width:20%;" />
					<col style="width:80%;" />
				</colgroup>
				<tbody>
					<tr>
						<th class="width100">계좌번호</th>
						<td>
							<input class="txt" id="acntNo" type="text" />
							<input id="btnSearch" type="button" value="조회" class="search_btn"/>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<br />
		   입금 계좌 정보:
		<table class="global_table" cellpadding="0" cellspacing="0">
			<colgroup>
				<col style="width:20%;" />
				<col style="width:30%;" />
				<col style="width:20%;" />
				<col style="width:30%;" />
			</colgroup>
			<tbody>
				<tr>
					<th class="width100">계좌명</th>
					<td name="acntNm"></td>
					<th class="width100">계좌번호</th>
					<td name="acntNo"></td>
				</tr>
				<tr>
					<th class="width100">고객ID</th>
					<td name="cstmId"></td>
					<th class="width100">고객명</th>
					<td name="cstmNm"></td>
				</tr>
				<tr>
					<th class="width100">입금금액</th>
					<td colspan="3"><input type="text" id="trnsAmt"/> 원</td>
				</tr>
			</tbody>
		</table>
		<input type="hidden" id="trnsBrnch" value="역삼본점"/>
		<div class="g_tablebtn">
			<input id="btnCreate" type="button" value="입금" class="g_btn_bg" hidden="hidden" />
		</div>
	</div>
	<div id="depositResult_container">
		<br />
		<h2>
			<span>입금 결과</span>
		</h2>
		<table class="global_table" cellpadding="0" cellspacing="0">
			<colgroup>
				<col style="width:20%;" />
				<col style="width:30%;" />
				<col style="width:20%;" />
				<col style="width:30%;" />
			</colgroup>
			<tbody>
				<tr>
					<th class="width135">계좌번호</th>
					<td><span class="txt" id="resultAcntNo" name="resultAcntNo"></span></td>
					<th class="width135">입금전 잔고</th>
					<td><span class="txt" id="resultFormerBlnc" name="resultFormerBlnc"></span></td>
				</tr>
				<tr>
					<th class="width135">입금액</th>
					<td><span class="txt" id="resultTrnsAmt" name="resultTrnsAmt"></span></td>
					<th class="width135">현재 잔고</th>
					<td><span class="txt" id="resultAcntBlnc" name="resultAcntBlnc"></span></td>
				</tr>
				
			</tbody>
		</table>
		<br />
		<a  style="float : right;" href="<%=request.getContextPath()%>/account/view/deposit">돌아가기</a>
	</div>
</div>
<script type="text/javascript"
		src="<%=request.getContextPath()%>/resource/js/account/deposit.js"></script>

